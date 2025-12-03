package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Database.Entities.BankATransaction;

@Repository
@Transactional
public class BankATransactionDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE
    public void save(BankATransaction tr) {
        getSession().persist(tr);
    }

    // UPDATE
    public void update(BankATransaction tr) {
        getSession().merge(tr);
    }

    // DELETE
    public void delete(BankATransaction tr) {
        getSession().remove(tr);
    }

    // READ by ID
    // Not: BankATransaction entity'sinde ID String tanımlandığı için burayı String yaptım
    public BankATransaction getById(String id) {
        return getSession().find(BankATransaction.class, id);
    }

    // READ all
    public List<BankATransaction> getAll() {
        return getSession()
                .createQuery("FROM BankATransaction", BankATransaction.class)
                .getResultList();
    }
}