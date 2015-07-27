package com.gegel.xpath;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static com.gegel.xpath.XPathQueryExecutor.toSelector;

/**
 * @author Vyacheslav Lapin (http://vlapin.ru)
 * @version 0.1 (7/27/2015 8:24 PM).
 */
@FunctionalInterface
public interface Xml extends XPathQueryExecutor {

    XdmNode toXMLDocument();

    static Xml from(Path path) {
        return () -> {
            try {
                return DOCUMENT_BUILDER.build(path.toFile());
            } catch (SaxonApiException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static Xml from(InputStream inputStream) {
        return () -> {
            try {
                return DOCUMENT_BUILDER.build(new StreamSource(inputStream));
            } catch (SaxonApiException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static Xml from(String xml) {
        return () -> {
            try {
                return DOCUMENT_BUILDER.build(new StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
            } catch (UnsupportedEncodingException | SaxonApiException e) {
                throw new RuntimeException(e);
            }
        };
    }

    default Stream<String> stream(String xPathExpression) {
        return XPathQueryExecutor.stream(toXMLDocument(), toSelector(xPathExpression));
    }

    default Optional<String> getRootNamespaceURI() {
        return stream("namespace-uri(/*)").findFirst();
    }

    default String toText() {
        return toXMLDocument().toString();
    }
}