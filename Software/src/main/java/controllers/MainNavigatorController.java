package controllers;

import com.jfoenix.controls.JFXButton;
import entities.Employee;
import entities.Permissions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import repositories.PermissionRepository;
import utils.StageHelper;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainNavigatorController implements Initializable {

    static List<AnchorPane> grid = new ArrayList<>();

    @FXML
    private AnchorPane Host;
    @FXML
    private JFXButton orderButton;
    @FXML
    private JFXButton customerButton;
    @FXML
    private JFXButton receiptButton;
    @FXML
    private JFXButton employeeButton;
    @FXML
    private JFXButton merchandiseButton;
    @FXML
    private JFXButton importsButton;
    @FXML
    private JFXButton statisticButton;
    @FXML
    private JFXButton rolesButton;
    @FXML
    private JFXButton timeTableButton;
    @FXML
    private ImageView close;
    @FXML
    private ImageView minimize;
    @FXML
    private AnchorPane contentPanel;
    @FXML
    private VBox leftNav;

    //  For other class call function from this class
    public static MainNavigatorController instance;
    public MainNavigatorController() { instance = this; }
    public static MainNavigatorController getInstance() { return instance; }
    public AnchorPane getHost() { return this.Host; }
    //////////////////

    @FXML
    void close(MouseEvent mouseEvent) {
        StageHelper.closeStage(mouseEvent);
    }

    @FXML
    void minimizeWindow(MouseEvent event) {
        StageHelper.minimizeStage(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/OrderCategory.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/CustomerCategory.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ReceiptCategory.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/EmployeeCategory.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MerchandiseCategory.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Statistic.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RolesCategory.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ImportsCategory.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkTableCategory.fxml"))));
            grid.add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkShiftCategory.fxml"))));
            this.hideInaccessibleFeatures();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    private void hideInaccessibleFeatures() {
        Employee currentEmployee = new Employee(LoginController.getInstance().curEmployee);
        List<String> inAccessiblePermission = PermissionRepository.getEmployeeInaccessiblePermission(currentEmployee.getId());
        if (inAccessiblePermission.size() > 0) {
            for (String item : inAccessiblePermission) {
                switch (item) {
                    case "Qu???n l?? kh??ch h??ng" -> leftNav.getChildren().remove(customerButton);
                    case "Qu???n l?? nh??n vi??n" -> leftNav.getChildren().remove(employeeButton);
                    case "Qu???n l?? nh???p h??ng" -> leftNav.getChildren().remove(importsButton);
                    case "Qu???n l?? h??ng ho??" -> leftNav.getChildren().remove(merchandiseButton);
                    case "Qu???n l?? ????n h??ng" -> leftNav.getChildren().remove(orderButton);
                    case "Qu???n l?? ho?? ????n" -> leftNav.getChildren().remove(receiptButton);
                    case "Qu???n l?? ch???c v???" -> leftNav.getChildren().remove(rolesButton);
                    case "Th???ng k??" -> leftNav.getChildren().remove(statisticButton);
                    case "Qu???n l?? l???ch l??m" -> leftNav.getChildren().remove(timeTableButton);
                }
            }
        }
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        LoginController.getInstance().curEmployee = null;
        StageHelper.closeStage(event);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Login.fxml")));
        StageHelper.startStage(root);
    }


    @FXML
    void openOrderCategory(ActionEvent actionEvent) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(0));
    }

    @FXML
    void openCustomerCategory(ActionEvent event) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(1));
    }

    @FXML
    void openReceiptCategory(ActionEvent event) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(2));
    }

    @FXML
    void openEmployeeCategory(ActionEvent event) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(3));
    }

    @FXML
    void openMerchandiseCategory(ActionEvent event) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(4));
    }

    @FXML
    void openStatistic(ActionEvent event) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(5));
    }

    @FXML
    void openRolesCategory(ActionEvent event) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(6));
    }

    @FXML
    void openImportsCategory(ActionEvent event) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(7));
    }

    @FXML
    void openTimeTable(ActionEvent event) {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(8));
    }

    void openShiftTable() {
        contentPanel.getChildren().clear();
        contentPanel.getChildren().add(grid.get(9));
    }
}
