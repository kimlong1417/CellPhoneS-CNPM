package controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import repositories.MerchandiseRepository;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticMerchandiseController implements Initializable {
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private BarChart<String, Number> merchandiseChart;

    // For other class to call function from this class
    public static StatisticMerchandiseController instance;
    public StatisticMerchandiseController() { instance = this; }
    public static StatisticMerchandiseController getInstance() { return instance; }
    ///

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loadData(null);
    }

    @FXML
    void loadData(ActionEvent event) {
        merchandiseChart.getData().clear();
        merchandiseChart.getXAxis().setTickLabelRotation(360);
        Date fDate = fromDate.getValue() != null ? Date.from(fromDate.getValue().atStartOfDay(ZoneId.of("Etc/GMT+8")).toInstant()) : null;
        Date tDate = toDate.getValue() != null ? Date.from(toDate.getValue().atStartOfDay(ZoneId.of("Etc/GMT+8")).toInstant()) : null;
        List<Object> allBuyingAmount = MerchandiseRepository.getAmountBuying(fDate, tDate);
        // Load data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Object item : allBuyingAmount) {
            Object[] objects = (Object[]) item;
            series.getData().add(new XYChart.Data<>((String) objects[0], (Number) objects[2]));
        }
        merchandiseChart.getData().add(series);

        // Set bar width
        merchandiseChart.categoryGapProperty().bind(Bindings.createDoubleBinding(() ->
                        merchandiseChart.getXAxis().getWidth() / (4 * allBuyingAmount.size()),
                Bindings.size(merchandiseChart.getData()), merchandiseChart.getXAxis().widthProperty()));
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
