package Api.Soap;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://interbank.com/payment")
public class DepositRequest {
    public String userName;
    public double amount;

    public DepositRequest() {}
}