package com.hegel.xpath;

import com.sun.net.ssl.internal.ssl.Provider;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;
import static java.security.Security.addProvider;
import static org.jsoup.Connection.Method.GET;

@SuppressWarnings("WeakerAccess")
public class HttpRequest {

    private URL url;
    private Map<String, String> headers;
    private Connection.Method method;
    private int timeout;

    public HttpRequest(URL url) {
        this(url, getTypicalHeaders(url), GET, 3000);
    }

    public HttpRequest(URL url, Map<String, String> headers, Connection.Method method, int timeout) {
        this.url = url;
        this.headers = headers;
        this.method = method;
        this.timeout = timeout;
    }

    public String getResult() {
        if (url.getProtocol().equals("https:"))
            trustToAllCertificates();

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

    static public String get(URL url) {
        return new HttpRequest(url).getResult();
    }

    static public String get(URL url, Map<String, String> headers, Connection.Method method, int timeout) {
        return new HttpRequest(url, headers, method, timeout).getResult();
    }

    private static boolean isTrustToAllCertificatesNotCalled = true;

    private static void trustToAllCertificates() {
        if (isTrustToAllCertificatesNotCalled) // Method should execute one time for program start
            try {
                isTrustToAllCertificatesNotCalled = false;

                addProvider(new Provider());
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[]{new X509TrustManager() {
                    @Override public X509Certificate[] getAcceptedIssuers() { return null; }
                    @Override public void checkServerTrusted(X509Certificate[] c, String a) {}
                    @Override public void checkClientTrusted(X509Certificate[] c, String a) {}
                }}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier((urlHostName, session) -> {
                    if (!urlHostName.equalsIgnoreCase(session.getPeerHost()))
                        out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '"
                                + session.getPeerHost() + "'.");
                    return true;
                });
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }
    }

    private static Map<String, String> TYPICAL_HEADERS = new HashMap<>(5);

    private static Map<String, String> getTypicalHeaders(URL url) {
        if (TYPICAL_HEADERS.size() == 0) {
            TYPICAL_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:43.0) Gecko/20100101 Firefox/43.0");
            TYPICAL_HEADERS.put("Accept-Language", "en-US,en;q=0.5");
            TYPICAL_HEADERS.put("Accept-Encoding", "deflate");
            TYPICAL_HEADERS.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }

        TYPICAL_HEADERS.put("Host", url.getHost());

        return TYPICAL_HEADERS;
    }
}
