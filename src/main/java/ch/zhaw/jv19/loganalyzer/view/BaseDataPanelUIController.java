package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;

public class BaseDataPanelUIController implements UIPanelController {
    private AppDataController appDataController;

    @Override
    public void setAppDataController(AppDataController appDataController) {
        this.appDataController = appDataController;
    }
}