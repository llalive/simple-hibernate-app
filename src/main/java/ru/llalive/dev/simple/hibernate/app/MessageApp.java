package ru.llalive.dev.simple.hibernate.app;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author LLAlive
 */
public class MessageApp {

    private static SessionFactory factory;
    private static ServiceRegistry registry;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String m;
        System.out.println("Enter a message: ");
        m = in.nextLine();
        try {
            Configuration conf = new Configuration().configure();
            registry = new StandardServiceRegistryBuilder().applySettings(
                    conf.getProperties()).build();
            factory = conf.buildSessionFactory(registry);
        } catch (HibernateException ex) {
            System.err.println("Failed to create session factory object " + ex);
            throw new ExceptionInInitializerError(ex);
        }
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Message message = new Message(m);
            session.save(message);
            List messages = session.createQuery("FROM Message").list();
            Iterator it = messages.iterator();
            while (it.hasNext()) {
                Message msg = (Message) it.next();
                System.out.println("Message: " + msg.getMessage());
            }
            transaction.commit();
        } catch (HibernateException hex) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
            in.close();
        }
    }
}
