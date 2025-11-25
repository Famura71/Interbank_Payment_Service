package Api.Soap;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PaymentEndpoint {

    private static final String NAMESPACE = "http://interbank.com/payment";

    @PayloadRoot(namespace = NAMESPACE, localPart = "PaymentRequest")
    @ResponsePayload
    public PaymentResponse processPayment(@RequestPayload PaymentRequest req) {

        System.out.println("SOAP İsteği Alındı:");
        System.out.println(req.sender + " → " + req.receiver + " (" + req.amount + ")");

        PaymentResponse res = new PaymentResponse();
        res.status = "SUCCESS";

        return res;
    }
}
