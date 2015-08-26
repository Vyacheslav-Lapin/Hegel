package com.hegel.xpath;

import net.sf.saxon.s9api.XdmNode;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CompactXmlSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.jsoup.Connection;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author Vyacheslav Lapin (http://vlapin.ru)
 * @version 0.1 (8/24/2015 7:02 PM).
 */
@FunctionalInterface
public interface Xhtml extends Xml {

    HtmlCleaner HTML_CLEANER = new HtmlCleaner();
    CleanerProperties PROPS = HTML_CLEANER.getProperties();
    Pattern PATTERN = Pattern.compile("<!(DOCTYPE\\s+[^>]*)>");

    static InputStream toXhtmlInputStream(InputStream htmlInputStream) {
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

    static Xhtml fromInputStream(InputStream inputStream) {
        return from(Xml.from(toXhtmlInputStream(inputStream)));
    }

    static Xhtml fromFilePath(Path path) {
        try {
            return from(fromInputStream(new FileInputStream(path.toFile())));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static Xhtml fromHtmlText(String html) {
        return fromHtmlText(html, "UTF-8");
    }

    static Xhtml fromHtmlText(String html, String encoding) {
        try {
            return from(fromInputStream(new ByteArrayInputStream(html.getBytes(encoding))));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static Xhtml fromUrl(URL url, Map<String, String> headers, Connection.Method method, int timeout) {
        return fromHtmlText(Xml.getTextFromUrl(url, headers, method, timeout));
    }


    static Xhtml fromUrl(URL url) {
        return fromHtmlText(Xml.getTextFromUrl(url));
    }

    static Xhtml from(Supplier<XdmNode> xdmNodeSupplier) {
        return xdmNodeSupplier::get;
    }
}