package repositories;

import entities.Permissions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PermissionRepositoryTest {

    // TC10
    @Test
    void getAll() throws Exception {
        List<String> expectedResult = new ArrayList<>();
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\PermissionRepository\\getAll");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<Permissions> actualResult = PermissionRepository.getAll();
        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResult, actualResult.stream().map(Permissions::getCode).collect(Collectors.toList()));
    }

    // TC11
    @Test
    void getByName() throws FileNotFoundException {
        List<String> input = new ArrayList<>();
        List<String> expectedResult = new ArrayList<>();
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\PermissionRepository\\getByName");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            input.add(currentLine.split(", result: ")[0]);
            expectedResult.add(currentLine.split(", result: ")[1]);
        }
        scanner.close();

        for (int i = 0; i < input.size(); i++) {
            Permissions actualResult = PermissionRepository.getByName(input.get(i));
            if (i == input.size() - 1) {
                assertNull(actualResult);
            } else {
                assertNotNull(actualResult);
                assertEquals(expectedResult.get(i), actualResult.getCode());
            }
        }
    }

    // TC12
    @Test
    void getEmployeeInaccessiblePermission() throws FileNotFoundException {
        List<String> input = new ArrayList<>();
        List<String> expectedResult = new ArrayList<>();
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\PermissionRepository\\getEmployeeInaccessiblePermission");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            input.add(currentLine.split(", result: ")[0]);
            if (!currentLine.split(", result: ")[1].equals("empty")) {
                expectedResult.addAll(Arrays.asList(currentLine.split(", result: ")[1].split(", ")));
            }
        }
        scanner.close();

        for (int i = 0; i < input.size(); i++) {
            List<String> actualResult = PermissionRepository.getEmployeeInaccessiblePermission(input.get(0));
            if (i == input.size() - 1) {
                assertEquals(0, actualResult.size());
            } else {
                assertNotNull(actualResult);
                if (actualResult.size() > 0) {
                    assertEquals(expectedResult.get(i), actualResult.get(i));
                }
            }
        }
    }
}