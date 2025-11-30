package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository        // Spring'e bu sınıfın bir DAO/repository olduğunu söylüyor
@Transactional     // Her metotta otomatik transaction aç/kapa

public class BankBTransactionDao {
@Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE
    public void save(BankBTransactionDao tr) {
        getSession().persist(tr);
    }

    // UPDATE
    public void update(BankBTransactionDao tr) {
        getSession().merge(tr);
    }

    // DELETE
    public void delete(BankBTransactionDao tr) {
        getSession().remove(tr);
    }
    // READ (by ID)
    public BankBTransactionDao getById(Long id) {
        return getSession().find(BankBTransactionDao.class, id);
    }

    // READ (all)
    public List<BankBTransactionDao> getAll() {
        return getSession()
                .createQuery("FROM BankBTransactionDao", BankBTransactionDao.class)
                .getResultList();
    }


}
