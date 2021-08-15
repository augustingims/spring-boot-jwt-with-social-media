package cm.skysoft.app.service;

import cm.skysoft.app.domain.VisitNote;
import cm.skysoft.app.dto.VisitNoteDTO;
import cm.skysoft.app.service.beans.DetailsVisitNoteBeans;
import cm.skysoft.app.service.dto.VisitNoteMobileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface VisitNoteService {

    /**
     * save visitNote
     * @param visitNoteDTO by visitNoteDTO
     * @return save visitNote
     */
    VisitNote save(VisitNoteDTO visitNoteDTO);

    /**
     *
     * @param visitNoteDTO by visitNoteDTO
     * @return update visitNote
     */
    VisitNote update(VisitNoteDTO visitNoteDTO);

    /**
     * validate visit note
     * @param codeVisitNote by idVisitNote
     */
    VisitNote validatedVisitNote(String codeVisitNote);

    /**
     * save visiNote by mobile
     * @param files of files
     * @param visitNoteDTO of visitNoteDTO
     * @return new visitNote
     */
    VisitNote saveVisitNoteMobile(List<MultipartFile> files, VisitNoteMobileDTO visitNoteDTO) throws IOException;

    /**
     * update visiNote by mobile
     * @param files of files
     * @param visitNoteDTO of visitNoteDTO
     * @return new visitNote
     */
    VisitNote updateVisitNoteMobile(List<MultipartFile> files, VisitNoteMobileDTO visitNoteDTO) throws IOException;

    /**
     * find Visit Note by id
     * @param id of visitNote
     * @return visitNote by id
     */
    Optional<VisitNote> findVisitNoteById(Long id);

    /**
     * find Visit Note by idVisit
     * @param id of visitNote
     * @return visitNote by idVisit
     */
    Optional<VisitNote> findVisitNoteByidVisit(Long id);

    /**
     * find detail Visit Note by visitCode
     * @param visitCode of visit
     * @return detail visitNote by visitCode
     */
    DetailsVisitNoteBeans detailVisitNote(String visitCode);

    /**
     * get Visit Note by visitCode
     * @param visitCode of visitCode
     * @return visitNote by visitCode
     */
    VisitNote getVisitNoteByVisitCode(String visitCode);

    /**
     * get visitNote by visitCode
     * @param visitCode by visitCode
     * @return visitNote by visitCode
     */
    DetailsVisitNoteBeans getvisitnotebyvisitsCode(String visitCode);

    /**
     * get visitNote by visitCode
     * @param visitCode by visitCode
     * @return visitNote by visitCode
     */
    DetailsVisitNoteBeans getVisitNoteByVisitsCode(String visitCode);

    /**
     * @param codeClient by codeClient
     * @return number visit by current year
     */
    Long countVisitNoteByCurrentYear(String codeClient);

    /**
     *
     * @param codeClient by codeClient
     * @return last visit
     */
    VisitNote getLastVisitNote(String codeClient);

    /**
     * comparison of the current date with the creation date
     */
    void getBeetwenDateVisitNoteCreated();
}
