package org.pwd.domain.download;

/**
 * Created by kadamowi
 */
public enum Cms {
    WORDPRESS ("WordPress"),
    JOOMLA ("Joomla"),
    DRUPAL ("Drupal")
    ;

    private String namePl;

    Cms(String namePl) {
        this.namePl = namePl;
    }

    public String getNamePl() {
        return namePl;
    }
}
