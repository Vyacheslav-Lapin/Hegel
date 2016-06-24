package com.hegel.orm;

import com.hegel.reflect.fields.Field;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Column<T, C> extends Field<T, C> {

    static <T, C> Column<T, C> wrap(Field<T, C> cField) {
        return cField::toSrc;
    }

    default String toSqlName() {
        StringBuilder result = new StringBuilder();
        for (char character : toSrc().getName().toCharArray())
            if (Character.isUpperCase(character))
                result.append('_').append(Character.toLowerCase(character));
            else
                result.append(character);
        return result.toString();
    }

    static String fromSqlName(String sqlName) {
        StringBuilder result = new StringBuilder();
        char character,
                chars[] = sqlName.toCharArray();
        for (int i = 0; i < chars.length; )
            result.append((character = chars[i++]) == '_' ? Character.toUpperCase(chars[i++]) : character);
        return result.toString();
    }

    default void writeToLiquibaseXml(XMLStreamWriter xmlStreamWriter) {
        try {
            xmlStreamWriter.writeStartElement("column");
            xmlStreamWriter.writeAttribute("name", toSqlName());
            xmlStreamWriter.writeAttribute("type", sqlType());

            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    default String sqlType() {
        return SqlType.from(getOwnerClass()).toString();
//        switch (SqlType.from(getPrimitiveClass())) {
//            case BOOLEAN:
//                return "boolean";
//            case BYTE:
//            case SHORT:
//            case CHAR:
//            case INT:
//                return "int";
//            case LONG:
//                return "long";
//            case FLOAT:
//            case DOUBLE:
//                return "float";
//            case REFERENCE: //todo: String?
//            default:
//                return "object"; //todo: как-то передать что тут будет ссылка с ключём на другую таблицу
//        }
    }

    default Column<T, C> read(C object, ResultSet resultSet) {
        try {
            toSrc().set(object, resultSet.getDouble(toSqlName()));
        } catch (IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    default String toCreateQuery() {
        return toSqlName() + " " + sqlType();
    }
}
