package org.pwd.domain.download;

import org.pwd.hibernate.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author kadamowi
 */
@Entity(name = "download_request")
public class DownloadRequest {
    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "download_request_id_seq", name = "download_request_id_seq")
    @GeneratedValue(generator = "download_request_id_seq", strategy = GenerationType.SEQUENCE)
    private int id;

    private String templateName;
    private String cms;
    private String file;
    private String name;
    private String administrativeEmail;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;

    //only for Hibernate
    public DownloadRequest() {
    }

    public DownloadRequest(String templateName, String cms, String file, String name, String administrativeEmail) {
        this.templateName = templateName;
        this.cms = cms;
        this.file = file;
        this.name = name;
        this.administrativeEmail = administrativeEmail;
        this.created = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getCms() {
        return cms;
    }

    public String getAdministrativeEmail() {
        return administrativeEmail;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setCms(String cms) {
        this.cms = cms;
    }

    public void setAdministrativeEmail(String administrativeEmail) {
        this.administrativeEmail = administrativeEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "DownloadRequest{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                ", cms='" + cms + '\'' +
                ", file='" + file + '\'' +
                ", name='" + name + '\'' +
                ", administrativeEmail='" + administrativeEmail + '\'' +
                ", created=" + created +
                '}';
    }
}
