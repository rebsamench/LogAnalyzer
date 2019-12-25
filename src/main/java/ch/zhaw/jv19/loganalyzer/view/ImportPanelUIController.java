package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import ch.zhaw.jv19.loganalyzer.util.FileWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;


import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
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
    private TableView<FileWrapper> showSelectedFiles;
    private ObservableList<Files> selectedFiles = FXCollections.observableArrayList();

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
        showSelectedFiles(fileList);
    }

    @FXML
    public void showSelectedFiles(List<File> fileList) {
        ArrayList listToArrayList = new ArrayList();
        for (File file : fileList) {
            listToArrayList.add(new FileWrapper(file));
        }
        selectedFiles.clear();
        selectedFiles.addAll(listToArrayList);
        buildSelectedFilesTable(selectedFiles);
    }

    public void buildSelectedFilesTable (ObservableList selectedFiles) {
        TableColumn <FileWrapper, String> fileColumn  = new TableColumn<>("selected files");
        fileColumn.setCellValueFactory(new PropertyValueFactory("name"));
        showSelectedFiles.getColumns().add(fileColumn);
        showSelectedFiles.setItems(selectedFiles);
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
