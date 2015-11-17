package org.pwd.domain.download;

import java.util.Optional;

/**
 * @author kadamowi
 */
public enum Template {
    T11 (1, 0, "Szablon PWD T1.1", "szablon-pwd-t1-1", "bip_vol_1_1.zip", "Szablon Biuletynu Informacji Publicznej w wersji szarej"),
    T12 (0, 0, "Szablon PWD T1.2", "szablon-pwd-t1-2", "bip_vol_1_2.zip", "Szablon Biuletynu Informacji Publicznej w wersji zielonej"),
    T13 (0, 1, "Szablon PWD T1.3", "szablon-pwd-t1-3", "bip_vol_1_3.zip", "Szablon Biuletynu Informacji Publicznej w wersji czerwonej"),
    T21 (1, 0, "Szablon PWD T2.1", "szablon-pwd-t2-1", "bip_vol_2_1.zip", "Alternatywny szablon Biuletynu Informacji Publicznej w kolorystyce szarej"),
    T22 (0, 0, "Szablon PWD T2.2", "szablon-pwd-t2-2", "bip_vol_2_2.zip", "Alternatywny szablon Biuletynu Informacji Publicznej w kolorystyce niebieskiej"),
    T23 (0, 1, "Szablon PWD T2.3", "szablon-pwd-t2-3", "bip_vol_2_3.zip", "Alternatywny szablon Biuletynu Informacji Publicznej w kolorystyce czerwonej"),
    T31 (1, 0, "Szablon PWD T3.1", "szablon-pwd-t3-1", "", "Szablon dla Urzędu Miasta lub Gminy w wersji podstawowej (z elementami BIP)"),
    T32 (0, 0, "Szablon PWD T3.2", "szablon-pwd-t3-2", "", "Szablon dla Urzędu Miasta lub Gminy w wersji okolicznościowej (z elementami BIP)"),
    T33 (0, 1, "Szablon PWD T3.3", "szablon-pwd-t3-3", "", "Szablon dla Urzędu Miasta lub Gminy w wersji alternatywnej (z elementami BIP)"),
    T41 (1, 0, "Szablon PWD T4.1", "szablon-pwd-t4-1", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej"),
    T42 (0, 0, "Szablon PWD T4.2", "szablon-pwd-t4-2", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji alternatywnej"),
    T43 (0, 1, "Szablon PWD T4.3", "szablon-pwd-t4-3", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej z elementami BIP"),
    T51 (1, 0, "Szablon PWD T5.1", "szablon-pwd-t5-1", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej"),
    T52 (0, 0, "Szablon PWD T5.2", "szablon-pwd-t5-2", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji alternatywnej"),
    T53 (0, 1, "Szablon PWD T5.3", "szablon-pwd-t5-3", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej z elementami BIP"),
    T61 (1, 0, "Szablon PWD T6.1", "szablon-pwd-t6-1", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej"),
    T62 (0, 0, "Szablon PWD T6.2", "szablon-pwd-t6-2", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji alternatywnej"),
    T63 (0, 1, "Szablon PWD T6.3", "szablon-pwd-t6-3", "", "Szablon dla Stowarzyszenia lub Fundacji w wersji alternatywnej")
    ;

    public static final String DOWNLOAD_PATH_WORDPRESS = "http://studzieniec.home.pl/pwd_templates/wordpress";
    public static final String DOWNLOAD_PATH_DRUPAL = "http://studzieniec.home.pl/pwd_templates/drupal";
    public static final String DOWNLOAD_PATH_JOOMLA = "http://studzieniec.home.pl/pwd_templates/joomla";

    private int startRow;
    private int endRow;
    private String namePl;
    private String imageName;
    private String downloadName;
    private String description;

    Template(int startRow, int endRow, String namePl, String imageName, String downloadName, String description) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.namePl = namePl;
        this.imageName = imageName;
        this.downloadName = downloadName;
        this.description = description;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public String getNamePl() {
        return namePl;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public int getOrder() {
        return ordinal();
    }
}
