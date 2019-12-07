package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppData;
import ch.zhaw.jv19.loganalyzer.model.AppDataController;

public interface UIPanelController {
    AppDataController controller = null;
    void setAppDataController(AppDataController appDataController);
}
