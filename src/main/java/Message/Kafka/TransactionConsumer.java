package Message.Kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Database.DAO.UserDao;
import Database.DAO.BankATransactionDao;
import Database.DAO.BankBTransactionDao;
import Database.DAO.BankCTransactionDao;
import Database.Entities.User;
import Database.Entities.BankATransaction;
import Database.Entities.BankBTransaction;
import Database.Entities.BankCTransaction;

@Component
public class TransactionConsumer {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BankATransactionDao bankADao;
    @Autowired
    private BankBTransactionDao bankBDao;
    @Autowired
    private BankCTransactionDao bankCDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "payment-transactions", groupId = "payment-group")
    @Transactional
    public void listenTransfer(String message) {
        System.out.println("📨 KAFKA: Mesaj alındı -> " + message);

        try {
            JsonNode json = objectMapper.readTree(message);

            // Mesaj tipini kontrol et (TRANSFER, DEPOSIT, WITHDRAW)
            String type = json.has("type") ? json.get("type").asText() : "TRANSFER";

            switch (type) {
                case "DEPOSIT":
                    handleDeposit(json);
                    break;

                case "WITHDRAW":
                    handleWithdraw(json);
                    break;

                case "TRANSFER":
                default:
                    handleTransfer(json);
                    break;
            }

        } catch (Exception e) {
            System.err.println("❌ HATA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * TRANSFER işlemi (Ahmet → Mehmet)
     * Sadece ALICI tarafı işlenir (gönderici SOAP'ta çekildi)
     */
    private void handleTransfer(JsonNode json) {
        if (!json.has("receiver") || !json.has("amount")) {
            System.err.println("❌ HATA: TRANSFER - Eksik veri (receiver veya amount yok)");
            return;
        }

        String receiverName = json.get("receiver").asText();
        double amount = json.get("amount").asDouble();

        User receiverUser = userDao.getByName(receiverName);

        if (receiverUser != null) {
            // Alıcıya parayı ekle
            double newBalance = receiverUser.getBalance() + amount;
            receiverUser.setBalance(newBalance);
            userDao.update(receiverUser);

            System.out.println("💰 TRANSFER: " + receiverName + " → +" + amount + " TL (Yeni Bakiye: " + newBalance + ")");

            // İşlemi logla
            if (receiverUser.getBank() != null) {
                saveTransactionToCorrectBank(receiverUser.getBank().getBankName(), receiverUser, amount, "INCOMING_TRANSFER");
            }
        } else {
            System.err.println("❌ HATA: Alıcı kullanıcı bulunamadı -> " + receiverName);
        }
    }

    /**
     * DEPOSIT işlemi (Para Yatırma)
     * Kullanıcının kendi hesabına para ekler
     */
    private void handleDeposit(JsonNode json) {
        if (!json.has("userName") || !json.has("amount")) {
            System.err.println("❌ HATA: DEPOSIT - Eksik veri (userName veya amount yok)");
            return;
        }

        String userName = json.get("userName").asText();
        double amount = json.get("amount").asDouble();

        User user = userDao.getByName(userName);

        if (user != null) {
            // Kullanıcıya parayı ekle
            double newBalance = user.getBalance() + amount;
            user.setBalance(newBalance);
            userDao.update(user);

            System.out.println("💵 DEPOSIT: " + userName + " → +" + amount + " TL (Yeni Bakiye: " + newBalance + ")");

            // İşlemi logla
            if (user.getBank() != null) {
                saveTransactionToCorrectBank(user.getBank().getBankName(), user, amount, "DEPOSIT");
            }
        } else {
            System.err.println("❌ HATA: Kullanıcı bulunamadı -> " + userName);
        }
    }

    /**
     * WITHDRAW işlemi (Para Çekme)
     * Kullanıcının hesabından para çeker
     */
    private void handleWithdraw(JsonNode json) {
        if (!json.has("userName") || !json.has("amount")) {
            System.err.println("❌ HATA: WITHDRAW - Eksik veri (userName veya amount yok)");
            return;
        }

        String userName = json.get("userName").asText();
        double amount = json.get("amount").asDouble();

        User user = userDao.getByName(userName);

        if (user != null) {
            // Bakiye kontrolü
            if (user.getBalance() >= amount) {
                // Kullanıcıdan parayı çek
                double newBalance = user.getBalance() - amount;
                user.setBalance(newBalance);
                userDao.update(user);

                System.out.println("💸 WITHDRAW: " + userName + " → -" + amount + " TL (Yeni Bakiye: " + newBalance + ")");

                // İşlemi logla
                if (user.getBank() != null) {
                    saveTransactionToCorrectBank(user.getBank().getBankName(), user, amount, "WITHDRAWAL");
                }
            } else {
                System.err.println("❌ HATA: Yetersiz bakiye! " + userName + " → Bakiye: " + user.getBalance() + " TL < " + amount + " TL");
            }
        } else {
            System.err.println("❌ HATA: Kullanıcı bulunamadı -> " + userName);
        }
    }

    /**
     * İşlemi doğru banka tablosuna kaydet
     */
    private void saveTransactionToCorrectBank(String bankName, User user, double amount, String logType) {
        switch (bankName) {
            case "Bank A":
                BankATransaction trA = new BankATransaction();
                trA.setUser(user);
                trA.setAmount(amount);
                trA.setLogType(logType);
                bankADao.save(trA);
                System.out.println("✅ Log Bank A tablosuna yazıldı.");
                break;

            case "Bank B":
                BankBTransaction trB = new BankBTransaction();
                trB.setUser(user);
                trB.setAmount(amount);
                trB.setLogType(logType);
                bankBDao.save(trB);
                System.out.println("✅ Log Bank B tablosuna yazıldı.");
                break;

            case "Bank C":
                BankCTransaction trC = new BankCTransaction();
                trC.setUser(user);
                trC.setAmount(amount);
                trC.setLogType(logType);
                bankCDao.save(trC);
                System.out.println("✅ Log Bank C tablosuna yazıldı.");
                break;

            default:
                System.err.println("⚠️ Bilinmeyen Banka: " + bankName);
                break;
        }
    }
}