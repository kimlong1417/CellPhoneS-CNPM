package repositories;

import entities.Imports;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ImportsRepositoryTest {

    // TC407
    @Test
    void getAll() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsRepository\\getAll");
        Scanner scanner = new Scanner(testInputs);

        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<Imports> importsList = ImportsRepository.getAll();
        assertNotNull(importsList);
        assertTrue(importsList.size() > 0);
        List<String> actualResult = importsList.stream().map(Imports::getId).collect(Collectors.toList());
        assertEquals(expectedResult.size(), actualResult.stream().filter(expectedResult::contains).count());
    }

    // TC408
    @Test
    void getById() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsRepository\\getById");
        Scanner scanner = new Scanner(testInputs);

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Imports actualResult = ImportsRepository.getById(currentLine);
            assertNotNull(actualResult);
            assertEquals(currentLine, actualResult.getId());
        }
        scanner.close();
    }

    // TC409
    @Test
    void getByOrdersId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsRepository\\getByOrdersId");
        Scanner scanner = new Scanner(testInputs);

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));
            List<Imports> actualResult = ImportsRepository.getByOrdersId(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertTrue(actualResult.size() > 0);
            assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
        }
        scanner.close();
    }

    // TC410
    @Test
    void getLikeCustomerName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsRepository\\getLikeCustomerName");
        Scanner scanner = new Scanner(testInputs);

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));
            List<Imports> actualResult = ImportsRepository.getLikeCustomerName(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertTrue(actualResult.size() > 0);
            assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
        }
        scanner.close();
    }
}