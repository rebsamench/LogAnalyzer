package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePanelUIController implements Initializable, UIPanelController {
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
    }
}
