package cm.skysoft.app.repository;

import cm.skysoft.app.domain.ProductVisitNote;
import cm.skysoft.app.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVisitNoteRepository extends JpaRepository<ProductVisitNote, Long> {

    @Query("select p from ProductVisitNote p where p.visitNote.id = :visitId")
    List<ProductVisitNote> findProductVisitNoteByVisitNoteId(@Param("visitId") Long visitId);

    @Query("select p.products from ProductVisitNote p where p.visitNote.id = :visitNoteId")
    List<Products> findProductByVisitNoteId(@Param(value = "visitNoteId") Long visitNoteId);
}
