package repositories;

import entities.Roles;
import entities.RolesDetail;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class RolesRepositoryTest {

    // TC420
    @Test
    void getAll() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\RolesRepository\\getAll");
        Scanner scanner = new Scanner(testInputs);
        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<Roles> actualResult = RolesRepository.getAll();
        assertNotNull(actualResult);
        assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
    }

    // TC421
    @Test
    void getByName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\RolesRepository\\getByName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Roles actualResult = RolesRepository.getByName(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertEquals(currentLine.split(", result: ")[1], actualResult.getId());
        }
        scanner.close();
    }

    // TC422
    @Test
    void getLikeName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\RolesRepository\\getLikeName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));
            List<Roles> actualResult = RolesRepository.getLikeName(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
        }
        scanner.close();
    }
}