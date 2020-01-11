package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.util.properties.PropertyHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Controls ui interactions of main app. Provides main menu and handles creation and selection of panels.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class MainAppUIController implements Initializable {
    @FXML
    AnchorPane contentPane;
    @FXML
    HBox logoBox;
    @FXML
    private ToggleButton btnImport;
    @FXML
    private ToggleButton btnReports;
    @FXML
    private ToggleButton btnSettings;
    @FXML
    private ToggleButton btnBaseData;
    @FXML
    private TextArea messageBox;
    private ArrayList<AnchorPane> uiPanelList;
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set logo
        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("/images/logo_177x69.png");
        Image logo = new Image(input);
        ImageView imageView = new ImageView(logo);
        logoBox.getChildren().add(imageView);
        // create global appdata controller
        appDataController = AppDataController.getInstance();
        uiPanelList = new ArrayList<>();
        ToggleGroup mainMenu = new ToggleGroup();
        btnImport.setToggleGroup(mainMenu);
        btnReports.setToggleGroup(mainMenu);
        btnSettings.setToggleGroup(mainMenu);
        btnBaseData.setToggleGroup(mainMenu);
        // disable unusable panels by disabling buttons if database is not accessible
        btnImport.disableProperty().bind(appDataController.isDatabaseAccessible().not());
        btnReports.disableProperty().bind(appDataController.isDatabaseAccessible().not());
        btnBaseData.disableProperty().bind(appDataController.isDatabaseAccessible().not());
        // bind message box to message in app data
        messageBox.textProperty().bind(appDataController.getMessage());
        // all panels must be created here by providing the name of the fxml file
        addPanelToPanelList("ImportPanel");
        addPanelToPanelList("ReportPanel");
        addPanelToPanelList("SettingsPanel");
        addPanelToPanelList("BaseDataPanel");
        //make panel list available on appdata for further usage
        appDataController.fillPanelList(uiPanelList);
        // select default panel. overridden if db is not available
        if(!appDataController.isDatabaseAccessible().get()) {
            appDataController.setMessage("Could not connect to database. Adjust database connection settings in properties file or on panel 'Settings' and restart app");
            selectPanel("SettingsPanel", true);
        } else {
            // select default panel defined in properties
            selectPanel(PropertyHandler.getInstance().getValue("selectedPanelOnStartup"), true);
        }
    }

    /**
     * Handles clicks on menu buttons. Each menu button must be bound with the according panel in this method.
     * @param event Mouse event of the clicked button.
     */
    @FXML
    public void handleMenuButtonClicks(MouseEvent event) {
        if (event.getSource() == btnImport) {
            selectPanel("ImportPanel", false);
        } else if (event.getSource() == btnReports) {
            selectPanel("ReportPanel", false);
        } else if (event.getSource() == btnSettings) {
            selectPanel("SettingsPanel", false);
        } else if (event.getSource() == btnBaseData) {
            selectPanel("BaseDataPanel", false);
        }
        else {
            appDataController.setMessage("Button " + " not bound with a panel id. Check MainAppUIController.");
        }
    }

    /**
     * Selects panel in uiPanels list by panel id and displays it.
     * @param panelId ID of panel to show. ID must correspond with
     *                an id attribute of a Pane (root element) in uiPanels list.
     * @param ignoreClearMessageBoxOnPanelChange true, if clearMessageBoxOnPanelChange property should be ignored
     */
    private void selectPanel(String panelId, boolean ignoreClearMessageBoxOnPanelChange) {
        Iterator<AnchorPane> it = uiPanelList.iterator();
        boolean panelFound = false;
        while(!panelFound && it.hasNext()) {
            AnchorPane pane = it.next();
            if(pane.getId().equals(panelId)) {
                panelFound = true;
                // replace current content panel
                contentPane.getChildren().clear();
                pane.minWidthProperty().bind(contentPane.widthProperty());
                pane.minHeightProperty().bind(contentPane.heightProperty());
                contentPane.getChildren().add(pane);
            }
            if (PropertyHandler.getInstance().getValue("clearMessageBoxOnPanelChange").equals("true") && !ignoreClearMessageBoxOnPanelChange) {
                appDataController.setMessage("");
            }
        }
    }

    /**
     * Creates panel by loading given FXMl file and adds it to panel list
     * @param fxmlName name of FXML file without (.fxml) file extension
     */
    private void addPanelToPanelList(String fxmlName) {
        AnchorPane pane;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName + ".fxml"));
            pane = fxmlLoader.load();
            // top level pane in panel needs an id attribute in order to load it by id
            if(pane.getId() != null) {
                uiPanelList.add(pane);
            }
            else {
                appDataController.setMessage("Panel not added: Top level pane in Panel " + fxmlName + " has no id attribute" +
                        " or id attribute does not match name of fxml file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
