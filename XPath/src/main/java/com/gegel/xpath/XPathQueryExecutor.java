package com.gegel.xpath;

import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Vyacheslav Lapin (http://vlapin.ru)
 * @version 0.1 (7/27/2015 8:16 PM).
 */
public interface XPathQueryExecutor {

    Processor PROCESSOR = new Processor(false);
    DocumentBuilder DOCUMENT_BUILDER = PROCESSOR.newDocumentBuilder();
    XPathCompiler XPATH_COMPILER = PROCESSOR.newXPathCompiler();

    static Stream<String> stream(XdmNode document, XPathSelector selector) {
        try {
            selector.setContextItem(document);
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
        return StreamSupport.stream(selector.spliterator(), true).map(XdmItem::getStringValue);
    }

    static Stream<String> stream(InputStream inputStream, String xPathExpression) {
        try {
            return stream(DOCUMENT_BUILDER.build(new StreamSource(inputStream)), toSelector(xPathExpression));
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }

    static Stream<String> stream(String xml, String xPathExpression) {
        try {
            return stream(new ByteArrayInputStream(xml.getBytes("UTF-8")), xPathExpression);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static Stream<String> stream(Path path, String xPathExpression) {
        try {
            return stream(DOCUMENT_BUILDER.build(path.toFile()), toSelector(xPathExpression));
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }

    static XPathSelector toSelector(String xPathExpression) {
        try {
            return XPATH_COMPILER.compile(xPathExpression).load();
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }
}