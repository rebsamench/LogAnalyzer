package ch.zhaw.jv19.loganalyzer.view;
import ch.zhaw.jv19.loganalyzer.MainApp;
import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
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
    private ToggleButton btnHome;
    @FXML
    private ToggleButton btnImport;
    @FXML
    private ToggleButton btnReports;
    @FXML
    private ToggleButton btnSQL;
    @FXML
    private ToggleButton btnSettings;
    @FXML
    private ToggleButton btnBaseData;
    @FXML
    private TextArea messageBox;
    private ArrayList<AnchorPane> uiPanels;
    private MainApp mainApp;
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create global appdata controller
        appDataController = AppDataController.getInstance();
        uiPanels = new ArrayList<>();
        ToggleGroup mainMenu = new ToggleGroup();
        btnHome.setToggleGroup(mainMenu);
        btnImport.setToggleGroup(mainMenu);
        btnReports.setToggleGroup(mainMenu);
        btnSQL.setToggleGroup(mainMenu);
        btnSettings.setToggleGroup(mainMenu);
        btnBaseData.setToggleGroup(mainMenu);
        // bind message box to message in app data
        messageBox.textProperty().bind(appDataController.getMessage());
        // all panels must be created here by providing the name of the fxml file
        addPanelToPanelList("HomePanel");
        addPanelToPanelList("ImportPanel");
        addPanelToPanelList("ReportPanel");
        addPanelToPanelList("SQLPanel");
        addPanelToPanelList("SettingsPanel");
        addPanelToPanelList("BaseDataPanel");
    }

    /**
     * Handles clicks on menu buttons. Each menu button must be bound with the according panel in this method.
     * @param event Mouse event of the clicked button.
     */
    @FXML
    public void handleMenuButtonClicks(MouseEvent event) {
        if (event.getSource() == btnHome) {
            selectPanel("HomePanel");
        } else if (event.getSource() == btnImport) {
            selectPanel("ImportPanel");
        } else if (event.getSource() == btnReports) {
            selectPanel("ReportPanel");
        } else if (event.getSource() == btnSQL) {
            selectPanel("SQLPanel");
        } else if (event.getSource() == btnSettings) {
            selectPanel("SettingsPanel");
        } else if (event.getSource() == btnBaseData) {
            selectPanel("BaseDataPanel");
        }
        else {
            appDataController.setMessage("Button " + " not bound with a panel id. Check MainAppUIController.");
        }
    }

    /**
     * Selects panel in uiPanels list by panel id and displays it.
     * @param panelId ID of panel to show. ID must correspond with
     *                an id attribute of a Pane (root element) in uiPanels list.
     */
    private void selectPanel(String panelId) {
        Iterator<AnchorPane> it = uiPanels.iterator();
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
                // panels need to know app data controller in order to show global messages
                UIPanelController uiPanelController = fxmlLoader.getController();
                uiPanels.add(pane);
            }
            else {
                System.out.println("Panel not added: Top level pane in Panel " + fxmlName + " has no fx:id.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp main) {
        this.mainApp = main;
    }

}
