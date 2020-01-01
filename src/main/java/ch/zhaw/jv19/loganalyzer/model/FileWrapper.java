package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;

public class FileWrapper {
    private File file;
    private SimpleStringProperty name;

    public FileWrapper(File file) {
        name = new SimpleStringProperty();
        name.set(file.getName());
        this.file = file;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }
}
