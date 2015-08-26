package com.hegel.xpath;

import net.sf.saxon.s9api.XdmNode;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Vyacheslav Lapin (http://vlapin.ru)
 * @version 0.1 (7/27/2015 10:35 PM).
 */
public class TypedXPathQuery<T> {

    private Function<String, T> transformer;
    private XPathQuery xPathQuery;

    public TypedXPathQuery(String xPathExpression, Function<String, T> transformer) {
        xPathQuery = new XPathQuery(xPathExpression);
        this.transformer = transformer;
    }

    protected Stream<T> stream(XdmNode document) {
        return xPathQuery.stream(document).map(transformer);
    }

    public Stream<T> stream(Path path) {
        return xPathQuery.stream(path).map(transformer);
    }

    public Stream<T> stream(InputStream inputStream) {
        return xPathQuery.stream(inputStream).map(transformer);
    }

    public Stream<T> stream(String xml) {
        return xPathQuery.stream(xml).map(transformer);
    }
}