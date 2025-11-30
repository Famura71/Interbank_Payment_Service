package Database.DAO;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// ÖNEMLİ: Entity sınıfını import ediyoruz
import Database.Entities.BankATransaction;

@Repository
@Transactional
public class BankATransactionDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE (DÜZELTİLDİ: Artık parametre olarak Entity alıyor)
    public void save(BankATransaction tr) {
        getSession().persist(tr);
    }

    // UPDATE (DÜZELTİLDİ)
    public void update(BankATransaction tr) {
        getSession().merge(tr);
    }

    // DELETE (DÜZELTİLDİ)
    public void delete(BankATransaction tr) {
        getSession().remove(tr);
    }

    // READ by ID (DÜZELTİLDİ: Entity döndürüyor)
    // Not: BankATransaction entity'sinde ID String tanımlandığı için burayı String yaptım
    public BankATransaction getById(String id) {
        return getSession().find(BankATransaction.class, id);
    }

    // READ all (DÜZELTİLDİ: Entity listesi döndürüyor)
    public List<BankATransaction> getAll() {
        return getSession()
                .createQuery("FROM BankATransaction", BankATransaction.class)
                .getResultList();
    }
}