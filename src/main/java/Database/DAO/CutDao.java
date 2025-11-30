package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Database.Entities.Bank;
import Database.Entities.Cut;

@Repository        // Spring'e bu sınıfın bir DAO/repository olduğunu söylüyor
@Transactional     // Her metotta otomatik transaction aç/kapa
public class CutDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE (insert)
    public void save(Cut cut) {
        getSession().persist(cut);
    }

    // UPDATE
    public void update(Cut cut) {
        getSession().merge(cut);
    }

    // DELETE
    public void delete(Cut cut) {
        getSession().remove(cut);
    }
    // READ (by ID)
    public Cut getById(Long id) {
        return getSession().find(Cut.class, id);
    }

    // READ (all)
    public List<Cut> getAll() {
        return getSession()
                .createQuery("FROM Cut", Cut.class)
                .getResultList();
    }
    // FIND by 2 Bank objects
    public Cut getByBanks(Bank bank1, Bank bank2) {
        return getSession()
            .createQuery("FROM Cut c WHERE c.bank1 = :b1 AND c.bank2 = :b2", Cut.class)
            .setParameter("b1", bank1)
            .setParameter("b2", bank2)
            .uniqueResult();
    }
}
