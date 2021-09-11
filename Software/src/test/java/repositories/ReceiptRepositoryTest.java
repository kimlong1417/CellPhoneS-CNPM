package repositories;

import entities.Receipt;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptRepositoryTest {
    Session session;

    // TC108
    @Test
    void getAll() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("9c8c8503-dcc6-4f3b-a37f-091334a8e769");
        expectedResult.add("2675d78c-f2ba-4ea0-8195-ea41c30989c6");
        expectedResult.add("30e6c6dc-a88d-49b9-b40c-646870e5ed8b");

        List<Receipt> actualResult = ReceiptRepository.getAll();
        assertNotNull(actualResult);
        assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
    }

    // TC109
    @Test
    void getLikeCustomerName() {
        String expectedResultId = "2675d78c-f2ba-4ea0-8195-ea41c30989c6";
        List<Receipt> actualResult = ReceiptRepository.getLikeCustomerName("Huy");
        assertNotNull(actualResult);
        assertTrue(actualResult.size() > 0);
        assertEquals(expectedResultId, actualResult.get(0).getId());
    }
}