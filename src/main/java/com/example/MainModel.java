package com.example;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Model for the main view. Contains the models for every subview.
 */
public class MainModel {

    /**
     * Model for the settings dialog inside the main view.
     */
    public static class SettingsModel {

        private final ObjectProperty<Settings.Language> languageProperty = new SimpleObjectProperty<Settings.Language>(this, "language");
        private final ObjectProperty<Settings.Style> styleProperty = new SimpleObjectProperty<Settings.Style>(this, "style");
        private final ObjectProperty<Settings.Startup> startupProperty = new SimpleObjectProperty<Settings.Startup>(this, "startup");
        private final ObjectProperty<Settings.Currency> currencyProperty = new SimpleObjectProperty<Settings.Currency>(this, "currency");
        private final BooleanProperty autoSaveProperty = new SimpleBooleanProperty(this, "autoSave");
        private final BooleanProperty showTipsProperty = new SimpleBooleanProperty(this, "showTips");

        /**
         * Constructor for SettingsModel. Initializes the model with the given settings.
         * 
         * @param settings the settings to be used to initialize the model
         */
        public SettingsModel(Settings settings) {
            this.languageProperty.setValue(settings.getLanguage());
            this.styleProperty.setValue(settings.getStyle());
            this.startupProperty.setValue(settings.getStartup());
            this.currencyProperty.setValue(settings.getCurrency());
            this.autoSaveProperty.setValue(settings.getAutoSave());
            this.showTipsProperty.setValue(settings.getShowTips());
        }

        /**
         * Builds a new settings object from the model.
         * 
         * @return the new settings object
         */
        public Settings buildSettings() {
            Settings settings = new Settings();

            settings.setLanguage(this.languageProperty.getValue());
            settings.setStyle(this.styleProperty.getValue());
            settings.setStartup(this.startupProperty.getValue());
            settings.setCurrency(this.currencyProperty.getValue());
            settings.setAutoSave(this.autoSaveProperty.getValue());
            settings.setShowTips(this.showTipsProperty.getValue());

            return settings;
        }

        public Settings.Language getLanguage() {
            return this.languageProperty.getValue();
        }

        public void setLanguage(Settings.Language language) {
            this.languageProperty.setValue(language);
        }

        public ObjectProperty<Settings.Language> languageProperty() {
            return this.languageProperty;
        }

        public Settings.Style getStyle() {
            return this.styleProperty.getValue();
        }

        public void setStyle(Settings.Style style) {
            this.styleProperty.setValue(style);
        }

        public ObjectProperty<Settings.Style> styleProperty() {
            return this.styleProperty;
        }

        public Settings.Startup getStartup() {
            return this.startupProperty.getValue();
        }

        public void setStartup(Settings.Startup startup) {
            this.startupProperty.setValue(startup);
        }

        public ObjectProperty<Settings.Startup> startupProperty() {
            return this.startupProperty;
        }

        public Settings.Currency getCurrency() {
            return this.currencyProperty.getValue();
        }

        public void setCurrency(Settings.Currency currency) {
            this.currencyProperty.setValue(currency);
        }

        public ObjectProperty<Settings.Currency> currencyProperty() {
            return this.currencyProperty;
        }

        public Boolean getAutoSave() {
            return this.autoSaveProperty.getValue();
        }

        public void setAutoSave(Boolean autoSave) {
            this.autoSaveProperty.setValue(autoSave);
        }

        public BooleanProperty autoSaveProperty() {
            return this.autoSaveProperty;
        }

        public Boolean getShowTips() {
            return this.showTipsProperty.getValue();
        }

        public void setShowTips(Boolean showTips) {
            this.showTipsProperty.setValue(showTips);
        }

        public BooleanProperty showTipsProperty() {
            return this.showTipsProperty;
        }
    }

    public static class CreateAuthorizationModel extends AuthorizationModel {

        public CreateAuthorizationModel() {
            super();
        }
    }

    public static class AuthorizeModel extends AuthorizationModel {

        public AuthorizeModel(Map<String, String> authorizations) {
            super();

            setAuthorizations(authorizations);
        }
    }

    public static class ChangeAuthorizationModel extends AuthorizationModel {

        public ChangeAuthorizationModel(Map<String, String> authorizations) {
            super();

            setAuthorizations(authorizations);
        }
    }

    public static class AuthorizationModel {

        private final Map<StringProperty, StringProperty> authorizationProperties;

        public AuthorizationModel() {
            this.authorizationProperties = new HashMap<StringProperty, StringProperty>();
        }

        public StringProperty createStringProperty(String name) {
            StringProperty stringProperty = new SimpleStringProperty(this, name);

            return stringProperty;
        }

        public void addAuthorization(StringProperty keyProperty, StringProperty valueProperty) {
            this.authorizationProperties.put(keyProperty, valueProperty);
        }

        public void removeAuthorization(StringProperty keyProperty) {
            this.authorizationProperties.remove(keyProperty);
        }

        public Map<String, String> buildAuthorizations() {
            Map<String, String> authorizations = getAuthorizations();

            Map<String, String> filteredAuthorizations = new HashMap<String, String>();

            for (String key : authorizations.keySet()) {
                String value = authorizations.get(key);

                if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
                    continue;
                }

                filteredAuthorizations.put(key, value);
            }

            return filteredAuthorizations;
        }

        /**
         * Gets the authorizations from the model.
         * 
         * @return the authorizations as a map
         */
        public Map<String, String> getAuthorizations() {
            // Creates a new map
            Map<String, String> authorizations = new HashMap<String, String>();

            // Iterates through the map of properties
            for (StringProperty keyProperty : this.authorizationProperties.keySet()) {
                // Gets the value property
                StringProperty valueProperty = this.authorizationProperties.get(keyProperty);

                // Gets the key and value
                String key = keyProperty.getValue();
                String value = valueProperty.getValue();

                // Puts the key and value in the map
                authorizations.put(key, value);
            }

            // Returns the map
            return authorizations;
        }

        public void setAuthorizations(Map<String, String> authorizations) {
            this.authorizationProperties.clear();

            for (String key : authorizations.keySet()) {
                String value = authorizations.get(key);

                StringProperty keyProperty = new SimpleStringProperty(this, "key");
                StringProperty valueProperty = new SimpleStringProperty(this, "value");

                keyProperty.setValue(key);
                valueProperty.setValue(value);

                this.authorizationProperties.put(keyProperty, valueProperty);
            }
        }

        public Map<StringProperty, StringProperty> authorizationProperties() {
            return this.authorizationProperties;
        }
    }

    public static class AddModel extends EntryModel {

        /**
         * Nothing to do here.
         */
        public AddModel() {

        }

        public AddModel(Entry entry) {
            super(entry);
        }

        @Override
        public Entry buildEntry() {
            Entry entry = super.buildEntry();

            return entry;
        }
    }

    public static class EditModel extends EntryModel {

        private final DoubleProperty totalPaymentProperty = new SimpleDoubleProperty(this, "totalPayment");
        private final DoubleProperty alreadyPaidProperty = new SimpleDoubleProperty(this, "alreadyPaid");

        /**
         * Nothing to do here.
         */
        public EditModel() {

        }

        public EditModel(Entry entry) {
            super(entry);

            this.totalPaymentProperty.setValue(entry.getTotalPayment());
            this.alreadyPaidProperty.setValue(entry.getAlreadyPaid());
        }

        @Override
        public Entry buildEntry() {
            Entry entry = super.buildEntry();

            entry.setTotalPayment(this.totalPaymentProperty.getValue());
            entry.setAlreadyPaid(this.alreadyPaidProperty.getValue());

            return entry;
        }

        public Double getTotalPayment() {
            return this.totalPaymentProperty.getValue();
        }

        public void setTotalPayment(Double totalPayment) {
            this.totalPaymentProperty.setValue(totalPayment);
        }

        public DoubleProperty totalPaymentProperty() {
            return this.totalPaymentProperty;
        }

        public Double getAlreadyPaid() {
            return this.alreadyPaidProperty.getValue();
        }

        public void setAlreadyPaid(Double alreadyPaid) {
            this.alreadyPaidProperty.setValue(alreadyPaid);
        }

        public DoubleProperty alreadyPaidProperty() {
            return this.alreadyPaidProperty;
        }
    }

    /**
     * Model for the add and edit dialog inside the main view. Created to avoid code
     * duplication.
     */
    public static class EntryModel {

        private final LongProperty idProperty = new SimpleLongProperty(this, "id");
        private final StringProperty firstNameProperty = new SimpleStringProperty(this, "firstName");
        private final StringProperty lastNameProperty = new SimpleStringProperty(this, "lastName");

        /**
         * Nothing to do here.
         */
        public EntryModel() {

        }

        public EntryModel(Entry entry) {
            this.idProperty.setValue(entry.getId());
            this.firstNameProperty.setValue(entry.getFirstName());
            this.lastNameProperty.setValue(entry.getLastName());
        }

        /**
         * Builds a new entry object from the model.
         * 
         * @return the new entry object
         */
        public Entry buildEntry() {
            Entry entry = new Entry();

            entry.setId(this.idProperty.getValue());
            entry.setFirstName(this.firstNameProperty.getValue());
            entry.setLastName(this.lastNameProperty.getValue());

            return entry;
        }

        public Long getId() {
            return this.idProperty.getValue();
        }

        public void setId(Long id) {
            this.idProperty.setValue(id);
        }

        public LongProperty idProperty() {
            return this.idProperty;
        }

        public String getFirstName() {
            return this.firstNameProperty.getValue();
        }

        public void setFirstName(String firstName) {
            this.firstNameProperty.setValue(firstName);
        }

        public StringProperty firstNameProperty() {
            return this.firstNameProperty;
        }

        public String getLastName() {
            return this.lastNameProperty.getValue();
        }

        public void setLastName(String lastName) {
            this.lastNameProperty.setValue(lastName);
        }

        public StringProperty lastNameProperty() {
            return this.lastNameProperty;
        }
    }

    /**
     * Model for the request dialog inside the main view. Extends the amount model.
     */
    public static class RequestModel extends AmountModel {

        /**
         * Nothing to do here.
         */
        public RequestModel() {

        }
    }

    /**
     * Model for the deposit dialog inside the main view. Extends the amount model.
     */
    public static class DepositModel extends AmountModel {

        /**
         * Nothing to do here.
         */
        public DepositModel() {

        }
    }

    /**
     * Model for the request and deposit dialog inside the main view. Created to
     * avoid code duplication.
     */
    public static class AmountModel {

        private final DoubleProperty amountProperty = new SimpleDoubleProperty(this, "amount");

        public AmountModel() {
        }

        public Double getAmount() {
            return this.amountProperty.getValue();
        }

        public void setAmount(Double amount) {
            this.amountProperty.setValue(amount);
        }

        public DoubleProperty amountProperty() {
            return this.amountProperty;
        }
    }

    private final IntegerProperty datesetProperty = new SimpleIntegerProperty(this, "dataset");

    /**
     * Nothing to do here.
     */
    public MainModel() {

    }

    public Integer getDataset() {
        return this.datesetProperty.getValue();
    }

    public void setDataset(Integer dataset) {
        this.datesetProperty.setValue(dataset);
    }

    public IntegerProperty datasetProperty() {
        return this.datesetProperty;
    }
}