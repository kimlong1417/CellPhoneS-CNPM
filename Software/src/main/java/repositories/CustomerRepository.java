package repositories;

import entities.Customer;
import entities.Orders;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.Arrays;
import java.util.List;

public class CustomerRepository {
    private static Session session;

    public static List<Customer> getAll() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Customer> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Customer c");
            List<Customer> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Customer> getByNameOrPhone(String keySearch) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Customer> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Customer c " +
                    "WHERE c.fullName LIKE :keySearch OR c.phone LIKE :keySearch");
            query.setParameter("keySearch", "%" + keySearch + "%");
            List<Customer> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;
        }
    }

    public static Customer getByName(String name) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Customer> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Customer c " +
                    "WHERE c.fullName = :name");
            query.setParameter("name", name);
            Customer result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;
        }
    }

    public static Customer getByPhone(String phone) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Customer> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Customer c " +
                    "WHERE c.phone = :phone");
            query.setParameter("phone", phone);
            Customer result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;
        }
    }

    public static Customer getByEmail(String email) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Customer> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Customer c " +
                    "WHERE c.email = :email");
            query.setParameter("email", email);
            Customer result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Customer> getAllCustomerActiveOrders() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Customer> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Customer c " +
                    "WHERE c.type = 'Kh??ch h??ng' AND c.id in (" +
                    "SELECT o.customer.id " +
                    "FROM Orders o " +
                    "WHERE o.status = 'Ch??a ho??n t???t')");
            List<Customer> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Customer> getAllSupplierActiveOrders() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Customer> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Customer c " +
                    "WHERE c.type = 'Nh?? cung c???p' AND c.id in (" +
                    "SELECT o.customer.id " +
                    "FROM Orders o " +
                    "WHERE o.status = 'Ch??a ho??n t???t')");
            List<Customer> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;
        }
    }
}
