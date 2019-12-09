package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.Busline;
import ch.zhaw.jv19.loganalyzer.model.FileImportController;
import ch.zhaw.jv19.loganalyzer.model.Site;
import ch.zhaw.jv19.loganalyzer.model.User;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class ImportPanelUIController {

    // TODO
    // Will be collected by the import form
    private User user;
    private Site site;
    private Busline busline;

    private List<File> fileList;

    public void handleImportSelectedFiles(ActionEvent event) {

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.txt");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);

        /* Show open file dialog to select multiple files. */
        fileList = fileChooser.showOpenMultipleDialog(null);
        FileImportController fileImport = new FileImportController(user, site, busline, fileList);
    }
}
