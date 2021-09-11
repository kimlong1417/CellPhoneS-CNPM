package repositories;

import entities.EmployeeRoles;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.Arrays;
import java.util.List;

public class EmployeeRolesRepository {
    private static Session session;

    public static EmployeeRoles getByEmployeeId(String employeeId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<EmployeeRoles> query = session.createQuery("" +
                    "SELECT e " +
                    "FROM EmployeeRoles e " +
                    "WHERE e.employee.id = :employeeId");
            query.setParameter("employeeId", employeeId);
            EmployeeRoles result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<EmployeeRoles> getByRolesId(String rolesId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<EmployeeRoles> query = session.createQuery("" +
                    "SELECT er " +
                    "FROM EmployeeRoles er " +
                    "WHERE er.roles.id = :rolesId");
            query.setParameter("rolesId", rolesId);
            List<EmployeeRoles> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static void deleteByEmployeeId(String employeeId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<EmployeeRoles> query = session.createQuery("" +
                    "DELETE " +
                    "FROM EmployeeRoles e " +
                    "WHERE e.employee.id = :employeeId");
            query.setParameter("employeeId", employeeId);
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
        }
    }
}
