package com.example;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Pattern;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

/**
 * FloatField class of the application. This is a custom TextField which only
 * accepts numbers. It is built to be used for money values.
 */
public class FloatField extends NumberField {

    /**
     * The FloatProperty of the FloatField. This is the value of the field which can
     * be bound to other properties.
     */
    private final FloatProperty value = new SimpleFloatProperty(this, "value");

    /**
     * The DecimalFormat of the FloatField. This is used to format the value of the
     * field.
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("0.##");

    private final String regex = "^\\d{0,5}(\\.\\d{0,2})?$";

    private final Pattern pattern = Pattern.compile(this.regex);

    /**
     * Constructor for FloatField.
     */
    public FloatField() {
        super();

        initHandlers();
    }

    /**
     * Returns the value of the FloatField.
     * 
     * @return the value of the FloatField
     */
    public FloatProperty valueProperty() {
        return this.value;
    }

    /**
     * Initializes the handlers of the FloatField.
     */
    private void initHandlers() {
        this.decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));

        // Listener for the text property
        // This is used to change the value when the text changes
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidNumber(newValue)) {
                if (newValue.isEmpty()) {
                    this.value.setValue(0);

                    setText("");
                } else {
                    if (newValue.charAt(0) == '.' && newValue.length() == 1) {
                        newValue = "0.";
                    }

                    this.value.setValue(Float.parseFloat(newValue));

                    setText(newValue);
                }
            } else {
                setText(oldValue);
            }
        });

        // Listener for the value property
        // This is used to change the text when the value changes
        this.value.addListener(observable -> {
            String text = decimalFormat.format(this.value.getValue());

            this.value.setValue(Float.parseFloat(text));

            if (!text.equals(getText())) {
                setText(text);
            }
        });
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