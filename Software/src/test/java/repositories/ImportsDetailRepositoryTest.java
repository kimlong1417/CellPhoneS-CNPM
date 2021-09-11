package repositories;

import entities.ImportsDetail;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ImportsDetailRepositoryTest {

    // TC402
    @Test
    void getSumAmountByImportsId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsDetailRepository\\getSumAmountByImportsId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Long expectedResult = Long.parseLong(currentLine.split(", result: ")[1]);
            String input = currentLine.split(", result: ")[0];
            Long actualResult = ImportsDetailRepository.getSumAmountByImportsId(input);

            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult);
        }
        scanner.close();
    }

    // TC403
    @Test
    void getSumQuantityByImportsId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsDetailRepository\\getSumQuantityByImportsId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Long expectedResult = Long.parseLong(currentLine.split(", result: ")[1]);
            String input = currentLine.split(", result: ")[0];
            Long actualResult = ImportsDetailRepository.getSumQuantityByImportsId(input);

            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult);
        }
        scanner.close();
    }

    // TC404
    @Test
    void getByImportsId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsDetailRepository\\getByImportsId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));
            String input = currentLine.split(", result: ")[0];
            List<ImportsDetail> actualResult = ImportsDetailRepository.getByImportsId(input);

            assertNotNull(actualResult);
            assertTrue(actualResult.size() > 0);
            List<String> actualResultIds = actualResult.stream().map(ImportsDetail::getId).collect(Collectors.toList());
            assertEquals(expectedResult.size(), actualResultIds.stream().filter(expectedResult::contains).count());
        }
        scanner.close();
    }

    // TC405
    @Test
    void getDistinctByImportsIdIn() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsDetailRepository\\getDistinctByImportsIdIn");
        Scanner scanner = new Scanner(testInputs);

        List<String> input = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<String> importsIds = new ArrayList<>();
        List<String> merchandiseIds = new ArrayList<>();
        List<String> sumQuantities = new ArrayList<>();
        List<String> sumAmounts = new ArrayList<>();
        int expectedResultSize = 0;
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            input.add(currentLine.split(", result: ")[0]);
            ids.add(currentLine.split(", result: ")[1].split(", ")[0]);
            importsIds.add(currentLine.split(", result: ")[1].split(", ")[1]);
            merchandiseIds.add(currentLine.split(", result: ")[1].split(", ")[2]);
            sumQuantities.add(currentLine.split(", result: ")[1].split(", ")[3]);
            sumAmounts.add(currentLine.split(", result: ")[1].split(", ")[4]);
            ++expectedResultSize;
        }
        scanner.close();

        List<ImportsDetail> actualResult = ImportsDetailRepository.getDistinctByImportsIdIn(input);
        assertNotNull(actualResult);
        assertEquals(expectedResultSize, actualResult.size());
        for (int i = 0; i < expectedResultSize; i++) {
            assertEquals(ids.get(i), actualResult.get(i).getId());
            assertEquals(importsIds.get(i), actualResult.get(i).getImports().getId());
            assertEquals(merchandiseIds.get(i), actualResult.get(i).getMerchandise().getId());
            assertEquals(Long.parseLong(sumQuantities.get(i)), actualResult.get(i).getQuantity());
            assertEquals(Long.parseLong(sumAmounts.get(i)), actualResult.get(i).getAmount());
        }

    }

    // TC406
    @Test
    void getBoughtQuantityOfMerchandise() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\ImportsDetailRepository\\getBoughtQuantityOfMerchandise");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];

            String inputOrdersId = currentLine.split(", result: ")[0].split(", ")[0];
            String inputMerchandiseId = currentLine.split(", result: ")[0].split(", ")[1];
            String inputImportsId = currentLine.split(", result: ")[0].split(", ")[2];
            Long actualResult = ImportsDetailRepository.getBoughtQuantityOfMerchandise(inputOrdersId, inputMerchandiseId, inputImportsId);

            assertNotNull(actualResult);
            assertEquals(Long.parseLong(expectedResult), actualResult);
        }
        scanner.close();
    }
}