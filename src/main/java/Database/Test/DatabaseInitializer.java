package Database.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import Database.Config.HibernateConfig;
import Database.DAO.BankDao;
import Database.DAO.UserDao;
import Database.Entities.Bank;
import Database.Entities.User;

public class DatabaseInitializer {

    public static void main(String[] args) {
        System.out.println("🚀 Database Initializer başlatılıyor...");

        // Spring Context'i başlat
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);

        // DAO'ları al
        BankDao bankDao = context.getBean(BankDao.class);
        UserDao userDao = context.getBean(UserDao.class);

        try {
            // 1️⃣ Bankaları oluştur veya al
            Bank bankA = createOrGetBank(bankDao, "Bank A", 5);
            Bank bankB = createOrGetBank(bankDao, "Bank B", 7);
            Bank bankC = createOrGetBank(bankDao, "Bank C", 10);

            System.out.println("✅ Bankalar hazır!");

            // 2️⃣ Kullanıcıları oluştur
            createUserIfNotExists(userDao, "Famura", "famura@banka.com", "1234", 50.0, bankA);
            createUserIfNotExists(userDao, "Aybuke", "aybuke@banka.com", "4567", 75.0, bankB);
            createUserIfNotExists(userDao, "Tuna", "tuna@banka.com", "7890", 100.0, bankC);

            System.out.println("✅ Kullanıcılar başarıyla eklendi!");
            System.out.println("\n📋 Veritabanı Durumu:");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            // Tüm kullanıcıları listele
            userDao.getAll().forEach(user -> {
                System.out.printf("👤 %s | Email: %s | Banka: %s | Bakiye: %.2f TL%n",
                        user.getName(),
                        user.getEmail(),
                        user.getBank().getBankName(),
                        user.getBalance());
            });

            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("✅ İşlem tamamlandı!");

        } catch (Exception e) {
            System.err.println("❌ Hata oluştu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Context'i kapat
            ((AnnotationConfigApplicationContext) context).close();
            System.out.println("🛑 Spring Context kapatıldı.");
        }
    }

    /**
     * Bankayı oluştur veya varsa getir
     */
    private static Bank createOrGetBank(BankDao bankDao, String bankName, int cut) {
        Bank existingBank = bankDao.getByBankName(bankName);

        if (existingBank != null) {
            System.out.println("ℹ️ " + bankName + " zaten mevcut.");
            return existingBank;
        }

        Bank bank = new Bank();
        bank.setBankName(bankName);
        bank.setCut(cut);
        bankDao.save(bank);

        System.out.println("✅ " + bankName + " oluşturuldu.");
        return bank;
    }

    /**
     * Kullanıcı yoksa oluştur
     */
    private static void createUserIfNotExists(UserDao userDao, String name, String email,
                                               String password, double balance, Bank bank) {
        User existingUser = userDao.getByEmail(email);

        if (existingUser != null) {
            System.out.println("ℹ️ " + name + " (" + email + ") zaten mevcut.");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setBalance(balance);
        user.setBank(bank);

        userDao.save(user);
        System.out.println("✅ " + name + " (" + email + ") eklendi.");
    }
}
