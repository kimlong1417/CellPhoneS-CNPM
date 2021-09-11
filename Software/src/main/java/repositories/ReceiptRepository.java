package repositories;

import entities.Receipt;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.Arrays;
import java.util.List;

public class ReceiptRepository {
    private static Session session;

    public static List<Receipt> getAll() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Receipt> query = session.createQuery("" +
                    "SELECT r " +
                    "FROM Receipt r");
            List<Receipt> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Receipt> getLikeCustomerName(String customerName) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Receipt> query = session.createQuery("" +
                    "SELECT r " +
                    "FROM Receipt r " +
                    "WHERE r.orders.customer.fullName LIKE :customerName");
            query.setParameter("customerName", "%" + customerName + "%");
            List<Receipt> result = query.getResultList();
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
