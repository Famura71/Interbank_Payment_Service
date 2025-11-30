package Database.Config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"Database", "Message.Kafka"}) // ← Kafka paketini ekle
public class HibernateConfig {

    // 1️⃣ DataSource: MySQL bağlantısı
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/mydb"); // Docker container'daki DB
        ds.setUsername("user"); // Docker'da tanımlı kullanıcı
        ds.setPassword("pass"); // Docker'da tanımlı şifre
        return ds;
    }

    // 2️⃣ SessionFactory: Hibernate'in DB ile konuşmasını sağlar
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("Database.Entities"); // Entity sınıflarını tara

        // Hibernate özellikleri
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        hibernateProperties.put("hibernate.show_sql", "true");      // Konsola SQL yaz
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update"); // Tabloyu otomatik oluştur / güncelle

        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    // 3️⃣ Transaction Manager: DB işlemlerinin bütünlüğünü sağlar
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}

