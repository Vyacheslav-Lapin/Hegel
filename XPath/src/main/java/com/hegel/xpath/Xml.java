package com.hegel.xpath;

import com.sun.deploy.net.*;
import com.sun.net.ssl.internal.ssl.Provider;
import net.sf.saxon.s9api.XdmNode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.jsoup.Connection.Method.GET;

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
        //TODO: Сделать через Future
        document = xPathQueryExecutor.toDocument(HttpRequest.get(url, headers, method, timeout));
    }

    public Xml(URL url) {
        this();
        //TODO: Сделать через Future
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