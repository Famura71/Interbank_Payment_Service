package Api.Soap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import Message.Kafka.TransactionProducer;

@Endpoint
public class PaymentEndpoint {

    private static final String NAMESPACE = "http://interbank.com/payment";

    // Kafka Producer
    @Autowired
    private TransactionProducer transactionProducer;

    @PayloadRoot(namespace = NAMESPACE, localPart = "PaymentRequest")
    @ResponsePayload
    public PaymentResponse processPayment(@RequestPayload PaymentRequest req) {

        System.out.println("SOAP İsteği Alındı:");
        System.out.println(req.sender + " → " + req.receiver + " (" + req.amount + ")");

        //Create Transaction ID
        String transactionId = generateTransactionId();

        // Make Transaction data in JSON format
        String transactionData = createTransactionJson(
            transactionId,
            req.sender,
            req.receiver,
            req.amount
        );

        // Send to Kafka
        transactionProducer.sendTransaction(transactionId, transactionData);

        // Return response
        PaymentResponse res = new PaymentResponse();
        res.status = "SUCCESS";

        return res;
    }

    //Create unique transaction id
    private String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "TXN-" + timestamp;
    }

    //Turn transacton to JSON format
    private String createTransactionJson(String id, String sender, String receiver, double amount) {
        return String.format(
            "{\"transactionId\":\"%s\",\"sender\":\"%s\",\"receiver\":\"%s\",\"amount\":%.2f,\"timestamp\":\"%s\"}",
            id,
            sender,
            receiver,
            amount,
            LocalDateTime.now().toString()
        );
    }
}