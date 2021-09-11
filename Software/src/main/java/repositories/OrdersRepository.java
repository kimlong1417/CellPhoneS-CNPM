package repositories;

import entities.Orders;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.Arrays;
import java.util.List;

public class OrdersRepository {
    private static Session session;

    public static List<Orders> getAll() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Orders> query = session.createQuery("" +
                    "SELECT o " +
                    "FROM Orders o");
            List<Orders> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Orders> getLikeCustomerName(String customerName) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Orders> query = session.createQuery("" +
                    "SELECT o " +
                    "FROM Orders o " +
                    "WHERE o.customer.fullName LIKE :customerName");
            query.setParameter("customerName", "%" + customerName + "%");
            List<Orders> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Orders> getByCustomerName(String customerName) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Orders> query = session.createQuery("" +
                    "SELECT o " +
                    "FROM Orders o " +
                    "WHERE o.customer.fullName = :customerName");
            query.setParameter("customerName", customerName);
            List<Orders> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Orders> getActiveByCustomerName(String customerName) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Orders> query = session.createQuery("" +
                    "SELECT o " +
                    "FROM Orders o " +
                    "WHERE o.customer.fullName = :customerName AND o.status = 'Chưa hoàn tất'");
            query.setParameter("customerName", customerName);
            List<Orders> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Orders getById(String id) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Orders> query = session.createQuery("" +
                    "SELECT o " +
                    "FROM Orders o " +
                    "WHERE o.id = :id");
            query.setParameter("id", id);
            Orders result = query.uniqueResult();
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
