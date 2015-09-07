package com.hegel.xpath;

import junit.framework.TestCase;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;

import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.joining;
import static junit.framework.TestCase.assertEquals;

public class XPathTests {

    static final String XML_FILE_PATH = "target/test-classes/document.xml",
            XML = "<beans xmlns=\"http://www.springframework.org/schema/beans\"><!-- TpEvent construction --><bean id=\"tpEventMessageConverter\" class=\"com.db.swdealer.bbgconnector.jms.impl.TpEventMessageConverterImpl\" init-method=\"init\"><property name=\"packageName\" value=\"com.db.rtp.tpmon.tpevent\"/></bean><bean id=\"tpEventObjectFactory\" class=\"com.db.rtp.tpmon.tpevent.ObjectFactory\"/><bean id=\"upstreamNewMessageEvent\" class=\"com.db.swdealer.bbgconnector.tpevent.TpEventBuilder\" init-method=\"init\"><property name=\"fixml\" value=\"true\"/><property name=\"catalogCode\" value=\"6151\"/><property name=\"codeText\" value=\"Received new upstream message. mpp_id:%XML, version:%XML, product:%XML \"/><property name=\"eventFactory\" ref=\"tpEventObjectFactory\"/></bean><!-- Filters --><bean id=\"nullFilter\" class=\"com.db.swdealer.bbgconnector.nodeStream.impl.NullObjectFilter\"/><!-- Camel context section --><camel:camelContext xmlns:camel=\"http://camel.apache.org/schema/spring\"><camel:route><camel:from uri=\"bean:withoutCloneProducer?method=produceMessage\"/><camel:nodeStream><camel:method bean=\"nullFilter\" method=\"isAllowed\"/><camel:to uri=\"bean:notificationCache?method=saveNotification\"/><camel:to uri=\"bean:notificationStatusService2?method=markSentAndSave\"/><camel:to uri=\"bean:waitObject?method=notify\"/></camel:nodeStream></camel:route></camel:camelContext><!-- Test beans section --><bean id=\"waitObject\" class=\"com.db.swdealer.bbgconnector.sync.SyncObject\"/></beans>",
            X_PATH = "//*[namespace-uri()='http://www.springframework.org/schema/beans']/@id";

    @Test
    public void xPathQueryAsObject() {
        TestCase.assertEquals(
                "tpEventMessageConverter, tpEventObjectFactory, upstreamNewMessageEvent, nullFilter, waitObject",
                new XPathQueryExecutor().stream(XML, X_PATH).collect(joining(", ")));
    }

    @Test
    public void invokeRootNamespace() {
        assertEquals(
                "http://www.springframework.org/schema/beans",
                new Xml(get(XML_FILE_PATH)).getRootNamespaceURI().get());
    }

    @Test
    public void xPathQuery() {
        assertEquals(
                "tpEventMessageConverter, tpEventObjectFactory, upstreamNewMessageEvent, nullFilter, waitObject",
                new XPathQuery(X_PATH).stream(get(XML_FILE_PATH)).collect(joining(", ")));
    }

    @Test
    public void xPathQueryToURL() throws MalformedURLException {
        assertEquals(
                "http://www.w3.org/1999/xhtml",
                Xhtml.from(new URL("http://vlapin.ru/"))
                        .getRootNamespaceURI().get());
    }
}