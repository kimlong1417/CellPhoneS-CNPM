package validation;

import entities.WorkTable;
import org.hibernate.SessionFactory;
import repositories.WorkTableRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkTableValidation {

    public static List<String> validateInsert(WorkTable workTable) {
        List<String> msg = new ArrayList<>();

        // Check if employee has WorkTable
        List<WorkTable> workTableEmployee = WorkTableRepository.getByEmployeeNameAndShift(workTable.getEmployee().getFullName(), workTable.getWorkShift());
        WorkTable workTEmployee = workTableEmployee.stream().filter(t -> !t.getId().equals(workTable.getId())).findAny().orElse(null);
        if (workTableEmployee.size() > 1) {
            msg.add("Đã đặt ca làm cho nhân viên này");
        }
        if (workTEmployee != null) {
            List<String> workDaysOfWeek = Arrays.asList(workTable.getDayOfWeek().split("-"));
            List<String> otherWorkDaysOfWeek = Arrays.asList(workTEmployee.getDayOfWeek().split("-"));
            for (String item : workDaysOfWeek) {
                if (otherWorkDaysOfWeek.stream().anyMatch(t -> t.equals(item))) {
                    msg.add("Đã đặt ca làm này cho những ngày này");
                }
            }
        }
        if (workTable.getDayOfWeek().equals("") || workTable.getDayOfWeek().isEmpty()) {
            msg.add("Chưa chọn ngày làm trong tuần cho nhân viên");
        }
        if (workTable.getEmployee().getId() == null) {
            msg.add("Chưa chọn nhân viên");
        }
        if (workTable.getWorkShift().getId() == null) {
            msg.add("Chưa chọn ca làm");
        }

        return msg;
    }

    public static List<String> validateUpdate(WorkTable workTable) {
        List<String> msg = new ArrayList<>();

        if (workTable.getDayOfWeek().equals("") || workTable.getDayOfWeek().isEmpty()) {
            msg.add("Chưa chọn ngày làm trong tuần cho nhân viên");
        }
        List<WorkTable> workTableEmployeeList = WorkTableRepository.getByEmployeeNameAndShift(workTable.getEmployee().getFullName(), workTable.getWorkShift());
        WorkTable workTableEmployee = workTableEmployeeList.stream().filter(t -> !t.getId().equals(workTable.getId())).findAny().orElse(null);
        if (workTableEmployee != null
                && !workTableEmployee.getEmployee().getId().equals(workTable.getEmployee().getId())) {
            msg.add("Đã đặt ca làm cho nhân viên này");
        }

        if (workTableEmployee != null) {
            List<String> workDaysOfWeek = Arrays.asList(workTable.getDayOfWeek().split("-"));
            List<String> otherWorkDaysOfWeek = Arrays.asList(workTableEmployee.getDayOfWeek().split("-"));
            for (String item : workDaysOfWeek) {
                if (otherWorkDaysOfWeek.stream().anyMatch(t -> t.equals(item))) {
                    msg.add("Đã đặt ca làm này cho những ngày này");
                }
            }
        }

        if (workTable.getEmployee().getId() == null) {
            msg.add("Chưa chọn nhân viên");
        }
        if (workTable.getWorkShift().getId() == null) {
            msg.add("Chưa chọn ca làm");
        }

        return msg;
    }
}
