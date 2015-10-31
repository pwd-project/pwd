package org.pwd.domain.download;

import java.util.Optional;

/**
 * @author kadamowi
 */
public enum Template {
    t11 (1, 0, "Szablon PWD T1.1", "szablon-pwd-t1-1", "Szablon Biuletynu Informacji Publicznej w wersji szarej"),
    t12 (0, 0, "Szablon PWD T1.2", "szablon-pwd-t1-2", "Szablon Biuletynu Informacji Publicznej w wersji zielonej"),
    t13 (0, 1, "Szablon PWD T1.3", "szablon-pwd-t1-3", "Szablon Biuletynu Informacji Publicznej w wersji czerwonej"),
    t21 (1, 0, "Szablon PWD T2.1", "szablon-pwd-t2-1", "Alternatywny szablon Biuletynu Informacji Publicznej w kolorystyce szarej"),
    t22 (0, 0, "Szablon PWD T2.2", "szablon-pwd-t2-2", "Alternatywny szablon Biuletynu Informacji Publicznej w kolorystyce niebieskiej"),
    t23 (0, 1, "Szablon PWD T2.3", "szablon-pwd-t2-3", "Alternatywny szablon Biuletynu Informacji Publicznej w kolorystyce czerwonej"),
    t31 (1, 0, "Szablon PWD T3.1", "szablon-pwd-t3-1", "Szablon dla Urzędu Miasta lub Gminy w wersji podstawowej (z elementami BIP)"),
    t32 (0, 0, "Szablon PWD T3.2", "szablon-pwd-t3-2", "Szablon dla Urzędu Miasta lub Gminy w wersji okolicznościowej (z elementami BIP)"),
    t33 (0, 1, "Szablon PWD T3.3", "szablon-pwd-t3-3", "Szablon dla Urzędu Miasta lub Gminy w wersji alternatywnej (z elementami BIP)"),
    t41 (1, 0, "Szablon PWD T4.1", "szablon-pwd-t4-1", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej"),
    t42 (0, 0, "Szablon PWD T4.2", "szablon-pwd-t4-2", "Szablon dla Stowarzyszenia lub Fundacji w wersji alternatywnej"),
    t43 (0, 1, "Szablon PWD T4.3", "szablon-pwd-t4-3", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej z elementami BIP"),
    t51 (1, 0, "Szablon PWD T5.1", "szablon-pwd-t5-1", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej"),
    t52 (0, 0, "Szablon PWD T5.2", "szablon-pwd-t5-2", "Szablon dla Stowarzyszenia lub Fundacji w wersji alternatywnej"),
    t53 (0, 1, "Szablon PWD T5.3", "szablon-pwd-t5-3", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej z elementami BIP"),
    t61 (1, 0, "Szablon PWD T6.1", "szablon-pwd-t6-1", "Szablon dla Stowarzyszenia lub Fundacji w wersji podstawowej"),
    t62 (0, 0, "Szablon PWD T6.2", "szablon-pwd-t6-2", "Szablon dla Stowarzyszenia lub Fundacji w wersji alternatywnej"),
    t63 (0, 1, "Szablon PWD T6.3", "szablon-pwd-t6-3", "Szablon dla Stowarzyszenia lub Fundacji w wersji alternatywnej")
    ;

    private int startRow;
    private int endRow;
    private String namePl;
    private String imageName;
    private String description;

    Template(int startRow, int endRow, String namePl, String imageName, String description) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.namePl = namePl;
        this.imageName = imageName;
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

    public int getOrder() {
        return ordinal();
    }
}
