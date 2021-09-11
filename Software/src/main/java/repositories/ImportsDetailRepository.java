package repositories;

import entities.ImportsDetail;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportsDetailRepository {
    private static Session session;

    public static Long getSumAmountByImportsId(String importsId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Long> query = session.createQuery("" +
                    "SELECT SUM(od.amount) " +
                    "FROM ImportsDetail od " +
                    "WHERE od.imports.id = :importsId");
            query.setParameter("importsId", importsId);
            Long result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Long getSumQuantityByImportsId(String importsId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Long> query = session.createQuery("" +
                    "SELECT SUM(od.quantity) " +
                    "FROM ImportsDetail od " +
                    "WHERE od.imports.id = :importsId");
            query.setParameter("importsId", importsId);
            Long result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<ImportsDetail> getByImportsId(String importsId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<ImportsDetail> query = session.createQuery("" +
                    "SELECT i " +
                    "FROM ImportsDetail i " +
                    "WHERE i.imports.id = :importsId");
            query.setParameter("importsId", importsId);
            List<ImportsDetail> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<ImportsDetail> getDistinctByImportsIdIn(List<String> importsId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("" +
                    "SELECT i.id, i.imports.id, i.merchandise.id, sum(i.quantity) as quantity, sum(i.amount) as amount " +
                    "FROM ImportsDetail i " +
                    "WHERE i.imports.id IN :idList " +
                    "GROUP BY i.merchandise.id");
            query.setParameter("idList", importsId);
            List<Object> queryResult = query.getResultList();
            List<ImportsDetail> result = new ArrayList<>();
            for (Object c : queryResult) {
                result.add(new ImportsDetail(c));
            }
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Long getBoughtQuantityOfMerchandise(String ordersId, String merchandiseId, String importsId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Long> query = session.createQuery("" +
                    "SELECT sum(t.quantity) " +
                    "FROM ImportsDetail t " +
                    "WHERE t.imports.id IN (SELECT u.id FROM Imports u WHERE u.orders.id = :ordersId) " +
                    "AND t.merchandise.id = :merchandiseId " +
                    "AND t.imports.id <> :importsId");
            query.setParameter("ordersId", ordersId);
            query.setParameter("merchandiseId", merchandiseId);
            query.setParameter("importsId", importsId);
            Long result = query.uniqueResult();
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
