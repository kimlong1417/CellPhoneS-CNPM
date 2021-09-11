package repositories;

import entities.WorkShift;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WorkShiftRepositoryTest {

    // TC423
    @Test
    void getAll() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\WorkShiftRepository\\getAll");
        Scanner scanner = new Scanner(testInputs);
        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<WorkShift> actualResult = WorkShiftRepository.getAll();
        assertNotNull(actualResult);
        assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
    }

    // TC424
    @Test
    void getByName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\WorkShiftRepository\\getByName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            WorkShift actualResult = WorkShiftRepository.getByName(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertEquals(currentLine.split(", result: ")[1], actualResult.getId());
        }
        scanner.close();
    }

    // TC425
    @Test
    void getLikeName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\WorkShiftRepository\\getLikeName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));
            List<WorkShift> actualResult = WorkShiftRepository.getLikeName(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
        }
        scanner.close();
    }
}