package org.pwd.websites;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URL;

/**
 * @author bartosz.walacik
 */
@Entity
public class Website {
    @Id
    private int id;

    private URL url;

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
}
