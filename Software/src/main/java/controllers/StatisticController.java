package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.*;

public class StatisticController implements Initializable {
    @FXML
    private AnchorPane host;
    @FXML
    private AnchorPane statisticHolder;
    @FXML
    private ComboBox<String> statisticChooser;

    List<String> allStatistic;
    List<AnchorPane> grid = new ArrayList<>();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allStatistic = Arrays.asList("Khách hàng chi nhiều nhất", "Hàng hoá bán chạy nhất", "Doanh thu");
        statisticChooser.getItems().addAll(allStatistic);

        grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/StatisticCustomer.fxml"))));
        grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/StatisticMerchandise.fxml"))));
        grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/StatisticRevenue.fxml"))));
    }

    @FXML
    void chooseStatistic(ActionEvent event) {
        switch (statisticChooser.getValue()) {
            case "Khách hàng chi nhiều nhất":
                statisticHolder.getChildren().clear();
                statisticHolder.getChildren().add(grid.get(0));
                break;
            case "Hàng hoá bán chạy nhất":
                statisticHolder.getChildren().clear();
                statisticHolder.getChildren().add(grid.get(1));
                break;
            case "Doanh thu":
                statisticHolder.getChildren().clear();
                statisticHolder.getChildren().add(grid.get(2));
                break;
        }
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
