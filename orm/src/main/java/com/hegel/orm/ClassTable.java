package com.hegel.orm;

import com.hegel.reflect.Class;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ClassTable<C> extends Class<C> {

    default String selectQuery() {
        return "SELECT " + columns().map(FieldColumn::toSqlName).collect(Collectors.joining(", ")) + " FROM " + toSrc().getSimpleName();
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
        return "";
    }

    static <C> ClassTable<C> wrap(java.lang.Class<C> aClass) {
        return () -> aClass;
    }

    @SuppressWarnings("unchecked")
    static <C> ClassTable<C> wrap(C aClass) {
        return wrap((java.lang.Class<C>) aClass.getClass());
    }

    default Stream<FieldColumn<C>> columns() {
        return dynamicFields().map(FieldColumn::wrap);
    }
}
