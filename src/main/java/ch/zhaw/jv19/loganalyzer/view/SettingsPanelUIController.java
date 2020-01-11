package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.util.properties.PropertyHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controls changes to settings.
 *
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class SettingsPanelUIController implements Initializable, UIPanelController {
    private AppDataController appDataController;
    private PropertyHandler propHandler;
    @FXML
    private TextArea dbJDBC;
    @FXML
    private TextField dbPassword;
    @FXML
    private TextField dbUsername;
    @FXML
    private Button dbTestConnectionButton;
    @FXML
    private Button dbApplyChangesButton;
    @FXML
    private CheckBox clearMessageBoxOnPanelChangeCb;
    @FXML
    private ComboBox<String> availablePanelComboBox;
    private final HashMap<String, String> settingsMap = new HashMap<>();
    private final SimpleBooleanProperty hasUnchangedSettings = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        propHandler = PropertyHandler.getInstance();
        hasUnchangedSettings.setValue(false);
        dbTestConnectionButton.disableProperty().bind(hasUnchangedSettings);
        dbApplyChangesButton.disableProperty().bind(hasUnchangedSettings.not());
        dbJDBC.setText(propHandler.getValue("db.conn.url"));
        dbJDBC.textProperty().addListener((obs, oldText, newText) -> {
            hasUnchangedSettings.set(true);
            settingsMap.put("db.conn.url", dbJDBC.getText());
        });
        dbUsername.setText(propHandler.getValue("db.username"));
        dbUsername.textProperty().addListener((obs, oldText, newText) -> {
            hasUnchangedSettings.set(true);
            settingsMap.put("db.username", dbUsername.getText());
        });
        dbPassword.setText(propHandler.getValue("db.password"));
        dbPassword.textProperty().addListener((obs, oldText, newText) -> {
            hasUnchangedSettings.set(true);
            settingsMap.put("db.password", dbPassword.getText());
        });
        clearMessageBoxOnPanelChangeCb.setSelected(Boolean.parseBoolean(propHandler.getValue("clearMessageBoxOnPanelChange")));
        clearMessageBoxOnPanelChangeCb.selectedProperty().addListener((obs, oldValue, newValue) -> {
            settingsMap.put("clearMessageBoxOnPanelChange", clearMessageBoxOnPanelChangeCb.selectedProperty().getValue().toString());
            propHandler.writePropertiesFromMap(settingsMap);
            settingsMap.clear();
        });
        // when initialize method is called by fxml loader, panelList in AppData is still empty
        availablePanelComboBox.addEventHandler(ComboBox.ON_SHOWING, event -> fillPanelComboBox());
        availablePanelComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            settingsMap.put("selectedPanelOnStartup", availablePanelComboBox.getSelectionModel().getSelectedItem());
            propHandler.writePropertiesFromMap(settingsMap);
            settingsMap.clear();
        });
    }

    /**
     * Fills panel combobox and selects default panel on first click,
     * since panel list in appdata is not filled, when panel is initialized
     */
    private void fillPanelComboBox() {
        if (availablePanelComboBox.getItems().size() == 0) {
            availablePanelComboBox.setItems(appDataController.getPanelList());
            availablePanelComboBox.getSelectionModel().select(propHandler.getValue("selectedPanelOnStartup"));
        }
    }

    /**
     * Tests database connection and displays result in message box
     */
    @FXML
    public void testDbConnection() {
        if (appDataController.isDatabaseAccessible().get()) {
            appDataController.setMessage("Database successfully connected.");
        } else {
            appDataController.setMessage("Connection failed. Check connection settings.");
        }
    }

    /**
     * Applies changes to properties file.
     */
    @FXML
    public void applyDBSettingChanges() {
        propHandler.writePropertiesFromMap(settingsMap);
        //reset
        settingsMap.clear();
        hasUnchangedSettings.set(false);
    }
}
