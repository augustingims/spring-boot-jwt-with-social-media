package cm.skysoft.app.service;

import cm.skysoft.app.domain.FileUploadVisitNote;

import java.util.List;
import java.util.Optional;

public interface FileUploadService {

    /**
     * save fileUpload of the visitNote
     * @param fileUploadVisitNote by file
     * @return save fileUpload of the visitNote
     */
    FileUploadVisitNote save(FileUploadVisitNote fileUploadVisitNote);

    /**
     * find File upload by id
     * @param id of id FileUpload
     * @return fileUpload by Id
     */
    Optional<FileUploadVisitNote> findFileUploadById(Long id);

    /**
     * find File upload by IdVisitNote
     * @param id of id FileUpload
     * @return fileUpload by IdVisitNote
     */
    List<FileUploadVisitNote> findFileUploadByIdVisitNote(Long id);

    /**
     * delete fileUpload by fileUpload name
     * @param nameFile by nameFile
     */
    void deleteAllFileUploadVisitNote(String nameFile);
    /**
     * find File upload by IdVisit
     * @param id of id FileUpload
     * @return fileUpload by IdVisit
     */
    List<FileUploadVisitNote> findFileUploadByIdVisitNoteVisitId(Long id);
}
