package utils;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;

import java.util.Objects;

public class AlertBoxHelper {

    public static void showMessageBox(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message);
        alert.getDialogPane().getStylesheets().add("css/style.css");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.initStyle(StageStyle.TRANSPARENT);

        // Style alert box
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(AlertBoxHelper.class.getClassLoader().getResource("css/style.css")).toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        // Add button and button style
        ButtonType buttonType = new ButtonType("Đóng");
        alert.getButtonTypes().setAll(buttonType);
        Node discardButton = alert.getDialogPane().lookupButton(buttonType);
        discardButton.getStyleClass().addAll( "dialog-close-button", "top-font");

        // Show alert box
        alert.show();
    }
}
