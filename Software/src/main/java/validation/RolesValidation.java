package validation;

import entities.EmployeeRoles;
import entities.Roles;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.EmployeeRolesRepository;
import repositories.RolesRepository;

import java.util.ArrayList;
import java.util.List;

public class RolesValidation {

    public static List<String> validateInsert(Roles roles) {
        List<String> msg = new ArrayList<>();

        Roles rolesName = RolesRepository.getByName(roles.getName());

        if (roles.getName() == null || roles.getName().isEmpty()) {
            msg.add("Chưa điền tên chức vụ");
        } else if (rolesName != null) {
            msg.add("Tên đã được sử dụng");
        }

        return msg;
    }

    public static List<String> validateUpdate(Roles roles) {
        List<String> msg = new ArrayList<>();

        Roles rolesName = RolesRepository.getByName(roles.getName());

        if (roles.getName() == null || roles.getName().isEmpty()) {
            msg.add("Chưa điền tên");
        } else if (rolesName != null && !rolesName.getId().equals(roles.getId())) {
            msg.add("Tên đã được sử dụng");
        }

        return msg;
    }

    public static List<String> validateDelete(Roles roles) {
        List<String> msg = new ArrayList<>();

        // Check if someone is using role
        List<EmployeeRoles> employeeRolesList = EmployeeRolesRepository.getByRolesId(roles.getId());
        if (employeeRolesList != null && employeeRolesList.size() > 0) {
            msg.add("Chức vụ đang được sử dụng");
        }

        return msg;
    }
}
