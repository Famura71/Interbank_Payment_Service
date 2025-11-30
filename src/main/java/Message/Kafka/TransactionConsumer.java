package Message.Kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Database.DAO.UserDao; // ARTIK ACCOUNT DEĞİL USER DAO
import Database.DAO.BankATransactionDao;
import Database.DAO.BankBTransactionDao;
import Database.DAO.BankCTransactionDao;
import Database.Entities.User; // ARTIK USER ENTITY
import Database.Entities.BankATransaction;
import Database.Entities.BankBTransaction;
import Database.Entities.BankCTransaction;

@Component
public class TransactionConsumer {

    @Autowired
    private UserDao userDao; // Alıcıyı bulmak için UserDAO kullanıyoruz

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

            // Null kontrolü
            if (!json.has("receiver") || !json.has("amount")) {
                System.err.println("❌ HATA: Eksik veri geldi.");
                return;
            }

            String receiverName = json.get("receiver").asText();
            double amount = json.get("amount").asDouble();

            // 1. Alıcı Kullanıcıyı Bul (getByName metodunu UserDao'ya eklediğini varsayıyoruz)
            User receiverUser = userDao.getByName(receiverName);

            if (receiverUser != null) {
                // 2. Parayı Yükle (Direkt User'ın bakiyesine)
                double yeniBakiye = receiverUser.getBalance() + amount;
                receiverUser.setBalance(yeniBakiye);

                // 3. Güncellemeyi Kaydet
                userDao.update(receiverUser);
                System.out.println("💰 Bakiye Güncellendi: " + receiverName + " (Yeni Bakiye: " + yeniBakiye + ")");

                // 4. HANGİ BANKA?
                if (receiverUser.getBank() != null) {
                    String bankName = receiverUser.getBank().getBankName();

                    // 5. İşlemi Log Tablosuna Yaz
                    saveTransactionToCorrectBank(bankName, receiverUser, amount);
                }

            } else {
                System.err.println("❌ HATA: Kullanıcı bulunamadı -> " + receiverName);
            }

        } catch (Exception e) {
            System.err.println("❌ HATA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveTransactionToCorrectBank(String bankName, User user, double amount) {

        // NOT: BankATransaction vb. sınıflarında "setAccount" yerine "setUser"
        // metodunu kullanacak şekilde güncellemiş olmalısın.

        switch (bankName) {
            case "Bank A":
                BankATransaction trA = new BankATransaction();
                trA.setUser(user); // Entity güncellemesi gerekli!
                trA.setAmount(amount);
                trA.setLogType("INCOMING_TRANSFER");
                bankADao.save(trA);
                System.out.println("✅ Log Bank A tablosuna yazıldı.");
                break;

            case "Bank B":
                BankBTransaction trB = new BankBTransaction();
                trB.setUser(user);
                trB.setAmount(amount);
                trB.setLogType("INCOMING_TRANSFER");
                bankBDao.save(trB);
                System.out.println("✅ Log Bank B tablosuna yazıldı.");
                break;

            case "Bank C":
                BankCTransaction trC = new BankCTransaction();
                trC.setUser(user);
                trC.setAmount(amount);
                trC.setLogType("INCOMING_TRANSFER");
                bankCDao.save(trC);
                System.out.println("✅ Log Bank C tablosuna yazıldı.");
                break;

            default:
                System.err.println("⚠️ Bilinmeyen Banka: " + bankName);
                break;
        }
    }
}