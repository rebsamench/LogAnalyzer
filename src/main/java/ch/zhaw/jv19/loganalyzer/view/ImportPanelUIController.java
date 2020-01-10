package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import ch.zhaw.jv19.loganalyzer.model.FileWrapper;
import javafx.beans.binding.Bindings;
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

/**
 * Handles user interactions on the import panel.
 * Collects the chosen data and hands it over to the FileImportController as a list of files.
 *
 * @autor: Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
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
        if (appDataController.isDatabaseAccessible().get() == true) {
            chooseCreatedUser.getItems().addAll(appDataController.getUserList());
            chooseSite.getItems().addAll(appDataController.getSiteList());
            chooseBusline.getItems().addAll(appDataController.getBuslineList());
            selectLogFiles.disableProperty().bind((chooseSite.valueProperty().isNull())
                    .or(chooseBusline.valueProperty().isNull())
                    .or(chooseCreatedUser.valueProperty().isNull()));
            importData.disableProperty().bind((chooseSite.valueProperty().isNull())
                    .or(chooseBusline.valueProperty().isNull())
                    .or(chooseCreatedUser.valueProperty().isNull())
                    .or(Bindings.size(selectedFiles).isEqualTo(0)));
        }
    }

    /**
     * Makes a file chooser available and collects the chosen files in a list.
     */
    @FXML
    public void handleSelectedLogFiles() {
        // Set extension filter
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show open file dialog to select multiple files.
        fileList = fileChooser.showOpenMultipleDialog(null);
        // check for null in case user cancels
        if(fileList != null) {
            showSelectedFiles(fileList);
        }
    }

    /**
     * Wraps the files in the file list in a FileWrapper and hand it over to the table view.
     * The wrapper is necessary for the table view.
     *
     * @param fileList: list of chosen files for import.
     */
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

    /**
     * Bundles all the selected data such as user, site, busline and the selected files and hands it over to
     * the the FileImportController.
     */
    @FXML
    public void handleImportData() {
        User user = chooseCreatedUser.getSelectionModel().getSelectedItem();
        Site site = chooseSite.getSelectionModel().getSelectedItem();
        Busline busline = chooseBusline.getSelectionModel().getSelectedItem();
        new FileImportController(user, site, busline, fileList);
        appDataController.setMessage("Data Import completed");
        showSelectedFiles.getItems().clear();
        chooseCreatedUser.getItems().clear();
        chooseBusline.getItems().clear();
        chooseSite.getItems().clear();
    }

    /**
     * Creates a table view of the selected files.
     *
     * @param selectedFiles : observable list of the selected files.
     */
    public void buildSelectedFilesTable(ObservableList selectedFiles) {
        TableColumn<FileWrapper, String> fileColumn = new TableColumn<>("selected files");
        fileColumn.setCellValueFactory(new PropertyValueFactory("name"));
        showSelectedFiles.getColumns().add(fileColumn);
        showSelectedFiles.setItems(selectedFiles);
    }
}
