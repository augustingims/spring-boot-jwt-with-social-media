package cm.skysoft.app.service.dto;

import cm.skysoft.app.dto.EngagementTypeDTO;
import cm.skysoft.app.dto.ProductDTO;
import cm.skysoft.app.dto.VisitDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class VisitNoteMobileDTO implements Serializable {
    private Long id;

    private String noteVisit;

    private String localisation;

    private String visitCode;

    private LocalDateTime dateCreation;

    private VisitDTO visits;

    private List<EngagementTypeDTO> engagement;

    private List<ProductDTO> product;

    private List<MultipartFile> files;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoteVisit() {
        return noteVisit;
    }

    public void setNoteVisit(String noteVisit) {
        this.noteVisit = noteVisit;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public VisitDTO getVisits() {
        return visits;
    }

    public void setVisits(VisitDTO visits) {
        this.visits = visits;
    }

    public List<EngagementTypeDTO> getEngagement() {
        return engagement;
    }

    public void setEngagement(List<EngagementTypeDTO> engagement) {
        this.engagement = engagement;
    }

    public List<ProductDTO> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDTO> product) {
        this.product = product;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public String getVisitCode() {
        return visitCode;
    }

    public void setVisitCode(String visitCode) {
        this.visitCode = visitCode;
    }
}

