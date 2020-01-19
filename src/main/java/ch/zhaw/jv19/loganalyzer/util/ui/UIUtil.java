package ch.zhaw.jv19.loganalyzer.util.ui;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Provides helper methods for ui nodes.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class UIUtil {

    /**
     * Restricts input values in text field to specific length
     *
     * @param textField TextField to be restricted in length
     * @param length    max length of field
     */
    public static void addLengthEventFilter(TextField textField, int length) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (textField.getText().length() >= length) {
                keyEvent.consume();
            }
        });
    }

    /**
     * Restricts input values in text field to numeric values
     * https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
     *
     * @param textField numeric node (e. g. TextField)
     */
    public static void addNumericEventFilter(TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });
    }
}
