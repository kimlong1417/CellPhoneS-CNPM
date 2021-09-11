package repositories;

import entities.WorkShift;
import entities.WorkTable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

public class WorkTableRepository {
    private static Session session;

    public static List<WorkTable> getByShiftId(String shiftId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<WorkTable> query = session.createQuery("" +
                    "SELECT w " +
                    "FROM WorkTable w " +
                    "WHERE w.workShift.id = :shiftId");
            query.setParameter("shiftId", shiftId);
            List<WorkTable> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<WorkTable> getAll() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<WorkTable> query = session.createQuery("" +
                    "SELECT w " +
                    "FROM WorkTable w");
            List<WorkTable> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<WorkTable> getByEmployeeOrShift(String searchText) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<WorkTable> query = session.createQuery("" +
                    "SELECT w " +
                    "FROM WorkTable w " +
                    "WHERE w.employee.fullName LIKE :searchText OR w.workShift.name LIKE :searchText");
            query.setParameter("searchText", "%" + searchText + "%");
            List<WorkTable> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<WorkTable> getByEmployeeNameAndShift(String employeeName, WorkShift workShift) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<WorkTable> query = session.createQuery("" +
                    "SELECT w " +
                    "FROM WorkTable w " +
                    "WHERE w.employee.fullName = :employeeName " +
                    "AND w.workShift.id = :workShiftId");
            query.setParameter("employeeName", employeeName);
            query.setParameter("workShiftId", workShift.getId());
            List<WorkTable> result = query.getResultList();
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
