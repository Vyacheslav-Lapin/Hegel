package com.hegel.xpath;

import net.sf.saxon.s9api.XdmNode;
import org.jsoup.Connection;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class Xml {

    private XdmNode document;
    private static XPathQueryExecutor xPathQueryExecutor;

    private Xml() {
        if (xPathQueryExecutor == null)
            synchronized (XPathQuery.class) {
                if (xPathQueryExecutor == null)
                    xPathQueryExecutor = new XPathQueryExecutor(); }
    }

    public Xml(InputStream inputStream) {
        this();
        document = xPathQueryExecutor.toDocument(inputStream);
    }

    public Xml(Path path) {
        this();
        document = xPathQueryExecutor.toDocument(path);
    }

    public Xml(String xml) {
        this();
        document = xPathQueryExecutor.toDocument(xml);
    }

    public Xml(URL url, Map<String, String> headers, Connection.Method method, int timeout) {
        this();
        //TODO: Rewrite with Future
        document = xPathQueryExecutor.toDocument(HttpRequest.get(url, headers, method, timeout));
    }

    public Xml(URL url) {
        this();
        //TODO: Rewrite with Future
        document = xPathQueryExecutor.toDocument(HttpRequest.get(url));
    }


    public Stream<String> stream(String xPathExpression) {
        return xPathQueryExecutor.stream(document, xPathQueryExecutor.toSelector(xPathExpression));
    }

    public Optional<String> getRootNamespaceURI() {
        return stream("namespace-uri(/*)").findFirst();
    }

    public String toText() {
        return document.toString();
    }
}