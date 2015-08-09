package org.pwd.websites;

import javax.persistence.*;
import java.net.URL;

/**
 * @author bartosz.walacik
 */
@Entity(name = "web_site")
public class Website {
    @Id
    //@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="web_site_id_seq", name="web_site_id_seq")
    //@GeneratedValue(generator="web_site_id_seq", strategy= GenerationType.SEQUENCE)
    private int id;

    private URL url;

    private String administrativeUnit;

    protected Website(){
    }

    public Website(int id, URL url) {
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