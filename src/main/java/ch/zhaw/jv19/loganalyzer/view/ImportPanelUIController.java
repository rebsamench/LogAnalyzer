package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;


import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ImportPanelUIController implements Initializable, UIPanelController {
    private AppDataController appDataController;
    private List<File> fileList;

    @FXML
    private ComboBox<User> chooseCreatedUser;

    @FXML
    private ComboBox<Site> chooseSite;

    @FXML
    private ComboBox<Busline> chooseBusline;

    @FXML
    private Button selectLogFiles;

    @FXML
    private Button importData;

    @FXML
    private TableView showFileList;

    public ImportPanelUIController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        chooseCreatedUser.getItems().addAll(appDataController.getUserList());
        chooseSite.getItems().addAll(appDataController.getSiteList());
        chooseBusline.getItems().addAll(appDataController.getBuslineList());
    }

    @FXML
    public void handleSelectedLogFiles() {
        // Set extension filter
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show open file dialog to select multiple files.
        fileList = fileChooser.showOpenMultipleDialog(null);
    }

    @FXML
    public void handleImportData() {
        User user = chooseCreatedUser.getSelectionModel().getSelectedItem();
        Site site = chooseSite.getSelectionModel().getSelectedItem();
        Busline busline = chooseBusline.getSelectionModel().getSelectedItem();
        if(user == null) {appDataController.setMessage("Select Created User!");}
        else if(site == null) {appDataController.setMessage("Select site!");}
        else if(busline == null){appDataController.setMessage("Select Busline!");}
        else if(fileList == null){appDataController.setMessage("No files selected!");}
        else {new FileImportController(user, site, busline, fileList);}
        appDataController.setMessage("Data Import completed");
    }
}
