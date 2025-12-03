package Database.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import Database.Config.HibernateConfig;
import Database.DAO.BankDao;
import Database.DAO.UserDao;
import Database.Entities.Bank;
import Database.Entities.User;

public class AddBanksAndUsers {
    public static void main(String[] args) {
        // Start Cpring context
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        
        // DAO objects
        BankDao bankDao = context.getBean(BankDao.class);
        UserDao userDao = context.getBean(UserDao.class);
        
        try {
            //Bank A
            Bank bankA = bankDao.getByBankName("Bank A");
            if (bankA == null) {
                bankA = new Bank();
                bankA.setBankName("Bank A");
                bankA.setCut(1);
                bankDao.save(bankA);
                System.out.println("Bank A yeni oluşturuldu!");
            } else {
                System.out.println("Bank A zaten mevcut, kullanılıyor.");
            }
            
            //Bank B'
            Bank bankB = bankDao.getByBankName("Bank B");
            if (bankB == null) {
                bankB = new Bank();
                bankB.setBankName("Bank B");
                bankB.setCut(2);
                bankDao.save(bankB);
                System.out.println("Bank B yeni oluşturuldu!");
            } else {
                System.out.println("Bank B zaten mevcut, kullanılıyor.");
            }
            
            // Bank C
            Bank bankC = bankDao.getByBankName("Bank C");
            if (bankC == null) {
                bankC = new Bank();
                bankC.setBankName("Bank C");
                bankC.setCut(3);
                bankDao.save(bankC);
                System.out.println("Bank C yeni oluşturuldu!");
            } else {
                System.out.println("Bank C zaten mevcut, kullanılıyor.");
            }
            
            System.out.println("\n--- Kullanıcılar Ekleniyor ---\n");
            
            // famura
            User famura = userDao.getByEmail("famura@example.com");
            if (famura == null) {
                famura = new User();
                famura.setName("Famura");
                famura.setEmail("famura@example.com");
                famura.setPassword("1234");
                famura.setBank(bankA);
                userDao.save(famura);
                System.out.println("Famura (Bank A) yeni eklendi!");
            } else {
                System.out.println("Famura zaten mevcut, kullanılıyor.");
            }
            
            // aybüke
            User aybuke = userDao.getByEmail("aybuke@example.com");
            if (aybuke == null) {
                aybuke = new User();
                aybuke.setName("Aybüke");
                aybuke.setEmail("aybuke@example.com");
                aybuke.setPassword("4567");
                aybuke.setBank(bankB);
                userDao.save(aybuke);
                System.out.println("Aybüke (Bank B) yeni eklendi!");
            } else {
                System.out.println("Aybüke zaten mevcut, kullanılıyor.");
            }
            
            // tuna
            User tuna = userDao.getByEmail("tuna@example.com");
            if (tuna == null) {
                tuna = new User();
                tuna.setName("Tuna");
                tuna.setEmail("tuna@example.com");
                tuna.setPassword("7890");
                tuna.setBank(bankC);
                userDao.save(tuna);
                System.out.println("Tuna (Bank C) yeni eklendi!");
            } else {
                System.out.println("Tuna zaten mevcut, kullanılıyor.");
            }
            
            System.out.println("\n🎉 Tüm işlemler başarıyla tamamlandı!");
            
        } catch (Exception e) {
            System.err.println("❌ Hata oluştu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ((AnnotationConfigApplicationContext) context).close();
        }
    }
}
