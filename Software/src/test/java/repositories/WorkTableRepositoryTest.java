package repositories;

import entities.Roles;
import entities.WorkShift;
import entities.WorkTable;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import utils.HibernateUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class WorkTableRepositoryTest {

    @Test
    void getByShiftId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\WorkTableRepository\\getByShiftId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));
            List<WorkTable> actualResult = WorkTableRepository.getByShiftId(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
        }
        scanner.close();
    }

    @Test
    void getAll() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\WorkTableRepository\\getAll");
        Scanner scanner = new Scanner(testInputs);
        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<WorkTable> actualResult = WorkTableRepository.getAll();
        assertNotNull(actualResult);
        assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
    }

    @Test
    void getByEmployeeOrShift() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\WorkTableRepository\\getByEmployeeOrShift");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));
            List<WorkTable> actualResult = WorkTableRepository.getByEmployeeOrShift(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
        }
        scanner.close();
    }

    @Test
    void getByEmployeeNameAndShift() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\WorkTableRepository\\getByEmployeeNameAndShift");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));

            String inputEmployeeName = currentLine.split(", result: ")[0].split(", ")[0];

            WorkShift inputShift = WorkShiftRepository.getByName(currentLine.split(", result: ")[0].split(", ")[1]);
            assertNotNull(inputShift);
            List<WorkTable> actualResult = WorkTableRepository.getByEmployeeNameAndShift(inputEmployeeName, inputShift);
            assertNotNull(actualResult);
            assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
        }
        scanner.close();
    }
}