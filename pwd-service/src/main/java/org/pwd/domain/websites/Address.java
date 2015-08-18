package org.pwd.domain.websites;

import javax.persistence.Embeddable;

/**
 * @author bartosz.walacik
 */
@Embeddable
public class Address {
    private final String city;
    private final String province;

    public Address(String city, String province) {
        this.city = city;
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }
}
