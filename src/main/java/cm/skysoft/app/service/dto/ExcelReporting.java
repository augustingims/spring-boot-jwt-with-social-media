package cm.skysoft.app.service.dto;

/**
 * Created by francis on 4/28/21.
 */

public class ExcelReporting {

    private String message;
    private String directoryUrl;
    private String fileName;
    private String fileStore;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDirectoryUrl() {
        return directoryUrl;
    }

    public void setDirectoryUrl(String directoryUrl) {
        this.directoryUrl = directoryUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileStore() {
        return fileStore;
    }

    public void setFileStore(String fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public String toString() {
        return "ExcelReporting{" +
                "message='" + message + '\'' +
                ", directoryUrl='" + directoryUrl + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileStore='" + fileStore + '\'' +
                '}';
    }
}
