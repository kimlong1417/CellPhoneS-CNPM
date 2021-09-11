package validation;

import entities.Employee;
import org.hibernate.Session;
import repositories.EmployeeRepository;
import utils.NumberHelper;
import utils.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeValidation {

    public static List<String> validateInsert(Employee employee) {
        List<String> msg = new ArrayList<>();

        Employee employeeName = EmployeeRepository.getByName(employee.getFullName());
        Employee employeePhone = EmployeeRepository.getByPhone(employee.getPhone());
        Employee employeeEmail = EmployeeRepository.getByEmail(employee.getEmail());

        if (employee.getFullName() == null || employee.getFullName().isEmpty()) {
            msg.add("Chưa điền tên");
        } else if (employeeName != null) {
            msg.add("Tên đã được sử dụng");
        }
        if (employee.getPhone() == null || employee.getPhone().isEmpty()) {
            msg.add("Chưa điền số điện thoại");
        } else if (!NumberHelper.isNumber(employee.getPhone())) {
            msg.add("Số điện thoại phải là số");
        } else if (employee.getPhone().length() != 10) {
            msg.add("Số điện thoại phải có 10 chữ số");
        } else if (employeePhone != null) {
            msg.add("Số điện thoại đã được sử dụng");
        }
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            msg.add("Chưa điền email");
        } else if (!StringHelper.isEmail(employee.getEmail())) {
            msg.add("Email không hợp lệ");
        } else if (employeeEmail != null) {
            msg.add("Email đã được sử dụng");
        }
        if (employee.getPassword() == null) {
            msg.add("Chưa điền mật khẩu");
        }
        if (employee.getBirthDay() == null) {
            msg.add("Chưa chọn ngày sinh");
        }

        return msg;
    }

    public static List<String> validateUpdate(Employee employee) {
        List<String> msg = new ArrayList<>();

        Employee employeeName = EmployeeRepository.getByName(employee.getFullName());
        Employee employeePhone = EmployeeRepository.getByPhone(employee.getPhone());
        Employee employeeEmail = EmployeeRepository.getByEmail(employee.getEmail());

        // check name
        if (employee.getFullName() == null || employee.getFullName().isEmpty()) {
            msg.add("Chưa điền tên");
        } else if (employeeName != null && !employeeName.getId().equals(employee.getId())) {
            msg.add("Tên đã được sử dụng");
        }
        // check phone
        if (employee.getPhone() == null || employee.getPhone().isEmpty()) {
            msg.add("Chưa điền số điện thoại");
        } else if (!NumberHelper.isNumber(employee.getPhone())) {
            msg.add("Số điện thoại phải là số");
        } else if (employee.getPhone().length() != 10) {
            msg.add("Số điện thoại phải có 10 chữ số");
        } else if (employeePhone != null && !employeePhone.getId().equals(employee.getId())) {
            msg.add("Số điện thoại đã được sử dụng");
        }
        // check email
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            msg.add("Chưa điền email");
        } else if (!StringHelper.isEmail(employee.getEmail())) {
            msg.add("Email không hợp lệ");
        } else if (employeeEmail != null && !employeeEmail.getId().equals(employee.getId())) {
            msg.add("Email đã được sử dụng");
        }
        if (employee.getPassword() == null) {
            msg.add("Chưa điền mật khẩu");
        }
        if (employee.getBirthDay() == null) {
            msg.add("Chưa chọn ngày sinh");
        }

        return msg;
    }
}
