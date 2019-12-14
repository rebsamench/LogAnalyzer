package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.MainApp;
import ch.zhaw.jv19.loganalyzer.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ImportPanelUIController implements Initializable, UIPanelController {

    private AppDataController appDataController;
    private MainApp mainApp;
    private List<File> fileList;

    // TODO
    // Will be collected by the import form
    private User user;
    private Site site;
    private Busline busline;

    @FXML
    private CheckComboBox<User> chooseCreatedUser;

    @FXML
    private CheckComboBox<Site> chooseSite;

    @FXML
    private CheckComboBox<Busline> chooseBusline;

    public ImportPanelUIController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseCreatedUser.addEventHandler(ComboBox.ON_SHOWING, event -> {
            fillUserList();
        });
        chooseCreatedUser.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user.getName();
            }

            @Override
            public User fromString(String string) {
                return chooseCreatedUser.getItems().stream().filter(user ->
                        user.getName().equals(string)).findFirst().orElse(null);
            }
        });

        chooseSite.addEventHandler(ComboBox.ON_SHOWING, event -> {
            fillSiteList();
        });
        chooseSite.setConverter(new StringConverter<Site>() {
            @Override
            public String toString(Site site) {
                return site.getName();
            }

            @Override
            public Site fromString(String string) {
                return chooseSite.getItems().stream().filter(site ->
                        site.getName().equals(string)).findFirst().orElse(null);
            }
        });

        chooseBusline.addEventHandler(ComboBox.ON_SHOWING, event -> {
            fillBuslineList();
        });
        chooseBusline.setConverter(new StringConverter<Busline>() {
            @Override
            public String toString(Busline busline) {
                return busline.getName();
            }

            @Override
            public Busline fromString(String string) {
                return chooseBusline.getItems().stream().filter(busline ->
                        busline.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    @Override
    public void setAppDataController(AppDataController appDataController) {
        this.appDataController = appDataController;
    }

    @FXML
    public void handleImportSelectedFiles(ActionEvent event) {

        // Set extension filter
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog to select multiple files.
        fileList = fileChooser.showOpenMultipleDialog(null);
        new FileImportController(user, site, busline, fileList);
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
        if (chooseCreatedUser.getItems().size() == 0) {
            chooseCreatedUser.getItems().addAll(appDataController.getUserList());
        }
    }
}
