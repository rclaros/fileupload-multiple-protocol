package org.sotech.fileupload.bean;

import java.io.Serializable;
import java.util.Date;

public class Documento implements Serializable {

    private String id;
    private String filename;
    private String userFrom;
    private String userTo;
    private Date created;
    private String checksum;
    private String protocol;
    private Boolean hasContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public Boolean getHasContent() {
        return hasContent;
    }

    public void setHasContent(Boolean hasContent) {
        this.hasContent = hasContent;
    }

    @Override
    public String toString() {
        return "Documento{" + "id=" + id + ", filename=" + filename + ", userFrom=" + userFrom + ", userTo=" + userTo + ", created=" + created + ", checksum=" + checksum + ", protocol=" + protocol + ", hasContent=" + hasContent + '}';
    }
    
    

}
