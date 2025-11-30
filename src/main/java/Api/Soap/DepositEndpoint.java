package Api.Soap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import Message.Kafka.TransactionProducer;
import Database.DAO.UserDao;
import Database.Entities.User;

@Endpoint
public class DepositEndpoint {

    private static final String NAMESPACE = "http://interbank.com/payment";

    @Autowired
    private TransactionProducer transactionProducer;

    @Autowired
    private UserDao userDao;

    @PayloadRoot(namespace = NAMESPACE, localPart = "DepositRequest")
    @ResponsePayload
    public DepositResponse processDeposit(@RequestPayload DepositRequest req) {

        System.out.println("💵 DEPOSIT İsteği Alındı:");
        System.out.println("   " + req.userName + " → +" + req.amount + " TL");

        // 1. Kullanıcıyı kontrol et (opsiyonel, sadece var mı diye)
        User user = userDao.getByName(req.userName);

        if (user == null) {
            System.err.println("❌ Kullanıcı bulunamadı: " + req.userName);
            DepositResponse res = new DepositResponse();
            res.status = "FAILED - User not found";
            return res;
        }

        // 2. Transaction ID oluştur
        String transactionId = generateTransactionId();

        // 3. Kafka mesajı oluştur (type: DEPOSIT)
        String message = createDepositJson(transactionId, req.userName, req.amount);

        // 4. Kafka'ya gönder
        transactionProducer.sendTransaction(transactionId, message);
        System.out.println("📨 Kafka'ya DEPOSIT mesajı gönderildi: " + transactionId);

        // 5. Response döndür
        DepositResponse res = new DepositResponse();
        res.status = "SUCCESS";

        return res;
    }

    private String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "DEP-" + timestamp;
    }

    private String createDepositJson(String id, String userName, double amount) {
        return String.format(
                "{\"type\":\"DEPOSIT\",\"transactionId\":\"%s\",\"userName\":\"%s\",\"amount\":%.2f,\"timestamp\":\"%s\"}",
                id,
                userName,
                amount,
                LocalDateTime.now().toString()
        );
    }
}