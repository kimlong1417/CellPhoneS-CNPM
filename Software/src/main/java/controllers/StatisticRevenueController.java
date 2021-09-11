package controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import repositories.OrdersDetailRepository;

import java.net.URL;
import java.time.ZoneId;
import java.util.*;

public class StatisticRevenueController implements Initializable {

    @FXML
    private DatePicker fromDate;

    @FXML
    private DatePicker toDate;

    @FXML
    private LineChart<String, Number> lineChart;

    // For other class to call function from this class
    public static StatisticRevenueController instance;
    public StatisticRevenueController() { instance = this; }
    public static StatisticRevenueController getInstance() { return instance; }
    ///

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loadData(null);
    }

    @FXML
    void loadData(ActionEvent event) {
        lineChart.getData().clear();
        Date fDate = fromDate.getValue() != null ? Date.from(fromDate.getValue().atStartOfDay(ZoneId.of("Etc/GMT+8")).toInstant()) : null;
        Date tDate = toDate.getValue() != null ? Date.from(toDate.getValue().atStartOfDay(ZoneId.of("Etc/GMT+8")).toInstant()) : null;
        List<Object> allBuyingAmount = OrdersDetailRepository.getAmountBuying(fDate, tDate);
        // Load data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 1; i <= 12; i++) {
            Number sumAmount = 0;
            for (Object item : allBuyingAmount) {
                Object[] fields = (Object[]) item;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime((Date) fields[2]);
                if ((calendar.get(Calendar.MONTH) + 1) == i) {
                    sumAmount = sumAmount.longValue() + ((Number) fields[1]).longValue();
                }
            }
            series.getData().add(new XYChart.Data<>("T" + i, sumAmount));
        }
        lineChart.getData().add(series);
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
