package Database.DAO;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Entity sınıfını import ediyoruz
import Database.Entities.BankBTransaction;

@Repository
@Transactional
public class BankBTransactionDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE (Parametre artık Entity: BankBTransaction)
    public void save(BankBTransaction tr) {
        getSession().persist(tr);
    }

    // UPDATE
    public void update(BankBTransaction tr) {
        getSession().merge(tr);
    }

    // DELETE
    public void delete(BankBTransaction tr) {
        getSession().remove(tr);
    }

    // READ by ID (ID String olduğu için String yapıldı)
    public BankBTransaction getById(String id) {
        return getSession().find(BankBTransaction.class, id);
    }

    // READ all
    public List<BankBTransaction> getAll() {
        return getSession()
                .createQuery("FROM BankBTransaction", BankBTransaction.class)
                .getResultList();
    }
}