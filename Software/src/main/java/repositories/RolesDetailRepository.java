package repositories;

import entities.RolesDetail;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.List;

public class RolesDetailRepository {
    private static Session session;

    public static List<RolesDetail> getByRolesId(String rolesId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<RolesDetail> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM RolesDetail c " +
                    "WHERE c.roles.id = :rolesId");
            query.setParameter("rolesId", rolesId);
            List<RolesDetail> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static void deleteByRoleId(String rolesId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("" +
                    "DELETE " +
                    "FROM RolesDetail r " +
                    "WHERE r.roles.id = :rolesId");
            query.setParameter("rolesId", rolesId);
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
        }
    }
}
