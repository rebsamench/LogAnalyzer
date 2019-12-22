package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

    public ImportPanelUIController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseCreatedUser.addEventHandler(ComboBox.ON_SHOWING, event -> fillUserList());
        chooseSite.addEventHandler(ComboBox.ON_SHOWING, event -> fillSiteList());
        chooseBusline.addEventHandler(ComboBox.ON_SHOWING, event -> fillBuslineList());
    }

    @Override
    public void setAppDataController(AppDataController appDataController) {
        this.appDataController = appDataController;
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
    }

    private void fillUserList() {
        if (chooseCreatedUser.getItems().size() == 0) {
            chooseCreatedUser.getItems().addAll(appDataController.getUserList());
        }
    }

    private void fillSiteList() {
        if (chooseSite.getItems().size() == 0) {
            chooseSite.getItems().addAll(appDataController.getSiteList());
        }
    }

    private void fillBuslineList() {
        if (chooseBusline.getItems().size() == 0) {
            chooseBusline.getItems().addAll(appDataController.getBuslineList());
        }
    }
}
