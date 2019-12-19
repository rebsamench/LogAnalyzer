package ch.zhaw.jv19.loganalyzer.util.datatype;

import java.util.function.Function;

public class StringUtil {
    public static final Function<String, String> addQuotes = string -> "'" + string + "'";

    public static final Function<String, String> addBrackets = string -> "(" + string + ")";

    public static final Function<String, String> addPercent = string -> "%" + string + "%";

    public static int countCharacterOcurrenceInString(String string, char character) {
        int count = 0;
        for(int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == character)
                count++;
        }
        return count;
    }
}
