package at.geyser.cashier;

import java.util.regex.Pattern;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

/**
 * LongField class of the application. This is a custom TextField which only
 * accepts numbers. It is built to be used for long values.
 */
public class LongField extends NumberField {

    /**
     * The LongProperty of the LongField. This is the value of the field which can
     * be bound to other properties.
     */
    private final LongProperty value = new SimpleLongProperty(this, "value");

    /**
     * The regex which is used to check if the text is a valid number.
     */
    private final String regex = "^\\d{0,18}$";

    /**
     * The pattern which is used to check if the text is a valid number.
     */
    private final Pattern pattern = Pattern.compile(this.regex);

    /**
     * Constructor for LongField.
     */
    public LongField() {
        super();

        initHandlers();
    }

    /**
     * Initializes the handlers of the LongField.
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
                    this.value.set(Long.parseLong(newValue));
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
     * Returns the value of the LongField.
     * 
     * @return the value of the LongField
     */
    public LongProperty valueProperty() {
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