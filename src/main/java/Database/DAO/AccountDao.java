package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Database.Entities.Account;
import Database.Entities.User;

@Repository        // Spring'e bu sınıfın bir DAO/repository olduğunu söylüyor
@Transactional     // Her metotta otomatik transaction aç/kapa
public class AccountDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE
    public void save(Account account) {
        getSession().persist(account);
    }

    // UPDATE
    public void update(Account account) {
        getSession().merge(account);
    }

    // DELETE
    public void delete(Account account) {
        getSession().remove(account);
    }
    // READ (by ID)
    public Account getById(Long id) {
        return getSession().find(Account.class, id);
    }

    // READ (all)
    public List<Account> getAll() {
        return getSession()
                .createQuery("FROM Account", Account.class)
                .getResultList();
    }
// FIND by User
    public List<Account> getByUser(User user) {
        return getSession()
                .createQuery("FROM Account a WHERE a.user = :user", Account.class)
                .setParameter("user", user)
                .getResultList();
    }

}
