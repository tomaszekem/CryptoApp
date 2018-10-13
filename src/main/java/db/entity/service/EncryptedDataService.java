package db.entity.service;

import crypto.TripleDESEncrypter;
import db.entity.EncryptedData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EncryptedDataService {

    private static EncryptedDataService instance;
    private final TripleDESEncrypter tripleDESEncrypter = TripleDESEncrypter.getInstance();

    private EncryptedDataService() {}

    public static EncryptedDataService getInstance() {
        if (instance == null) {
            return new EncryptedDataService();
        }
        return instance;
    }

    public EncryptedData encryptAndSave(File file) throws IOException {
        EncryptedData encryptedData = tripleDESEncrypter.encryptFile(file);
        save(encryptedData);
        return encryptedData;
    }

    public EncryptedData save(EncryptedData encryptedData) {
        Session session = getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.save(encryptedData);
        trans.commit();
        session.close();
        return encryptedData;

    }

    public List<String> getAllEncryptedFileNames() {
        Session session = getSessionFactory().openSession();
        String hql = "SELECT e.fileName FROM EncryptedData e";
        Query query = session.createQuery(hql);
        return query.list();
    }

    public EncryptedData getByFileName(String fileName) {
        Session session = getSessionFactory().openSession();
        String hql = "SELECT e FROM EncryptedData e WHERE e.fileName = :fileName";
        Query query = session.createQuery(hql);
        query.setParameter("fileName", fileName);
        return (EncryptedData) query.uniqueResult();
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
