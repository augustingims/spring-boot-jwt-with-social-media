package cm.skysoft.app.service;

import cm.skysoft.app.config.ApplicationProperties;
import cm.skysoft.app.domain.*;
import cm.skysoft.app.dto.AccountDTO;
import cm.skysoft.app.dto.ClientPrepaInfosDTO;
import cm.skysoft.app.dto.ProductDTO;
import cm.skysoft.app.service.dto.ExcelReporting;
import cm.skysoft.app.utils.ExcelUtil;
import cm.skysoft.app.utils.MethoUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 4/28/21.
 */
@Service
public class JxlsManagerService {

    private final Logger log = LoggerFactory.getLogger(JxlsManagerService.class);
    private final VisitsService visitsService;
    private final VisitReportService visitReportService;
    private final AfbExternalService afbExternalService;
    private final ApplicationProperties applicationProperties;
    private final VisitParticipantsService visitParticipantsService;
    private final FileUploadService fileUploadService;

    public JxlsManagerService(VisitsService visitsService, VisitReportService visitReportService, AfbExternalService afbExternalService, ApplicationProperties applicationProperties, VisitParticipantsService visitParticipantsService, FileUploadService fileUploadService) {
        this.visitsService = visitsService;
        this.visitReportService = visitReportService;
        this.afbExternalService = afbExternalService;
        this.applicationProperties = applicationProperties;
        this.visitParticipantsService = visitParticipantsService;
        this.fileUploadService = fileUploadService;
    }

    @Transactional
    public ExcelReporting generateVisitReport(String visitCode) throws IOException {
        ExcelReporting excelReporting = new ExcelReporting();
        final Context context = new Context();
        List<byte[]> imageBytes = new ArrayList<>();
        StringBuilder account = new StringBuilder();
        int currentYear = LocalDate.now().getYear();
        int currentMonthValue = LocalDate.now().getMonthValue();


        String fileName = String.format("%s%s%s", "visit_report_", MethoUtils.getPrefixDocumentByDate(), ExcelUtil.EXTENTION);
        String fileStore = String.format("%s%s", applicationProperties.getUpload().getResourcesServerStore(), fileName);


        log.info("File name {}", fileName);
        log.info("File store {}", fileStore);

        Visits visit = visitsService.findVisitByCodeVisit(visitCode);
        List<VisitReport> visitReports = visitReportService.getAllVisitReportsByVisitCode(visitCode);
        Clients client = visit.getClient();
        ClientPrepaInfosDTO clientPrepaInfosDTO = afbExternalService.getInfoPrepaVisit(client.getCodeClient());
        List <VisitParticipants> visitParticipants = visitParticipantsService.getVisitParticipantsByIdVisit(visit.getId());
        List <FileUploadVisitNote> fileUploads = fileUploadService.findFileUploadByIdVisitNoteVisitId(visit.getId());



        for (FileUploadVisitNote fileUpload : fileUploads) {

            Optional<String> fileExtention = getExtensionByStringHandling(fileUpload.getNameFile());

            if (fileExtention.isPresent()) {

                if (fileExtention.get().toUpperCase().equals("PNG") || fileExtention.get().toUpperCase().equals("JPG") || fileExtention.get().toUpperCase().equals("JPEG")) {
                    InputStream imageInputStream;

                    try {
                        final String url = String.format("%s%s", applicationProperties.getUpload().getResourcesServerStore(), fileUpload.getNameFile());
                        imageInputStream = Files.newInputStream(Paths.get(url));
                        byte[] imageByte = Util.toByteArray(imageInputStream);
                        imageBytes.add(imageByte);

                    } catch (IOException ignored) {
                    }
                }
            }
        }

        for(int i=0; i<imageBytes.size(); i++) {
            context.putVar("image" + i, imageBytes.get(i));
        }

        context.putVar("data", visitReports);

        if(clientPrepaInfosDTO.getEquipment() != null && clientPrepaInfosDTO.getEquipment().getAccounts() != null) {
            for(AccountDTO accountDTO: clientPrepaInfosDTO.getEquipment().getAccounts()) {
                if (accountDTO.getAccountNumber() != null && !accountDTO.getAccountNumber().equals(""))
                    account.append(accountDTO.getAccountNumber()).append(" ,");
            }
        }


        context.putVar("dataengagement", clientPrepaInfosDTO.getEngagement().getEngagemts());

        context.putVar("roison_social_du_client", clientPrepaInfosDTO.getIdentification().getSocialReason());
        context.putVar("nemro_de_compte", account.toString());
        context.putVar("segment", clientPrepaInfosDTO.getIdentification().getSegment().getFr());
        context.putVar("date_creation", MethoUtils.conv(clientPrepaInfosDTO.getIdentification().getEntryDate()));
        context.putVar("activite_pricise", client.getActivityProfile());
        context.putVar("principal_manager", clientPrepaInfosDTO.getIdentification().getLastName() + " " + clientPrepaInfosDTO.getIdentification().getFirstName());
        context.putVar("entree_en_relation", client.getEntryDate());
        context.putVar("gfc", client.getManager().getLastName() +" "+  client.getManager().getFirstName());
        context.putVar("agence", client.getAgency().getName());
        if (visit.getHourVisit() != null){
            context.putVar("date_de_viste", visit.getVisitDateText2() + " " + visit.getHourVisit());
        } else {
            context.putVar("date_de_viste", visit.getVisitDateText());
        }
        context.putVar("lieux_visite", clientPrepaInfosDTO.getAddress().getPhysicalAddress());
        context.putVar("nom_personne_a_rencontrer", visit.getClient().getLastName() +" "+ visit.getClient().getFirstName());
        context.putVar("fonction_client", visit.getClient().getParticularProfile());

        context.putVar("objectif_visite", visit.getVisitType());
        context.putVar("commentaire_vsite", visit.getVisitObject());

        // Analyse des mouvements et de la rentabilité
        context.putVar("n", currentYear);
        context.putVar("n_3", currentYear - 3);
        context.putVar("n_2", currentYear - 2);
        context.putVar("n_1", currentYear - 1);
        context.putVar("m_3", MethoUtils.getMontName(currentMonthValue - 3));
        context.putVar("m_1", MethoUtils.getMontName(currentMonthValue - 1));
        context.putVar("m_2", MethoUtils.getMontName(currentMonthValue - 2));

        context.putVar("turnover_year_n3", clientPrepaInfosDTO.getTransaction().getTurnover().getYearN3Text());
        context.putVar("turnover_year_n2", clientPrepaInfosDTO.getTransaction().getTurnover().getYearN2Text());
        context.putVar("turnover_year_n1", clientPrepaInfosDTO.getTransaction().getTurnover().getYearN1Text());
        context.putVar("turnover_month_m2", clientPrepaInfosDTO.getTransaction().getTurnover().getMonthM2Text());
        context.putVar("turnover_month_m1", clientPrepaInfosDTO.getTransaction().getTurnover().getMonthM1Text());
        context.putVar("turnover_month_m3", clientPrepaInfosDTO.getTransaction().getTurnover().getMonthM3Text());

        context.putVar("entrustedCash_year_n3", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getYearN3Text());
        context.putVar("entrustedCash_year_n2", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getYearN2Text());
        context.putVar("entrustedCash_year_n1", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getYearN1Text());
        context.putVar("entrustedCash_month_m2", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM2Text());
        context.putVar("entrustedCash_month_m1", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM1Text());
        context.putVar("entrustedCash_month_m3", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM3Text());

        context.putVar("debitorCash_year_n3", clientPrepaInfosDTO.getTransaction().getDebitorCash().getYearN3Text());
        context.putVar("debitorCash_year_n2", clientPrepaInfosDTO.getTransaction().getDebitorCash().getYearN2Text());
        context.putVar("debitorCash_year_n1", clientPrepaInfosDTO.getTransaction().getDebitorCash().getYearN1Text());
        context.putVar("debitorCash_month_m2", clientPrepaInfosDTO.getTransaction().getDebitorCash().getMonthM2Text());
        context.putVar("debitorCash_month_m1", clientPrepaInfosDTO.getTransaction().getDebitorCash().getMonthM1Text());
        context.putVar("debitorCash_month_m3", clientPrepaInfosDTO.getTransaction().getDebitorCash().getMonthM3Text());

        context.putVar("internationalReceivedTransfert_year_n3", clientPrepaInfosDTO.getTransaction().getInternationalReceivedTransfert().getYearN3Text());
        context.putVar("internationalReceivedTransfert_year_n2", clientPrepaInfosDTO.getTransaction().getInternationalReceivedTransfert().getYearN2Text());
        context.putVar("internationalReceivedTransfert_year_n1", clientPrepaInfosDTO.getTransaction().getInternationalReceivedTransfert().getYearN1Text());
        context.putVar("internationalReceivedTransfert_month_m2", clientPrepaInfosDTO.getTransaction().getInternationalReceivedTransfert().getMonthM2Text());
        context.putVar("internationalReceivedTransfert_month_m1", clientPrepaInfosDTO.getTransaction().getInternationalReceivedTransfert().getMonthM1Text());
        context.putVar("internationalReceivedTransfert_month_m3", clientPrepaInfosDTO.getTransaction().getInternationalReceivedTransfert().getMonthM3Text());

        context.putVar("internationalEmittedTransfert_year_n3", clientPrepaInfosDTO.getTransaction().getInternationalEmittedTransfert().getYearN3Text());
        context.putVar("internationalEmittedTransfert_year_n2", clientPrepaInfosDTO.getTransaction().getInternationalEmittedTransfert().getYearN2Text());
        context.putVar("internationalEmittedTransfert_year_n1", clientPrepaInfosDTO.getTransaction().getInternationalEmittedTransfert().getYearN1Text());
        context.putVar("internationalEmittedTransfert_month_m2", clientPrepaInfosDTO.getTransaction().getInternationalEmittedTransfert().getMonthM2Text());
        context.putVar("internationalEmittedTransfert_month_m1", clientPrepaInfosDTO.getTransaction().getInternationalEmittedTransfert().getMonthM1Text());
        context.putVar("internationalEmittedTransfert_month_m3", clientPrepaInfosDTO.getTransaction().getInternationalEmittedTransfert().getMonthM3Text());

        context.putVar("productivity_year_n3", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getYearN3Text());
        context.putVar("productivity_year_n2", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getYearN2Text());
        context.putVar("productivity_year_n1", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getYearN1Text());
        context.putVar("productivity_month_m2", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM2Text());
        context.putVar("productivity_month_m1", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM1Text());
        context.putVar("productivity_month_m3", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM3Text());

        // Analyse des mouvements et de la rentabilité sur les 12 derniers mois
        context.putVar("entrustedCash_m12_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM12Text());
        context.putVar("entrustedCash_m1_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM1Text());
        context.putVar("entrustedCash_m2_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM2Text());
        context.putVar("entrustedCash_m3_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM3Text());
        context.putVar("entrustedCash_m4_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM4Text());
        context.putVar("entrustedCash_m5_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM5Text());
        context.putVar("entrustedCash_m6_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM6Text());
        context.putVar("entrustedCash_m7_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM7Text());
        context.putVar("entrustedCash_m8_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM8Text());
        context.putVar("entrustedCash_m9_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM9Text());
        context.putVar("entrustedCash_m10_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM10Text());
        context.putVar("entrustedCash_m11_n", clientPrepaInfosDTO.getTransaction().getEntrustedCash().getMonthM11Text());

        context.putVar("productivity_m1_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM1Text());
        context.putVar("productivity_m2_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM2Text());
        context.putVar("productivity_m3_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM3Text());
        context.putVar("productivity_m4_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM4Text());
        context.putVar("productivity_m5_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM5Text());
        context.putVar("productivity_m6_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM6Text());
        context.putVar("productivity_m7_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM7Text());
        context.putVar("productivity_m8_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM8Text());
        context.putVar("productivity_m9_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM9Text());
        context.putVar("productivity_m12_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM12Text());
        context.putVar("productivity_m10_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM10Text());
        context.putVar("productivity_m11_n", clientPrepaInfosDTO.getBehavior().getTransaction().getProductivity().getMonthM11Text());

        context.putVar("m_1_n", MethoUtils.getMontNameAndYear((currentMonthValue - 1), currentYear));
        context.putVar("m_2_n", MethoUtils.getMontNameAndYear((currentMonthValue - 2), currentYear));
        context.putVar("m_3_n", MethoUtils.getMontNameAndYear((currentMonthValue - 3), currentYear));
        context.putVar("m_4_n", MethoUtils.getMontNameAndYear((currentMonthValue - 4), currentYear));
        context.putVar("m_5_n", MethoUtils.getMontNameAndYear((currentMonthValue - 5), currentYear));
        context.putVar("m_6_n", MethoUtils.getMontNameAndYear((currentMonthValue - 6), currentYear));
        context.putVar("m_7_n", MethoUtils.getMontNameAndYear((currentMonthValue - 7), currentYear));
        context.putVar("m_8_n", MethoUtils.getMontNameAndYear((currentMonthValue - 8), currentYear));
        context.putVar("m_9_n", MethoUtils.getMontNameAndYear((currentMonthValue - 9), currentYear));
        context.putVar("m_10_n", MethoUtils.getMontNameAndYear((currentMonthValue - 10), currentYear));
        context.putVar("m_11_n", MethoUtils.getMontNameAndYear((currentMonthValue - 11), currentYear));
        context.putVar("m_12_n", MethoUtils.getMontNameAndYear(currentMonthValue - 12, currentYear));

        // Equipement
        List<ProductDTO> productDTOs = clientPrepaInfosDTO.getEquipment().getProducts();

        context.putVar("equipement", productDTOs);

        for(int i=0; i<visitParticipants.size(); i++) {
            context.putVar("representant_banque_"+i, visitParticipants.get(i).getUser().getLastName() +" "+visitParticipants.get(i).getUser().getFirstName());
            context.putVar("fonction_presentation_"+i, visitParticipants.get(i).getUser().getPositionName());
        }


        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileStore));

        final InputStream inputStream = getFileInputStream();

        final Workbook templ = WorkbookFactory.create(inputStream);

        final PoiTransformer transformer = PoiTransformer.createTransformer(templ);
        final AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);

        List<Area> areaList = areaBuilder.build();

        final Area xlsArea = areaList.get(0);
        xlsArea.applyAt(new CellRef("Template!A1"), context);

        xlsArea.processFormulas();
        templ.setSheetName(0, "Visit Report");
        templ.write(fileOutputStream);

        excelReporting.setFileName(fileName);
        excelReporting.setFileStore(fileStore);
        excelReporting.setMessage("Succes");
        excelReporting.setDirectoryUrl(String.format("%s%s", applicationProperties.getUpload().getResourcesServerStoreUrl(), fileName));

        fileOutputStream.flush();

        return excelReporting;
    }

    private InputStream getFileInputStream() {
        InputStream inputStream;

        final String fileName= "excels/Template.xlsx";
        ClassLoader classLoader = getClass().getClassLoader();

        inputStream = classLoader.getResourceAsStream(fileName);

        return inputStream;
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
