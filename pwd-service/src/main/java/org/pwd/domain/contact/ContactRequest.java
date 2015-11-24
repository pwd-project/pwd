package org.pwd.domain.contact;

import org.pwd.hibernate.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Sławomir Mikulski
 */
@Entity(name = "contact_request")
public class ContactRequest {
    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "contact_request_id_seq", name = "contact_request_id_seq")
    @GeneratedValue(generator = "contact_request_id_seq", strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;
    private String administrativeEmail;
    private String mobile;
    private String site;
    private String message;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;

    //only for Hibernate
    public ContactRequest() {
    }

    public ContactRequest(String name, String administrativeEmail, String mobile, String site, String message) {
        this.name = name;
        this.administrativeEmail = administrativeEmail;
        this.mobile = mobile;
        this.site = site;
        this.message = message;
        this.created = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAdministrativeEmail() {
        return administrativeEmail;
    }

    public String getMobile() {
        return mobile;
    }

    public String getSite() {
        return site;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdministrativeEmail(String administrativeEmail) {
        this.administrativeEmail = administrativeEmail;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
