package Api.Soap;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://interbank.com/payment")
public class PaymentRequest {
    public double amount;
    public String sender;
    public String receiver;

    public PaymentRequest() {}
}
