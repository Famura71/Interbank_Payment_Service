package Database.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Database.Entities.Transaction;

@Repository        
@Transactional     

public class TransactionDao {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // CREATE (insert)
    public void save(Transaction tra) {
        getSession().persist(tra);
    }

    // UPDATE
    public void update(Transaction tra) {
        getSession().merge(tra);
    }

    // DELETE
    public void delete(Transaction tra) {
        getSession().remove(tra);
    }
    // READ (by ID)
    public Transaction getById(String id) {
        return getSession().find(Transaction.class, id);
    }

    // READ (all)
    public List<Transaction> getAll() {
        return getSession()
                .createQuery("FROM Transaction", Transaction.class)
                .getResultList();
    }


}
