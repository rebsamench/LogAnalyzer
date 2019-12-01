package ch.zhaw.jv19.loganalyzer.model;

public class SearchCondition {
    private String joinTableName;
    private String key;
    private Object value;
    private String operator;
    private String comparisonOperator;

    public Object getValue() {
        return value;
    }

    public String getComparisonOperator() {
        return comparisonOperator;
    }

    public String getOperator() {
        return operator;
    }

    public SearchCondition(String key, Object value, String operator, String comparisonOperator) {
        this.key = key;
        this.value = value;
        this.operator = operator;
        this.comparisonOperator = comparisonOperator;
    }

    public String getKey() {
        return key;
    }
}
