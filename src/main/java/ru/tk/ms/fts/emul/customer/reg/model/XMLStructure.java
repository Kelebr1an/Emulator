package ru.tk.ms.fts.emul.customer.reg.model;

import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.UUID;


@Setter
@XmlRootElement(name = "Envelope", namespace = "http://www.w3.org/2001/06/soap-envelope")
public class XMLStructure implements Serializable {

    private static final String ENV_URI = "http://www.w3.org/2001/06/soap-envelope";
    private static final String ROUTING_URI = "urn:customs.ru:Envelope:RoutingInf:1.0";
    private static final String GW_URI = "urn:customs.ru:gwadmin:GWHeader:1.0";
    private static final String SIGNATURE_URI = "http://www.w3.org/2000/09/xmldsig#";
    private static final String CT_URI = "urn:customs.ru:gwadmin:ComplexType:1.0";
    private static final String CPI_URI = "urn:customs.ru:gwadmin:CreateParticipantInfo:1.0";
    private static final String RESULT_URI = "urn:customs.ru:gwadmin:Result:1.0";

    @XmlElement(name = "Header", namespace = ENV_URI)
    private Header header;

    @XmlElement(name = "Body", namespace = ENV_URI)
    private Body body;

    @Setter
    public static class Header implements Serializable {

        @XmlElement(name = "RoutingInf", namespace = ROUTING_URI)
        private RoutingInf routingInf;

        @XmlElement(name = "GWHeader", namespace = GW_URI)
        private GWHeader gwHeader;

        @Setter
        public static class RoutingInf implements Serializable {

            @XmlElement(name = "EnvelopeID", namespace = ROUTING_URI)
            private UUID envelopeID;

            @XmlElement(name = "InitialEnvelopeID", namespace = ROUTING_URI)
            private String initialEnvelopeID;

            @XmlElement(name = "SenderInformation", namespace = ROUTING_URI)
            private String senderInformation;

            @XmlElement(name = "ReceiverInformation", namespace = ROUTING_URI)
            private String receiverInformation;

            @XmlElement(name = "PreparationDateTime", namespace = ROUTING_URI)
            private String preparationDateTime;

            @XmlElement(name = "Priority", namespace = ROUTING_URI)
            private String priority;
        }

        @Setter
        public static class GWHeader implements Serializable {

            @XmlElement(name = "MessageType", namespace = GW_URI)
            private String messageType;

            @XmlElement(name = "InfoBrokerId", namespace = GW_URI)
            private UUID infoBrokerId;
        }
    }

    @Setter
    public static class Body implements Serializable {

        @XmlElement(name = "Signature", namespace = SIGNATURE_URI)
        private Signature signature;

        @Setter
        public static class Signature implements Serializable {

            @XmlElement(name = "SignedInfo", namespace = SIGNATURE_URI)
            private SignedInfo signedInfo;

            @XmlElement(name = "SignatureValue", namespace = SIGNATURE_URI)
            private String signatureValue;

            @XmlElement(name = "KeyInfo", namespace = SIGNATURE_URI)
            private KeyInfo keyInfo;

            @XmlElement(name = "Object", namespace = SIGNATURE_URI)
            private ObjectInfo objectInfo;

            @Setter
            public static class SignedInfo implements Serializable {

                @XmlElement(name = "CanonicalizationMethod", namespace = SIGNATURE_URI)
                private CanonicalizationMethod canonicalizationMethod;

                @XmlElement(name = "SignatureMethod", namespace = SIGNATURE_URI)
                private SignatureMethod signatureMethod;

                @XmlElement(name = "Reference", namespace = SIGNATURE_URI)
                private Reference[] reference;

                @Setter
                public static class CanonicalizationMethod implements Serializable {
                    @XmlAttribute(name = "Algorithm")
                    private String algorithm;
                }

                @Setter
                public static class SignatureMethod implements Serializable {
                    @XmlAttribute(name = "Algorithm")
                    private String algorithm;
                }

                @Setter
                public static class Reference implements Serializable {

                    @XmlAttribute(name = "URI")
                    private String uri;

                    @XmlElement(name = "Transforms", namespace = SIGNATURE_URI)
                    private Transforms transforms;

                    @XmlElement(name = "DigestMethod", namespace = SIGNATURE_URI)
                    private DigestMethod digestMethod;

                    @XmlElement(name = "DigestValue", namespace = SIGNATURE_URI)
                    private String digestValue;

                    @Setter
                    public static class Transforms implements Serializable {

                        @XmlElement(name = "Transform", namespace = SIGNATURE_URI)
                        private Transform transform;

                        @Setter
                        public static class Transform implements Serializable {
                            @XmlAttribute(name = "Algorithm")
                            private String algorithm;
                        }

                    }

                    @Setter
                    public static class DigestMethod implements Serializable {
                        @XmlAttribute(name = "Algorithm")
                        private String algorithm;
                    }
                }
            }

            @Setter
            public static class KeyInfo implements Serializable {

                @XmlAttribute(name = "Id")
                private String id;

                @XmlElement(name = "X509Data", namespace = SIGNATURE_URI)
                private X509Data x509Data;

                @Setter
                public static class X509Data implements Serializable {

                    @XmlElement(name = "X509Certificate", namespace = SIGNATURE_URI)
                    private String x509Certificate;

                }

            }

            @Setter
            public static class ObjectInfo implements Serializable {

                @XmlAttribute(name = "Id")
                private String id;

                @XmlElement(name = "CreateParticipantInfo", namespace = CPI_URI)
                private CreateParticipantInfo createParticipantInfo;

                @XmlElement(name = "Result", namespace = RESULT_URI)
                private Result result;

                @Setter
                public static class CreateParticipantInfo implements Serializable {

                    @XmlElement(name = "DocumentID", namespace = CT_URI)
                    private UUID documentID;

                    @XmlElement(name = "RefDocumentID", namespace = CT_URI)
                    private UUID refDocumentID;

                    @XmlElement(name = "ParticipantId", namespace = CPI_URI)
                    private String participantId;

                    @XmlElement(name = "TransportAddress", namespace = CPI_URI)
                    private String transportAddress;

                }

                @Setter
                public static class Result implements Serializable {

                    @XmlElement(name = "DocumentID", namespace = CT_URI)
                    private UUID documentID;

                    @XmlElement(name = "RefDocumentID", namespace = CT_URI)
                    private UUID refDocumentID;

                    @XmlElement(name = "ResultInfo", namespace = RESULT_URI)
                    private ResultInfoXML resultInfoXML;

                    @Setter
                    public static class ResultInfoXML implements Serializable {

                        @XmlElement(name = "Code", namespace = RESULT_URI)
                        private String code;

                        @XmlElement(name = "Description", namespace = RESULT_URI)
                        private String description;

                    }
                }
            }
        }
    }
}
