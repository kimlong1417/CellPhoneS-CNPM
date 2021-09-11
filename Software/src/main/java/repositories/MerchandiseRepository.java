package repositories;

import entities.Merchandise;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.Date;
import java.util.List;

public class MerchandiseRepository {
    private static Session session;

    public static List<Merchandise> getAll() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Merchandise> query = session.createQuery("" +
                    "SELECT m " +
                    "FROM Merchandise m");
            List<Merchandise> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Merchandise getByName(String name) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Merchandise> query = session.createQuery("" +
                    "SELECT c " +
                    "FROM Merchandise c " +
                    "WHERE c.name = :name");
            query.setParameter("name", name);
            Merchandise result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Merchandise> getLikeNameAndBranch(String keySearch) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Merchandise> query = session.createQuery("" +
                    "SELECT m " +
                    "FROM Merchandise m " +
                    "WHERE m.name LIKE :keySearch OR m.branch LIKE :keySearch");
            query.setParameter("keySearch", "%" + keySearch + "%");
            List<Merchandise> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Merchandise> getHasQuantity() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Merchandise> query = session.createQuery("" +
                    "SELECT m " +
                    "FROM Merchandise m " +
                    "WHERE m.quantity > 0");
            List<Merchandise> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static Merchandise getById(String id) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Merchandise> query = session.createQuery("" +
                    "SELECT m " +
                    "FROM Merchandise m " +
                    "WHERE m.id = :id");
            query.setParameter("id", id);
            Merchandise result = query.uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<String> getAllMerchandiseTypes() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<String> query = session.createQuery("" +
                    "SELECT DISTINCT c.type " +
                    "FROM Merchandise c");
            List<String> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static List<Object> getAmountBuying(Date fromDate, Date toDate) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Object> query;
            if (fromDate == null && toDate == null) {
                query = session.createQuery("" +
                        "SELECT od.merchandise.name, SUM(od.quantity) , SUM(od.amount) AS sumAmount " +
                        "FROM OrdersDetail od " +
                        "WHERE od.orders.id IN (" +
                        "SELECT o.id " +
                        "FROM Orders o " +
                        "WHERE o.type = 'Bán hàng' AND o.status = 'Hoàn tất')" +
                        "GROUP BY od.merchandise.id " +
                        "ORDER BY sumAmount DESC ");
            } else if (fromDate != null && toDate == null) {
                query = session.createQuery("" +
                        "SELECT od.merchandise.name, SUM(od.quantity) , SUM(od.amount) AS sumAmount " +
                        "FROM OrdersDetail od " +
                        "WHERE od.orders.id IN (" +
                        "SELECT o.id " +
                        "FROM Orders o " +
                        "WHERE o.type = 'Bán hàng' AND o.status = 'Hoàn tất' AND o.createdDate >= :fromDate)" +
                        "GROUP BY od.merchandise.id " +
                        "ORDER BY sumAmount DESC ");
                query.setParameter("fromDate", fromDate);
            } else if (fromDate == null && toDate != null) {
                query = session.createQuery("" +
                        "SELECT od.merchandise.name, SUM(od.quantity) , SUM(od.amount) AS sumAmount " +
                        "FROM OrdersDetail od " +
                        "WHERE od.orders.id IN (" +
                        "SELECT o.id " +
                        "FROM Orders o " +
                        "WHERE o.type = 'Bán hàng' AND o.status = 'Hoàn tất' AND o.createdDate <= :toDate)" +
                        "GROUP BY od.merchandise.id " +
                        "ORDER BY sumAmount DESC ");
                query.setParameter("toDate", toDate);
            } else {
                query = session.createQuery("" +
                        "SELECT od.merchandise.name, SUM(od.quantity) , SUM(od.amount) AS sumAmount " +
                        "FROM OrdersDetail od " +
                        "WHERE od.orders.id IN (" +
                        "SELECT o.id " +
                        "FROM Orders o " +
                        "WHERE o.type = 'Bán hàng' AND o.status = 'Hoàn tất' AND o.createdDate >= :fromDate AND o.createdDate <= :toDate )" +
                        "GROUP BY od.merchandise.id " +
                        "ORDER BY sumAmount DESC ");
                query.setParameter("fromDate", fromDate);
                query.setParameter("toDate", toDate);
            }
            query.setMaxResults(8);
            List<Object> result = query.getResultList();
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
