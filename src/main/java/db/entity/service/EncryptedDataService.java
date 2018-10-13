package db.entity.service;

import db.entity.EncryptedData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class EncryptedDataService {

    private static EncryptedDataService instance;

    private EncryptedDataService() {}

    public static EncryptedDataService getInstance() {
        if (instance == null) {
            return new EncryptedDataService();
        }
        return instance;
    }

    public EncryptedData save(EncryptedData encryptedData) {
        Session session = getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.save(encryptedData);
        trans.commit();
        session.close();
        return encryptedData;

    }

    private static SessionFactory getSessionFactory() {
        Configuration conf = new Configuration();
        conf.configure("hibernate.cfg.xml");
        conf.addAnnotatedClass(EncryptedData.class);

        ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();

        SessionFactory factoryObj = conf.buildSessionFactory(serviceRegistryObj);
        return factoryObj;
    }


}
