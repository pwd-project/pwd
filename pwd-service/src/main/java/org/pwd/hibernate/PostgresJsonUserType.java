package org.pwd.hibernate;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

/**
 * @author bartosz.walacik
 */
public class PostgresJsonUserType implements UserType {
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public Class returnedClass() {
        return Document.class;
    }

    @Override
    public boolean equals(Object o, Object o2) throws HibernateException {
        return Objects.equals(o, o2);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
        if (resultSet.getObject(names[0]) == null) {
            return null;
        }
        PGobject pGobject = (PGobject) resultSet.getObject(names[0]);
        Object jsonObject = null;
        try {
            jsonObject = objectMapper.readValue(pGobject.getValue(), this.returnedClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
        if (value == null) {
            preparedStatement.setNull(index, Types.NULL);
            return;
        }
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PGobject pGobject = new PGobject();
        pGobject.setType("json");
        pGobject.setValue(jsonString);
        preparedStatement.setObject(index, pGobject);
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        return o; //It is not necessary to copy immutable objects
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) this.deepCopy(o);
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return this.deepCopy(serializable);
    }

    @Override
    public Object replace(Object o, Object o2, Object o3) throws HibernateException {
        return this.deepCopy(o);
    }
}
