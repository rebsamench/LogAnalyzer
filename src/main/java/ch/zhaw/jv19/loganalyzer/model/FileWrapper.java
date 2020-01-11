package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;

/**
 * Wraps a file object in order to provide property fields.
 * This is necessary for table views.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class FileWrapper {
    private final File file;
    private final SimpleStringProperty name;

    public FileWrapper(File file) {
        name = new SimpleStringProperty();
        name.set(file.getName());
        this.file = file;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }
}
