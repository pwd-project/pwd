package org.pwd.domain.websites;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URL;

/**
 * @author bartosz.walacik
 */
@Entity(name = "website")
public class Website {
    @Id
    //@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="web_site_id_seq", name="web_site_id_seq")
    //@GeneratedValue(generator="web_site_id_seq", strategy= GenerationType.SEQUENCE)
    private int id;

    // Special territorial code assigned by governance
    private Integer areaCode;

    private URL url;

    private String unitType;

    private String administrativeUnit;

    // Email to a person who governs this unit
    private String email;

    @Embedded
    private Address address;

    @Embedded
    private Person person;

    // only for Hibernate
    Website() {
    }

    public Website(int id, URL url) {
        Preconditions.checkArgument(url != null);
        this.id = id;
        this.url = url;
    }

    public Website(int id, Integer areaCode, URL url,
                   String unitType, String administrativeUnit, String email,
                   Address address, Person person) {
        this.id = id;
        this.areaCode = areaCode;
        this.url = url;
        this.unitType = unitType;
        this.administrativeUnit = administrativeUnit;
        this.email = email;
        this.address = address;
        this.person = person;
    }

    public Address getAddress() {
        return address;
    }

    public Person getPerson() {
        return person;
    }

    public int getId() {
        return id;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public URL getUrl() {
        return url;
    }

    public String getUnitType() {
        return unitType;
    }

    public String getAdministrativeUnit() {
        return administrativeUnit;
    }

    public String getEmail() {
        return email;
    }
}