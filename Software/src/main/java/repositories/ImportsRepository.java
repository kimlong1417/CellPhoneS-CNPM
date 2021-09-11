package repositories;

import entities.Imports;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.Arrays;
import java.util.List;

public class ImportsRepository {
    private static Session session;

    public static List<Imports> getAll() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Imports> query = session.createQuery("" +
                    "SELECT i " +
                    "FROM Imports i");
            List<Imports> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Imports getById(String id) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Imports> query = session.createQuery("" +
                    "SELECT i " +
                    "FROM Imports i " +
                    "WHERE i.id = :id");
            query.setParameter("id", id);
            Imports result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Imports> getByOrdersId(String ordersId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Imports> query = session.createQuery("" +
                    "SELECT i " +
                    "FROM Imports i " +
                    "WHERE i.orders.id = :id");
            query.setParameter("id", ordersId);
            List<Imports> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Imports> getLikeCustomerName(String customerName) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Imports> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Imports c " +
                    "WHERE c.orders.customer.fullName LIKE :customerName");
            query.setParameter("customerName", "%" + customerName + "%");
            List<Imports> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }
}
