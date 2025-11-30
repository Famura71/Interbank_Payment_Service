package Api.Soap;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://interbank.com/payment")
public class WithdrawRequest {
    public String userName;
    public double amount;

    public WithdrawRequest() {}
}