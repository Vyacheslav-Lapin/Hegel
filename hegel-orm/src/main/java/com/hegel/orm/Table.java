package com.hegel.orm;

import com.hegel.orm.columns.Column;
import com.hegel.reflect.Class;
import lombok.SneakyThrows;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public interface Table<C> extends Class<C> {

    static <C> Table<C> wrap(java.lang.Class<C> aClass) {
        return () -> aClass;
    }

    static <C> Table<C> wrap(Class<C> cClass) {
        return wrap(cClass.get());
    }

    @SuppressWarnings("unchecked")
    static <C> Table<C> wrap(C c) {
        return wrap((java.lang.Class<C>) c.getClass());
    }

    default String selectQuery() {
        // TODO: 06/07/2017 Realize behavior of classes linkage
//        if (dynamicFields()
//                .map(Field::getType)
//                .filter(baseType -> baseType == BaseType.REFERENCE)
//                .filter(baseType -> baseType))
        return String.format("SELECT %s FROM %s",
                columns().map(Column::toSqlName).collect(joining(", ")),
                get().getSimpleName());
    }

    @SneakyThrows
    default void toLiquibaseXML(Writer writer) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = factory.createXMLStreamWriter(writer);
        xmlStreamWriter.writeStartDocument();

        xmlStreamWriter.writeStartElement("createTable");
        xmlStreamWriter.writeAttribute("tableName", get().getSimpleName());
        xmlStreamWriter.writeDefaultNamespace("http://www.liquibase.org/xml/ns/dbchangelog");
        xmlStreamWriter.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "schemaLocation",
                "http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd");

        columns().forEach(cSqlColumn -> cSqlColumn.writeToLiquibaseXml(xmlStreamWriter));

        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
        xmlStreamWriter.close();
    }

    default String sqlCreateQuery() {
        return String.format("CREATE TABLE %s (%s",
                get().getSimpleName(),
                columns()
                        .map(Column::toCreateQuery)
                        .collect(joining(", ")));
    }

    default Stream<Column<C>> columns() {
        return dynamicFields().map(Column::wrap);
    }
}
