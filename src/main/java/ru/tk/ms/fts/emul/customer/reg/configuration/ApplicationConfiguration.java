package ru.tk.ms.fts.emul.customer.reg.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public DocumentBuilder documentBuilder() throws ParserConfigurationException {
        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setNamespaceAware(true);

        return documentBuilderFactory.newDocumentBuilder();
    }

}
