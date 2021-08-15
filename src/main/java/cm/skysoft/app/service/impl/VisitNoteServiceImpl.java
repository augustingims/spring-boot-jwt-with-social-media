package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.*;
import cm.skysoft.app.dto.*;
import cm.skysoft.app.repository.*;
import cm.skysoft.app.service.*;
import cm.skysoft.app.service.beans.DetailsVisitNoteBeans;
import cm.skysoft.app.service.dto.VisitNoteMobileDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class VisitNoteServiceImpl implements VisitNoteService {

    private final VisitNoteRepository visitNoteRepository;
    private final VisitsService visitsService;
    private final UserService userService;
    private final ProductVisitNoteService productVisitNoteService;
    private final ProductsService productsService;
    private final FileUploadService fileUploadService;
    private final FileUploadRepository fileUploadRepository;
    private final NotificationRepository notificationRepository;
    private final VisitsRepository visitsRepository;
    private final AfbExternalService afbExternalService;
    private final ConfigApplicationService configApplicationService;
    private final UserAfbsService userAfbsService;
    private final EngagementTypeRepository engagementTypeRepository;
    private final EngagementTypeVisitNoteService engagementTypeVisitNoteService;
    private final MailService mailService;

    public VisitNoteServiceImpl(VisitNoteRepository visitNoteRepository, VisitsService visitsService, UserService userService,
                                ProductVisitNoteService productVisitNoteService, ProductsService productsService,
                                FileUploadService fileUploadService, FileUploadRepository fileUploadRepository, NotificationRepository notificationRepository, VisitsRepository visitsRepository, AfbExternalService afbExternalService, ConfigApplicationService configApplicationService, UserAfbsService userAfbsService, EngagementTypeRepository engagementTypeRepository, EngagementTypeVisitNoteService engagementTypeVisitNoteService, MailService mailService) {
        this.visitNoteRepository = visitNoteRepository;
        this.visitsService = visitsService;
        this.userService = userService;
        this.productVisitNoteService = productVisitNoteService;
        this.productsService = productsService;
        this.fileUploadService = fileUploadService;
        this.fileUploadRepository = fileUploadRepository;
        this.notificationRepository = notificationRepository;
        this.visitsRepository = visitsRepository;
        this.afbExternalService = afbExternalService;
        this.configApplicationService = configApplicationService;
        this.userAfbsService = userAfbsService;
        this.engagementTypeRepository = engagementTypeRepository;
        this.engagementTypeVisitNoteService = engagementTypeVisitNoteService;
        this.mailService = mailService;
    }

    @Override
    public VisitNote save(VisitNoteDTO visitNoteDTO) {

        VisitNote v = new VisitNote();
        User user = userService.getCurrentUser();

        Optional<Visits> visitsOptional = visitsService.getVisitById(visitNoteDTO.getVisits().getId());

        if(visitsOptional.isPresent()) {
            Visits visits = visitsOptional.get();

            v.setDateCreated(LocalDateTime.now());
            v.setUser(user);
            v.setNoteVisit(visitNoteDTO.getNoteVisit());
            v.setVisits(visits);

            v = visitNoteRepository.save(v);

            if(visitNoteDTO.getProduct() != null){
                for(ProductDTO productDTO : visitNoteDTO.getProduct()){

                    Optional<Products> p = productsService.findProductsByIdProduct(productDTO.getId());

                    if(p.isPresent()){
                        ProductVisitNote productVisitNote = new ProductVisitNote();
                        productVisitNote.setProducts(p.get());
                        productVisitNote.setVisitNote(v);
                        productVisitNoteService.save(productVisitNote);
                    }
                }
            }

            if(visitNoteDTO.getEngagement() != null) {
                for (EngagementTypeDTO engagementTypeDTO : visitNoteDTO.getEngagement()) {
                    Optional<EngagementType> e = engagementTypeRepository.findById((long) engagementTypeDTO.getId());

                    if(e.isPresent()){
                        EngagementTypeVisitNote engagementTypeVisitNote = new EngagementTypeVisitNote();
                        engagementTypeVisitNote.setEngagementType(e.get());
                        engagementTypeVisitNote.setVisitNote(v);
                        engagementTypeVisitNoteService.save(engagementTypeVisitNote);
                    }
                }
            }

            if(visitNoteDTO.getFileUpload() != null){
                for(FileUploadDTO fileUpload : visitNoteDTO.getFileUpload()){
                    FileUploadVisitNote file = new FileUploadVisitNote();
                    file.setNameFile(fileUpload.getNameFile());
                    file.setDefaultImage(fileUpload.getDefaultImage());
                    file.setContentType(fileUpload.getContentType());
                    file.setVisitNote(v);
                    fileUploadService.save(file);
                }
            }

            visits.setSaveExecution(Boolean.TRUE);

            visitsRepository.save(visits);
        }

        return v;
    }

    @Transactional
    @Override
    public VisitNote update(VisitNoteDTO visitNoteDTO) {
        Optional<VisitNote> v = visitNoteRepository.findVisitNoteByVisitsCode(visitNoteDTO.getVisitCode());

        if(v.isPresent()) {
            v.get().setNoteVisit(visitNoteDTO.getNoteVisit());
            v.get().setDateLastUpdated(LocalDateTime.now());

            List<ProductVisitNote> productVisitNotes = productVisitNoteService.loadAllByVisitNoteId(v.get().getId());
            productVisitNoteService.deleteAllProducts(productVisitNotes);

            if(visitNoteDTO.getProduct() != null){
                for(ProductDTO productDTO : visitNoteDTO.getProduct()){

                    Optional<Products> p = productsService.findProductsByIdProduct(productDTO.getId());

                    if(p.isPresent()){
                        ProductVisitNote productVisitNote = new ProductVisitNote();
                        productVisitNote.setProducts(p.get());
                        productVisitNote.setVisitNote(v.get());
                        productVisitNoteService.save(productVisitNote);
                    }
                }
            }

            List<EngagementTypeVisitNote> engagementTypeVisitNotes = engagementTypeVisitNoteService.findAllByVisitNoteId(v.get().getId());
            engagementTypeVisitNoteService.deleteAllEngagementType(engagementTypeVisitNotes);

            if(visitNoteDTO.getEngagement() != null) {
                for (EngagementTypeDTO engagementTypeDTO : visitNoteDTO.getEngagement()) {
                    Optional<EngagementType> e = engagementTypeRepository.findById((long) engagementTypeDTO.getId());

                    if(e.isPresent()){
                        EngagementTypeVisitNote engagementTypeVisitNote = new EngagementTypeVisitNote();
                        engagementTypeVisitNote.setEngagementType(e.get());
                        engagementTypeVisitNote.setVisitNote(v.get());
                        engagementTypeVisitNoteService.save(engagementTypeVisitNote);
                    }
                }
            }

            List<FileUploadVisitNote> fileUploadVisitNotes = fileUploadService.findFileUploadByIdVisitNote(v.get().getId());

            Collection<String> listOne = new ArrayList<>();
            Collection<String> listTwo = new ArrayList<>();

            for(FileUploadVisitNote fileUploadVisitNote : fileUploadVisitNotes) {
                listOne.add(fileUploadVisitNote.getNameFile());
            }

            for(FileUploadDTO fileUploadDTO : visitNoteDTO.getFileUpload()) {
                listTwo.add(fileUploadDTO.getNameFile());
            }

            listOne.removeAll( listTwo );
            listTwo.removeAll(listOne);

            try {
                for (String name : listOne) {
                    fileUploadService.deleteAllFileUploadVisitNote(name);
                }
            } catch (Exception Ignored) {
            }

            if(visitNoteDTO.getFileUpload() != null){

                for(FileUploadDTO fileUpload : visitNoteDTO.getFileUpload()){
                    Optional<FileUploadVisitNote> fileUploadVisitNote = fileUploadRepository.findByNameFile(fileUpload.getNameFile());
                    if(!fileUploadVisitNote.isPresent()) {
                        if(fileUpload.getDefaultImage() != null) {
                            FileUploadVisitNote file = new FileUploadVisitNote();
                            file.setNameFile(fileUpload.getNameFile());
                            file.setDefaultImage(fileUpload.getDefaultImage());
                            file.setContentType(fileUpload.getContentType());
                            file.setVisitNote(v.get());
                            fileUploadService.save(file);
                        }
                    }
                }
            }
        }

        assert v.orElse(null) != null;
        return visitNoteRepository.save(v.orElse(null));
    }

    @Override
    public VisitNote validatedVisitNote(String codeVisitNote) {
        if(codeVisitNote != null) {
            Optional<VisitNote> v = visitNoteRepository.findVisitNoteByVisitsCode(codeVisitNote);
            User user = userService.getCurrentUser();

            List<UserAfbDTO> userAfbDTOList = afbExternalService.getAllUserSupervisorsByUSerCode(user.getUserCode());

            ConfigApplication configApplications = configApplicationService.findOne();

            if(v.isPresent()) {

                Visits visitsOptional = visitsRepository.findVisitsByVisitCode(codeVisitNote);

                if(visitsOptional != null) {

                    visitsOptional.setExecution(Boolean.TRUE);

                    visitsRepository.save(visitsOptional);

                    v.get().setDateValidated(LocalDateTime.now());

                    for (UserAfbDTO userAfbDTO : userAfbDTOList){
                        User userRecieved = userAfbsService.getUserById(userAfbDTO.getUserCode());

                        if(userRecieved != null) {

                            Notifications notification = new Notifications();
                            notification.setCreateBy(user);
                            notification.setTypeNotification("EXECUTION DE LA VISITE");
                            notification.setDescriptionNotification(patternNotification(configApplications != null ? configApplications.getPatternExecutionVisit() : "", v.get().getVisits(), user));
                            notification.setCreateDate(LocalDateTime.now());
                            notification.setStatus(Boolean.FALSE);
                            notification.setSendTo(userRecieved);

                            notificationRepository.save(notification);

                            sendMail(userRecieved, patternNotification(configApplications != null ? configApplications.getPatternExecutionVisit() : "", v.get().getVisits(), user));
                        }
                    }
                }
            }
            return v.map(visitNoteRepository::save).orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public VisitNote updateVisitNoteMobile(List<MultipartFile> files, VisitNoteMobileDTO visitNoteDTO) throws IOException {

        Optional<VisitNote> v = visitNoteRepository.findVisitNoteByVisitsCode(visitNoteDTO.getVisitCode());
        User user = userService.getCurrentUser();
        Optional<Visits> visitsOptional = visitsService.getVisitById(visitNoteDTO.getVisits().getId());

        if(v.isPresent()) {

            v.get().setNoteVisit(visitNoteDTO.getNoteVisit());
            v.get().setDateLastUpdated(LocalDateTime.now());

            List<ProductVisitNote> productVisitNotes = productVisitNoteService.loadAllByVisitNoteId(v.get().getId());
            productVisitNoteService.deleteAllProducts(productVisitNotes);

            if(visitNoteDTO.getProduct() != null){
                for(ProductDTO productDTO : visitNoteDTO.getProduct()){

                    Optional<Products> p = productsService.findProductsByIdProduct(productDTO.getId());

                    if(p.isPresent()){
                        ProductVisitNote productVisitNote = new ProductVisitNote();
                        productVisitNote.setProducts(p.get());
                        productVisitNote.setVisitNote(v.get());
                        productVisitNoteService.save(productVisitNote);
                    }
                }
            }

            List<EngagementTypeVisitNote> engagementTypeVisitNotes = engagementTypeVisitNoteService.findAllByVisitNoteId(v.get().getId());
            engagementTypeVisitNoteService.deleteAllEngagementType(engagementTypeVisitNotes);

            if(visitNoteDTO.getEngagement() != null) {
                for (EngagementTypeDTO engagementTypeDTO : visitNoteDTO.getEngagement()) {
                    Optional<EngagementType> e = engagementTypeRepository.findById((long) engagementTypeDTO.getId());

                    if(e.isPresent()){
                        EngagementTypeVisitNote engagementTypeVisitNote = new EngagementTypeVisitNote();
                        engagementTypeVisitNote.setEngagementType(e.get());
                        engagementTypeVisitNote.setVisitNote(v.get());
                        engagementTypeVisitNoteService.save(engagementTypeVisitNote);
                    }
                }
            }
        }

        if(visitsOptional.isPresent()) {

            if (files != null) {
                for (MultipartFile fileUpload : files) {
                    FileUploadVisitNote file = new FileUploadVisitNote();
                    file.setNameFile(fileUpload.getOriginalFilename());
                    file.setDefaultImage(fileUpload.getBytes());
                    file.setContentType(fileUpload.getContentType());
                    file.setVisitNote(v.get());
                    fileUploadService.save(file);
                }
            }
        }

        return visitNoteRepository.save(v.get());
    }

    @Override
    public VisitNote saveVisitNoteMobile(List<MultipartFile> files, VisitNoteMobileDTO visitNoteDTO) throws IOException {
        VisitNote v = new VisitNote();
        User user = userService.getCurrentUser();

        Optional<Visits> visitsOptional = visitsService.getVisitById(visitNoteDTO.getVisits().getId());

        if(visitsOptional.isPresent()) {

            Visits visits = visitsOptional.get();

            v.setDateCreated(LocalDateTime.now());
            v.setUser(user);
            v.setNoteVisit(visitNoteDTO.getNoteVisit());
            v.setVisits(visits);

            List<UserAfbDTO> userAfbDTOList = afbExternalService.getAllUserSupervisorsByUSerCode(user.getUserCode());

            v = visitNoteRepository.save(v);

            for (UserAfbDTO userAfbDTO : userAfbDTOList) {
                User userRecieved = userAfbsService.getUserById(userAfbDTO.getUserCode());

                if (userRecieved != null) {

                    ConfigApplication configApplications = configApplicationService.findOne();

                    Notifications notification = new Notifications();
                    notification.setCreateBy(user);
                    notification.setTypeNotification("EXECUTION DE LA VISITE");
                    notification.setCreateDate(LocalDateTime.now());
                    notification.setDescriptionNotification(patternNotification(configApplications != null ? configApplications.getPatternExecutionVisit() : "", v.getVisits(), user));
                    notification.setStatus(Boolean.TRUE);
                    notification.setSendTo(userRecieved);

                    notificationRepository.save(notification);

                    sendMail(userRecieved, patternNotification(configApplications != null ? configApplications.getPatternExecutionVisit() : "", v.getVisits(), user));
                }
            }

            if (visitNoteDTO.getProduct() != null) {
                for (ProductDTO productDTO : visitNoteDTO.getProduct()) {

                    Optional<Products> p = productsService.findProductsByIdProduct(productDTO.getId());

                    if (p.isPresent()) {
                        ProductVisitNote productVisitNote = new ProductVisitNote();
                        productVisitNote.setProducts(p.get());
                        productVisitNote.setVisitNote(v);
                        productVisitNoteService.save(productVisitNote);
                    }
                }
            }

            if (visitNoteDTO.getEngagement() != null) {
                for (EngagementTypeDTO engagementTypeDTO : visitNoteDTO.getEngagement()) {
                    Optional<EngagementType> e = engagementTypeRepository.findById((long) engagementTypeDTO.getId());

                    if (e.isPresent()) {
                        EngagementTypeVisitNote engagementTypeVisitNote = new EngagementTypeVisitNote();
                        engagementTypeVisitNote.setEngagementType(e.get());
                        engagementTypeVisitNote.setVisitNote(v);
                        engagementTypeVisitNoteService.save(engagementTypeVisitNote);
                    }
                }
            }

            if (files != null) {
                for (MultipartFile fileUpload : files) {
                    FileUploadVisitNote file = new FileUploadVisitNote();
                    file.setNameFile(fileUpload.getOriginalFilename());
                    file.setDefaultImage(fileUpload.getBytes());
                    file.setContentType(fileUpload.getContentType());
                    file.setVisitNote(v);
                    fileUploadService.save(file);
                }
            }

            //visits.setExecution(Boolean.TRUE);
            visits.setSaveExecution(Boolean.TRUE);
            visitsRepository.save(visits);
        }

        return v;
    }

    @Override
    public Optional<VisitNote> findVisitNoteById(Long id) {
        return visitNoteRepository.findVisitNoteById(id);
    }

    @Override
    public Optional<VisitNote> findVisitNoteByidVisit(Long id) {
        return visitNoteRepository.findVisitNoteByVisitsId(id);
    }

    @Override
    public DetailsVisitNoteBeans detailVisitNote(String visitCode) {
        Optional<VisitNote> v = visitNoteRepository.findVisitNoteByVisitsVisitCode(visitCode);
        return v.map(visitNote -> new DetailsVisitNoteBeans(visitNote.getId(), visitNote,
                productVisitNoteService.loadAllByVisitNoteId(visitNote.getId()),
                fileUploadService.findFileUploadByIdVisitNote(visitNote.getId()),
                engagementTypeVisitNoteService.findAllByVisitNoteId(visitNote.getId()))).orElse(null);
    }

    @Override
    public VisitNote getVisitNoteByVisitCode(String visitCode) {
        return visitNoteRepository.findVisitNoteByVisitsVisitCode(visitCode).orElse(null);
    }

    @Override
    public DetailsVisitNoteBeans getvisitnotebyvisitsCode(String visitCode) {
        Optional<VisitNote> v = visitNoteRepository.findVisitNoteByVisitsVisitCode(visitCode);
        return v.map(visitNote -> new DetailsVisitNoteBeans(visitNote.getId(), visitNote,
                productVisitNoteService.loadAllByVisitNoteId(visitNote.getId()),
                fileUploadService.findFileUploadByIdVisitNote(visitNote.getId()),
                engagementTypeVisitNoteService.findAllByVisitNoteId(visitNote.getId()))).orElse(null);
    }

    @Override
    public DetailsVisitNoteBeans getVisitNoteByVisitsCode(String visitCode) {
        Optional<VisitNote> v = visitNoteRepository.findVisitNoteByVisitsCode(visitCode);
        return v.map(visitNote -> new DetailsVisitNoteBeans(visitNote.getId(), visitNote,
                productVisitNoteService.loadAllByVisitNoteId(visitNote.getId()),
                fileUploadService.findFileUploadByIdVisitNote(visitNote.getId()),
                engagementTypeVisitNoteService.findAllByVisitNoteId(visitNote.getId()))).orElse(null);
    }

    @Override
    public Long countVisitNoteByCurrentYear(String codeClient) {
        return visitNoteRepository.countVisitNoteByCurrentYear(codeClient);
    }

    @Override
    public VisitNote getLastVisitNote(String codeClient) {
        return visitNoteRepository.getLastVisitNote(codeClient);
    }

    @Override
    public void getBeetwenDateVisitNoteCreated() {
        List<VisitNote> visitNotes = visitNoteRepository.getNumberVisiteToExcecuted();
        LocalDateTime dateTime = LocalDateTime.now();
        Long value = null;

        for(VisitNote v : visitNotes) {
            value = ChronoUnit.DAYS.between(v.getDateValidated(), dateTime);
            if(value == 2L){
                sendNotificationVisit(v, value);
                sendMail(v);
            }
        }
    }

    private void sendMail(User user, String content) {
        if (user.getEmail() != null) {
            mailService.sendEmail(user.getEmail(), "EXECUTION DE LA VISITE",
                    content, true, true);
        }
    }

    private static String patternNotification(String text, Visits visits, User user) {

        text = text
                .replace("{NOM_CLIENT}", visits.getClient().getFirstName().toUpperCase()
                        + " " + visits.getClient().getLastName().toUpperCase());
        text = text.replace("{DATE_VISITE}", visits.getVisitDateText());
        text = text.replace("{NOM_EXPEDITEUR}", user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
        return text;
    }

    /**
     * send mail
     * @param v by Visit
     */
    private void sendMail(VisitNote v) {
        if(v.getVisits().getUserAfb().getEmail() != null) {
            mailService.sendEmail(v.getVisits().getUserAfb().getEmail(),
        "RAPPEL DE LA VISITE EN ATTENTE DE RAPPORT", "Nom du Client : <b>" + v.getVisits().getClient().getFirstName() + " " + v.getVisits().getClient().getLastName() + "</b><br/> " +
                "Date de la visite : <b>" + v.getVisits().getVisitDateText() + "</b><br/>" +
                "Date d'exécution de la visite: <b>" + v.getDateCreatedText() + "</b><br/>" +
                "Date de validation de la visite exécutée: <b>" + v.getDateValidatedText() + "</b><br/>" +
                "Type de visite : <b>" + v.getVisits().getVisitType() + "</b><br/>" +
                "Moyen utilisé : <b>" + v.getVisits().getMoyenUtilise() + "</b><br/><br/>", true, true);
        }
    }

    private void sendNotificationVisit(VisitNote v, Long value) {
        Notifications notifications = new Notifications();
        notifications.setSendTo(v.getVisits().getUserAfb());
        notifications.setCreateDate(LocalDateTime.now());
        notifications.setStatus(false);
        notifications.setCreateBy(v.getVisits().getUserAfb());
        notifications.setTypeNotification("RAPPEL DE LA VISITE EN ATTENTE DE RAPPORT");
        notifications.setDescriptionNotification("Nom du Client : <b>" + v.getVisits().getClient().getFirstName() + " " + v.getVisits().getClient().getLastName() + "</b><br/> " +
            "Date de la visite : <b>" + v.getVisits().getVisitDateText() + "</b><br/>" +
            "Date d'exécution de la visite : <b>" + v.getDateCreatedText() + "</b><br/>" +
            "Date de validation de la visite exécutée: <b>" + v.getDateValidatedText() + "</b><br/>" +
            "Type de visite : <b>" + v.getVisits().getVisitType() + "</b><br/>" +
            "Moyen utilisé : <b>" + v.getVisits().getMoyenUtilise() + "</b><br/><br/>");
        notificationRepository.save(notifications);
    }
}
