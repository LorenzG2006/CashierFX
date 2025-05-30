package com.example;

import java.util.regex.Pattern;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * IntegerField class of the application. This is a custom TextField which only
 * accepts numbers. It is built to be used for integer values.
 */
public class IntegerField extends NumberField {

    /**
     * The IntegerProperty of the IntegerField. This is the value of the field which
     * can be bound to other properties.
     */
    private final IntegerProperty value = new SimpleIntegerProperty(this, "value");

    private final String regex = "^\\d{0,9}$";

    private final Pattern pattern = Pattern.compile(this.regex);

    /**
     * Constructor for IntegerField.
     */
    public IntegerField() {
        super();

        initHandlers();
    }

    /**
     * Initializes the handlers of the IntegerField.
     */
    private void initHandlers() {
        // Listener for the text property
        // This is used to change the value when the text changes
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidNumber(newValue)) {
                if (newValue.isEmpty()) {
                    this.value.setValue(0);

                    setText("");
                } else {
                    this.value.set(Integer.parseInt(newValue));
                }
            } else {
                setText(oldValue);
            }
        });

        // Listener for the value property
        // This is used to change the text when the value changes
        this.value.addListener(observable -> {
            String text = this.value.getValue().toString();

            if (!text.equals(getText())) {
                setText(text);
            }
        });
    }

    /**
     * Returns the value of the IntegerField.
     * 
     * @return the value of the IntegerField
     */
    public IntegerProperty valueProperty() {
        return this.value;
    }

    /**
     * Checks if the given text is a valid number.
     * 
     * @param text the text to check
     * @return true if the text matches the regex, false otherwise
     */
    private boolean isValidNumber(String text) {
        return this.pattern.matcher(text).matches();
    }
}