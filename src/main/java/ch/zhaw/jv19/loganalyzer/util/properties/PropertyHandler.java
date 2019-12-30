package ch.zhaw.jv19.loganalyzer.util.properties;

import javafx.fxml.FXML;
import org.apache.commons.collections4.properties.SortedProperties;

import java.io.*;
import java.util.*;

/**
 * Handles access (read and change) to property file.
 *
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 * copied from http://www.gridtec.at/java-properties-file-beispiel/ with minor changes
 */
public class PropertyHandler {

    private static PropertyHandler instance = null;
    private SortedProperties prop = null;
    private String filepath = "src/main/resources/properties/loganalyzer.properties";

    private PropertyHandler() {
        InputStream input = null;
        try {
            if (System.getProperty("CONFIG_DIR") != null) {
                filepath = System.getProperty("CONFIG_DIR") + "/loganalyzer.properties";
            }
            input = new FileInputStream(filepath);
            this.prop = new SortedProperties();
            this.prop.load(input);
        } catch (FileNotFoundException e) {
            System.out.println("File loganalyzer.properties not found in " + filepath + "." +
                    "JAR: Check if system parameter CONFIG_DIR points to directory." +
                    "IDE: Check if properties file is accessible in filepath.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
     * Refreshes cached properties by creating a new instance. Needs to be called, after properties file
     * was changed.
     */
    private void refreshInstance() {
        instance = new PropertyHandler();
    }

    /**
     * Gets instance of PropertyHandler. PropertyHandler is a Singleton.
     *
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
     *
     * @param propKey key of property
     * @return property that matches given key.
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

    /**
     * Saves given key and value as property in property file path
     *
     * @param key   key of property to be stored
     * @param value value of property to be stored
     */
    private void saveProperty(String key, String value) {
        try {
            this.prop.setProperty(key, value);
            OutputStream out = new FileOutputStream(filepath);
            // show last changes as comment in property file
            this.prop.store(out, "Last changes: " + key + "=" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all properties in given properties map.
     * @param propertiesMap HashMap of property key (String) and property values (String)
     */
    public void writePropertiesFromMap(HashMap<String, String> propertiesMap) {
        Iterator<HashMap.Entry<String, String>> entries = propertiesMap.entrySet().iterator();
        while (entries.hasNext()) {
            HashMap.Entry<String, String> entry = entries.next();
            saveProperty(entry.getKey(), entry.getValue());
        }
        refreshInstance();
    }
}


