package com.hegel.orm.columns;

import com.hegel.orm.JdbcType;
import com.hegel.reflect.fields.Field;
import lombok.SneakyThrows;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.sql.ResultSet;

import static java.lang.Character.toUpperCase;

public interface Column<C> extends Field<C> {

    static <C> Column<C> wrap(Field<C> cField) {
        return cField::get;
    }

    default String toSqlName() {
        return get().getName().replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    static String fromSqlName(String sqlName) {

        boolean isFirstString = true;
        StringBuilder result = new StringBuilder();
        for (String string : sqlName.split("_"))
            if (isFirstString) {
                result.append(string);
                isFirstString = false;
            } else
                result.append(toUpperCase(string.charAt(0)))
                        .append(string.substring(1));

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
        return JdbcType.from(getOwnerClass()).toString();
//        switch (JdbcType.make(getPrimitiveClass())) {
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

    @SneakyThrows
    default Column<C> read(C object, ResultSet resultSet) {
        get().set(object, resultSet.getDouble(toSqlName()));
        return this;
    }

    default String toCreateQuery() {
        return toSqlName() + " " + sqlType();
    }
}
