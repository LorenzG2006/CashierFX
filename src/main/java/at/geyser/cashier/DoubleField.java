package at.geyser.cashier;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Pattern;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * DoubleField class of the application. This is a custom TextField which only
 * accepts numbers. It is built to be used for money values.
 */
public class DoubleField extends NumberField {

    /**
     * The DoubleProperty of the FloatField. This is the value of the field which
     * can be bound to other properties.
     */
    private final DoubleProperty value = new SimpleDoubleProperty(this, "value");

    /**
     * The DecimalFormat of the DoubleField. This is used to format the value of the
     * field.
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("0.##");

    private final String regex = "^\\d{0,13}(\\.\\d{0,2})?$";

    private final Pattern pattern = Pattern.compile(this.regex);

    /**
     * Constructor for DoubleField.
     */
    public DoubleField() {
        super();

        initHandlers();
    }

    /**
     * Returns the value of the DoubleField.
     * 
     * @return the value of the DoubleField
     */
    public DoubleProperty valueProperty() {
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

                    this.value.setValue(Double.parseDouble(newValue));

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

            this.value.setValue(Double.parseDouble(text));

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