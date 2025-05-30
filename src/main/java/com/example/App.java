package com.example;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX App class of the CashierFX application.
 *
 * @author Lorenz Geyser
 * @version 1.0.0
 */
public class App extends Application {

    /**
     * The name of the application.
     */
    public static final String APP_NAME = "CashierFX";

    /**
     * The group id of the application.
     */
    public static final String GROUP_ID = "com.example";

    /**
     * The directory path where all application data is stored.
     */
    public static final String DIR_PATH = System.getProperty("user.home") + "/.geyser/Applications/" + APP_NAME;

    /**
     * The directory path where all datasets are stored by default.
     */
    public static final String DATASETS_DIR_PATH = DIR_PATH + "/Datasets";

    /**
     * The file path where the settings are stored.
     */
    public static final String SETTINGS_FILE_PATH = DIR_PATH + "/settings.json";

    /**
     * The file path where the session is stored.
     */
    public static final String SESSION_FILE_PATH = DIR_PATH + "/session.json";

    /**
     * The default locale of the application. This is the operating system's
     * default.
     */
    public static final Locale LOCALE = Locale.getDefault();

    /**
     * The minimum width of the application.
     */
    private static final int MIN_WIDTH = 640;

    /**
     * The minimum height of the application.
     */
    private static final int MIN_HEIGHT = 480;

    /**
     * The default width of the application.
     */
    private static final int WIDTH = 800;

    /**
     * The default height of the application.
     */
    private static final int HEIGHT = 600;

    /**
     * Stage used by the application.
     */
    private static Stage stage;

    /**
     * Scene used by the application.
     */
    private static Scene scene;

    /**
     * Main method of the application.
     * 
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        // Launches the application
        launch();
    }

    /**
     * Changes the root of the application.
     * 
     * @param fxml the name of the fxml file
     */
    public static void changeRoot(String fxml) throws IOException {
        // Loads the fxml file
        Parent root = loadFXML(fxml);

        // Sets the new root of the scene
        scene.setRoot(root);
    }

    /**
     * Changes the title of the application.
     * 
     * @param title the new title of the application
     */
    public static void changeTitle(String title) {
        stage.setTitle(title);
    }

    /**
     * Changes the locale of the application.
     * 
     * @param locale the new locale of the application
     */
    public static void changeLocale(Locale locale) {
        // Sets the new locale as default
        Locale.setDefault(locale);

        // Only changes the root if the scene is not null (testable)
        if (scene != null) {
            try {
                // Changes the root of the scene
                changeRoot("MainView");
            } catch (IOException exception) {
                // If an error occurs, prints the stack trace and exits the application
                exception.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Changes the style of the application.
     * 
     * @param style the name of the css file
     */
    public static void changeStyle(String style) {
        // Only changes the style if the scene is not null (testable)
        if (scene != null) {
            // Gets the path of the css file
            String styleObject = Objects.requireNonNull(App.class.getResource("styles/" + style + ".css")).toExternalForm();

            // Checks if the style is already added to the scene
            if (!scene.getStylesheets().contains(styleObject)) {
                scene.getStylesheets().clear();

                scene.getStylesheets().add(styleObject);
            }
        }
    }

    /**
     * Loads the fxml file with the given name and setups the controller.
     * 
     * @param fxml the name of the fxml file
     * @return the root node of the fxml file
     * @throws IOException if the fxml file could not be found
     */
    public static Parent loadFXML(String fxml) throws IOException {
        // Loads the fxml and properties file
        ResourceBundle resourceBundle = ResourceBundle.getBundle(GROUP_ID + ".properties." + fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"), resourceBundle);

        // Loads the root node
        Parent parent = fxmlLoader.load();

        // Gets the controller of the fxml file
        Controller controller = fxmlLoader.getController();

        // Calls the init method of the controller
        controller.init();

        // Sets the controller as user data of the root node
        parent.setUserData(controller);

        // Returns the root node
        return parent;
    }

    /**
     * Start method of the application. Setups the stage and loads the MainView.
     * 
     * @param stage the stage of the application
     * @throws IOException if an error occurs
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Sets the stage as static variable because it is needed to change the title
        App.stage = stage;

        // Loads the MainView
        scene = new Scene(loadFXML("MainView"), WIDTH, HEIGHT);

        // Adds the icons to the stage
        stage.getIcons().addAll(
                // 32x32
                new Image((Objects.requireNonNull(App.class.getResource("icons/icon_32x32.png")).toExternalForm())),
                // 64x64
                new Image((Objects.requireNonNull(App.class.getResource("icons/icon_64x64.png")).toExternalForm())),
                // 128x128
                new Image((Objects.requireNonNull(App.class.getResource("icons/icon_128x128.png")).toExternalForm())),
                // 256x256
                new Image((Objects.requireNonNull(App.class.getResource("icons/icon_256x256.png")).toExternalForm())),
                // 512x512
                new Image((Objects.requireNonNull(App.class.getResource("icons/icon_512x512.png")).toExternalForm())));

        // Sets the scene of the stage
        stage.setScene(scene);

        // Sets the title of the stage
        stage.setTitle(APP_NAME);

        // Sets the minimum dimensions
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        // Sets the default dimensions
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);

        // Shows the stage
        stage.show();
    }

    /**
     * Called when the application is stopped. Calls the stop method of the
     * controller of the root node.
     */
    @Override
    public void stop() {
        // Finds the controller of the root node
        MainController controller = (MainController) scene.getRoot().getUserData();

        // Calls the stop method of the controller
        controller.stop();
    }
}