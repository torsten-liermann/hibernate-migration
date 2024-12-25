package org.examples.migration.hibernate.types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.IntegerType;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class MyEnumerationPersistence implements UserType, ParameterizedType {

    private static final long serialVersionUID = 1L;

    private final String identifierMethodName = "getKey";

    private String valueOfMethodName = "get";

    private Class<? extends Enum> enumClass;
    private Class<?> identifierType;
    private Method identifierMethod;
    private Method valueOfMethod;
    private IntegerType type = new IntegerType();
    private int[] sqlTypes;


    public void setParameterValues(Properties parameters) {
        String methodName = parameters.getProperty("valueOfMethodName");
        if (methodName != null) {
            valueOfMethodName = methodName;
        }

        String enumClassName = parameters.getProperty("enumClass");
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException cfne) {
            throw new HibernateException("Enum class not found", cfne);
        }

        try {

            identifierMethod = enumClass.getMethod(identifierMethodName,
                    new Class[0]);
            identifierType = identifierMethod.getReturnType();
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain identifier method",
                    e);
        }

        sqlTypes = new int[]{type.sqlType()};

        try {
            valueOfMethod = enumClass.getMethod(valueOfMethodName,
                    new Class[]{identifierType});
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain valueOf method", e);
        }
    }

    public Class<?> returnedClass() {
        return this.enumClass;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor sessionImplementor, Object owner)
            throws HibernateException, SQLException {
        if (rs == null) {
            return null;
        }

        Object identifier = type.get(rs, names[0], sessionImplementor);

        if (identifier == null) {
            return null;
        }
        try {
            Object obj = valueOfMethod.invoke(enumClass,
                    new Object[]{identifier});
            return obj;
        } catch (Exception e) {
            throw new HibernateException(
                    "Exception while invoking valueOf method '"
                            + valueOfMethod.getName() + "' of "
                            + "enumeration class '" + enumClass + "'", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement statement, Object value, int index, SharedSessionContractImplementor sessionImplementor)
            throws HibernateException, SQLException {
        try {
            if (value == null) {
                statement.setNull(index, type.sqlType());
            } else {
                Integer identifier = (Integer) identifierMethod.invoke(value,
                        new Object[0]);
                type.set(statement, identifier, index, sessionImplementor);
            }
        } catch (Exception e) {
            throw new HibernateException(
                    "Exception while invoking identifierMethod '"
                            + identifierMethod.getName() + "' of "
                            + "enumeration class '" + enumClass + "'", e);
        }
    }

    public int[] sqlTypes() {
        return sqlTypes;
    }

    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }
}