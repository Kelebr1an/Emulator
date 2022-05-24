package ru.tk.ms.fts.emul.customer.reg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class MQMessage {

    private MessageType messageType;

    private Object message;

    public enum MessageType {
        SUCCESS, ERROR
    }

    @AllArgsConstructor
    @Getter
    public enum MessageElements {
        INN("Inn"),
        TITLE("Title"),
        ADDRESS("Address"),
        OGRN("Ogrn"),
        ENVELOPE_ID("EnvelopeID"),
        DOCUMENT_ID("DocumentID"),
        INFO_BROKER_ID("InfoBrokerId"),
        MESSAGE_TYPE("MessageType");

        private String element;
    }

}
