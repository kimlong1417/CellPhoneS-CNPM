package repositories;

import entities.WorkShift;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

public class WorkShiftRepository {
    private static Session session;

    public static List<WorkShift> getAll() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<WorkShift> query = session.createQuery("" +
                    "SELECT w " +
                    "FROM WorkShift w");
            List<WorkShift> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static WorkShift getByName(String name) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<WorkShift> query = session.createQuery("" +
                    "SELECT w " +
                    "FROM WorkShift w " +
                    "WHERE w.name = :name");
            query.setParameter("name", name);
            WorkShift result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<WorkShift> getLikeName(String name) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<WorkShift> query = session.createQuery("" +
                    "SELECT w " +
                    "FROM WorkShift w " +
                    "WHERE w.name like :name");
            query.setParameter("name", "%" + name + "%");
            List<WorkShift> result = query.getResultList();
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
