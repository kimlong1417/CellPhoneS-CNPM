package controllers;

import entities.Roles;
import holders.RolesHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import repositories.RolesRepository;
import utils.StageHelper;
import utils.TableHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class RolesCategoryController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Roles> contentTable;
    @FXML
    private TableColumn<Roles, String> nameCol;
    @FXML
    private TableColumn<Roles, Date> createdDateCol;

    // For other class call function from this class
    public static RolesCategoryController instance;

    public RolesCategoryController() {
        instance = this;
    }

    public static RolesCategoryController getInstance() {
        return instance;
    }
    ///

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Roles> rolesList = RolesRepository.getAll();
        TableHelper.setRolesTable(rolesList, contentTable, nameCol, createdDateCol);
    }

    @FXML
    void insert(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RolesAdd.fxml")));
        StageHelper.startStage(root);
        // Hide host
        MainNavigatorController.instance.getHost().setDisable(true);
    }

    @FXML
    void search(ActionEvent event) {
        List<Roles> rolesList = RolesRepository.getLikeName(searchBar.getText());
        TableHelper.setRolesTable(rolesList, contentTable, nameCol, createdDateCol);
    }

    @FXML
    void select(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            Roles roles = contentTable.getSelectionModel().getSelectedItem();
            contentTable.getSelectionModel().clearSelection();
            // Store Roles to use in another class
            if (roles != null) {
                RolesHolder.getInstance().setRoles(roles);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RolesUpdate.fxml")));
                StageHelper.startStage(root);
                // Hide host
                MainNavigatorController.instance.getHost().setDisable(true);
            }
        }
    }

    // Refresh table
    public void refresh() {
        List<Roles> rolesList = RolesRepository.getAll();
        TableHelper.setRolesTable(rolesList, contentTable, nameCol, createdDateCol);
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
