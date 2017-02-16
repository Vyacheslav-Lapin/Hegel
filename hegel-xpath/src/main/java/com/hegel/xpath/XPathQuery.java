package com.hegel.xpath;

import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;


@SuppressWarnings("WeakerAccess")
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