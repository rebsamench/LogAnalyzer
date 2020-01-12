package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Handles user interactions on the import panel.
 * Collects the chosen data and hands it over to the FileImportController as a list of files.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class ImportPanelUIController implements Initializable, UIPanelController {
    private AppDataController appDataController;
    private List<File> fileList;

    @FXML
    private ComboBox<User> chooseCreatedUser;

    @FXML
    private ComboBox<Site> chooseSite;

    @FXML
    private ComboBox<BusLine> chooseBusLine;

    @FXML
    private Button selectLogFiles;

    @FXML
    private Button importData;

    @FXML
    private TableView<FileWrapper> selectedFilesTable;
    private final ObservableList<FileWrapper> selectedFilesList = FXCollections.observableArrayList();

    public ImportPanelUIController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        if (appDataController.isDatabaseAccessible().get()) {
            chooseCreatedUser.getItems().addAll(appDataController.getUserList());
            chooseSite.getItems().addAll(appDataController.getSiteList());
            chooseBusLine.getItems().addAll(appDataController.getBusLineList());
            selectLogFiles.disableProperty().bind((chooseSite.valueProperty().isNull())
                    .or(chooseBusLine.valueProperty().isNull())
                    .or(chooseCreatedUser.valueProperty().isNull()));
            importData.disableProperty().bind((chooseSite.valueProperty().isNull())
                    .or(chooseBusLine.valueProperty().isNull())
                    .or(chooseCreatedUser.valueProperty().isNull())
                    .or(Bindings.size(selectedFilesList).isEqualTo(0)));
        }
    }

    /**
     * Makes a file chooser available and collects the chosen files in a list.
     */
    @FXML
    public void handleSelectedLogFiles() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileList = fileChooser.showOpenMultipleDialog(null);
        if(fileList != null) {
            showSelectedFiles(fileList);
        }
    }

    /**
     * Wraps the files in the file list in a FileWrapper and hands it over to the table view.
     * The wrapper is necessary for the table view.
     *
     * @param fileList: list of chosen files for import.
     */
    @FXML
    private void showSelectedFiles(List<File> fileList) {
        ArrayList<FileWrapper> listToArrayList = new ArrayList();
        for (File file : fileList)
            listToArrayList.add(new FileWrapper(file));
        selectedFilesList.clear();
        selectedFilesList.addAll(listToArrayList);
        buildSelectedFilesTable(selectedFilesList);
    }

    /**
     * Bundles all the selected data such as user, site, busLine and the selected files and hands it over to
     * the the FileImportController.
     */
    @FXML
    private void handleImportData() {
        User user = chooseCreatedUser.getSelectionModel().getSelectedItem();
        Site site = chooseSite.getSelectionModel().getSelectedItem();
        BusLine busLine = chooseBusLine.getSelectionModel().getSelectedItem();
        try {
            new FileImportController(user, site, busLine, fileList);
        } catch (Exception e) {
            e.printStackTrace();
            appDataController.setMessage("SQL Error: " + e.getMessage());
        }
        appDataController.setMessage("Data Import completed");
        selectedFilesTable.getItems().clear();
        selectedFilesTable.getColumns().clear();
        chooseCreatedUser.getSelectionModel().clearSelection();
        chooseBusLine.getSelectionModel().clearSelection();
        chooseSite.getSelectionModel().clearSelection();
    }

    /**
     * Creates a table view of the selected files.
     *
     * @param selectedFilesList observable list of the selected files.
     */
    private void buildSelectedFilesTable(ObservableList<FileWrapper> selectedFilesList) {
        TableColumn<FileWrapper, String> fileColumn = new TableColumn<>("selected files");
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        selectedFilesTable.getColumns().add(fileColumn);
        selectedFilesTable.setItems(selectedFilesList);
    }
}
