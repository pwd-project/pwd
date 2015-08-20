package org.pwd.domain.websites;

import com.google.common.base.Preconditions;

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

    private URL url;

    private String administrativeUnit;

    @Embedded
    private Address address;

    // only for Hibernate
    Website(){
    }

    public Website(int id, URL url) {
        Preconditions.checkArgument(url != null);

        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public URL getUrl() {
        return url;
    }

    public String getAdministrativeUnit() {
        return administrativeUnit;
    }
}