package repositories;

import entities.Employee;
import entities.EmployeeRoles;
import entities.Roles;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import utils.HibernateUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRolesRepositoryTest {

    //TC253
    @Test
    void getByEmployeeId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRolesRepository\\getByEmployeeId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String expectedResult = currentLine.split(", result: ")[1];
            String input = currentLine.split(", result: ")[0];
            EmployeeRoles actualResult = EmployeeRolesRepository.getByEmployeeId(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.getId());
        }
        scanner.close();
    }

    //TC254
    @Test
    void getByRolesId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRolesRepository\\getByRolesId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            List<String> expectedResult = new ArrayList<>();
            if (currentLine.split(", result: ")[1].contains(", ")) {
                expectedResult.addAll(Arrays.asList(currentLine.split(", result: ")[1].split(", ")));
            } else {
                expectedResult.add(currentLine.split(", result: ")[1]);
            }
            String input = currentLine.split(", result: ")[0];
            List<EmployeeRoles> actualResult = EmployeeRolesRepository.getByRolesId(input);
            assertNotNull(actualResult);
            assertEquals(expectedResult, actualResult.stream().map(EmployeeRoles::getId).collect(Collectors.toList()));
        }
        scanner.close();
    }

    //TC255
    @Test
    void deleteByEmployeeId() throws FileNotFoundException {
        File testInputs = new File("D:\\Github\\CNPM\\Software\\src\\test\\java\\test_inputs\\EmployeeRolesRepository\\deleteByEmployeeId");
        Scanner scanner = new Scanner(testInputs);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();

            Session session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(new EmployeeRoles(currentLine.split(", ")[1],
                                      session.get(Roles.class, currentLine.split(", ")[2]),
                                      session.get(Employee.class, currentLine.split(", ")[0])));
            session.getTransaction().commit();
            session.close();

            String input = currentLine.split(", ")[0];
            EmployeeRolesRepository.deleteByEmployeeId(input);

            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            assertNull(session.get(EmployeeRoles.class, currentLine.split(", ")[0]));
            session.getTransaction().commit();
            session.close();
        }
        scanner.close();
    }
}