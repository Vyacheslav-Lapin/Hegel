package com.hegel.xpath;

import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@SuppressWarnings("WeakerAccess")
public class XPathQueryExecutor {

    protected Processor processor;
    protected DocumentBuilder documentBuilder;
    protected XPathCompiler xPathCompiler;

    public XPathQueryExecutor() {
        this(new Processor(false));
    }

    public XPathQueryExecutor(Processor processor) {
        this(processor, processor.newDocumentBuilder(), processor.newXPathCompiler());
    }

    public XPathQueryExecutor(Processor processor, DocumentBuilder documentBuilder, XPathCompiler xPathCompiler) {
        this.processor = processor;
        this.documentBuilder = documentBuilder;
        this.xPathCompiler = xPathCompiler;
    }

    public Stream<String> stream(InputStream inputStream, String xPathExpression) {
        return stream(toDocument(inputStream), toSelector(xPathExpression));
    }

    public Stream<String> stream(String xml, String xPathExpression) {
        return stream(toDocument(xml), toSelector(xPathExpression));
    }

    public Stream<String> stream(Path path, String xPathExpression) {
        return stream(toDocument(path), toSelector(xPathExpression));
    }


    protected Stream<String> stream(XdmNode document, XPathSelector selector) {
        try {
            selector.setContextItem(document);
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
        return StreamSupport.stream(selector.spliterator(), true).map(XdmItem::getStringValue);
    }

    protected XPathSelector toSelector(String xPathExpression) {
        try {
            return xPathCompiler.compile(xPathExpression).load();
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }

    protected XdmNode toDocument(InputStream inputStream) {
        try {
            return documentBuilder.build(new StreamSource(inputStream));
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }

    protected XdmNode toDocument(String xml) {
        try {
            return toDocument(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected XdmNode toDocument(Path path) {
        try {
            return documentBuilder.build(path.toFile());
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
    }
}