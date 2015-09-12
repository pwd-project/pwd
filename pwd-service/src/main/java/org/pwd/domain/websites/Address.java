package org.pwd.domain.websites;

import javax.persistence.Embeddable;

/**
 * @author bartosz.walacik
 */
@Embeddable
public class Address {
    private String city;

    /**
     * wojewodztwo
     */
    private String voivodeship;

    /**
     * powiat
     */
    private String county;

    //only for Hibernate
    Address() {
    }

    public Address(String city, String voivodeship, String county) {
        this.city = city;
        this.voivodeship = voivodeship;
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public String getVoivodeship() {
        return voivodeship;
    }

    public String getCounty() {
        return county;
    }
}
