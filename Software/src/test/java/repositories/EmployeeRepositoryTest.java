package repositories;

import entities.Employee;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRepositoryTest {

    // TC247
    @Test
    void getByEmail() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRepository\\getByEmail");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            Employee actualResult = EmployeeRepository.getByEmail(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    // TC248
    @Test
    void getAll() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRepository\\getAll");
        List<Employee> actualResult = EmployeeRepository.getAll();
        List<String> expectedResult = new ArrayList<>();
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResult, actualResult.stream().map(Employee::getId).collect(Collectors.toList()));
    }

    // TC249
    @Test
    void getByName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRepository\\getByName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            Employee actualResult = EmployeeRepository.getByName(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    // TC250
    @Test
    void getByEmployeeName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRepository\\getByEmployeeName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            Employee actualResult = EmployeeRepository.getByEmployeeName(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    // TC251
    @Test
    void getByPhone() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRepository\\getByPhone");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            Employee actualResult = EmployeeRepository.getByPhone(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    // TC252
    @Test
    void getByNamePhoneEmail() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRepository\\getByNamePhoneEmail");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            List<Employee> actualResult = EmployeeRepository.getByNamePhoneEmail(input);
            assertNotNull(actualResult);
            assertTrue(actualResult.size() > 0);
            assertEquals(expectedResult, actualResult.get(0).getId());
        }
        scanner.close();
    }
}