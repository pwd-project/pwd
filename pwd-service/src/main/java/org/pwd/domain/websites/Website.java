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
    private int id;

    // Special territorial code assigned by governance
    private Integer areaCode;

    private URL url;

    private String unitType;

    private String administrativeUnit;

    private String administrativeEmail;

    @Embedded
    private Address address;

    @Embedded
    private Person person;

    // email to unit was sended
    @Column(nullable = true)
    private int sended;

    // blocked for audit
    @Column(nullable = true)
    private int blocked;

    // only for Hibernate
    Website() {
    }

    public Website(int id, URL url) {
        Preconditions.checkArgument(url != null);
        this.id = id;
        this.url = url;
    }

    public Website(int id, Integer areaCode, URL url,
                   String unitType, String administrativeUnit, String administrativeEmail,
                   Address address, Person person, int sended, int blocked) {
        this.id = id;
        this.areaCode = areaCode;
        this.url = url;
        this.unitType = unitType;
        this.administrativeUnit = administrativeUnit;
        this.administrativeEmail = administrativeEmail;
        this.address = address;
        this.person = person;
        this.sended = sended;
        this.blocked = blocked;
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

    public String getAdministrativeEmail() {
        return administrativeEmail;
    }

    public int getSended() {
        return sended;
    }

    public void setSended(int sended) {
        this.sended = sended;
    }

    public int getBlocked() {
        return blocked;
    }
}