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
public class WithdrawEndpoint {

    private static final String NAMESPACE = "http://interbank.com/payment";

    @Autowired
    private TransactionProducer transactionProducer;

    @Autowired
    private UserDao userDao;

    @PayloadRoot(namespace = NAMESPACE, localPart = "WithdrawRequest")
    @ResponsePayload
    public WithdrawResponse processWithdraw(@RequestPayload WithdrawRequest req) {

        System.out.println("💸 WITHDRAW İsteği Alındı:");
        System.out.println("   " + req.userName + " → -" + req.amount + " TL");

        // 1. Kullanıcıyı kontrol et
        User user = userDao.getByName(req.userName);

        if (user == null) {
            System.err.println("❌ Kullanıcı bulunamadı: " + req.userName);
            WithdrawResponse res = new WithdrawResponse();
            res.status = "FAILED - User not found";
            return res;
        }

        // 2. Bakiye kontrolü (opsiyonel - Consumer'da da yapılıyor ama burada da yapabiliriz)
        if (user.getBalance() < req.amount) {
            System.err.println("❌ Yetersiz bakiye: " + user.getBalance() + " TL < " + req.amount + " TL");
            WithdrawResponse res = new WithdrawResponse();
            res.status = "FAILED - Insufficient balance";
            return res;
        }

        // 3. Transaction ID oluştur
        String transactionId = generateTransactionId();

        // 4. Kafka mesajı oluştur (type: WITHDRAW)
        String message = createWithdrawJson(transactionId, req.userName, req.amount);

        // 5. Kafka'ya gönder
        transactionProducer.sendTransaction(transactionId, message);
        System.out.println("📨 Kafka'ya WITHDRAW mesajı gönderildi: " + transactionId);

        // 6. Response döndür
        WithdrawResponse res = new WithdrawResponse();
        res.status = "SUCCESS";

        return res;
    }

    private String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "WTH-" + timestamp;
    }

    private String createWithdrawJson(String id, String userName, double amount) {
        return String.format(
                "{\"type\":\"WITHDRAW\",\"transactionId\":\"%s\",\"userName\":\"%s\",\"amount\":%.2f,\"timestamp\":\"%s\"}",
                id,
                userName,
                amount,
                LocalDateTime.now().toString()
        );
    }
}