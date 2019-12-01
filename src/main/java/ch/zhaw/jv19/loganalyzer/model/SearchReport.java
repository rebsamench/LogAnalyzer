package ch.zhaw.jv19.loganalyzer.model;

import java.util.ArrayList;


public class SearchReport {
    ArrayList<SearchCondition> formData;

    public SearchReport() {
        formData = new ArrayList<>();
    }

    public void addCondition(SearchCondition condition) {
        formData.add(condition);
    }

    public ArrayList<SearchCondition> getFormData() {
        return formData;
    }

}
