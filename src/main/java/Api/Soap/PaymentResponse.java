package Api.Soap;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://interbank.com/payment")
public class PaymentResponse {
    public String status;

    public PaymentResponse() {}
}
