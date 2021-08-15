package cm.skysoft.app.service.impl;

import cm.skysoft.app.config.ApplicationProperties;
import cm.skysoft.app.domain.FileUploadVisitNote;
import cm.skysoft.app.repository.FileUploadRepository;
import cm.skysoft.app.service.FileUploadService;
import cm.skysoft.app.utils.MethoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final FileUploadRepository fileUploadRepository;


    private final ApplicationProperties applicationProperties;

    public FileUploadServiceImpl(FileUploadRepository fileUploadRepository, ApplicationProperties applicationProperties) {
        this.fileUploadRepository = fileUploadRepository;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public FileUploadVisitNote save(FileUploadVisitNote fileUploadVisitNote){
        uploadFile(fileUploadVisitNote);
        return fileUploadRepository.save(fileUploadVisitNote);
    }

    @Override
    public Optional<FileUploadVisitNote> findFileUploadById(Long id) {
        return fileUploadRepository.findById(id);
    }

    @Override
    public List<FileUploadVisitNote> findFileUploadByIdVisitNote(Long id) {
        return fileUploadRepository.findFileUploadVisitNotesByVisitNoteId(id);
    }

    @Override
    public void deleteAllFileUploadVisitNote(String nameFile) {
        fileUploadRepository.deleteByNameFile(nameFile);
    }

    @Override
    public List<FileUploadVisitNote> findFileUploadByIdVisitNoteVisitId(Long id) {
        return fileUploadRepository.findFileUploadVisitNotesByvisitNoteVisitsId(id);
    }

    private void uploadFile(FileUploadVisitNote fileUploadVisitNote) {

        log.debug("fileUploadServiceImpl: {}", fileUploadVisitNote);

        if (fileUploadVisitNote.getContentType() != null) {

            String prefixe = MethoUtils.getPrefixDocumentByDate();

            String fileStore = applicationProperties.getUpload().getResourcesServerStore() + prefixe + "_" + fileUploadVisitNote.getNameFile();

            String fileUrl = applicationProperties.getUpload().getResourcesServerStoreUrl() + prefixe + "_" + fileUploadVisitNote.getNameFile();


                log.debug("fileStore: {}", fileStore);
                log.debug("fileUrl: {}", fileUrl);

                try (FileOutputStream fileOutputStream = new FileOutputStream(fileStore)) {
                    fileOutputStream.write(fileUploadVisitNote.getDefaultImage());

                    fileUploadVisitNote.setUrlFileUpload(fileUrl);
                    fileUploadVisitNote.setNameFile(prefixe + "_" + fileUploadVisitNote.getNameFile());
                } catch (IOException ex){
                    log.error("Exception occured while zipping file", ex);
                }
        }

    }

}
