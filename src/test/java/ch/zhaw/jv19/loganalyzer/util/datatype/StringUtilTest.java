package ch.zhaw.jv19.loganalyzer.util.datatype;

import static org.junit.Assert.*;
/**
 * Provides unit tests for string conversion and formatting methods.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class StringUtilTest {
    @org.junit.Test
    public void wrapQuotes() {
        assertEquals("'Test test'", StringUtil.wrapQuotes.apply("Test test"));
    }

    @org.junit.Test
    public void wrapBrackets() {
        assertEquals("(Test test)", StringUtil.wrapBrackets.apply("Test test"));
    }

    @org.junit.Test
    public void wrapPercent() {
        assertEquals("%Test test%", StringUtil.wrapPercent.apply("Test test"));
    }
}