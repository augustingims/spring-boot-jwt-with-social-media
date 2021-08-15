package cm.skysoft.app.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "file_upload_visit_note")
public class FileUploadVisitNote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private VisitNote visitNote;

    @Column(name = "url_file_upload")
    private String urlFileUpload;

    @Column(name = "name_file")
    private String nameFile;

    @Column(name = "content_type")
    private String contentType;

    @JsonDeserialize
    @JsonSerialize
    @Transient
    private byte[] defaultImage;


    public FileUploadVisitNote() {
    }

    public FileUploadVisitNote(VisitNote visitNote, String urlFileUpload, String nameFile, String contentType) {
        this.visitNote = visitNote;
        this.urlFileUpload = urlFileUpload;
        this.nameFile = nameFile;
        this.contentType = contentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VisitNote getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(VisitNote visitNote) {
        this.visitNote = visitNote;
    }

    public String getUrlFileUpload() {
        return urlFileUpload;
    }

    public void setUrlFileUpload(String urlFileUpload) {
        this.urlFileUpload = urlFileUpload;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(byte[] defaultImage) {
        this.defaultImage = defaultImage;
    }

    @Override
    public String toString() {
        return "FileUpload{" +
                "id=" + id +
                ", nameFile='" + nameFile + '\'' +
                '}';
    }
}
