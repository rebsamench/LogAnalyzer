package ch.zhaw.jv19.loganalyzer.util.datatype;

import java.util.function.Function;

/**
 * Provides string conversion and formatting methods.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class StringUtil {
    public static final Function<String, String> wrapQuotes = string -> "'" + string + "'";

    public static final Function<String, String> wrapBrackets = string -> "(" + string + ")";

    public static final Function<String, String> wrapPercent = string -> "%" + string + "%";
}