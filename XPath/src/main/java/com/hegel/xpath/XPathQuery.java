package com.hegel.xpath;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Lapin (http://vlapin.ru)
 * @version 0.1 (7/27/2015 9:03 PM).
 */
public class XPathQuery {

    private XPathSelector xPathSelector;
    private static XPathQueryExecutor xPathQueryExecutor;

    public XPathQuery(String xPathExpression) {
        if (xPathQueryExecutor == null)
            synchronized (XPathQuery.class) {
                if (xPathQueryExecutor == null)
                    xPathQueryExecutor = new XPathQueryExecutor(); }
        xPathSelector = xPathQueryExecutor.toSelector(xPathExpression);
    }

    protected Stream<String> stream(XdmNode document) {
        return xPathQueryExecutor.stream(document, xPathSelector);
    }

    public Stream<String> stream(Path path) {
        return xPathQueryExecutor.stream(xPathQueryExecutor.toDocument(path), xPathSelector);
    }

    public Stream<String> stream(InputStream inputStream) {
        return xPathQueryExecutor.stream(xPathQueryExecutor.toDocument(inputStream), xPathSelector);
    }

    public Stream<String> stream(String xml) {
        return xPathQueryExecutor.stream(xPathQueryExecutor.toDocument(xml), xPathSelector);
    }
}