package cm.skysoft.app.repository;

import cm.skysoft.app.domain.FileUploadVisitNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadVisitNote, Long> {

    @Query("select f from FileUploadVisitNote f where f.visitNote.id = :id")
    List<FileUploadVisitNote> findFileUploadVisitNotesByVisitNoteId(@Param("id") Long id);
    List<FileUploadVisitNote> findFileUploadVisitNotesByvisitNoteVisitsId(Long id);
    Optional<FileUploadVisitNote> findByNameFile(String nameFile);
    void deleteByNameFile(String nameFile);
}
