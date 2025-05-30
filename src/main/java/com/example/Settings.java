package com.example;

/**
 * Settings class of the application. This is the class which will be saved when
 * saving the settings.
 */
public class Settings {

    // Static is not necessary because enums are static by default

    /**
     * Language enum for the application.
     */
    public static enum Language {
        /**
         * System: The application starts with the default language of the system.
         */
        SYSTEM("System"),
        /**
         * Chinese: The application starts with the Chinese language.
         */
        CHINESE("Chinese (Not supported yet)"),
        /**
         * English: The application starts with the English language.
         */
        ENGLISH("English"),
        /**
         * French: The application starts with the French language.
         */
        FRENCH("French (Not supported yet)"),
        /**
         * German: The application starts with the German language.
         */
        GERMAN("German"),
        /**
         * Italian: The application starts with the Italian language.
         */
        ITALIAN("Italian (Not supported yet)"),
        /**
         * Spanish: The application starts with the Japanese language.
         */
        JAPANESE("Japanese (Not supported yet)"),
        /**
         * Korean: The application starts with the Korean language.
         */
        KOREAN("Korean (Not supported yet)");

        /**
         * The display text of the language.
         */
        private final String text;

        /**
         * Constructor for Language. Sets the display text of the language.
         * 
         * @param text the display text of the language
         */
        private Language(String text) {
            this.text = text;
        }

        /**
         * The toString method of the language.
         * 
         * @return the display text of the language
         */
        @Override
        public String toString() {
            return this.text;
        }
    }

    /**
     * Style enum for the application.
     */
    public static enum Style {
        /**
         * Default: The application starts with the default JavaFX style.
         */
        DEFAULT("Default"),
        /**
         * Light: The application starts with the light style.
         */
        LIGHT("Light"),
        /**
         * Dark: The application starts with the dark style.
         */
        DARK("Dark");

        /**
         * The display text of the style.
         */
        private final String text;

        /**
         * Constructor for Style. Sets the display text of the style.
         * 
         * @param text the display text of the style
         */
        private Style(String text) {
            this.text = text;
        }

        /**
         * The toString method of the style.
         * 
         * @return the display text of the style
         */
        @Override
        public String toString() {
            return this.text;
        }
    }

    /**
     * Startup enum for the application.
     */
    public static enum Startup {
        /**
         * Empty: The application starts completely empty.
         */
        EMPTY("Empty"),
        /**
         * Datasets: The datasets saved in the datasets directory are loaded.
         */
        DATASETS_DIR("Datasets"),
        /**
         * Session: The session file is loaded which contains the files of the last
         * opened datasets.
         */
        SESSION_FILE("Session");

        /**
         * The display text of the startup.
         */
        private final String text;

        /**
         * Constructor for Startup. Sets the display text of the startup.
         * 
         * @param text the display text of the startup
         */
        private Startup(String text) {
            this.text = text;
        }

        /**
         * The toString method of the startup.
         * 
         * @return the display text of the startup
         */
        @Override
        public String toString() {
            return this.text;
        }
    }

    /**
     * Currency enum for the application.
     */
    public static enum Currency {
        /**
         * USD: The application uses USD as currency.
         */
        USD("USD"),
        /**
         * EUR: The application uses EUR as currency.
         */
        EUR("EUR");

        /**
         * The display text of the currency.
         */
        private final String text;

        /**
         * Constructor for Currency. Sets the display text of the currency.
         * 
         * @param text The display text of the currency
         */
        private Currency(String text) {
            this.text = text;
        }

        /**
         * The toString method of the currency.
         * 
         * @return the display text of the currency
         */
        @Override
        public String toString() {
            return this.text;
        }
    }

    /**
     * Language of the application.
     */
    private Language language;

    /**
     * Style of the application.
     */
    private Style style;

    /**
     * Startup of the application.
     */
    private Startup startup;

    /**
     * Currency of the application.
     */
    private Currency currency;

    /**
     * AutoSave of the application.
     */
    private boolean autoSave;

    /**
     * ShowTips of the application.
     */
    private boolean showTips;

    /**
     * Constructor for Settings. Initializes the settings with the default values.
     */
    public Settings() {
        this.language = Language.SYSTEM;
        this.style = Style.DEFAULT;
        this.startup = Startup.EMPTY;
        this.currency = Currency.USD;
        this.autoSave = Boolean.TRUE;
        this.showTips = Boolean.TRUE;
    }

    /**
     * Constructor for Settings. Initializes with the given values.
     * 
     * @param language the language of the applications
     * @param style    the style of the application
     * @param startup  the startup of the application
     * @param currency the currency of the application
     * @param autoSave the autoSave of the application
     * @param showTips the showTips of the application
     */
    public Settings(Language language, Style style, Startup startup, Currency currency, Boolean autoSave, Boolean showTips) {
        this.language = language;
        this.style = style;
        this.startup = startup;
        this.currency = currency;
        this.autoSave = autoSave;
        this.showTips = showTips;
    }

    /**
     * Getter for language.
     * 
     * @return the language of the application
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Setter for language.
     * 
     * @param language the value to set
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Getter for style.
     * 
     * @return the style of the application
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Setter for style.
     * 
     * @param style the value to set
     */
    public void setStyle(Style style) {
        this.style = style;
    }

    /**
     * Getter for startup.
     * 
     * @return the startup of the application
     */
    public Startup getStartup() {
        return startup;
    }

    /**
     * Setter for startup.
     * 
     * @param startup the value to set
     */
    public void setStartup(Startup startup) {
        this.startup = startup;
    }

    /**
     * Getter for currency.
     * 
     * @return the currency of the application
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Setter for currency.
     * 
     * @param currency the value to set
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Getter for showTips.
     * 
     * @return the showTips of the application
     */
    public Boolean getShowTips() {
        return showTips;
    }

    /**
     * Setter for autoSave.
     * 
     * @param autoSave the value to set
     */
    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
    }

    /**
     * Setter for showTips.
     * 
     * @param showTips the value to set
     */
    public void setShowTips(Boolean showTips) {
        this.showTips = showTips;
    }

    /**
     * Getter for autoSave.
     * 
     * @return the autoSave of the application
     */
    public Boolean getAutoSave() {
        return autoSave;
    }
}