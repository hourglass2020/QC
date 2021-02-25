package DeviecPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Attribute
{
    public static String TEXT_FIELD = "TEXT_FIELD";
    public static String RADIO_BTN_GP = "RADIO_BTN_GP";

    // There is no value !!
    private String name;
    private String defaultValue;
    private String mode;
    private String category;
    private double min = 0;
    private double max = 0;
    private String mainValue;
    private boolean automaticFill = false;
    private JSONArray choices = new JSONArray();

    public Attribute()
    {
        this.name = "N/A";
        this.defaultValue = "N/A";
        this.mode = TEXT_FIELD;
        this.setCategory("No Title");
    }

    public Attribute(String name, String defaultValue, String category , double min , double max , String mode)
    {
        this.name = name;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.mode = mode;
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getMode() {
        return mode;
    }

    public String getStringMode()
    {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

//    public List<String> getChoices() {
//        return choices;
//    }


    public String getMainValue() {
        return mainValue;
    }

    public void setMainValue(String mainValue) {
        this.mainValue = mainValue;
    }

    public void setChoices(Object[] choices)
    {
        for (Object obj : choices)
            this.choices.add(obj.toString());
    }

    public void setChoices(JSONArray choices)
    {
        this.choices = choices;
    }

    public JSONArray getChoicesNames()
    {
        return this.choices;
    }

    public boolean isAutomaticFill() {
        return automaticFill;
    }

    public void setAutomaticFill(boolean automaticFill) {
        this.automaticFill = automaticFill;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", mode='" + mode + '\'' +
                ", category='" + category + '\'' +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
