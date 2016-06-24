package com.hegel.orm;

import com.hegel.reflect.Class;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Table<C> extends Class<C> {

    default String selectQuery() {
        return "SELECT " + columns().map(Column::toSqlName).collect(Collectors.joining(", ")) + " FROM " + toSrc().getSimpleName();
    }

    default void toLiquibaseXML(Writer writer) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter xmlStreamWriter = factory.createXMLStreamWriter(writer);
            xmlStreamWriter.writeStartDocument();

            xmlStreamWriter.writeStartElement("createTable");
            xmlStreamWriter.writeAttribute("tableName", toSrc().getSimpleName());
            xmlStreamWriter.writeDefaultNamespace("http://www.liquibase.org/xml/ns/dbchangelog");
            xmlStreamWriter.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "schemaLocation",
                    "http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd");

            columns().forEach(cSqlColumn -> cSqlColumn.writeToLiquibaseXml(xmlStreamWriter));

            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    default String sqlCreateQuery() {
        return "CREATE TABLE " + toSrc().getSimpleName() + " (" +
                columns().map(Column::toCreateQuery).collect(Collectors.joining(", "));
    }

    static <C> Table<C> wrap(java.lang.Class<C> aClass) {
        return () -> aClass;
    }

    static <C> Table<C> wrap(Class<C> cClass) {
        return wrap(cClass.toSrc());
    }

    @SuppressWarnings("unchecked")
    static <C> Table<C> wrap(C c) {
        return wrap((java.lang.Class<C>) c.getClass());
    }

    default Stream<Column<C>> columns() {
        return dynamicFields().map(Column::wrap);
    }
}
