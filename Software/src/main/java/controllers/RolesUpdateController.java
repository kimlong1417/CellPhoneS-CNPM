package controllers;

import com.jfoenix.controls.JFXListView;
import entities.Permissions;
import entities.Roles;
import entities.RolesDetail;
import holders.RolesHolder;
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
import repositories.PermissionRepository;
import repositories.RolesDetailRepository;
import utils.*;
import validation.RolesValidation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RolesUpdateController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField nameHolder;
    @FXML
    private Label errorMessage;
    @FXML
    private JFXListView<String> selectedPermissionList;
    @FXML
    private TableView<Permissions> permissionTable;
    @FXML
    private TableColumn<Permissions, String> nameCol;

    private List<String> selectedPermission = new ArrayList<>();

    public static RolesUpdateController instance;
    public RolesUpdateController() { instance = this; }

    // Get Roles from RolesCategoryController select(MouseEvent event)
    Roles roles = RolesHolder.getInstance().getRoles();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add value to permissionTable
        List<Permissions> permissionsList = PermissionRepository.getAll();
        TableHelper.setPermissionNameTable(permissionsList, permissionTable, nameCol);
        // Add value to selectedPermissionList
        List<RolesDetail> rolesDetailList = RolesDetailRepository.getByRolesId(roles.getId());
        for (RolesDetail item : rolesDetailList) {
            selectedPermission.add(permissionsList.stream().filter(t -> t.getCode().equals(item.getPermissions().getCode())).findFirst().get().getName());
        }
        populateSelectedPermissionList();
        // Set Roles name
        nameHolder.setText(roles.getName());
    }

    @FXML
    void close(MouseEvent event) {
        // clear rolesHolder
        RolesHolder.getInstance().setRoles(null);
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void delete(ActionEvent event) {
        Session session;
        List<String> validateDelete = RolesValidation.validateDelete(roles);
        if (validateDelete.size() == 0) {
            // Delete RolesDetail
            RolesDetailRepository.deleteByRoleId(roles.getId());
            // Delete Roles
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(roles);
            session.getTransaction().commit();
            session.close();

            // Refresh content table
            RolesCategoryController.getInstance().refresh();
            // clear rolesHolder
            RolesHolder.getInstance().setRoles(null);
            // Close stage
            StageHelper.closeStage(event);
            // Show alert box
            AlertBoxHelper.showMessageBox("Xoá thành công");
            // Unhide host
            MainNavigatorController.instance.getHost().setDisable(false);
        } else {
            errorMessage.setText(validateDelete.get(0));
        }
    }

    @FXML
    void removeSelected(MouseEvent event) {
        if (event.getClickCount() == 2) {
            selectedPermission.remove(selectedPermissionList.getSelectionModel().getSelectedItem());
            populateSelectedPermissionList();
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
    void update(ActionEvent event) {
        Session session;
        Roles roles = RolesHolder.getInstance().getRoles();
        roles.setName(nameHolder.getText());
        List<String> validateUpdate = RolesValidation.validateUpdate(roles);
        if (validateUpdate.size() == 0) {
            // Update Roles Info
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(roles);
            session.getTransaction().commit();
            session.close();
            // Update Roles Detail
            RolesDetailRepository.deleteByRoleId(roles.getId());
            for (String item : selectedPermission){
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
            AlertBoxHelper.showMessageBox("Cập nhật thành công");
            // Refresh content table
            RolesCategoryController.getInstance().refresh();
            // Set roles holder
            RolesHolder.getInstance().setRoles(roles);
            // Unhide host
            MainNavigatorController.instance.getHost().setDisable(false);
        } else {
            errorMessage.setText(validateUpdate.get(0));
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
