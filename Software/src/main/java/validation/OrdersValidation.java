package validation;

import entities.Imports;
import entities.Orders;
import entities.Receipt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.ImportsRepository;
import repositories.ReceiptRepository;

import java.util.ArrayList;
import java.util.List;

public class OrdersValidation {

    public static List<String> validateUpdate(Orders orders) {
        List<String> msg = new ArrayList<>();

        if (orders.getStatus().equals("Hoàn tất")) {
            msg.add("Không được cập nhật đơn hàng đã hoàn tất");
        }

        return msg;
    }

    public static List<String> validateDelete(Orders orders) {
        List<String> msg = new ArrayList<>();

        List<Receipt> receiptList = ReceiptRepository.getAll();
        List<Imports> importsList = ImportsRepository.getAll();

        if (orders.getStatus().equals("Hoàn tất")) {
            msg.add("Không được xoá đơn hàng đã hoàn tất");
        }
        if (receiptList != null) {
            if (receiptList.stream().anyMatch(t -> t.getOrders().getId().equals(orders.getId()))) {
                msg.add("Không được xoá đơn hàng có hoá đơn");
            }
        }
        if (importsList != null) {
            if (importsList.stream().anyMatch(t -> t.getOrders().getId().equals(orders.getId()))) {
                msg.add("Không được xoá đơn hàng có phiếu nhập hàng");
            }
        }

        return msg;
    }
}
