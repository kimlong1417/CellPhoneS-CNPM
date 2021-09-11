package repositories;

import entities.Orders;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OrdersRepositoryTest {

    // TC272
    @Test
    void getAll() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersRepository\\getAll");
        List<Orders> actualResult = OrdersRepository.getAll();
        List<String> expectedResult = new ArrayList<>();
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        List<String> actualResultIds = actualResult.stream().map(Orders::getId).collect(Collectors.toList());
        assertEquals(expectedResult.size(), actualResultIds.stream().filter(expectedResult::contains).count());
    }

    // TC273
    @Test
    void getLikeCustomerName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersRepository\\getLikeCustomerName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();

            List<String> expectedResult = new ArrayList<>(Arrays.asList(currentLine.split(", result: ")[1].split(", ")));

            String input = currentLine.split(", result: ")[0];
            List<Orders> actualResult = OrdersRepository.getLikeCustomerName(input);
            assertNotNull(actualResult);
            List<String> actualResultIds = actualResult.stream().map(Orders::getId).collect(Collectors.toList());
            assertEquals(expectedResult.size(), actualResultIds.stream().filter(expectedResult::contains).count());
        }
        scanner.close();
    }

    // TC274
    @Test
    void getByCustomerName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersRepository\\getByCustomerName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();

            List<String> expectedResult = new ArrayList<>(Arrays.asList(currentLine.split(", result: ")[1].split(", ")));

            String input = currentLine.split(", result: ")[0];
            List<Orders> actualResult = OrdersRepository.getByCustomerName(input);
            assertNotNull(actualResult);
            List<String> actualResultIds = actualResult.stream().map(Orders::getId).collect(Collectors.toList());
            assertEquals(expectedResult.size(), actualResultIds.stream().filter(expectedResult::contains).count());
        }
        scanner.close();
    }

    // TC275
    @Test
    void getActiveByCustomerName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersRepository\\getActiveByCustomerName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();

            List<String> expectedResult = new ArrayList<>(Arrays.asList(currentLine.split(", result: ")[1].split(", ")));

            String input = currentLine.split(", result: ")[0];
            List<Orders> actualResult = OrdersRepository.getActiveByCustomerName(input);
            assertNotNull(actualResult);
            List<String> actualResultIds = actualResult.stream().map(Orders::getId).collect(Collectors.toList());
            assertEquals(expectedResult.size(), actualResultIds.stream().filter(expectedResult::contains).count());
        }
        scanner.close();
    }

    // TC276
    @Test
    void getById() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\OrdersRepository\\getById");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Orders actualResult = OrdersRepository.getById(currentLine);
            assertNotNull(actualResult);
            assertEquals(currentLine, actualResult.getId());
        }
        scanner.close();
    }
}