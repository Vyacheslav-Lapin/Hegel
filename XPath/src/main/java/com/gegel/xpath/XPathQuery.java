package com.gegel.xpath;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathSelector;
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
 * @version 0.1 (7/27/2015 9:03 PM).
 */
public interface XPathQuery extends XPathQueryExecutor {

    XPathSelector getSelector();

    static XPathQuery from(String xPathExpression) {
        return () -> toSelector(xPathExpression);
    }

    default Stream<String> stream(XdmNode document) {
        return XPathQueryExecutor.stream(document, getSelector());
    }

    default Stream<String> stream(Path path) {
        try {
            return stream(DOCUMENT_BUILDER.build(path.toFile()));
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }

    default Stream<String> stream(InputStream inputStream) {
        try {
            return stream(DOCUMENT_BUILDER.build(new StreamSource(inputStream)));
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }

    default Stream<String> stream(String xml) {
        try {
            return stream(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}