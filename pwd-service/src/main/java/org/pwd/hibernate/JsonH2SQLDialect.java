package org.pwd.hibernate;

import org.hibernate.dialect.H2Dialect;

import java.sql.Types;

/**
 * @author bartosz.walacik
 */
public class JsonH2SQLDialect extends H2Dialect {
    public JsonH2SQLDialect() {
        super();
        this.registerColumnType(Types.JAVA_OBJECT, "json");
    }
}
