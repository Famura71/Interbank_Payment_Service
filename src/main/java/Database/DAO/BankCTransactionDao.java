package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository        // Spring'e bu sınıfın bir DAO/repository olduğunu söylüyor
@Transactional     // Her metotta otomatik transaction aç/kapa

public class BankCTransactionDao {
@Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE
    public void save(BankCTransactionDao tr) {
        getSession().persist(tr);
    }

    // UPDATE
    public void update(BankCTransactionDao tr) {
        getSession().merge(tr);
    }

    // DELETE
    public void delete(BankCTransactionDao tr) {
        getSession().remove(tr);
    }
    // READ (by ID)
    public BankCTransactionDao getById(Long id) {
        return getSession().find(BankCTransactionDao.class, id);
    }

    // READ (all)
    public List<BankCTransactionDao> getAll() {
        return getSession()
                .createQuery("FROM BankCTransactionDao", BankCTransactionDao.class)
                .getResultList();
    }


}
