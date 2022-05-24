package ru.tk.ms.fts.emul.customer.reg.service;

import org.w3c.dom.Document;
import ru.tk.ms.fts.emul.customer.reg.model.MQMessage;
import ru.tk.ms.fts.emul.customer.reg.model.Participant;
import ru.tk.ms.fts.emul.customer.reg.model.ResultInfo;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.ObjectInfo;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Header.GWHeader;

import javax.xml.bind.JAXBException;

public interface MQMessageService {

    void sendMessage(MQMessage mqMessage);

    String getMessageElement(Document document, String elementName);

    byte[] createMessage(Participant participant, Document document) throws JAXBException;

    byte[] createMessage(ResultInfo resultInfo, Document document) throws JAXBException;

    XMLStructure createXMLStructure(ObjectInfo objectInfo, GWHeader gwHeader, Document document);

    byte[] convertXMLtoMessage(XMLStructure xmlStructure) throws JAXBException;

}
