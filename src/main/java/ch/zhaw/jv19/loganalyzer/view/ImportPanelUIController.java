package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.MainApp;
import ch.zhaw.jv19.loganalyzer.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ImportPanelUIController implements UIPanelController {

    private AppDataController appDataController;
    private MainApp mainApp;
    private AnchorPane dialog;

    @FXML
    private boolean openImportDialog() {
        try {
            // Load the fxml file and create a new pane for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ch/zhaw/jv19/loganalyzer/view/ImportDialog.fxml"));
            dialog = loader.load();

            // Create the dialog Stage.
            Scene scene = new Scene(dialog);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Import Details");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setScene(scene);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setAppDataController(AppDataController appDataController) {
        this.appDataController = appDataController;
    }
}
