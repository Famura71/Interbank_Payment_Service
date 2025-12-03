package Message.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Database.DAO.BankATransactionDao;
import Database.DAO.BankBTransactionDao;
import Database.DAO.BankCTransactionDao;
import Database.DAO.UserDao;
import Database.Entities.BankATransaction;
import Database.Entities.BankBTransaction;
import Database.Entities.BankCTransaction;
import Database.Entities.User;

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
    public void listenTransaction(String message) {
        System.out.println("KAFKA: Mesaj alındı -> " + message);

        try {
            JsonNode json = objectMapper.readTree(message);

           
            if (!json.has("type") || !json.has("userEmail") || !json.has("amount") || !json.has("bankName")) {
                System.err.println("HATA: Eksik veri geldi.");
                return;
            }

            String type = json.get("type").asText();
            String userEmail = json.get("userEmail").asText();
            double amount = json.get("amount").asDouble();
            String bankName = normalizeBankName(json.get("bankName").asText());

            // Find user
            User user = userDao.getByEmail(userEmail);
            if (user == null) {
                System.err.println("HATA: Kullanıcı bulunamadı -> " + userEmail);
                return;
            }

            switch (type) {
                case "deposit":
                    handleDeposit(user, amount, bankName);
                    break;
                case "withdraw":
                    handleWithdraw(user, amount, bankName);
                    break;
                case "transfer":
                    handleTransfer(json, user, amount, bankName);
                    break;
                default:
                    System.err.println("Bilinmeyen transaction türü: " + type);
            }

        } catch (Exception e) {
            System.err.println("HATA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Deposit
    private void handleDeposit(User user, double amount, String bankName) {
        // Increase balance
        double newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);
        userDao.update(user);

        // Log
        saveTransactionLog(bankName, user, amount, "DEPOSIT");

        System.out.println("DEPOSIT: " + user.getName() + " - Yeni bakiye: " + newBalance);
    }

    //Withdraw
    private void handleWithdraw(User user, double amount, String bankName) {
        // Balance control
        if (user.getBalance() < amount) {
            System.err.println("Yetersiz bakiye: " + user.getName());
            return;
        }

        // Lower balance
        double newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);
        userDao.update(user);

        // Log
        saveTransactionLog(bankName, user, amount, "WITHDRAW");

        System.out.println("WITHDRAW: " + user.getName() + " - Yeni bakiye: " + newBalance);
    }

    //Transfer
    private void handleTransfer(JsonNode json, User sender, double amount, String bankName) {
        // Balance control
        if (sender.getBalance() < amount) {
            System.err.println("Yetersiz bakiye: " + sender.getName());
            return;
        }

        String receiverName = json.get("receiverName").asText();
        String receiverBank = normalizeBankName(json.get("receiverBank").asText());

        // Lower senders balance
        double newBalance = sender.getBalance() - amount;
        sender.setBalance(newBalance);
        userDao.update(sender);

        // Log for sender
        saveTransactionLog(bankName, sender, amount, "TRANSFER_OUT");

        // Find receiver and increase balance
        User receiver = userDao.getByNameAndBank(receiverName, receiverBank);

        if (receiver != null) {
            double receiverNewBalance = receiver.getBalance() + amount;
            receiver.setBalance(receiverNewBalance);
            userDao.update(receiver);

            // Log receiver
            saveTransactionLog(receiverBank, receiver, amount, "TRANSFER_IN");

            System.out.println("TRANSFER: " + sender.getName() + " -> " + receiver.getName() + " (" + amount + " TL)");
        } else {
            System.err.println("Alıcı bulunamadı: " + receiverName);
        }
    }

    //Save transaction log
    private void saveTransactionLog(String bankName, User user, double amount, String logType) {
        switch (bankName) {
            case "Bank A":
                BankATransaction trA = new BankATransaction();
                trA.setUser(user);
                trA.setAmount(amount);
                trA.setLogType(logType);
                bankADao.save(trA);
                System.out.println("Log Bank A tablosuna yazıldı.");
                break;

            case "Bank B":
                BankBTransaction trB = new BankBTransaction();
                trB.setUser(user);
                trB.setAmount(amount);
                trB.setLogType(logType);
                bankBDao.save(trB);
                System.out.println("Log Bank B tablosuna yazıldı.");
                break;

            case "Bank C":
                BankCTransaction trC = new BankCTransaction();
                trC.setUser(user);
                trC.setAmount(amount);
                trC.setLogType(logType);
                bankCDao.save(trC);
                System.out.println("Log Bank C tablosuna yazıldı.");
                break;

            default:
                System.err.println("Bilinmeyen Banka: " + bankName);
                break;
        }
    }

    //Change the code from front end to bank name
    private String normalizeBankName(String raw) {
        if (raw == null) return null;

        switch (raw.trim()) {
            case "A":
            case "Bank A":
                return "Bank A";
            case "B":
            case "Bank B":
                return "Bank B";
            case "C":
            case "Bank C":
                return "Bank C";
            default:
                return raw.trim();
        }
    }
}