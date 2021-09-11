package validation;

import entities.Merchandise;
import org.hibernate.Session;
import repositories.MerchandiseRepository;
import utils.NumberHelper;

import java.util.ArrayList;
import java.util.List;

public class MerchandiseValidation {

    public static List<String> validateInsert(Merchandise merchandise) {
        List<String> msg = new ArrayList<>();

        Merchandise merchandiseName = MerchandiseRepository.getByName(merchandise.getName());

        if (merchandise.getName() == null || merchandise.getName().isEmpty()) {
            msg.add("Chưa điền tên");
        } else if (merchandiseName != null) {
            msg.add("Tên đã được sử dụng");
        }
        if (merchandise.getBranch() == null || merchandise.getBranch().isEmpty()) {
            msg.add("Chưa điền thương hiệu");
        }
        if (merchandise.getType() == null || merchandise.getType().isEmpty()) {
            msg.add("Chưa điền loại");
        }
        if (merchandise.getImportPrice() == null || merchandise.getImportPrice().isEmpty()) {
            msg.add("Chưa điền giá mua");
        } else if (!NumberHelper.isNumber(merchandise.getImportPrice())) {
            msg.add("Giá mua phải là số");
        }
        if (merchandise.getPrice() == null || merchandise.getPrice().isEmpty()) {
            msg.add("Chưa điền giá bán");
        } else if (!NumberHelper.isNumber(merchandise.getPrice())) {
            msg.add("Giá bán phải là số");
        }

        return msg;
    }

    public static List<String> validateUpdate(Merchandise merchandise) {
        List<String> msg = new ArrayList<>();

        Merchandise merchandiseName = MerchandiseRepository.getByName(merchandise.getName());

        // Check name
        if (merchandise.getName() == null || merchandise.getName().isEmpty()) {
            msg.add("Chưa điền tên");
        } else if (merchandiseName != null && !merchandiseName.getId().equals(merchandise.getId())) {
            msg.add("Tên đã được sử dụng");
        }
        if (merchandise.getBranch() == null || merchandise.getBranch().isEmpty()) {
            msg.add("Chưa điền thương hiệu");
        }
        if (merchandise.getType() == null || merchandise.getType().isEmpty()) {
            msg.add("Chưa điền loại");
        }
        if (merchandise.getImportPrice() == null || merchandise.getImportPrice().isEmpty()) {
            msg.add("Chưa điền giá mua");
        } else if (!NumberHelper.isNumber(merchandise.getImportPrice())) {
            msg.add("Giá mua phải là số");
        }
        if (merchandise.getPrice() == null || merchandise.getPrice().isEmpty()) {
            msg.add("Chưa điền giá bán");
        } else if (!NumberHelper.isNumber(merchandise.getPrice())) {
            msg.add("Giá bán phải là số");
        }

        return msg;
    }
}
