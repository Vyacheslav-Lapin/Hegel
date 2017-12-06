package com.hegel.xpath;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CompactXmlSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.jsoup.Connection;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("WeakerAccess")
public class Xhtml extends Xml {

    private static HtmlCleaner HTML_CLEANER = new HtmlCleaner();
    private static CleanerProperties PROPS = HTML_CLEANER.getProperties();
    private static Pattern PATTERN = Pattern.compile("<!(DOCTYPE\\s+[^>]*)>");

    protected Xhtml(InputStream inputStream) {
        super(toXhtmlInputStream(inputStream));
    }

    protected Xhtml(Path path) throws FileNotFoundException {
        this(new FileInputStream(path.toFile()));
    }

    protected Xhtml(String html, Charset encoding) {
        this(new ByteArrayInputStream(html.getBytes(encoding)));
    }


    protected Xhtml(String html) {
        this(html, UTF_8);
    }

    protected Xhtml(URL url, Map<String, String> headers, Connection.Method method, int timeout) throws UnsupportedEncodingException {
        this(HttpRequest.get(url, headers, method, timeout));
    }

    protected Xhtml(URL url) {
        this(HttpRequest.get(url));
    }

    public static Xhtml from(InputStream inputStream) {
        return new Xhtml(inputStream);
    }

    public static Xhtml from(String html, Charset charset) {
        return new Xhtml(html, charset);
    }

    public static Xhtml from(Path path) {
        try {
            return new Xhtml(new String(Files.readAllBytes(path), UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Xhtml from(String html) {
        return from(html, UTF_8);
    }

    public static Xhtml from(URL url, Map<String, String> headers, Connection.Method method, int timeout) {
        try {
            return new Xhtml(url, headers, method, timeout);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Xhtml from(URL url) {
        return new Xhtml(url);
    }

    protected static String getCleanDocument(InputStream htmlInputStream) {
        try (InputStream inputStream = htmlInputStream) {
            // Comment DOCTYPE-section, because Saxon (and Altova XMLSpy too) can`t work with it...
            return PATTERN.matcher(
                    new CompactXmlSerializer(PROPS)
                            .getAsString(HTML_CLEANER.clean(inputStream)))
                    .replaceFirst("<!--$1-->");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static InputStream toXhtmlInputStream(InputStream htmlInputStream) {
        return new ByteArrayInputStream(getCleanDocument(htmlInputStream).getBytes(UTF_8));
    }
}
