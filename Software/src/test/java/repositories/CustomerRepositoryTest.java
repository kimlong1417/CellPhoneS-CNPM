package repositories;

import entities.Customer;
import entities.OrdersDetail;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import utils.HibernateUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest {

    // TC240
    @Test
    void getAll() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\CustomerRepository\\getAll");
        List<Customer> actualResult = CustomerRepository.getAll();
        List<String> expectedResult = new ArrayList<>();
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResult, actualResult.stream().map(Customer::getId).collect(Collectors.toList()));
    }

    // TC241
    @Test
    void getByNameOrPhone() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\CustomerRepository\\getByNameOrPhone");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            List<Customer> actualResult = CustomerRepository.getByNameOrPhone(input);
            assertNotNull(actualResult);
            assertTrue(actualResult.size() > 0);
            assertEquals(expectedResult, actualResult.get(0).getId());
        }
        scanner.close();
    }

    // TC242
    @Test
    void getByName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\CustomerRepository\\getByName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            Customer actualResult = CustomerRepository.getByName(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    // TC243
    @Test
    void getByPhone() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\CustomerRepository\\getByPhone");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            Customer actualResult = CustomerRepository.getByPhone(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    // TC244
    @Test
    void getByEmail() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\CustomerRepository\\getByEmail");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            Customer actualResult = CustomerRepository.getByEmail(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    // TC245
    @Test
    void getAllCustomerActiveOrders() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\CustomerRepository\\getAllCustomerActiveOrders");
        Scanner scanner = new Scanner(testInputs);
        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<Customer> actualResult = CustomerRepository.getAllCustomerActiveOrders();
        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResult, actualResult.stream().map(Customer::getId).collect(Collectors.toList()));
    }

    // TC246
    @Test
    void getAllSupplierActiveOrders() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\CustomerRepository\\getAllSupplierActiveOrders");
        Scanner scanner = new Scanner(testInputs);
        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<Customer> actualResult = CustomerRepository.getAllSupplierActiveOrders();
        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResult, actualResult.stream().map(Customer::getId).collect(Collectors.toList()));
    }
}