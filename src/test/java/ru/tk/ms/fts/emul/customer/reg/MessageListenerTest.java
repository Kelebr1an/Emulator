package ru.tk.ms.fts.emul.customer.reg;

import com.ibm.jms.JMSBytesMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xml.sax.SAXException;
import ru.tk.ms.fts.emul.customer.reg.consumer.MessageListener;
import ru.tk.ms.fts.emul.customer.reg.model.Organization;
import ru.tk.ms.fts.emul.customer.reg.repository.OrganizationRepository;
import ru.tk.ms.fts.emul.customer.reg.repository.ParticipantRepository;

import javax.jms.JMSException;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageListenerTest {
    @Autowired
    private MessageListener messageListener;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    private static final String TEST_INN = "773175231270";

    private static final String TEST_FILE_NAME = "GW.CREATE.REQ.xml";

    @BeforeEach
    void beforeEach() {
        organizationRepository.deleteAll();
        participantRepository.deleteAll();
    }

    @Test
    void checkInnIsPresentInDB() throws IOException, JMSException, SAXException, JAXBException {
        var messageFile = TestUtils.readFile(TEST_FILE_NAME);
        assertTrue(messageFile.isPresent(), "File " + TEST_FILE_NAME + " is not present");

        var messageBody = new String(messageFile.get().readAllBytes());

        var message = mock(JMSBytesMessage.class);

        when(message.getBody(byte[].class))
                .thenReturn(messageBody.getBytes(StandardCharsets.UTF_8));

        messageListener.receiveMessage(message);

        Optional<Organization> organization = organizationRepository.findAll().stream().findFirst();

        organization.ifPresent(value -> assertEquals(TEST_INN, value.getInn(), "Wrong INN value"));
    }
}
