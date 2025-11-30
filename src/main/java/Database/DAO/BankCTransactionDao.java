package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Database.Entities.BankCTransaction;

@Repository        // Spring'e bu sınıfın bir DAO/repository olduğunu söylüyor
@Transactional     // Her metotta otomatik transaction aç/kapa

public class BankCTransactionDao {
@Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE
    public void save(BankCTransaction tr) {
        getSession().persist(tr);
    }

    // UPDATE
    public void update(BankCTransaction tr) {
        getSession().merge(tr);
    }

    // DELETE
    public void delete(BankCTransaction tr) {
        getSession().remove(tr);
    }
    // READ (by ID)
    public BankCTransaction getById(Long id) {
        return getSession().find(BankCTransaction.class, id);
    }

    // READ (all)
    public List<BankCTransaction> getAll() {
        return getSession()
                .createQuery("FROM BankCTransaction", BankCTransaction.class)
                .getResultList();
    }


}
