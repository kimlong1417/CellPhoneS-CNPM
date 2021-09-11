package repositories;

import entities.Employee;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeRepository {
    private static Session session;

    public static Employee getByEmail(String email) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Employee> query = session.createQuery("" +
                    "SELECT e " +
                    "FROM Employee e " +
                    "WHERE e.email = :email");
            query.setParameter("email", email);
            Employee result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Employee> getAll() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Employee> query = session.createQuery("" +
                    "SELECT e " +
                    "FROM Employee e");
            List<Employee> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Employee getByName(String name) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Employee> query = session.createQuery("" +
                    "SELECT e " +
                    "FROM Employee e " +
                    "WHERE e.fullName = :name");
            query.setParameter("name", name);
            Employee result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Employee getByEmployeeName(String employeeName) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Employee> query = session.createQuery("" +
                    "SELECT e " +
                    "FROM Employee e " +
                    "WHERE e.fullName = :employeeName");
            query.setParameter("employeeName", employeeName);
            Employee result = query.getSingleResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Employee getByPhone(String phone) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Employee> query = session.createQuery("" +
                    "SELECT e " +
                    "FROM Employee e " +
                    "WHERE e.phone = :phone");
            query.setParameter("phone", phone);
            Employee result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Employee> getByNamePhoneEmail(String keySearch) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Employee> query = session.createQuery("" +
                    "SELECT e " +
                    "FROM Employee e " +
                    "WHERE e.fullName LIKE :keySearch OR e.phone LIKE :keySearch");
            query.setParameter("keySearch", "%" + keySearch + "%");
            List<Employee> result = query.getResultList();
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
