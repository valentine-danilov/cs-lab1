package by.danilov.cs.data;

import java.text.MessageFormat;

public class KeyValuePair {

    private String key;
    private String value;

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValuePair(Integer key, Integer value) {
        this.key = String.valueOf(key);
        this.value = String.valueOf(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Expected: {0} --- Actual: {1}\n", key, value);
    }
}
