package ch.zhaw.jv19.loganalyzer.util.properties;

import org.apache.commons.collections4.properties.SortedProperties;

import java.io.*;
import java.util.Set;

/**
 * Handles access to property file.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 * copied from http://www.gridtec.at/java-properties-file-beispiel/ with minor changes
 */
public class PropertyHandler {

    private static PropertyHandler instance = null;
    private SortedProperties prop = null;
    private static String propertyFilePath = "src/main/resources/properties/loganalyzer.properties";

    private PropertyHandler() {
        InputStream input = null;
        try {
            input = new FileInputStream(propertyFilePath);
            this.prop = new SortedProperties();
            this.prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets instance of PropertyHandler. PropertyHandler is a Singleton.
     * @return Singleton instance of PropertyHandler
     */
    public static PropertyHandler getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (PropertyHandler.class) {

            if (instance == null) {
                instance = new PropertyHandler();
            }
        }
        return instance;
    }

    /**
     * Gets property by property key.
     * @param propKey key of property
     * @return property that matches fiven key.
     */
    public String getValue(String propKey) {
        return this.prop.getProperty(propKey);
    }

    public Set<String> getAllPropertyNames() {
        return this.prop.stringPropertyNames();
    }

    public boolean containsKey(String key) {
        return this.prop.containsKey(key);
    }

    public void saveParamChanges(String key, String value) {
        try {
            this.prop.setProperty(key, value);
            OutputStream out = new FileOutputStream(propertyFilePath);
            // Zeige letzte Änderungen als Kommentar im Properies File
            this.prop.store(out, "Last changes: " + key + "=" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


