package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;

public class ExportPanelUIController {
    private final AppDataController appDataController = AppDataController.getInstance();

    /**
     * Exports current resultTable to Excel. Default file name is Export_[currentDateTime].xlsx.
     */
    void exportTable(TableView<?> table) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export table");
        chooser.setInitialFileName("Export_" + DateUtil.getCurrentDateTimeString("yyyy-MM-dd_HHmm") + ".xlsx");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx"));
        File file = chooser.showSaveDialog(table.getScene().getWindow());
        // file can be null in case user cancels in file chooser
        if (file != null) {
            appDataController.exportToExcel(table, file);
            appDataController.setMessage("File successfully saved to " + file.getAbsolutePath());
        }
    }
}
