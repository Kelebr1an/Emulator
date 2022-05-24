package ru.tk.ms.fts.emul.customer.reg.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import ru.tk.ms.fts.emul.customer.reg.model.MQMessage;
import ru.tk.ms.fts.emul.customer.reg.model.Participant;
import ru.tk.ms.fts.emul.customer.reg.model.ResultInfo;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.KeyInfo;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.KeyInfo.X509Data;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.ObjectInfo;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.ObjectInfo.CreateParticipantInfo;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.ObjectInfo.Result;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.ObjectInfo.Result.ResultInfoXML;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.SignedInfo;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.SignedInfo.CanonicalizationMethod;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.SignedInfo.Reference;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.SignedInfo.Reference.DigestMethod;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.SignedInfo.Reference.Transforms;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.SignedInfo.Reference.Transforms.Transform;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Body.Signature.SignedInfo.SignatureMethod;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Header.GWHeader;
import ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.Header.RoutingInf;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.tk.ms.fts.emul.customer.reg.model.XMLStructure.*;

@Slf4j
@Service
@EnableJms
public class MQMessageServiceImpl implements MQMessageService {

    @Autowired
    @Qualifier("queueTemplate")
    private JmsTemplate jmsTemplate;

    private static final String TRANSFORMATION_ALGORITHM = "urn:xml-dsig:transformation:v1.1";
    private static final String DIGEST_ALGORITHM = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34112012-256";
    private static final String SIGNATURE_ALGORITHM = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102012-gostr34112012-256";
    private static final String TRANSPORT_ADDRESS = "transportAddress";
    private static final String OBJECT_INFO_ID = "Object";
    private static final String KEY_INFO_ID = "KeyInfo";
    private static final String X509_CERTIFICATE = "MIIRTTCCEPqgAwIBAgIRAOGy2XrEDN2A6RFWtzAFk4QwCgYIKoUDBwEBAwIwggHJMSAwHgYJKoZIhvcNAQkBFhF2dWNAY2EuY3VzdG9tcy5ydTEYMBYGBSqFA2QBEg0xMTE3NzQ2ODg5OTQxMRowGAYIKoUDA4EDAQESDDAwNzczMDY1NDQ3MTELMAkGA1UEBhMCUlUxGDAWBgNVBAgMDzc3INCc0L7RgdC60LLQsDEZMBcGA1UEBwwQ0LMuINCc0L7RgdC60LLQsDEzMDEGA1UECQwq0YPQuy4g0J3QvtCy0L7Qt9Cw0LLQvtC00YHQutCw0Y8sINC0LiAxMS81MXsweQYDVQQKDHLQptC10L3RgtGA0LDQu9GM0L3QvtC1INC40L3RhNC";
    private static final String SIGNATURE_VALUE ="MIIFKAYJKoZIhvcNAQcCoIIFGTCCBRUCAQExDjAMBggqhQMHAQECAgUAMAsGCSqGSIb3DQEHATGCBPEwggTtAgEBMIIB4DCCAckxIDAeBgkqhkiG9w0BCQEWEXZ1Y0BjYS5jdXN0b21zLnJ1MRgwFgYFKoUDZAESDTExMTc3NDY4ODk5NDExGjAYBggqhQMDgQMBARIMMDA3NzMwNjU0NDcxMQswCQYDVQQGEwJSVTEYMBYGA1UECAwPNzcg0JzQvtGB0LrQstCwMRkwFwYDVQQHDBDQsy4g0JzQvtGB0LrQstCwMTMwMQYDVQQJDCrRg9C7LiDQndC";

    private static final String SUCCESS_MESSAGE_TYPE = "GW.CREATE.ACC";
    private static final String ERROR_MESSAGE_TYPE = "GW.ERROR";



    @Override
    public void sendMessage(MQMessage mqMessage) {

        if (mqMessage.getMessageType() == MQMessage.MessageType.SUCCESS) {

            try {
                jmsTemplate.convertAndSend(mqMessage.getMessage());
                log.info("Ответ с Participant_ID отправлен!");
            } catch (JmsException jmsException) {
                log.error(jmsException.getMessage());
            }

        } else if (mqMessage.getMessageType() == MQMessage.MessageType.ERROR) {

            try {
                jmsTemplate.convertAndSend(mqMessage.getMessage());
                log.info("Ответ с ошибкой отправлен!");
            } catch (JmsException jmsException) {
                log.error(jmsException.getMessage());
            }
        }
    }

    @Override
    public String getMessageElement(Document document, String elementName) {
        var elementByTagName = document.getElementsByTagNameNS("*", elementName);
        Stream<Node> value = IntStream.range(0, elementByTagName.getLength())
                .mapToObj(elementByTagName::item);
        return value.map(Node::getTextContent).findFirst().orElse(null);
    }

    public byte[] createMessage(Participant participant, Document document) throws JAXBException {
        var gwHeader = new GWHeader();
        var objectInfo = new ObjectInfo();

        var createParticipantInfo = new CreateParticipantInfo();

        createParticipantInfo.setDocumentID(UUID.randomUUID());
        createParticipantInfo.setRefDocumentID(UUID.fromString(getMessageElement(document,
                MQMessage.MessageElements.DOCUMENT_ID.getElement())));
        createParticipantInfo.setParticipantId(participant.getParticipantId());
        createParticipantInfo.setTransportAddress(TRANSPORT_ADDRESS);

        objectInfo.setCreateParticipantInfo(createParticipantInfo);

        gwHeader.setMessageType(SUCCESS_MESSAGE_TYPE);

        var xmlStructure = createXMLStructure(objectInfo, gwHeader, document);

        return convertXMLtoMessage(xmlStructure);
    }
    public byte[] createMessage(ResultInfo resultInfo, Document document) throws JAXBException {
        var gwHeader = new GWHeader();
        var objectInfo = new ObjectInfo();

        var result = new Result();
        var resultInfoXML = new ResultInfoXML();

        resultInfoXML.setCode(resultInfo.getCode());
        resultInfoXML.setDescription(resultInfo.getDescription());

        result.setDocumentID(UUID.randomUUID());
        result.setRefDocumentID(UUID.fromString(getMessageElement(document,
                MQMessage.MessageElements.DOCUMENT_ID.getElement())));
        result.setResultInfoXML(resultInfoXML);

        objectInfo.setResult(result);

        gwHeader.setMessageType(ERROR_MESSAGE_TYPE);

        var xmlStructure = createXMLStructure(objectInfo, gwHeader, document);

        return convertXMLtoMessage(xmlStructure);
    }

    @Override
    public XMLStructure createXMLStructure(ObjectInfo objectInfo, GWHeader gwHeader, Document document){
        var xmlStructure = new XMLStructure();

        var header = new Header();
        var body = new Body();

        var routingInf = new RoutingInf();

        var signature = new Signature();

        var signedInfo = new SignedInfo();
        var keyInfo = new KeyInfo();

        var canonicalizationMethod = new CanonicalizationMethod();
        var signatureMethod = new SignatureMethod();
        var referenceKey = new Reference();
        var referenceObject = new Reference();


        var transforms = new Transforms();
        var digestMethod = new DigestMethod();

        var transform = new Transform();

        var x509Data = new X509Data();

        objectInfo.setId(OBJECT_INFO_ID);

        x509Data.setX509Certificate(X509_CERTIFICATE);

        keyInfo.setId(KEY_INFO_ID);
        keyInfo.setX509Data(x509Data);

        transform.setAlgorithm(TRANSFORMATION_ALGORITHM);
        digestMethod.setAlgorithm(DIGEST_ALGORITHM);

        transforms.setTransform(transform);

        referenceObject.setTransforms(transforms);
        referenceObject.setDigestMethod(digestMethod);
        referenceObject.setDigestValue("zxc");
        referenceObject.setUri("#Object");

        referenceKey.setTransforms(transforms);
        referenceKey.setDigestMethod(digestMethod);
        referenceKey.setDigestValue("qwe");
        referenceKey.setUri("#KeyInfo");

        signatureMethod.setAlgorithm(SIGNATURE_ALGORITHM);
        canonicalizationMethod.setAlgorithm(TRANSFORMATION_ALGORITHM);

        signedInfo.setCanonicalizationMethod(canonicalizationMethod);
        signedInfo.setSignatureMethod(signatureMethod);
        signedInfo.setReference(new Reference[]{referenceKey, referenceObject});

        signature.setSignedInfo(signedInfo);
        signature.setSignatureValue(SIGNATURE_VALUE);
        signature.setKeyInfo(keyInfo);
        signature.setObjectInfo(objectInfo);

        body.setSignature(signature);

        gwHeader.setInfoBrokerId(UUID.fromString(getMessageElement(document,
                MQMessage.MessageElements.INFO_BROKER_ID.getElement())));

        routingInf.setEnvelopeID(UUID.randomUUID());
        routingInf.setInitialEnvelopeID(getMessageElement(document,
                MQMessage.MessageElements.ENVELOPE_ID.getElement()));
        routingInf.setSenderInformation("senderInformation");
        routingInf.setReceiverInformation("receiverInformation");
        routingInf.setPreparationDateTime(LocalDateTime.now()
                .format(DateTimeFormatter.ISO_DATE_TIME));
        routingInf.setPriority("priority");

        header.setRoutingInf(routingInf);
        header.setGwHeader(gwHeader);

        xmlStructure.setHeader(header);
        xmlStructure.setBody(body);

        return xmlStructure;
    }

    @Override
    public byte[] convertXMLtoMessage(XMLStructure xmlStructure) throws JAXBException{
        var context = JAXBContext.newInstance(XMLStructure.class);
        var marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        var outputStream = new ByteArrayOutputStream();
        marshaller.marshal(xmlStructure, outputStream);

        return outputStream.toByteArray();
    }
}
