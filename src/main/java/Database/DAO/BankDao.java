package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Database.Entities.Bank;

@Repository        // Spring'e bu sınıfın bir DAO/repository olduğunu söylüyor
@Transactional     // Her metotta otomatik transaction aç/kapa
public class BankDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE (insert)
    public void save(Bank bank) {
        getSession().persist(bank);
    }

    // UPDATE
    public void update(Bank bank) {
        getSession().merge(bank);
    }

    // DELETE
    public void delete(Bank bank) {
        getSession().remove(bank);
    }
    // READ (by ID)
    public Bank getById(Long id) {
        return getSession().find(Bank.class, id);
    }

    // READ (all)
    public List<Bank> getAll() {
        return getSession()
                .createQuery("FROM Bank", Bank.class)
                .getResultList();
    }
    // FIND BY bankName
    public Bank getByBankName(String bankName) {
        return getSession()
                .createQuery("FROM Bank b WHERE b.bankName = :name", Bank.class)
                .setParameter("name", bankName)
                .uniqueResult();
    }

}
