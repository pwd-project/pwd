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

    private String name;
    private String administrativeEmail;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;

    //only for Hibernate
    public DownloadRequest() {
    }

    public DownloadRequest(String name, String administrativeEmail) {
        this.name = name;
        this.administrativeEmail = administrativeEmail;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdministrativeEmail(String administrativeEmail) {
        this.administrativeEmail = administrativeEmail;
    }

    @Override
    public String toString() {
        return "DownloadRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", administrativeEmail='" + administrativeEmail + '\'' +
                ", created=" + created +
                '}';
    }
}
