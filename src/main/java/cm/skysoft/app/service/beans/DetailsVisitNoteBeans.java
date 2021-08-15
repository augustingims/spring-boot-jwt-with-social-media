package cm.skysoft.app.service.beans;

import cm.skysoft.app.domain.*;

import java.util.List;

public class DetailsVisitNoteBeans {
    private Long idVisit;
    private VisitNote visitNote;
    private Visits visit;
    private List<ProductVisitNote> products;
    private List<FileUploadVisitNote> fileUploadVisitNotes;
    private List<EngagementTypeVisitNote> engagementTypeVisitNotes;

    public DetailsVisitNoteBeans() {
    }

    public DetailsVisitNoteBeans(Long idVisit, VisitNote visitNote, List<ProductVisitNote> products,
                                 List<FileUploadVisitNote> fileUploadVisitNotes, List<EngagementTypeVisitNote> engagementTypeVisitNotes) {
        this.idVisit = idVisit;
        this.visitNote = visitNote;
        this.products = products;
        this.fileUploadVisitNotes = fileUploadVisitNotes;
        this.engagementTypeVisitNotes = engagementTypeVisitNotes;
    }

    public DetailsVisitNoteBeans(Long idVisit, VisitNote visitNote, List<ProductVisitNote> products,
                                 List<FileUploadVisitNote> fileUploadVisitNotes, List<EngagementTypeVisitNote> engagementTypeVisitNotes, Visits visit) {
        this.idVisit = idVisit;
        this.visitNote = visitNote;
        this.products = products;
        this.fileUploadVisitNotes = fileUploadVisitNotes;
        this.engagementTypeVisitNotes = engagementTypeVisitNotes;
        this.visit = visit;
    }

    public Long getIdVisit() {
        return idVisit;
    }

    public void setIdVisit(Long idVisit) {
        this.idVisit = idVisit;
    }

    public VisitNote getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(VisitNote visitNote) {
        this.visitNote = visitNote;
    }

    public List<ProductVisitNote> getProducts() {
        return products;
    }

    public void setProducts(List<ProductVisitNote> products) {
        this.products = products;
    }

    public List<FileUploadVisitNote> getFileUploadVisitNotes() {
        return fileUploadVisitNotes;
    }

    public void setFileUploadVisitNotes(List<FileUploadVisitNote> fileUploadVisitNotes) {
        this.fileUploadVisitNotes = fileUploadVisitNotes;
    }

    public List<EngagementTypeVisitNote> getEngagementTypeVisitNotes() {
        return engagementTypeVisitNotes;
    }

    public Visits getVisit() {
        return visit;
    }

    public void setVisit(Visits visit) {
        this.visit = visit;
    }
}
