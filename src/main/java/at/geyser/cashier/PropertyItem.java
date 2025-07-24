package at.geyser.cashier;

import java.util.Optional;

import org.controlsfx.control.PropertySheet.Item;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

/**
 * PropertyItem class of the CashierFX application. Used to display the
 * settings.
 * 
 * @param <T>
 */
public class PropertyItem<T> implements Item {

    private final String name;
    private final String category;
    private final String description;
    private final Class<T> type;
    private final WritableValue<T> value;

    public PropertyItem(String[] properties, Class<T> type, WritableValue<T> value) {
        this.name = properties[0];
        this.category = properties[1];
        this.description = properties[2];
        this.type = type;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Optional<ObservableValue<?>> getObservableValue() {
        return Optional.empty();
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }

    @Override
    public T getValue() {
        return this.value.getValue();
    }

    @Override
    public void setValue(Object value) {
        this.value.setValue(type.cast(value));
    }
}