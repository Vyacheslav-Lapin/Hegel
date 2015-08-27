package com.hegel.xpath;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CompactXmlSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.jsoup.Connection;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Vyacheslav Lapin (http://vlapin.ru)
 * @version 0.1 (8/24/2015 7:02 PM).
 */
public class Xhtml extends Xml {

    protected Xhtml(InputStream inputStream) {
        super(toXhtmlInputStream(inputStream));
    }

    protected Xhtml(Path path) throws FileNotFoundException {
        this(new FileInputStream(path.toFile()));
    }

    protected Xhtml(String html, String encoding) throws UnsupportedEncodingException {
        this(new ByteArrayInputStream(html.getBytes(encoding)));
    }

    protected Xhtml(String html) throws UnsupportedEncodingException {
        this(html, "UTF-8");
    }

    protected Xhtml(URL url, Map<String, String> headers, Connection.Method method, int timeout) throws UnsupportedEncodingException {
        this(HttpRequest.get(url, headers, method, timeout));
    }

    protected Xhtml(URL url) throws UnsupportedEncodingException {
        this(HttpRequest.get(url));
    }


    public static Xhtml from(InputStream inputStream) {
        return new Xhtml(inputStream);
    }

    public static Xhtml from(Path path) {
        try {
            return new Xhtml(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Xhtml from(String html, String encoding) {
        try {
            return new Xhtml(html, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Xhtml from(String html) {
        return from(html, "UTF-8");
    }

    public static Xhtml from(URL url, Map<String, String> headers, Connection.Method method, int timeout) {
        try {
            return new Xhtml(url, headers, method, timeout);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Xhtml from(URL url) {
        try {
            return new Xhtml(url);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    static private HtmlCleaner HTML_CLEANER = new HtmlCleaner();
    static private CleanerProperties PROPS = HTML_CLEANER.getProperties();
    static private Pattern PATTERN = Pattern.compile("<!(DOCTYPE\\s+[^>]*)>");

    static protected InputStream toXhtmlInputStream(InputStream htmlInputStream) {
        try (InputStream inputStream = htmlInputStream) {
            return new ByteArrayInputStream(
                    // Comment DOCTYPE-section, because Saxon (and Altova XMLSpy too) can`t work with it...
                    PATTERN.matcher(new CompactXmlSerializer(PROPS).getAsString(HTML_CLEANER.clean(inputStream)))
                            .replaceFirst("<!--$1-->")
                            .getBytes("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}