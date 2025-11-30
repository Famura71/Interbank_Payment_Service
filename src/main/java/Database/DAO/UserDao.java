package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Database.Entities.User;

@Repository        // Spring'e bu sınıfın bir DAO/repository olduğunu söylüyor
@Transactional     // Her metotta otomatik transaction aç/kapa
public class UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE (insert)
    public void save(User user) {
        getSession().persist(user);
    }

    // UPDATE
    public void update(User user) {
        getSession().merge(user);
    }

    // DELETE
    public void delete(User user) {
        getSession().remove(user);
    }

    // READ (by ID)
    public User getById(Long id) {
        return getSession().find(User.class, id);
    }

    // READ (all)
    public List<User> getAll() {
        return getSession()
                .createQuery("FROM User", User.class)
                .getResultList();
    }

    // READ (by email)
    public User getByEmail(String email) {
        return getSession()
                .createQuery("FROM User WHERE email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();
    }
    
    // Login için: email, password ve banka kontrolü
    public User login(String email, String password, String bankName) {
        return getSession()
                .createQuery("FROM User u WHERE u.email = :email AND u.password = :password AND u.bank.bankName = :bankName", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .setParameter("bankName", bankName)
                .uniqueResult();
    }
    // User isminden Kullanıcıyı bulma (KAFKA İÇİN GEREKLİ)
    public User getByName(String name) {
        return getSession()
                .createQuery("FROM User WHERE name = :name", User.class)
                .setParameter("name", name)
                .uniqueResult();
    }
}
