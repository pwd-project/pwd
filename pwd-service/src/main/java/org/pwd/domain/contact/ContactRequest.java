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

    String name;
    String administrativeEmail;
    String mobile;
    String site;
    String message;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;

    //only for Hibernate
    public ContactRequest() {
    }

    public ContactRequest(String name, String administrativeEmail, String mobile, String site, String message){
        this.name = name;
        this.administrativeEmail = administrativeEmail;
        this.mobile = mobile;
        this.site  = site;
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
}
