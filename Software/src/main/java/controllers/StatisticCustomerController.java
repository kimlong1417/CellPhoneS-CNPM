package controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import repositories.OrdersDetailRepository;

import java.net.URL;
import java.time.ZoneId;
import java.util.*;

public class StatisticCustomerController implements Initializable {
    @FXML
    private BarChart<String, Number> customerChart;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;

    // For other class to call function from this class
    public static StatisticCustomerController instance;
    public StatisticCustomerController() { instance = this; }
    public static StatisticCustomerController getInstance() { return instance; }
    ///

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loadData(null);
    }

    @FXML
    void loadData(ActionEvent event) {
        customerChart.getData().clear();
        Date fDate = fromDate.getValue() != null ? Date.from(fromDate.getValue().atStartOfDay(ZoneId.of("Etc/GMT+8")).toInstant()) : null;
        Date tDate = toDate.getValue() != null ? Date.from(toDate.getValue().atStartOfDay(ZoneId.of("Etc/GMT+8")).toInstant()) : null;
        List<Object> allBuyingAmount = OrdersDetailRepository.getAmountBuying(fDate, tDate);
        // Load data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Object item : allBuyingAmount) {
            Object[] fields = (Object[]) item;
            series.getData().add(new XYChart.Data<>((String) fields[0], (Number) fields[1]));
        }
        customerChart.getData().add(series);

        // Set bar width
        customerChart.categoryGapProperty().bind(Bindings.createDoubleBinding(() ->
                        customerChart.getXAxis().getWidth() / (4 * allBuyingAmount.size()),
                        Bindings.size(customerChart.getData()), customerChart.getXAxis().widthProperty()));
    }

    @FXML
    void refresh(ActionEvent event) {
        // Clear date holder
        fromDate.setValue(null);
        fromDate.getEditor().clear();
        toDate.setValue(null);
        toDate.getEditor().clear();
        this.loadData(null);
    }
}
