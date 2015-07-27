package com.gegel.xpath;

import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.gegel.xpath.XPathQueryExecutor.toSelector;

/**
 * @author Vyacheslav Lapin (http://vlapin.ru)
 * @version 0.1 (7/27/2015 10:35 PM).
 */
public interface TypedXPathQuery<T> extends XPathQuery {

    T transform(String xPathResult);

    static <T> TypedXPathQuery<T> from(String xPathExpression, Function<String, T> transform) {

        return new TypedXPathQuery<T>() {
            @Override
            public T transform(String xPathResult) {
                return transform.apply(xPathExpression);
            }

            @Override
            public XPathSelector getSelector() {
                return toSelector(xPathExpression);
            }
        };
    }

    default Stream<T> typedStream(XdmNode document) {
        return stream(document).map(this::transform);
    }

    default Stream<T> typedStream(Path path) {
        return stream(path).map(this::transform);
    }

    default Stream<T> typedStream(InputStream inputStream) {
        return stream(inputStream).map(this::transform);
    }

    default Stream<T> typedStream(String xml) {
        return stream(xml).map(this::transform);
    }
}