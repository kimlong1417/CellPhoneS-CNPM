package controllers;

import com.jfoenix.controls.JFXListView;
import entities.Permissions;
import entities.Roles;
import entities.RolesDetail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import repositories.PermissionRepository;
import utils.*;
import validation.RolesValidation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RolesAddController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField nameHolder;
    @FXML
    private Label errorMessage;
    @FXML
    private TableView<Permissions> permissionTable;
    @FXML
    private JFXListView<String> selectedPermissionList;
    @FXML
    private TableColumn<Permissions, String> nameCol;

    public static RolesAddController instance;
    public RolesAddController() { instance = this; }

    private List<String> selectedPermission = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Permissions> permissionsList = PermissionRepository.getAll();
        TableHelper.setPermissionNameTable(permissionsList, permissionTable, nameCol);
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void save(ActionEvent event) {
        Session session;
        Roles roles = new Roles();
        roles.setId(UUIDHelper.generateType4UUID().toString());
        roles.setName(nameHolder.getText());

        List<String> validateInsert = RolesValidation.validateInsert(roles);
        if (validateInsert.size() == 0) {
            // Save new roles
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(roles);
            session.getTransaction().commit();
            session.close();
            // Save roles_detail
            for (String item : selectedPermission) {
                Permissions permissions = PermissionRepository.getByName(item);
                RolesDetail rolesDetail = new RolesDetail();
                rolesDetail.setId(UUIDHelper.generateType4UUID().toString());
                rolesDetail.setRoles(roles);
                rolesDetail.setPermissions(permissions);

                session = HibernateUtils.getSessionFactory().openSession();
                session.beginTransaction();
                session.save(rolesDetail);
                session.getTransaction().commit();
                session.close();
            }

            // Close stage
            StageHelper.closeStage(event);
            // Show alert box
            AlertBoxHelper.showMessageBox("Thêm thành công");
            // Refresh content table
            RolesCategoryController.getInstance().refresh();
            // Unhide host
            MainNavigatorController.instance.getHost().setDisable(false);
        } else {
            errorMessage.setText(validateInsert.get(0));
        }
    }

    @FXML
    void select(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Permissions permissions = permissionTable.getSelectionModel().getSelectedItem();
            permissionTable.getSelectionModel().clearSelection();

            selectedPermission.add(permissions.getName());
            // Remove duplicate items
            selectedPermission = selectedPermission.stream().distinct().collect(Collectors.toList());
            populateSelectedPermissionList();
        }
    }

    @FXML
    void removeSelected(MouseEvent event) {
        if (event.getClickCount() == 2) {
            selectedPermission.remove(selectedPermissionList.getSelectionModel().getSelectedItem());
            populateSelectedPermissionList();
        }
    }

    void populateSelectedPermissionList() {
        selectedPermissionList.getItems().clear();
        for (String item : selectedPermission) {
            selectedPermissionList.getItems().add(item);
        }
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
