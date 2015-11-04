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
    private String unitName;
    private String city;
    private String name;
    private String administrativeEmail;
    private String mobile;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;

    //only for Hibernate
    public DownloadRequest() {
    }

    public DownloadRequest(String templateName, String cms, String unitName, String city, String administrativeEmail) {
        this.templateName = templateName;
        this.cms = cms;
        this.unitName = unitName;
        this.city = city;
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

    public String getUnitName() {
        return unitName;
    }

    public String getAdministrativeEmail() {
        return administrativeEmail;
    }

    public String getCity() {
        return city;
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

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "DownloadRequest{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                ", cms='" + cms + '\'' +
                ", name='" + name + '\'' +
                ", unitName='" + unitName + '\'' +
                ", city='" + city + '\'' +
                ", mobile='" + mobile + '\'' +
                ", administrativeEmail='" + administrativeEmail + '\'' +
                ", created=" + created +
                '}';
    }
}
