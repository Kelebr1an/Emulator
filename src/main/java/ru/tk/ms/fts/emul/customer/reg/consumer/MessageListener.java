package ru.tk.ms.fts.emul.customer.reg.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import ru.tk.ms.fts.emul.customer.reg.exception.ElementNotFoundException;
import ru.tk.ms.fts.emul.customer.reg.model.MQMessage;
import ru.tk.ms.fts.emul.customer.reg.service.*;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final MQMessageService mqMessageService;

    private final DocumentBuilder documentBuilder;

    private final OrganizationService organizationService;

    private final ParticipantService participantService;

    private final ResultInfoService resultInfoService;

    private final WrongInnService wrongInnService;

    private static final String REQUEST_MESSAGE_TYPE = "GW.CREATE.REQ";

    private static final String WRONG_INN_ID = "927f2b8b-29a5-4f37-babb-ca30fc41221d";

    @Value("${settings.innChecker}")
    private boolean innChecker;

    @JmsListener(destination = "${mq.queues.in}", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Message message) throws JMSException, IOException, SAXException, JAXBException {
        byte[] body = message.getBody(byte[].class);

        log.info("Получено сообщение с телом:\n {}", new String(body, StandardCharsets.UTF_8));

        var document = documentBuilder.parse(new ByteArrayInputStream(body));
        var messageType = mqMessageService.getMessageElement(
                document,
                MQMessage.MessageElements.MESSAGE_TYPE.getElement());

        log.info("Тип сообщения: '{}'", messageType);

        if (messageType.equals(REQUEST_MESSAGE_TYPE)) {

            var organizationInn = mqMessageService.getMessageElement(
                    document,
                    MQMessage.MessageElements.INN.getElement());

            var organizationFromDB = organizationService.getOrganizationByInn(organizationInn);

            if (wrongInnService.getWrongInnByInn(organizationInn) != null) {

                try {
                    var resultInfo = resultInfoService.getResultInfoByID(UUID.fromString(WRONG_INN_ID));
                    var mqMessage = new MQMessage(MQMessage.MessageType.ERROR,
                            mqMessageService.createMessage(resultInfo, document));

                    mqMessageService.sendMessage(mqMessage);

                    log.info("Код ошибки: '{}'. Описание: '{}'", resultInfo.getCode(), resultInfo.getDescription());

                } catch (ElementNotFoundException exception) {
                    log.error(exception.getMessage());
                }

            } else if (organizationFromDB == null) {

                try {
                    var organization = organizationService.insertOrganization(document);
                    var participant = participantService.insertParticipant(organization);
                    var mqMessage = new MQMessage(MQMessage.MessageType.SUCCESS,
                            mqMessageService.createMessage(participant, document));

                    mqMessageService.sendMessage(mqMessage);

                    log.info("Participant_ID: '{}'", participant.getParticipantId());

                } catch (ElementNotFoundException exception) {
                    log.error(exception.getMessage());
                }

            } else if (organizationFromDB.getInn().equals(organizationInn)) {

                if (!innChecker) {
                    var organization = organizationService.insertOrganization(document);
                    var participant = participantService.insertParticipant(organization);
                    var mqMessage = new MQMessage(MQMessage.MessageType.SUCCESS,
                            mqMessageService.createMessage(participant, document));

                    mqMessageService.sendMessage(mqMessage);

                    log.info("Participant_ID: '{}'", participant.getParticipantId());

                } else {

                    var participantFromDB = organizationFromDB.getParticipant();
                    var mqMessage = new MQMessage(MQMessage.MessageType.SUCCESS,
                            mqMessageService.createMessage(participantFromDB, document));

                    mqMessageService.sendMessage(mqMessage);

                    log.info("ИНН '{}' уже существует в базе данных. Participant_ID: '{}'", organizationInn, participantFromDB.getParticipantId());
                }
            }

        }
    }

}

