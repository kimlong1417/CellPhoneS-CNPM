package controllers;

import entities.Merchandise;
import holders.MerchandiseHolder;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.MerchandiseRepository;
import utils.HibernateUtils;
import utils.StageHelper;
import utils.TableHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MerchandiseCategoryController implements Initializable {
    @FXML
    private AnchorPane host;
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Merchandise> contentTable;
    @FXML
    private TableColumn<Merchandise, String> nameCol;
    @FXML
    private TableColumn<Merchandise, String> typeCol;
    @FXML
    private TableColumn<Merchandise, Integer> quantityCol;
    @FXML
    private TableColumn<Merchandise, String> priceCol;

    // For other class call function from this class
    public static MerchandiseCategoryController instance;

    public MerchandiseCategoryController() {
        instance = this;
    }

    public static MerchandiseCategoryController getInstance() {
        return instance;
    }
    ///

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Merchandise> merchandiseList = MerchandiseRepository.getAll();
        TableHelper.setMerchandiseTable(merchandiseList, contentTable, nameCol, typeCol, quantityCol, priceCol);
    }

    @FXML
    void insert(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MerchandiseAdd.fxml")));
        StageHelper.startStage(root);
        // Hide host
        MainNavigatorController.instance.getHost().setDisable(true);
    }

    @FXML
    void select(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            Merchandise merchandise = contentTable.getSelectionModel().getSelectedItem();
            contentTable.getSelectionModel().clearSelection();
            // Store Merchandise to use in another class
            if (merchandise != null) {
                MerchandiseHolder.getInstance().setMerchandise(merchandise);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MerchandiseUpdate.fxml")));
                StageHelper.startStage(root);
                // Hide host
                MainNavigatorController.instance.getHost().setDisable(true);
            }
        }
    }

    @FXML
    void search(ActionEvent event) {
        List<Merchandise> merchandiseList = MerchandiseRepository.getLikeNameAndBranch(searchBar.getText());
        TableHelper.setMerchandiseTable(merchandiseList, contentTable, nameCol, typeCol, quantityCol, priceCol);
    }

    // Refresh table
    public void refresh() {
        this.initialize(null, null);
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
