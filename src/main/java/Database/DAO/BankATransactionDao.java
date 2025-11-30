package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository        // Spring'e bu sınıfın bir DAO/repository olduğunu söylüyor
@Transactional     // Her metotta otomatik transaction aç/kapa

public class BankATransactionDao {
@Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE
    public void save(BankATransactionDao tr) {
        getSession().persist(tr);
    }

    // UPDATE
    public void update(BankATransactionDao tr) {
        getSession().merge(tr);
    }

    // DELETE
    public void delete(BankATransactionDao tr) {
        getSession().remove(tr);
    }
    // READ (by ID)
    public BankATransactionDao getById(Long id) {
        return getSession().find(BankATransactionDao.class, id);
    }

    // READ (all)
    public List<BankATransactionDao> getAll() {
        return getSession()
                .createQuery("FROM BankATransactionDao", BankATransactionDao.class)
                .getResultList();
    }


}

