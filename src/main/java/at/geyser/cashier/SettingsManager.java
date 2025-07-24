package at.geyser.cashier;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * SettingsManager class which is responsible for saving and loading the
 * settings. It also contains the applySettings method which applies the
 * settings to the application.
 */
public class SettingsManager {

    /**
     * Loads the settings from the settings file. If the settings file does not
     * exist the default settings are returned. Uses the JsonMapper class.
     * 
     * @return the loaded settings
     * @throws IOException if the settings file could not be loaded
     */
    private static Settings load(File settingsFile) throws IOException {
        Settings settings = JsonMapper.load(settingsFile, Settings.class);

        return settings;
    }

    /**
     * Saves the settings to the settings file. Uses the JsonMapper class.
     * 
     * @param settings the settings to save
     * @throws IOException if the settings file could not be saved
     */
    private static void save(File settingsFile, Settings settings) throws IOException {
        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
        }

        JsonMapper.save(settingsFile, settings);
    }

    /**
     * Applies the settings to the application.
     * 
     * @param settings the settings to apply
     */
    private static void applySettings(Settings settings) {
        applyLanguage(settings.getLanguage());
        applyStyle(settings.getStyle());
    }

    private static boolean reloadRequired(Settings settings) {
        return languageChanged(settings.getLanguage());
    }

    private static boolean languageChanged(Settings.Language language) {
        return translateLanguage(language) != Locale.getDefault();
    }

    private static void applyLanguage(Settings.Language language) {
        if (languageChanged(language)) {
            App.changeLocale(translateLanguage(language));
        }
    }

    private static void applyStyle(Settings.Style style) {
        App.changeStyle(translateStyle(style));
    }

    private static Locale translateLanguage(Settings.Language language) {
        return switch (language) {
        case SYSTEM -> App.LOCALE;
        case CHINESE -> Locale.CHINESE;
        case ENGLISH -> Locale.ENGLISH;
        case FRENCH -> Locale.FRENCH;
        case GERMAN -> Locale.GERMAN;
        case ITALIAN -> Locale.ITALIAN;
        case JAPANESE -> Locale.JAPANESE;
        case KOREAN -> Locale.KOREAN;
        };
    }

    private static String translateStyle(Settings.Style style) {
        return switch (style) {
        case DEFAULT -> "default";
        case LIGHT -> "light";
        case DARK -> "dark";
        };
    }

    private final File file;

    private Settings settings;

    public SettingsManager() {
        this.file = new File(App.SETTINGS_FILE_PATH);
    }

    public Settings loadSettings() throws IOException {
        return load(this.file);
    }

    public void saveSettings() throws IOException {
        save(this.file, this.settings);
    }

    public boolean reloadRequired() {
        return reloadRequired(this.settings);
    }

    public void applySettings() {
        applySettings(settings);
    }

    public void applyLanguage() {
        applyLanguage(this.settings.getLanguage());
    }

    public void applyStyle() {
        applyStyle(this.settings.getStyle());
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}