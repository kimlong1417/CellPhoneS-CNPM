package repositories;

import entities.Merchandise;
import entities.Orders;
import entities.OrdersDetail;
import javafx.scene.chart.XYChart;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import utils.HibernateUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OrdersDetailRepositoryTest {
    // TC113
    @Test
    void getByOrdersId() throws FileNotFoundException {
        List<String> input = new ArrayList<>();
        List<String> expectedResult = new ArrayList<>();
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersDetailRepository\\getByOrdersId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            input.add(currentLine.split(", result: ")[0]);
            expectedResult.addAll(Arrays.asList(currentLine.split(", result: ")[1].split(", ")));
        }
        scanner.close();

        for (int i = 0; i < input.size(); i++) {
            List<OrdersDetail> ordersDetailList = OrdersDetailRepository.getByOrdersId(input.get(i));
            if (i == input.size() - 1) {
                assertEquals(0, ordersDetailList.size());
            } else {
                assertNotNull(ordersDetailList);
                List<String> actualResult = ordersDetailList.stream().map(OrdersDetail::getId).collect(Collectors.toList());
                assertEquals(actualResult.size(), actualResult.stream().filter(expectedResult::contains).count());
            }
        }
    }

    // TC114
    @Test
    void deleteByOrdersId() throws FileNotFoundException {
        Session session;
        List<String> inputIds = new ArrayList<>();
        List<String> inputOrdersId = new ArrayList<>();
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersDetailRepository\\deleteByOrdersId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String id = currentLine.split(", ")[0];
            String ordersId = currentLine.split(", ")[1];
            String merchandiseId = currentLine.split(", ")[2];
            String quantity = currentLine.split(", ")[3];
            String amount = currentLine.split(", ")[4];
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(new OrdersDetail(id, session.get(Orders.class, ordersId), session.get(Merchandise.class, merchandiseId), Integer.parseInt(quantity), Long.parseLong(amount)));
            session.getTransaction().commit();
            session.close();

            inputIds.add(id);
            inputOrdersId.add(ordersId);
        }
        scanner.close();

        for (String id : inputOrdersId) {
            OrdersDetailRepository.deleteByOrdersId(id);
        }

        for (String id : inputIds) {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            OrdersDetail ordersDetail = session.get(OrdersDetail.class, id);
            session.getTransaction().commit();
            session.close();

            assertNull(ordersDetail);
        }
    }

    // TC115
    @Test
    void getSumAmountById() throws FileNotFoundException {
        List<String> input = new ArrayList<>();
        List<String> expectedResult = new ArrayList<>();
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersDetailRepository\\getSumAmountById");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            input.add(currentLine.split(", result: ")[0]);
            expectedResult.add(currentLine.split(", result: ")[1]);
        }
        scanner.close();

        for (int i = 0; i < expectedResult.size(); i++) {
            Long actualResult = OrdersDetailRepository.getSumAmountById(input.get(i));
            assertEquals(Long.parseLong(expectedResult.get(i)), actualResult);
        }
    }

    // TC116
    @Test
    void getSumQuantityById() throws FileNotFoundException {
        List<String> input = new ArrayList<>();
        List<String> expectedResult = new ArrayList<>();
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersDetailRepository\\getSumQuantityById");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            input.add(currentLine.split(", result: ")[0]);
            expectedResult.add(currentLine.split(", result: ")[1]);
        }
        scanner.close();

        for (int i = 0; i < expectedResult.size(); i++) {
            Long actualResult = OrdersDetailRepository.getSumQuantityById(input.get(i));
            assertEquals(Long.parseLong(expectedResult.get(i)), actualResult);
        }
    }

    // TC117
    @Test
    void getById() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersDetailRepository\\getById");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Session session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            OrdersDetail ordersDetail = session.get(OrdersDetail.class, currentLine);
            session.getTransaction().commit();
            session.close();

            assertNotNull(ordersDetail);
            assertEquals(currentLine, ordersDetail.getId());
        }
        scanner.close();
    }

    // TC118
    @Test
    void getAmountBuying() throws FileNotFoundException, ParseException {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersDetailRepository\\getAmountBuying");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Date fromDate = currentLine.split(", ")[0].equals("null") ? null : dateFormat.parse(currentLine.split(", ")[0]);
            Date toDate = currentLine.split(", ")[1].equals("null") ? null : dateFormat.parse(currentLine.split(", ")[1]);

            if (fromDate == null && toDate == null) {
                List<String> expectedResult = Arrays.asList(currentLine.split(", ")[2].split(": ")[1].split(", "));
                for (int i = 0; i < expectedResult.size(); i++) {
                    List<Object> allBuyingAmount = OrdersDetailRepository.getAmountBuying(fromDate, toDate);
                    assertNotNull(allBuyingAmount);
                    List<Object[]> allObjectArrays = allBuyingAmount.stream().map(t -> (Object[]) t).collect(Collectors.toList());
                    List<Long> allActualResult = allObjectArrays.stream().map(t -> (Long) t[1]).collect(Collectors.toList());

                    int count = 0;
                    for (Object[] t : allObjectArrays) {
                        assertTrue(expectedResult.contains(String.valueOf(t[1])));
                        ++count;
                    }
                    assertEquals(count, allActualResult.size());
                }
            } else if (fromDate != null && toDate == null) {
                List<String> expectedResult = Arrays.asList(currentLine.split(", ")[2].split(": ")[1].split(", "));
                for (int i = 0; i < expectedResult.size(); i++) {
                    List<Object> allBuyingAmount = OrdersDetailRepository.getAmountBuying(fromDate, toDate);
                    assertNotNull(allBuyingAmount);
                    List<Object[]> allObjectArrays = allBuyingAmount.stream().map(t -> (Object[]) t).collect(Collectors.toList());
                    List<Long> allActualResult = allObjectArrays.stream().map(t -> (Long) t[1]).collect(Collectors.toList());

                    int count = 0;
                    for (Object[] t : allObjectArrays) {
                        assertTrue(expectedResult.contains(String.valueOf(t[1])));
                        ++count;
                    }
                    assertEquals(count, allActualResult.size());
                }
            } else if (fromDate == null && toDate != null) {
                List<String> expectedResult = Arrays.asList(currentLine.split(", ")[2].split(": ")[1].split(", "));
                for (int i = 0; i < expectedResult.size(); i++) {
                    List<Object> allBuyingAmount = OrdersDetailRepository.getAmountBuying(fromDate, toDate);
                    assertNotNull(allBuyingAmount);
                    List<Object[]> allObjectArrays = allBuyingAmount.stream().map(t -> (Object[]) t).collect(Collectors.toList());
                    List<Long> allActualResult = allObjectArrays.stream().map(t -> (Long) t[1]).collect(Collectors.toList());

                    int count = 0;
                    for (Object[] t : allObjectArrays) {
                        assertTrue(expectedResult.contains(String.valueOf(t[1])));
                        ++count;
                    }
                    assertEquals(count, allActualResult.size());
                }
            } else {
                List<String> expectedResult = Arrays.asList(currentLine.split(", ")[2].split(": ")[1].split(", "));
                for (int i = 0; i < expectedResult.size(); i++) {
                    List<Object> allBuyingAmount = OrdersDetailRepository.getAmountBuying(fromDate, toDate);
                    assertNotNull(allBuyingAmount);
                    List<Object[]> allObjectArrays = allBuyingAmount.stream().map(t -> (Object[]) t).collect(Collectors.toList());
                    List<Long> allActualResult = allObjectArrays.stream().map(t -> (Long) t[1]).collect(Collectors.toList());

                    int count = 0;
                    for (Object[] t : allObjectArrays) {
                        assertTrue(expectedResult.contains(String.valueOf(t[1])));
                        ++count;
                    }
                    assertEquals(count, allActualResult.size());
                }
            }
        }
        scanner.close();
    }
}