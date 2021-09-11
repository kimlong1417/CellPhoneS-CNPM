package repositories;

import entities.Permissions;
import entities.Roles;
import entities.RolesDetail;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import utils.HibernateUtils;
import utils.UUIDHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class RolesDetailRepositoryTest {

    // TC418
    @Test
    void getByRolesId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\RolesDetailRepository\\getByRolesId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = Arrays.asList(currentLine.split(", result: ")[1].split(", "));

            List<RolesDetail> actualResult = RolesDetailRepository.getByRolesId(currentLine.split(", result: ")[0]);
            assertNotNull(actualResult);
            assertTrue(actualResult.size() > 0);
            assertEquals(expectedResult.size(), actualResult.stream().filter(t -> expectedResult.contains(t.getId())).count());
        }
        scanner.close();
    }

    // TC419
    @Test
    void deleteByRoleId() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        String id = UUIDHelper.generateType4UUID().toString();
        RolesDetail rolesDetail = new RolesDetail(id, session.get(Roles.class, "c3faf74c-563b-48dc-9eaf-d5277273899e"), session.get(Permissions.class, "CUSTOMER_MANAGEMENT"));
        session.save(rolesDetail);
        session.getTransaction().commit();
        session.close();

        RolesDetailRepository.deleteByRoleId(id);

        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(rolesDetail);
        session.getTransaction().commit();
        session.close();

        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        rolesDetail = session.get(RolesDetail.class, id);
        session.getTransaction().commit();
        session.close();

        assertNull(rolesDetail);
    }
}