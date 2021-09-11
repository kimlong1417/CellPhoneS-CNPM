package repositories;

import entities.Merchandise;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MerchandiseRepositoryTest {
    static String stringFromDate = null;
    static String stringToDate = null;

    // TC411
    @Test
    void getAll() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\MerchandiseRepository\\getAll");
        Scanner scanner = new Scanner(testInputs);

        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<Merchandise> actualResult = MerchandiseRepository.getAll();
        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
    }

    // TC412
    @Test
    void getByName() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\MerchandiseRepository\\getByName");
        Scanner scanner = new Scanner(testInputs);

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String input = currentLine.split(", result: ")[0];
            String expectedResult = currentLine.split(", result: ")[1];
            Merchandise actualResult = MerchandiseRepository.getByName(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    // TC413
    @Test
    void getLikeNameAndBranch() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\MerchandiseRepository\\getLikeNameAndBranch");
        Scanner scanner = new Scanner(testInputs);

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String input = currentLine.split(", result: ")[0];
            String expectedResult = currentLine.split(", result: ")[1];
            List<Merchandise> actualResult = MerchandiseRepository.getLikeNameAndBranch(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.get(0).getId());
        }
        scanner.close();
    }

    // TC414
    @Test
    void getHasQuantity() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\MerchandiseRepository\\getHasQuantity");
        Scanner scanner = new Scanner(testInputs);

        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<Merchandise> actualResult = MerchandiseRepository.getHasQuantity();
        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
    }

    // TC415
    @Test
    void getById() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\MerchandiseRepository\\getHasQuantity");
        Scanner scanner = new Scanner(testInputs);

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Merchandise actualResult = MerchandiseRepository.getById(currentLine);
            assertNotNull(actualResult);
            assertEquals(currentLine, actualResult.getId());
        }
        scanner.close();
    }

    // TC416
    @Test
    void getAllMerchandiseTypes() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\MerchandiseRepository\\getAllMerchandiseTypes");
        Scanner scanner = new Scanner(testInputs);

        List<String> expectedResult = new ArrayList<>();
        while (scanner.hasNextLine()) {
            expectedResult.add(scanner.nextLine());
        }
        scanner.close();

        List<String> actualResult = MerchandiseRepository.getAllMerchandiseTypes();
        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResult.size(), actualResult.stream().filter(expectedResult::contains).count());
    }

    // TC417
    @Test
    @SuppressWarnings("unchecked")
    void getAmountBuying() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        List<String> names = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        List<String> amounts = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\MerchandiseRepository\\getAmountBuying.json");
        Object obj = jsonParser.parse(reader);
        JSONArray inputTests = (JSONArray) obj;
        stringFromDate = null;
        stringToDate = null;

        inputTests.forEach(inp -> parseGetAmountBuyingResult((JSONObject) inp, names, quantities, amounts));
        if (names.size() != 0) {
            for (int i = 0; i < names.size(); i++) {
                names.clear();
                amounts.clear();
                quantities.clear();
                inputTests.forEach(w -> parseGetAmountBuyingResult((JSONObject) w, names, quantities, amounts));
                Date fromDate = stringFromDate == null ? null : dateFormat.parse(stringFromDate);
                Date toDate = stringToDate == null ? null : dateFormat.parse(stringToDate);
                if (fromDate == null && toDate == null) {
                    List<Object> allBuyingAmount = MerchandiseRepository.getAmountBuying(fromDate, toDate);
                    assertNotNull(allBuyingAmount);
                    assertTrue(allBuyingAmount.size() > 0);

                    Object[] fields = (Object[]) allBuyingAmount.get(i);
                    assertEquals(names.get(i), fields[0]);
                    assertEquals(quantities.get(i), String.valueOf(fields[1]));
                    assertEquals(amounts.get(i), String.valueOf(fields[2]));
                } else if (fromDate != null && toDate == null) {
                    List<Object> allBuyingAmount = MerchandiseRepository.getAmountBuying(fromDate, toDate);
                    assertNotNull(allBuyingAmount);
                    assertTrue(allBuyingAmount.size() > 0);

                    Object[] fields = (Object[]) allBuyingAmount.get(i);
                    assertEquals(names.get(i), fields[0]);
                    assertEquals(quantities.get(i), String.valueOf(fields[1]));
                    assertEquals(amounts.get(i), String.valueOf(fields[2]));
                } else if (fromDate == null && toDate != null) {
                    List<Object> allBuyingAmount = MerchandiseRepository.getAmountBuying(fromDate, toDate);
                    assertNotNull(allBuyingAmount);
                    assertTrue(allBuyingAmount.size() > 0);

                    Object[] fields = (Object[]) allBuyingAmount.get(i);
                    assertEquals(names.get(i), fields[0]);
                    assertEquals(quantities.get(i), String.valueOf(fields[1]));
                    assertEquals(amounts.get(i), String.valueOf(fields[2]));
                } else {
                    List<Object> allBuyingAmount = MerchandiseRepository.getAmountBuying(fromDate, toDate);
                    assertNotNull(allBuyingAmount);
                    assertTrue(allBuyingAmount.size() > 0);

                    Object[] fields = (Object[]) allBuyingAmount.get(i);
                    assertEquals(names.get(i), fields[0]);
                    assertEquals(quantities.get(i), String.valueOf(fields[1]));
                    assertEquals(amounts.get(i), String.valueOf(fields[2]));
                }
            }
        }
    }

    private static void parseGetAmountBuyingResult(JSONObject employee, List<String> names, List<String> quantities, List<String> amounts) {
        //Get input object within list
        JSONObject employeeObject = (JSONObject) employee.get("input");

        String merchandiseName = (String) employeeObject.get("merchandiseName");
        names.add(merchandiseName);

        String quantity = (String) employeeObject.get("quantity");
        quantities.add(quantity);

        String amount = (String) employeeObject.get("amount");
        amounts.add(amount);

        stringFromDate = (String) employeeObject.get("fromDate");
        stringToDate = (String) employeeObject.get("toDate");
    }
}