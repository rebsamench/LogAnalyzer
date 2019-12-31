package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.properties.PropertyHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controls changes to settings.
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
    private HashMap<String, String> settingsMap = new HashMap<>();
    private SimpleBooleanProperty hasUnchangedSettings = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        propHandler = PropertyHandler.getInstance();
        hasUnchangedSettings.setValue(false);
        dbTestConnectionButton.disableProperty().bind(hasUnchangedSettings);
        dbApplyChangesButton.disableProperty().bind(hasUnchangedSettings.not());
        dbJDBC.setText(propHandler.getValue("db.conn.url"));
        dbUsername.setText(propHandler.getValue("db.username"));
        dbPassword.setText(propHandler.getValue("db.password"));
        observeTextAreaChanges(dbJDBC);
        observeTextFieldChanges(dbUsername);
        observeTextFieldChanges(dbPassword);
        clearMessageBoxOnPanelChangeCb.setSelected(Boolean.parseBoolean(propHandler.getValue("clearMessageBoxOnPanelChange")));
    }

    /**
     * Tests database connection and displays result in message box
     */
    @FXML
    public void testDbConnection() {
        try {
            DBUtil.getConnection();
            appDataController.setMessage("Database successfully connected.");
            } catch (Exception e) {
            appDataController.setMessage("Connection failed. Check connection settings." + "\n" +
                    e.getMessage());
        }
    }

    /**
     * Applies changes to properties file.
     */
    @FXML
    public void applyChanges() {
        settingsMap.put("db.conn.url",dbJDBC.getText());
        settingsMap.put("db.username", dbUsername.getText());
        settingsMap.put("db.password",dbPassword.getText());
        propHandler.writePropertiesFromMap(settingsMap);
        hasUnchangedSettings.set(false);
    }

    /**
     * Adds change listener to text area to observe for changes.
     * Sets hasUnchangedSettings to true if text area was changed.
     * @param textArea text area to observe
     */
    private void observeTextAreaChanges(TextArea textArea) {
        textArea.textProperty().addListener((obs, oldText, newText) -> hasUnchangedSettings.set(true));
    }

    /**
     * Adds change listener to text field to observe for changes.
     * Sets hasUnchangedSettings to true if text area was changed.
     * @param textField text field to observe
     */
    private void observeTextFieldChanges(TextField textField) {
        textField.textProperty().addListener((obs, oldText, newText) -> hasUnchangedSettings.set(true));
    }
}
