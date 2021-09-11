package repositories;

import entities.OrdersDetail;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OrdersDetailRepository {
    private static Session session;

    public static List<OrdersDetail> getByOrdersId(String id) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<OrdersDetail> query = session.createQuery("" +
                    "SELECT a " +
                    "FROM OrdersDetail a " +
                    "WHERE a.orders.id = :id");
            query.setParameter("id", id);
            List<OrdersDetail> result = query.getResultList();
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
            return null;
        }
    }

    public static void deleteByOrdersId(String id) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<OrdersDetail> query = session.createQuery("" +
                    "DELETE " +
                    "FROM OrdersDetail o " +
                    "WHERE o.orders.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session.close();
        }
    }

    public static Long getSumAmountById(String ordersId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Long> query = session.createQuery("" +
                    "SELECT SUM(od.amount) " +
                    "FROM OrdersDetail od " +
                    "WHERE od.orders.id = :ordersId");
            query.setParameter("ordersId", ordersId);
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

    public static Long getSumQuantityById(String ordersId) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<Long> query = session.createQuery("" +
                    "SELECT SUM(od.quantity) " +
                    "FROM OrdersDetail od " +
                    "WHERE od.orders.id = :ordersId");
            query.setParameter("ordersId", ordersId);
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

    public static OrdersDetail getById(String id) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Query<OrdersDetail> query = session.createQuery("" +
                    "SELECT d " +
                    "FROM OrdersDetail d " +
                    "WHERE d.id = :id");
            query.setParameter("id", id);
            OrdersDetail result = query.uniqueResult();
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
                        "SELECT od.orders.customer.fullName, SUM(od.amount) AS sumAmount, od.orders.createdDate " +
                        "FROM OrdersDetail od " +
                        "WHERE od.orders.id IN (" +
                        "SELECT o.id " +
                        "FROM Orders o " +
                        "WHERE o.type = 'Bán hàng' AND o.status = 'Hoàn tất') " +
                        "GROUP BY od.orders.customer.id " +
                        "ORDER BY sumAmount DESC ");
            } else if (fromDate != null && toDate == null) {
                query = session.createQuery("" +
                        "SELECT od.orders.customer.fullName, SUM(od.amount) AS sumAmount, od.orders.createdDate " +
                        "FROM OrdersDetail od " +
                        "WHERE od.orders.id IN (" +
                        "SELECT o.id " +
                        "FROM Orders o " +
                        "WHERE o.type = 'Bán hàng' AND o.status = 'Hoàn tất' AND o.createdDate >= :fromDate) " +
                        "GROUP BY od.orders.customer.id " +
                        "ORDER BY sumAmount DESC ");
                query.setParameter("fromDate", fromDate);
            } else if (fromDate == null && toDate != null) {
                query = session.createQuery("" +
                        "SELECT od.orders.customer.fullName, SUM(od.amount) AS sumAmount, od.orders.createdDate " +
                        "FROM OrdersDetail od " +
                        "WHERE od.orders.id IN (" +
                        "SELECT o.id " +
                        "FROM Orders o " +
                        "WHERE o.type = 'Bán hàng' AND o.status = 'Hoàn tất' AND o.createdDate <= :toDate) " +
                        "GROUP BY od.orders.customer.id " +
                        "ORDER BY sumAmount DESC ");
                query.setParameter("toDate", toDate);
            } else {
                query = session.createQuery("" +
                        "SELECT od.orders.customer.fullName, SUM(od.amount) AS sumAmount, od.orders.createdDate " +
                        "FROM OrdersDetail od " +
                        "WHERE od.orders.id IN (" +
                        "SELECT o.id " +
                        "FROM Orders o " +
                        "WHERE o.type = 'Bán hàng' AND o.status = 'Hoàn tất' AND o.createdDate >= :fromDate AND o.createdDate <= :toDate) " +
                        "GROUP BY od.orders.customer.id " +
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
