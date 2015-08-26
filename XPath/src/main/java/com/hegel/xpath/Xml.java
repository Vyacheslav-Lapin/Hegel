package com.hegel.xpath;

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
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.jsoup.Connection.Method.GET;

@FunctionalInterface
public interface Xml extends Supplier<XdmNode> {

    XPathQueryExecutor XPATH_QUERY_EXECUTOR = new XPathQueryExecutor();

    static Xml fromFilePath(Path path) {
        return from(from(path));
    }

    static Xml fromInputStream(InputStream inputStream) {
        return from(from(inputStream));
    }

    static Xml fromXmlText(String xml) {
        return from(from(xml));
    }

    static Xml fromUrl(URL url, Map<String, String> headers, Connection.Method method, int timeout) {
        return from(from(url, headers, method, timeout));
    }

    static Xml fromUrl(URL url) {
        return from(from(url));
    }


    static Xml from(Supplier<XdmNode> xdmNodeSupplier) {
        return xdmNodeSupplier::get;
    }


    static Supplier<XdmNode> from(Path path) {
        XdmNode xmlDocument = XPATH_QUERY_EXECUTOR.toDocument(path);
        return () -> xmlDocument;
    }

    static Supplier<XdmNode> from(InputStream inputStream) {
        XdmNode xmlDocument = XPATH_QUERY_EXECUTOR.toDocument(inputStream);
        return () -> xmlDocument;
    }

    static Supplier<XdmNode> from(String xml) {
        XdmNode xmlDocument = XPATH_QUERY_EXECUTOR.toDocument(xml);
        return () -> xmlDocument;
    }

    static Supplier<XdmNode> from(URL url, Map<String, String> headers, Connection.Method method, int timeout) {
        return from(getTextFromUrl(url, headers, method, timeout));
    }

    static Supplier<XdmNode> from(URL url) {
        return from(getTextFromUrl(url));
    }


    static String getTextFromUrl(URL url) {
        return getTextFromUrl(url, getTypicalHeaders(url), GET, 3000);
    }

    static String getTextFromUrl(URL url, Map<String, String> headers, Connection.Method method, int timeout) {

        if (url.getProtocol().equals("https:"))
            doTrustToCertificates();

        try {
            return Jsoup.connect(url.toString())
                    .data(headers)
                    .method(method)
                    .timeout(timeout)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Map<String, String> TYPICAL_HEADERS = new HashMap<>(5);
    static Map<String, String> getTypicalHeaders(URL url) {
        if (TYPICAL_HEADERS.size() == 0) {
            TYPICAL_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:43.0) Gecko/20100101 Firefox/43.0");
            TYPICAL_HEADERS.put("Accept-Language", "en-US,en;q=0.5");
            TYPICAL_HEADERS.put("Accept-Encoding", "deflate");
            TYPICAL_HEADERS.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }

        TYPICAL_HEADERS.put("Host", url.getHost());

        return TYPICAL_HEADERS;
    }

    static void doTrustToCertificates() {
        try {
            Security.addProvider(new Provider());
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override public X509Certificate[] getAcceptedIssuers() { return null; }
                @Override public void checkServerTrusted(X509Certificate[] c, String a) {}
                @Override public void checkClientTrusted(X509Certificate[] c, String a) {}
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((urlHostName, session) -> {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost()))
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '"
                            + session.getPeerHost() + "'.");
                return true;
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }


    default Stream<String> stream(String xPathExpression) {
        return XPATH_QUERY_EXECUTOR.stream(get(), XPATH_QUERY_EXECUTOR.toSelector(xPathExpression));
    }

    default Optional<String> getRootNamespaceURI() {
        return stream("namespace-uri(/*)").findFirst();
    }

    default String toText() {
        return get().toString();
    }
}