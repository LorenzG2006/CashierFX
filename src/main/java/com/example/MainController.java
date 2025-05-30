package com.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * Controller class for the main view.
 */
public class MainController implements Controller {

    /**
     * ContentAnchorPane of the view.
     */
    @FXML
    private AnchorPane contentAnchorPane;

    /**
     * LoadButton of the view.
     */
    @FXML
    private Button loadButton;

    /**
     * SaveButton of the view.
     */
    @FXML
    private Button saveButton;

    /**
     * SaveAsButton of the view.
     */
    @FXML
    private Button saveAsButton;

    /**
     * PreviousButton of the view.
     */
    @FXML
    private Button previousButton;

    /**
     * NextButton of the view.
     */
    @FXML
    private Button nextButton;

    /**
     * CloseButton of the view.
     */
    @FXML
    private Button closeButton;

    /**
     * DeleteButton of the view.
     */
    @FXML
    private Button deleteButton;

    /**
     * NewButton of the view.
     */
    @FXML
    private Button newButton;

    /**
     * SettingsButton of the view.
     */
    @FXML
    private Button settingsButton;

    /**
     * AuthorizationButton of the view.
     */
    @FXML
    private Button authorizationButton;

    /**
     * AddButton of the view.
     */
    @FXML
    private Button addButton;

    /**
     * EditButton of the view.
     */
    @FXML
    private Button editButton;

    /**
     * RequestButton of the view.
     */
    @FXML
    private Button requestButton;

    /**
     * DepositButton of the view.
     */
    @FXML
    private Button depositButton;

    /**
     * AnalyzeToggleButton of the view.
     */
    @FXML
    private ToggleButton analyzeToggleButton;

    /**
     * DatasetLabel of the view.
     */
    @FXML
    private Label datasetLabel;

    /**
     * DatasetTooltip of the view.
     */
    @FXML
    private Tooltip datasetTooltip;

    /**
     * ResourceBundle for the controller.
     */
    private final ResourceBundle resourceBundle;

    /**
     * SettingsManager for the controller.
     */
    private final SettingsManager settingsManager;

    /**
     * DatasetManager for the controller.
     */
    private final DatasetManager datasetManager;

    /**
     * MainModel for the controller.
     */
    private final MainModel mainModel;

    /**
     * PopOver for the controller.
     */
    private final PopOver popOver;

    /**
     * The table node.
     */
    private Node table;

    /**
     * The chart node.
     */
    private Node chart;

    /**
     * The table controller.
     */
    private TableController tableController;

    /**
     * The chart controller.
     */
    private ChartController chartController;

    /**
     * Constructor for the controller.
     */
    public MainController() {
        this.resourceBundle = ResourceBundle.getBundle(App.GROUP_ID + ".properties.MainController");

        this.settingsManager = new SettingsManager();
        this.datasetManager = new DatasetManager();

        this.mainModel = new MainModel();
        this.popOver = new PopOver();

        this.popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
        this.popOver.setArrowSize(0);
        this.popOver.setCornerRadius(0);
        this.popOver.setHideOnEscape(true);

        try {
            this.table = App.loadFXML("TableView");
            this.chart = App.loadFXML("ChartView");

            this.tableController = (TableController) table.getUserData();
            this.chartController = (ChartController) chart.getUserData();

            AnchorPane.setTopAnchor(table, 0.0d);
            AnchorPane.setBottomAnchor(table, 0.0d);
            AnchorPane.setLeftAnchor(table, 0.0d);
            AnchorPane.setRightAnchor(table, 0.0d);

            AnchorPane.setTopAnchor(chart, 0.0d);
            AnchorPane.setBottomAnchor(chart, 0.0d);
            AnchorPane.setLeftAnchor(chart, 0.0d);
            AnchorPane.setRightAnchor(chart, 0.0d);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void init() {
        // Run later because the scene is not yet created when this method is called
        // Otherwise, it will throw a NullPointerException
        Platform.runLater(() -> {
            startup();

            if (settingsManager.reloadRequired()) {
                settingsManager.applySettings();

                return;
            }

            initListener();

            changeContent(table);
            changeDataset(0);

            showInfoOnStartup();
            showTipsOnStartup();
        });
    }

    public void stop() {
        Settings settings = settingsManager.getSettings();

        if (settings.getAutoSave()) {
            datasetManager.saveAll();
        }

        File sessionFile = new File(App.SESSION_FILE_PATH);

        if (!sessionFile.exists()) {
            sessionFile.getParentFile().mkdirs();
        }

        Session session = datasetManager.buildSession();

        try {
            JsonMapper.save(sessionFile, session);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void startup() {
        // Declares settings here
        Settings settings;

        try {
            // Tries to load settings from the settings file
            settings = settingsManager.loadSettings();
        } catch (IOException exception) {
            // If the settings file does not exist, create new settings
            settings = new Settings();
        }

        // Sets the settings in the settings manager
        settingsManager.setSettings(settings);

        // Declares datasets here
        List<Dataset> datasets = null;

        try {
            // Tries to load datasets depending on the startup setting
            switch (settings.getStartup()) {
            case EMPTY -> throw new IOException();
            case DATASETS_DIR -> datasets = datasetManager.loadDatasetsDir();
            case SESSION_FILE -> datasets = datasetManager.loadSessionFile();
            }
        } catch (IOException exception) {
            datasets = new ArrayList<Dataset>();
        }

        datasetManager.addDatasets(datasets);
    }

    /**
     * Initializes event listeners for the application.
     */
    private void initListener() {
        // One possible way to get the scene in a controller class
        Scene scene = contentAnchorPane.getScene();

        // Gets the root node of the scene
        Node root = scene.getRoot();

        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F1) {
                showTips();
            } else if (event.isShortcutDown() && event.getCode() == KeyCode.Q) {
                // Way faster than System.exit(0);
                // Platform.exit() will also call stop() method of the controller
                // It is the proper way to exit the application
                Platform.exit();
            } else if (event.isShortcutDown() && event.getCode() == KeyCode.L) {
                // Simulates a click on the loadButton
                loadButton.fire();
            } else if (event.isShortcutDown() && event.getCode() == KeyCode.S) {
                // Simulates a click on the saveButton
                saveButton.fire();
            } else if (event.isShortcutDown() && !event.isAltDown() && event.getCode() == KeyCode.P) {
                // Simulates a click on the datasetLabel
                MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null);

                datasetLabel.fireEvent(mouseEvent);
            } else if (event.isShortcutDown() && !event.isAltDown() && event.getCode() == KeyCode.F) {
                // Simulates a click on the datasetLabel
                MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, MouseButton.SECONDARY, 1, false, false, false, false, false, false, false, false, false, false, null);

                datasetLabel.fireEvent(mouseEvent);
            } else if (event.isShortcutDown() && (event.getCode() == KeyCode.LEFT)) {
                // Simulates a click on the previousButton
                previousButton.fire();
            } else if (event.isShortcutDown() && (event.getCode() == KeyCode.RIGHT)) {
                // Simulates a click on the nextButton
                nextButton.fire();
            } else if (event.isShortcutDown() && event.getCode() == KeyCode.MINUS) {
                // Simulates a click on the settingsButton
                settingsButton.fire();
            } else if (event.isShortcutDown() && event.getCode() == KeyCode.COMMA) {
                // Simulates a click on the authorizeButton
                authorizationButton.fire();
            } else if (event.isShortcutDown() && event.getCode() == KeyCode.PERIOD) {
                // Simulates a click on the analyzeToggleButton
                analyzeToggleButton.fire();
            } else if (event.isShortcutDown() && event.isShiftDown() && event.getCode() == KeyCode.C) {
                // Simulates a click on the closeButton
                closeButton.fire();
            } else if (event.isShortcutDown() && event.isShiftDown() && event.getCode() == KeyCode.D) {
                // Simulates a click on the deleteButton
                deleteButton.fire();
            } else if (event.isShortcutDown() && event.isShiftDown() && event.getCode() == KeyCode.N) {
                // Simulates a click on the newButton
                newButton.fire();
            } else if (event.isShortcutDown() && event.isAltDown() && event.getCode() == KeyCode.S) {
                // Simulates a click on the saveAsButton
                saveAsButton.fire();
            } else if (event.isShortcutDown() && event.isAltDown() && event.getCode() == KeyCode.P) {
                // Simulates a click on the datasetLabel
                MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, true, false, false, false, false, false, false, false, false, null);

                datasetLabel.fireEvent(mouseEvent);
            } else if (event.isShortcutDown() && event.isAltDown() && event.getCode() == KeyCode.F) {
                // Simulates a click on the datasetLabel
                MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, MouseButton.SECONDARY, 1, false, true, false, false, false, false, false, false, false, false, null);

                datasetLabel.fireEvent(mouseEvent);
            } else if (event.isShortcutDown() && event.isAltDown() && event.getCode() == KeyCode.A) {
                // Simulates a click on the addButton
                addButton.fire();
            } else if (event.isShortcutDown() && event.isAltDown() && event.getCode() == KeyCode.E) {
                // Simulates a click on the editButton
                editButton.fire();
            } else if (event.isShortcutDown() && event.isAltDown() && event.getCode() == KeyCode.R) {
                // Simulates a click on the requestButton
                requestButton.fire();
            } else if (event.isShortcutDown() && event.isAltDown() && event.getCode() == KeyCode.D) {
                // Simulates a click on the depositButton
                depositButton.fire();
            }
        });

        this.datasetLabel.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Dataset dataset = datasetManager.getCurrentDataset();
                File file = dataset.getFile();

                String path = App.DATASETS_DIR_PATH;

                if (file != null) {
                    if (event.isShortcutDown()) {
                        path = file.getAbsolutePath();
                    } else {
                        path = file.getParentFile().getAbsolutePath();
                    }
                }

                path = path.replace("\\", "/");

                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();

                content.putString(path);

                clipboard.setContent(content);

                // Creates a notification to inform the user that the path has been copied
                Notifications notifications = Notifications.create();

                notifications.hideAfter(Duration.seconds(5d));
                notifications.title(resourceBundle.getString("notifications.title"));
                notifications.text(path);

                // Shows the notification
                notifications.show();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // Checks if the popOver is already showing
                if (popOver.isShowing()) {
                    return;
                }

                Node node;

                if (event.isShortcutDown()) {
                    List<Dataset> datasets = datasetManager.getDatasets();

                    node = createChooseDatasetNode(datasets);
                } else {
                    node = createEnterDatasetNode();
                }

                popOver.setContentNode(node);

                popOver.show(datasetLabel);

                datasetLabel.requestFocus();
            }
        });

        this.tableController.getTableView().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editButton.fire();
            } else if (event.getCode() == KeyCode.DELETE) {
                Dataset dataset = datasetManager.getCurrentDataset();

                boolean allowedToMakeChanges = allowedToMakeChanges();

                if (!allowedToMakeChanges) {
                    return;
                }

                List<Entry> entries = tableController.findSelectedEntries();

                if (entries.isEmpty()) {
                    return;
                }

                int index = tableController.getTableView().getSelectionModel().getSelectedIndices().get(0);

                for (Entry entry : entries) {
                    dataset.removeEntry(entry);
                }

                modified();

                refreshTable();

                index = index == 0 ? index : index - 1;

                tableController.getTableView().getSelectionModel().select(index);
            }
        });
    }

    private void changeContent(Node node) {
        contentAnchorPane.getChildren().clear();
        contentAnchorPane.getChildren().setAll(node);
    }

    private void changeDataset(int index) {
        int size = datasetManager.findSizeOfDatasets();

        // If the application starts with no dataset, create a new one
        // If the user deletes the last dataset, create a new one
        // Also index == size would be also an option instead of size == 0
        if (size == 0 || index == -1) {
            newButton.fire();

            return;
        }

        if (index >= size) {
            index = size - 1;
        }

        previousButton.setDisable(index == 0);
        nextButton.setDisable(index == size - 1);

        String text = index + 1 + "/" + size;

        datasetLabel.setText(text);

        Dataset dataset = datasetManager.getDatasets().get(index);

        datasetManager.setCurrentDataset(dataset);

        refreshTable();
        refreshChart();

        refreshTitle();
        refreshAuthorization();

        if (!dataset.getVerified()) {
            showWarning();
        }
    }

    private void modified() {
        Dataset dataset = datasetManager.getCurrentDataset();

        if (!dataset.getModified()) {
            dataset.setModified(true);
            refreshTitle();
        }
    }

    private void refreshTable() {
        Dataset dataset = datasetManager.getCurrentDataset();

        tableController.refresh(dataset);
    }

    private void refreshChart() {
        Dataset dataset = datasetManager.getCurrentDataset();

        chartController.refresh(dataset);
    }

    private void refreshTitle() {
        Dataset dataset = datasetManager.getCurrentDataset();
        File file = dataset.getFile();

        String datasetName;

        if (file != null) {
            String path = file.getAbsolutePath();

            if (path.length() > 50) {
                path = path.substring(0, 50) + "...";
            }

            datasetTooltip.setText(path);
            datasetName = file.getName();
        } else {
            datasetTooltip.setText(resourceBundle.getString("untitled.text"));
            datasetName = resourceBundle.getString("untitled.text");
        }

        // If the dataset has been modified, add a "*" to the title
        String extra = dataset.getModified() ? "*" : "";

        // If the dataset has not been verified, add a "!" to the title
        extra += dataset.getVerified() ? "" : "!";

        // Builds the title
        String title = App.APP_NAME + " - " + datasetName + extra;

        // Changes the title
        App.changeTitle(title);
    }

    private void refreshAuthorization() {
        Dataset dataset = datasetManager.getCurrentDataset();

        String text;

        if (!dataset.containsAuthorizations()) {
            text = resourceBundle.getString("authorizeButton.createAuthorization.text");
        } else if (!dataset.getAuthorized()) {
            text = resourceBundle.getString("authorizeButton.authorize.text");
        } else {
            text = resourceBundle.getString("authorizeButton.changeAuthorization.text");
        }

        authorizationButton.setText(text);

        checkAllowedToMakeChanges();
    }

    private void showInfoOnStartup() {
        File file = new File(App.DIR_PATH);

        if (!file.exists()) {
            showInfo();
        }
    }

    private void showTipsOnStartup() {
        if (settingsManager.getSettings().getShowTips()) {
            showTips();
        }
    }

    private void showInfo() {
        ActionEvent actionEvent = createActionEvent();

        Alert alert = createAlert(AlertType.INFORMATION, actionEvent, App.APP_NAME);

        Info info = new Info();

        alert.setHeaderText(info.getHeader());
        alert.setContentText(info.getContent());

        alert.showAndWait();
    }

    private void showTips() {
        ActionEvent actionEvent = createActionEvent();

        Alert alert = createAlert(AlertType.INFORMATION, actionEvent, App.APP_NAME);

        ButtonType okButtonType = alert.getButtonTypes().get(0);

        alert.getButtonTypes().remove(okButtonType);

        ButtonType finishButtonType = ButtonType.FINISH;

        ButtonType previousButtonType = ButtonType.PREVIOUS;
        ButtonType nextButtonType = ButtonType.NEXT;

        alert.getButtonTypes().add(finishButtonType);

        alert.getButtonTypes().add(previousButtonType);
        alert.getButtonTypes().add(nextButtonType);

        Window window = alert.getDialogPane().getScene().getWindow();

        window.setOnCloseRequest(event -> {
            alert.resultProperty().setValue(finishButtonType);
        });

        window.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                alert.resultProperty().setValue(finishButtonType);
            }
        });

        Node previousButton = alert.getDialogPane().lookupButton(previousButtonType);
        Node nextButton = alert.getDialogPane().lookupButton(nextButtonType);

        Tips tips = new Tips();

        int size = tips.size();

        int index = 0;

        do {
            previousButton.setDisable(index == 0);
            nextButton.setDisable(index == size - 1);

            String header = tips.findHeader(index);
            String content = tips.findContent(index);

            alert.setTitle(App.APP_NAME + " - " + (index + 1) + "/" + size);

            alert.setHeaderText(header);
            alert.setContentText(content);

            DialogPane dialogPane = alert.getDialogPane();

            dialogPane.setMinHeight(Region.USE_PREF_SIZE);

            alert.showAndWait();

            switch (alert.getResult().getButtonData()) {
            case BACK_PREVIOUS -> index--;
            case NEXT_FORWARD -> index++;
            default -> index = -1;
            }
        } while (index != -1 && index < size);
    }

    private void showWarning() {
        ActionEvent actionEvent = createActionEvent();

        Alert alert = createAlert(AlertType.WARNING, actionEvent, App.APP_NAME);

        alert.setHeaderText(resourceBundle.getString("unverifiedWarningAlert.header"));
        alert.setContentText(resourceBundle.getString("unverifiedWarningAlert.content"));

        alert.showAndWait();
    }

    private void checkAllowedToMakeChanges() {
        boolean allowedToMakeChanges = allowedToMakeChanges();

        addButton.setDisable(!allowedToMakeChanges);
        editButton.setDisable(!allowedToMakeChanges);
        requestButton.setDisable(!allowedToMakeChanges);
        depositButton.setDisable(!allowedToMakeChanges);
    }

    private boolean allowedToMakeChanges() {
        Dataset dataset = datasetManager.getCurrentDataset();

        boolean allowedToMakeChanges = dataset.permission() && !analyzeToggleButton.isSelected();

        return allowedToMakeChanges;
    }

    @FXML
    private void onLoadAction(ActionEvent event) {
        Dataset currentDataset = datasetManager.getCurrentDataset();

        File currentFile = currentDataset.getFile();

        FileChooser fileChooser = createFileChooser(resourceBundle.getString("loadFileChooser.title"), currentFile);

        File file = fileChooser.showOpenDialog(findParentWindow(event));

        if (file != null) {
            try {
                Dataset dataset = datasetManager.loadDataset(file);

                datasetManager.addDataset(dataset);

                int index = datasetManager.findIndexOfZeroDataset();

                if (index != -1) {
                    Dataset zeroDataset = datasetManager.getDatasets().get(index);

                    datasetManager.removeDataset(zeroDataset);
                }

                changeDataset(datasetManager.findSizeOfDatasets() - 1);
            } catch (FileIsLoadedException exception) {
                changeDataset(exception.getIndex());
            } catch (IOException exception) {
                Alert alert = createAlert(AlertType.ERROR, event, App.APP_NAME);

                alert.setHeaderText(resourceBundle.getString("loadDatasetErrorAlert.header"));
                alert.setContentText(resourceBundle.getString("loadDatasetErrorAlert.content") + " " + exception.getLocalizedMessage());

                alert.getDialogPane().setExpandableContent(createExceptionNode(exception));

                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onSaveAction(ActionEvent event) {
        try {
            datasetManager.saveDataset();

            refreshTitle();
        } catch (FileIsNullException exception) {
            saveAsButton.fire();
        } catch (IOException exception) {
            Alert alert = createAlert(AlertType.ERROR, event, App.APP_NAME);

            alert.setHeaderText(resourceBundle.getString("saveDatasetErrorAlert.header"));
            alert.setContentText(resourceBundle.getString("saveDatasetErrorAlert.content") + " " + exception.getLocalizedMessage());

            alert.getDialogPane().setExpandableContent(createExceptionNode(exception));

            alert.showAndWait();
        }
    }

    @FXML
    private void onSaveAsAction(ActionEvent event) {
        Dataset currentDataset = datasetManager.getCurrentDataset();

        File currentFile = currentDataset.getFile();

        FileChooser fileChooser = createFileChooser(resourceBundle.getString("saveFileChooser.title"), currentFile);

        File file = fileChooser.showSaveDialog(findParentWindow(event));

        if (file != null) {
            if (file.equals(currentFile)) {
                return;
            }

            if (datasetManager.containsFile(file)) {
                Alert alert = createWarningAlert(event);

                alert.setHeaderText(resourceBundle.getString("overwriteWarningAlert.header"));
                alert.setContentText(resourceBundle.getString("overwriteWarningAlert.content"));

                alert.showAndWait();

                if (alert.getResult().getButtonData() == ButtonData.NO) {
                    return;
                }
            }

            currentDataset.setFile(file);

            saveButton.fire();
        }
    }

    @FXML
    private void onPreviousAction(ActionEvent event) {
        int index = datasetManager.findIndexOfCurrentDataset();

        index -= 1;

        changeDataset(index);

        if (index == 0) {
            nextButton.requestFocus();
        }
    }

    @FXML
    private void onNextAction(ActionEvent event) {
        int index = datasetManager.findIndexOfCurrentDataset();

        index += 1;

        changeDataset(index);

        int size = datasetManager.findSizeOfDatasets();

        if (index == size - 1) {
            previousButton.requestFocus();
        }
    }

    @FXML
    private void onCloseAction(ActionEvent event) {
        Dataset dataset = datasetManager.getCurrentDataset();

        if (dataset.getModified()) {
            Alert alert = createAlert(AlertType.CONFIRMATION, event, App.APP_NAME);

            alert.setHeaderText(resourceBundle.getString("closeDatasetConfirmationAlert.header"));
            alert.setContentText(resourceBundle.getString("closeDatasetConfirmationAlert.content"));

            alert.showAndWait();

            if (alert.getResult().getButtonData() == ButtonData.CANCEL_CLOSE) {
                return;
            }
        }

        int index = datasetManager.findIndexOfCurrentDataset();

        datasetManager.removeDataset(dataset);

        changeDataset(datasetManager.findSizeOfDatasets() > index ? index : index - 1);
    }

    @FXML
    private void onDeleteAction(ActionEvent event) {
        Dataset dataset = datasetManager.getCurrentDataset();

        Alert alert = createAlert(AlertType.CONFIRMATION, event, App.APP_NAME);

        alert.setHeaderText(resourceBundle.getString("deleteDatasetConfirmationAlert.header"));
        alert.setContentText(resourceBundle.getString("deleteDatasetConfirmationAlert.content"));

        alert.showAndWait();

        if (alert.getResult().getButtonData() == ButtonData.CANCEL_CLOSE) {
            return;
        }

        File file = dataset.getFile();

        if (file != null && file.exists()) {
            file.delete();
        }

        int index = datasetManager.findIndexOfCurrentDataset();

        datasetManager.removeDataset(dataset);

        changeDataset(datasetManager.findSizeOfDatasets() > index ? index : index - 1);
    }

    @FXML
    private void onNewAction(ActionEvent event) {
        int index = datasetManager.findIndexOfZeroDataset();

        if (analyzeToggleButton.isSelected()) {
            analyzeToggleButton.fire();
        }

        if (index == -1) {
            Dataset dataset = new Dataset();

            datasetManager.addDataset(dataset);

            changeDataset(datasetManager.findSizeOfDatasets() - 1);
        } else {
            changeDataset(index);
        }
    }

    @FXML
    private void onSettingsAction(ActionEvent event) {
        MainModel.SettingsModel settingsModel = new MainModel.SettingsModel(settingsManager.getSettings());

        Dialog<ButtonType> dialog = createDialog(event, resourceBundle.getString("settingsDialog.title"));

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.getDialogPane().setContent(createSettingsNode(settingsModel));

        dialog.showAndWait();

        Settings settings = settingsModel.buildSettings();

        settingsManager.setSettings(settings);

        try {
            settingsManager.saveSettings();
        } catch (IOException exception) {
            Alert alert = createAlert(AlertType.ERROR, event, App.APP_NAME);

            alert.setHeaderText(resourceBundle.getString("saveSettingsErrorAlert.header"));
            alert.setContentText(resourceBundle.getString("saveSettingsErrorAlert.content") + " " + exception.getLocalizedMessage());

            alert.getDialogPane().setExpandableContent(createExceptionNode(exception));

            alert.showAndWait();
        }

        if (settingsManager.reloadRequired()) {
            Alert alert = createAlert(AlertType.CONFIRMATION, event, App.APP_NAME);

            alert.setHeaderText(resourceBundle.getString("settingsConfirmationAlert.header"));
            alert.setContentText(resourceBundle.getString("settingsConfirmationAlert.content"));

            alert.showAndWait();

            if (alert.getResult().getButtonData() == ButtonData.CANCEL_CLOSE) {
                return;
            }
        }

        settingsManager.applySettings();
    }

    @FXML
    private void onAuthorizationAction(ActionEvent event) {
        Dataset dataset = datasetManager.getCurrentDataset();

        boolean containsAuthorizations = dataset.containsAuthorizations();

        if (!containsAuthorizations) {
            createAuthorization(event);
        } else if (!dataset.getAuthorized()) {
            authorize(event);
        } else {
            changeAuthorization(event);
        }
    }

    private void createAuthorization(ActionEvent event) {
        Dataset dataset = datasetManager.getCurrentDataset();

        Map<String, String> authorizations = dataset.getAuthorizations();

        MainModel.CreateAuthorizationModel createAuthorizationModel = new MainModel.CreateAuthorizationModel();

        Dialog<ButtonType> dialog = createDialog(event, resourceBundle.getString("createAuthorizationDialog.title"));

        ButtonType okButtonType = ButtonType.OK;
        ButtonType closeButtonType = ButtonType.CLOSE;

        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        Node createAuthorizationNode = createCreateAuthorizationNode(createAuthorizationModel);

        dialog.getDialogPane().setContent(createAuthorizationNode);

        dialog.showAndWait();

        if (dialog.getResult().getButtonData() == ButtonData.OK_DONE) {
            authorizations = createAuthorizationModel.buildAuthorizations();

            // Creates a new map with hashed values authorizations
            Map<String, String> hashedAuthorizations = dataset.hashAuthorizations(authorizations);

            dataset.setAuthorizations(hashedAuthorizations);

            boolean authorized = dataset.containsAuthorizations();

            if (authorized) {
                dataset.setAuthorized(true);

                refreshAuthorization();

                modified();
            }

            Alert alert = createAlert(AlertType.INFORMATION, event, App.APP_NAME);

            if (authorized) {
                alert.setHeaderText(resourceBundle.getString("authorizationsAddedInformationAlert.header"));
                alert.setContentText(resourceBundle.getString("authorizationsAddedInformationAlert.content"));
            } else {
                alert.setHeaderText(resourceBundle.getString("noAuthorizationsAddedInformationAlert.header"));
                alert.setContentText(resourceBundle.getString("noAuthorizationsAddedInformationAlert.content"));
            }

            alert.showAndWait();
        }
    }

    private void authorize(ActionEvent event) {
        Dataset dataset = datasetManager.getCurrentDataset();

        Map<String, String> authorizations = dataset.getAuthorizations();

        MainModel.AuthorizeModel authorizeModel = new MainModel.AuthorizeModel(authorizations);

        Dialog<ButtonType> dialog = createDialog(event, resourceBundle.getString("authorizeDialog.title"));

        ButtonType okButtonType = ButtonType.OK;
        ButtonType closeButtonType = ButtonType.CLOSE;

        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        Node authorizeNode = createAuthorizeNode(authorizeModel);

        dialog.getDialogPane().setContent(authorizeNode);

        dialog.showAndWait();

        if (dialog.getResult().getButtonData() == ButtonData.OK_DONE) {
            authorizations = authorizeModel.getAuthorizations();

            boolean authorized = true;

            for (Map.Entry<String, String> entry : authorizations.entrySet()) {
                if (!dataset.checkAuthorization(entry)) {
                    authorized = false;
                    break;
                }
            }

            if (authorized) {
                dataset.setAuthorized(true);

                refreshAuthorization();

                if (!dataset.getVerified()) {
                    modified();
                }
            }

            Alert alert = createAlert(AlertType.INFORMATION, event, App.APP_NAME);

            if (authorized) {
                alert.setHeaderText(resourceBundle.getString("authorizationSuccessfulInformationAlert.header"));
                alert.setContentText(resourceBundle.getString("authorizationSuccessfulInformationAlert.content"));
            } else {
                alert.setHeaderText(resourceBundle.getString("authorizationFailedInformationAlert.header"));
                alert.setContentText(resourceBundle.getString("authorizationFailedInformationAlert.content"));
            }

            alert.showAndWait();
        }
    }

    private void changeAuthorization(ActionEvent event) {
        Dataset dataset = datasetManager.getCurrentDataset();

        Map<String, String> authorizations = dataset.getAuthorizations();

        MainModel.ChangeAuthorizationModel changeAuthorizationModel = new MainModel.ChangeAuthorizationModel(authorizations);

        Dialog<ButtonType> dialog = createDialog(event, resourceBundle.getString("changeAuthorizationDialog.title"));

        ButtonType okButtonType = ButtonType.OK;
        ButtonType closeButtonType = ButtonType.CLOSE;

        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        Node changeAuthorization = createChangeAuthorizationNode(changeAuthorizationModel);

        dialog.getDialogPane().setContent(changeAuthorization);

        dialog.showAndWait();

        if (dialog.getResult().getButtonData() == ButtonData.OK_DONE) {
            authorizations = changeAuthorizationModel.buildAuthorizations();

            // Creates a new map with hashed values authorizations
            Map<String, String> hashedAuthorizations = dataset.hashAuthorizations(authorizations);

            dataset.setAuthorizations(hashedAuthorizations);

            boolean authorized = dataset.containsAuthorizations();

            dataset.setAuthorized(authorized);

            refreshAuthorization();

            modified();

            Alert alert = createAlert(AlertType.INFORMATION, event, App.APP_NAME);

            if (authorized) {
                alert.setHeaderText(resourceBundle.getString("authorizationsChangedInformationAlert.header"));
                alert.setContentText(resourceBundle.getString("authorizationsChangedInformationAlert.content"));
            } else {
                alert.setHeaderText(resourceBundle.getString("authorizationsRemovedInformationAlert.header"));
                alert.setContentText(resourceBundle.getString("authorizationsRemovedInformationAlert.content"));
            }

            alert.showAndWait();
        }
    }

    /**
     * Method that is called when the analyze toggle button is clicked. Switches
     * between the table and the chart.
     *
     * @param event the ActionEvent that triggered the method
     */
    @FXML
    private void onAnalyzeAction(ActionEvent event) {
        boolean selected = analyzeToggleButton.isSelected();

        if (selected) {
            changeContent(chart);

            refreshChart();
        } else {
            changeContent(table);

            // Not necessary because no data is changed
            refreshTable();
        }

        checkAllowedToMakeChanges();
    }

    /**
     * Method that is called when the add button is clicked Shows a dialog where the
     * user can add a new entry.
     *
     * @param event the ActionEvent that triggered the method
     */
    @FXML
    private void onAddAction(ActionEvent event) {
        // Creates ValidationSupport and AddModel for the dialog
        ValidationSupport validationSupport = new ValidationSupport();
        MainModel.AddModel addModel = new MainModel.AddModel();

        // Creates ButtonTypes for the dialog
        ButtonType okButtonType = ButtonType.OK;
        ButtonType closeButtonType = ButtonType.CLOSE;

        // Creates the dialog with the ActionEvent and the title from the resource bundle
        Dialog<ButtonType> dialog = createDialog(event, resourceBundle.getString("addDialog.title"));

        // Adds the ButtonTypes to the dialog
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        // Sets the content of the dialog to the node returned by createAddNode
        Node addNode = createAddNode(validationSupport, addModel);

        dialog.getDialogPane().setContent(addNode);

        // Gets the OK Button from the dialog
        Node node = dialog.getDialogPane().lookupButton(okButtonType);

        // Disables the OK Button if the ValidationSupport is invalid
        node.disableProperty().bind(validationSupport.invalidProperty());

        // Shows the dialog and wait for the result
        dialog.showAndWait();

        // Checks the result of the dialog
        if (dialog.getResult().getButtonData() == ButtonData.OK_DONE) {
            Dataset dataset = datasetManager.getCurrentDataset();

            Entry newEntry = addModel.buildEntry();

            for (Entry entry : dataset.getEntries()) {
                if (newEntry.getId() == entry.getId()) {
                    Alert alert = createIDWarningAlert(event);

                    alert.showAndWait();

                    if (alert.getResult().getButtonData() == ButtonData.NO) {
                        return;
                    } else {
                        break;
                    }
                }
            }

            dataset.addEntry(newEntry);

            modified();

            refreshTable();

            tableController.getTableView().getSelectionModel().select(newEntry);
        }
    }

    @FXML
    private void onEditAction(ActionEvent event) {
        List<Entry> selectedEntries = tableController.findSelectedEntries();

        point: for (Entry selectedEntry : selectedEntries) {
            // Creates ValidationSupport and EditModel for the dialog
            ValidationSupport validationSupport = new ValidationSupport();
            MainModel.EditModel editModel = new MainModel.EditModel(selectedEntry);

            Dialog<ButtonType> dialog = createDialog(event, resourceBundle.getString("editDialog.title"));

            // Creates ButtonTypes for the dialog
            ButtonType okButtonType = ButtonType.OK;
            ButtonType closeButtonType = ButtonType.CLOSE;

            // Adds the ButtonTypes to the dialog
            dialog.getDialogPane().getButtonTypes().add(okButtonType);
            dialog.getDialogPane().getButtonTypes().add(closeButtonType);

            Node editNode = createEditNode(validationSupport, editModel);

            dialog.getDialogPane().setContent(editNode);

            // Gets the OK Button from the dialog
            Node node = dialog.getDialogPane().lookupButton(okButtonType);

            // Disables the OK Button if the ValidationSupport is invalid
            node.disableProperty().bind(validationSupport.invalidProperty());

            // Shows the dialog and wait for the result
            dialog.showAndWait();

            if (dialog.getResult().getButtonData() == ButtonData.OK_DONE) {
                Dataset dataset = datasetManager.getCurrentDataset();

                Entry newEntry = editModel.buildEntry();

                for (Entry entry : dataset.getEntries()) {
                    if (newEntry.getId() == entry.getId() && entry != selectedEntry) {
                        Alert alert = createIDWarningAlert(event);

                        alert.showAndWait();

                        if (alert.getResult().getButtonData() == ButtonData.NO) {
                            continue point;
                        } else {
                            break;
                        }
                    }
                }

                if (selectedEntry.equals(newEntry)) {
                    continue;
                }

                modified();

                dataset.removeEntry(selectedEntry);

                dataset.addEntry(newEntry);
            }
        }

        refreshTable();

        for (Entry entry : selectedEntries) {
            tableController.getTableView().getSelectionModel().select(entry);
        }
    }

    @FXML
    private void onRequestAction(ActionEvent event) {
        List<Entry> entries = tableController.findSelectedEntries();

        if (entries.isEmpty()) {
            return;
        }

        ValidationSupport validationSupport = new ValidationSupport();
        MainModel.RequestModel requestModel = new MainModel.RequestModel();

        ButtonType okButtonType = ButtonType.OK;
        ButtonType closeButtonType = ButtonType.CLOSE;

        Dialog<ButtonType> dialog = createDialog(event, resourceBundle.getString("requestDialog.title"));

        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        dialog.getDialogPane().setContent(createRequestNode(validationSupport, requestModel));

        Node node = dialog.getDialogPane().lookupButton(okButtonType);

        node.disableProperty().bind(validationSupport.invalidProperty());

        dialog.showAndWait();

        if (dialog.getResult().getButtonData() == ButtonData.OK_DONE) {
            if (requestModel.getAmount() == 0d) {
                return;
            }

            for (Entry entry : entries) {
                entry.request(requestModel.getAmount());
            }

            modified();

            refreshTable();

            for (Entry entry : entries) {
                tableController.getTableView().getSelectionModel().select(entry);
            }
        }
    }

    @FXML
    private void onDepositAction(ActionEvent event) {
        List<Entry> entries = tableController.findSelectedEntries();

        if (entries.isEmpty()) {
            return;
        }

        ValidationSupport validationSupport = new ValidationSupport();
        MainModel.DepositModel depositModel = new MainModel.DepositModel();

        ButtonType okButtonType = ButtonType.OK;
        ButtonType closeButtonType = ButtonType.CLOSE;

        Dialog<ButtonType> dialog = createDialog(event, resourceBundle.getString("depositDialog.title"));

        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        dialog.getDialogPane().setContent(createDepositNode(validationSupport, depositModel));

        Node node = dialog.getDialogPane().lookupButton(okButtonType);

        node.disableProperty().bind(validationSupport.invalidProperty());

        dialog.showAndWait();

        if (dialog.getResult().getButtonData() == ButtonData.OK_DONE) {
            if (depositModel.getAmount() == 0d) {
                return;
            }

            for (Entry entry : entries) {
                entry.deposit(depositModel.getAmount());
            }

            modified();

            refreshTable();

            for (Entry entry : entries) {
                tableController.getTableView().getSelectionModel().select(entry);
            }
        }
    }

    private ActionEvent createActionEvent() {
        Object source = new Object();

        Node root = contentAnchorPane.getParent();

        ActionEvent actionEvent = new ActionEvent(source, root);

        return actionEvent;
    }

    private FileChooser createFileChooser(String title, File file) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(title);

        File initialDirectory = new File(App.DATASETS_DIR_PATH);
        String initialFileName = resourceBundle.getString("untitled.text");

        if (file != null) {
            initialDirectory = new File(file.getParent());
            initialFileName = file.getName();
        }

        if (!initialDirectory.exists()) {
            initialDirectory.mkdirs();
        }

        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setInitialFileName(initialFileName);

        ExtensionFilter jsonExtensionFilter = new ExtensionFilter("JSON", "*.json");

        fileChooser.getExtensionFilters().add(jsonExtensionFilter);

        return fileChooser;
    }

    private Dialog<ButtonType> createDialog(ActionEvent event, String title) {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();

        dialog.initOwner(findParentWindow(event));
        dialog.setTitle(title);

        return dialog;
    }

    private Alert createAlert(AlertType alertType, ActionEvent event, String title) {
        Alert alert = new Alert(alertType);

        alert.initOwner(findParentWindow(event));
        alert.setTitle(title);

        return alert;
    }

    private Alert createWarningAlert(ActionEvent event) {
        Alert alert = createAlert(AlertType.WARNING, event, App.APP_NAME);

        ButtonType okButtonType = alert.getButtonTypes().get(0);

        alert.getButtonTypes().remove(okButtonType);

        ButtonType yesButtonType = ButtonType.YES;
        ButtonType noButtonType = ButtonType.NO;

        alert.getButtonTypes().add(yesButtonType);
        alert.getButtonTypes().add(noButtonType);

        return alert;
    }

    private Alert createIDWarningAlert(ActionEvent event) {
        Alert alert = createWarningAlert(event);

        alert.setHeaderText(resourceBundle.getString("idWarningAlert.header"));
        alert.setContentText(resourceBundle.getString("idWarningAlert.content"));

        return alert;
    }

    private Node createExceptionNode(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        exception.printStackTrace(printWriter);

        String exceptionText = stringWriter.toString();

        GridPane gridPane = new GridPane();

        Label exceptionLabel = new Label(resourceBundle.getString("exceptionLabel.text"));
        TextArea textArea = new TextArea(exceptionText);

        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        gridPane.add(exceptionLabel, 0, 0);
        gridPane.add(textArea, 0, 1);

        return gridPane;
    }

    private Node createChooseDatasetNode(List<Dataset> datasets) {
        GridPane gridPane = new GridPane();

        ListView<Label> listView = new ListView<Label>();

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            int index = listView.getSelectionModel().getSelectedIndex() + 1;

            mainModel.setDataset(index);
        });

        gridPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int index = mainModel.getDataset() - 1;

                changeDataset(index);

                popOver.hide();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                popOver.hide();
            }
        });

        gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() != 2) {
                    return;
                }

                int index = mainModel.getDataset() - 1;

                changeDataset(index);

                popOver.hide();
            }
        });

        for (Dataset dataset : datasets) {
            File file = dataset.getFile();

            String datasetName;

            if (file != null) {
                datasetName = file.getName();
            } else {
                datasetName = resourceBundle.getString("untitled.text");
            }

            Label label = new Label(datasetName);

            listView.getItems().add(label);
        }

        gridPane.add(listView, 0, 0);

        int index = datasetManager.findIndexOfCurrentDataset();

        mainModel.setDataset(index + 1);

        listView.getSelectionModel().select(mainModel.getDataset() - 1);

        return gridPane;
    }

    private Node createEnterDatasetNode() {
        GridPane gridPane = new GridPane();

        gridPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int index = mainModel.getDataset() - 1;

                changeDataset(index);

                popOver.hide();
            }
        });

        IntegerField textField = new IntegerField();

        textField.valueProperty().bindBidirectional(mainModel.datasetProperty());

        gridPane.add(textField, 0, 0);

        int index = datasetManager.findIndexOfCurrentDataset();

        mainModel.setDataset(index + 1);

        return gridPane;
    }

    /**
     * Create the Node for the Settings Dialog.
     *
     * @param settingsModel the Model for the Settings Dialog
     * @return the Node that is used as the content of the dialog created in the
     *         onSettingsAction method
     */
    private Node createSettingsNode(MainModel.SettingsModel settingsModel) {
        GridPane gridPane = new GridPane();

        PropertySheet propertySheet = new PropertySheet();

        String[] languageProperties = new String[3];
        String[] styleProperties = new String[3];
        String[] startupProperties = new String[3];
        String[] currencyProperties = new String[3];
        String[] autoSaveProperties = new String[3];
        String[] showTipsProperties = new String[3];

        languageProperties[0] = resourceBundle.getString("languageProperty.name");
        languageProperties[1] = resourceBundle.getString("languageProperty.category");
        languageProperties[2] = resourceBundle.getString("languageProperty.description");

        styleProperties[0] = resourceBundle.getString("styleProperty.name");
        styleProperties[1] = resourceBundle.getString("styleProperty.category");
        styleProperties[2] = resourceBundle.getString("styleProperty.description");

        startupProperties[0] = resourceBundle.getString("startupProperty.name");
        startupProperties[1] = resourceBundle.getString("startupProperty.category");
        startupProperties[2] = resourceBundle.getString("startupProperty.description");

        currencyProperties[0] = resourceBundle.getString("currencyProperty.name");
        currencyProperties[1] = resourceBundle.getString("currencyProperty.category");
        currencyProperties[2] = resourceBundle.getString("currencyProperty.description");

        autoSaveProperties[0] = resourceBundle.getString("autoSaveProperty.name");
        autoSaveProperties[1] = resourceBundle.getString("autoSaveProperty.category");
        autoSaveProperties[2] = resourceBundle.getString("autoSaveProperty.description");

        showTipsProperties[0] = resourceBundle.getString("showTipsProperty.name");
        showTipsProperties[1] = resourceBundle.getString("showTipsProperty.category");
        showTipsProperties[2] = resourceBundle.getString("showTipsProperty.description");

        PropertyItem<Settings.Language> languagePropertyItem = new PropertyItem<Settings.Language>(languageProperties, Settings.Language.class, settingsModel.languageProperty());
        PropertyItem<Settings.Style> stylePropertyItem = new PropertyItem<Settings.Style>(styleProperties, Settings.Style.class, settingsModel.styleProperty());
        PropertyItem<Settings.Startup> startupPropertyItem = new PropertyItem<Settings.Startup>(startupProperties, Settings.Startup.class, settingsModel.startupProperty());
        PropertyItem<Settings.Currency> currencyPropertyItem = new PropertyItem<Settings.Currency>(currencyProperties, Settings.Currency.class, settingsModel.currencyProperty());
        PropertyItem<Boolean> autoSavePropertyItem = new PropertyItem<Boolean>(autoSaveProperties, Boolean.class, settingsModel.autoSaveProperty());
        PropertyItem<Boolean> showTipsPropertyItem = new PropertyItem<Boolean>(showTipsProperties, Boolean.class, settingsModel.showTipsProperty());

        propertySheet.getItems().add(languagePropertyItem);
        propertySheet.getItems().add(stylePropertyItem);
        propertySheet.getItems().add(startupPropertyItem);
        propertySheet.getItems().add(currencyPropertyItem);
        propertySheet.getItems().add(autoSavePropertyItem);
        propertySheet.getItems().add(showTipsPropertyItem);

        propertySheet.setPrefSize(320, 240);

        gridPane.add(propertySheet, 0, 0);

        gridPane.setPadding(Insets.EMPTY);

        return gridPane;
    }

    private Node createCreateAuthorizationNode(MainModel.CreateAuthorizationModel createAuthorizationModel) {
        GridPane gridPane = (GridPane) createExtendedAuthorizationNode(createAuthorizationModel);

        ScrollPane scrollPane = (ScrollPane) gridPane.getChildren().get(0);

        VBox vBox = (VBox) scrollPane.getContent();

        plusAuthorization(vBox, createAuthorizationModel);

        return gridPane;
    }

    private Node createAuthorizeNode(MainModel.AuthorizeModel authorizeModel) {
        GridPane gridPane = (GridPane) createAuthorizationNode();

        ScrollPane scrollPane = (ScrollPane) gridPane.getChildren().get(0);
        VBox vBox = (VBox) scrollPane.getContent();

        Map<StringProperty, StringProperty> authorizationProperties = authorizeModel.authorizationProperties();

        for (StringProperty keyProperty : authorizationProperties.keySet()) {
            StringProperty valueProperty = authorizationProperties.get(keyProperty);

            HBox hBox = createAuthorizationHBox(keyProperty, valueProperty);

            TextField textField = (TextField) hBox.getChildren().get(0);

            textField.setEditable(false);
            textField.setFocusTraversable(false);

            Button resetAuthorizationButton = createResetAuthorizationButton();

            resetAuthorizationButton.setOnAction(event -> {
                valueProperty.setValue("");
            });

            resetAuthorizationButton.fire();

            hBox.getChildren().add(resetAuthorizationButton);

            vBox.getChildren().add(hBox);
        }

        Platform.runLater(() -> {
            HBox authorizationHBox = (HBox) vBox.getChildren().get(0);

            PasswordField passwordField = (PasswordField) authorizationHBox.getChildren().get(1);

            passwordField.requestFocus();
        });

        return gridPane;
    }

    private Node createChangeAuthorizationNode(MainModel.ChangeAuthorizationModel changeAuthorizationModel) {
        GridPane gridPane = (GridPane) createExtendedAuthorizationNode(changeAuthorizationModel);

        ScrollPane scrollPane = (ScrollPane) gridPane.getChildren().get(0);

        VBox vBox = (VBox) scrollPane.getContent();

        Map<StringProperty, StringProperty> authorizationProperties = changeAuthorizationModel.authorizationProperties();

        for (StringProperty keyProperty : authorizationProperties.keySet()) {
            StringProperty valueProperty = authorizationProperties.get(keyProperty);

            HBox hBox = createAuthorizationHBox(keyProperty, valueProperty);

            TextField textField = (TextField) hBox.getChildren().get(0);

            textField.setEditable(false);
            textField.setFocusTraversable(false);

            Button resetAuthorizationButton = createResetAuthorizationButton();

            resetAuthorizationButton.setOnAction(event -> {
                valueProperty.setValue("********");
            });

            resetAuthorizationButton.fire();

            Button removeAuthorizationButton = createRemoveAuthorizationButton();

            removeAuthorizationButton.setOnAction(event -> {
                vBox.getChildren().remove(hBox);

                authorizationProperties.remove(keyProperty);
            });

            hBox.getChildren().add(resetAuthorizationButton);
            hBox.getChildren().add(removeAuthorizationButton);

            vBox.getChildren().add(hBox);
        }

        Button deleteAuthorizationsButton = new Button();

        FontAwesomeIconView trashIcon = new FontAwesomeIconView();

        trashIcon.setIcon(FontAwesomeIcon.TRASH);

        deleteAuthorizationsButton.setGraphic(trashIcon);

        deleteAuthorizationsButton.setOnAction(event -> {
            vBox.getChildren().clear();

            authorizationProperties.clear();
        });

        HBox hBox = (HBox) gridPane.getChildren().get(1);

        hBox.getChildren().add(deleteAuthorizationsButton);

        Platform.runLater(() -> {
            HBox authorizationHBox = (HBox) vBox.getChildren().get(0);

            PasswordField passwordField = (PasswordField) authorizationHBox.getChildren().get(1);

            passwordField.requestFocus();
        });

        return gridPane;
    }

    private HBox createAuthorizationHBox(StringProperty keyProperty, StringProperty valueProperty) {
        HBox hBox = new HBox();

        Insets insets = new Insets(5, 10, 5, 10);

        hBox.setPadding(insets);

        hBox.setSpacing(10);

        TextField textField = createAuthorizationTextField(keyProperty);
        PasswordField passwordField = createAuthorizationPasswordField(valueProperty);

        hBox.getChildren().add(textField);
        hBox.getChildren().add(passwordField);

        return hBox;
    }

    private TextField createAuthorizationTextField(StringProperty stringProperty) {
        TextField textField = new TextField();

        textField.setPromptText(resourceBundle.getString("authorizationTextField.promptText"));

        textField.setMinWidth(125);

        textField.textProperty().bindBidirectional(stringProperty);

        return textField;
    }

    private PasswordField createAuthorizationPasswordField(StringProperty stringProperty) {
        PasswordField passwordField = new PasswordField();

        passwordField.setPromptText(resourceBundle.getString("authorizationPasswordField.promptText"));

        passwordField.textProperty().bindBidirectional(stringProperty);

        return passwordField;
    }

    private Button createRemoveAuthorizationButton() {
        Button button = new Button();

        FontAwesomeIconView icon = new FontAwesomeIconView();

        icon.setIcon(FontAwesomeIcon.REMOVE);

        button.setGraphic(icon);

        return button;
    }

    private Button createResetAuthorizationButton() {
        Button button = new Button();

        FontAwesomeIconView icon = new FontAwesomeIconView();

        icon.setIcon(FontAwesomeIcon.REFRESH);

        button.setGraphic(icon);

        return button;
    }

    private Node createExtendedAuthorizationNode(MainModel.AuthorizationModel authorizationModel) {
        GridPane gridPane = (GridPane) createAuthorizationNode();

        ScrollPane scrollPane = (ScrollPane) gridPane.getChildren().get(0);
        VBox vBox = (VBox) scrollPane.getContent();

        HBox hBox = new HBox();

        hBox.setSpacing(10);

        Button plusAuthorizationButton = new Button();
        Button minusAuthorizationButton = new Button();

        FontAwesomeIconView plusIcon = new FontAwesomeIconView();
        FontAwesomeIconView minusIcon = new FontAwesomeIconView();

        plusIcon.setIcon(FontAwesomeIcon.PLUS);
        minusIcon.setIcon(FontAwesomeIcon.MINUS);

        plusAuthorizationButton.setGraphic(plusIcon);
        minusAuthorizationButton.setGraphic(minusIcon);

        plusAuthorizationButton.setOnAction(event -> {
            plusAuthorization(vBox, authorizationModel);
        });

        minusAuthorizationButton.setOnAction(event -> {
            minusAuthorization(vBox, authorizationModel);
        });

        hBox.getChildren().add(plusAuthorizationButton);
        hBox.getChildren().add(minusAuthorizationButton);

        gridPane.add(hBox, 0, 1);

        return gridPane;
    }

    private void plusAuthorization(VBox vBox, MainModel.AuthorizationModel authorizationModel) {
        StringProperty keyProperty = authorizationModel.createStringProperty("key");
        StringProperty valueProperty = authorizationModel.createStringProperty("value");

        authorizationModel.addAuthorization(keyProperty, valueProperty);

        HBox hBox = createAuthorizationHBox(keyProperty, valueProperty);

        Button button = createRemoveAuthorizationButton();

        button.setOnAction(event -> {
            vBox.getChildren().remove(hBox);

            authorizationModel.removeAuthorization(keyProperty);
        });

        hBox.getChildren().add(button);

        vBox.getChildren().add(hBox);

        Platform.runLater(() -> {
            TextField textField = (TextField) hBox.getChildren().get(0);

            textField.requestFocus();
        });
    }

    private void minusAuthorization(VBox vBox, MainModel.AuthorizationModel authorizationModel) {
        int size = vBox.getChildren().size();

        if (size > 0) {
            Node node = vBox.getChildren().get(size - 1);

            HBox hBox = (HBox) node;

            vBox.getChildren().remove(hBox);

            TextField keyTextField = (TextField) hBox.getChildren().get(0);

            StringProperty keyProperty = keyTextField.textProperty();

            authorizationModel.removeAuthorization(keyProperty);
        }
    }

    private Node createAuthorizationNode() {
        GridPane gridPane = new GridPane();

        VBox vBox = new VBox();

        ScrollPane scrollPane = new ScrollPane(vBox);

        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(320, 240);

        gridPane.add(scrollPane, 0, 0);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Insets insets = new Insets(20, 10, 20, 10);

        gridPane.setPadding(insets);

        return gridPane;
    }

    /**
     * Create the Node for the Add Dialog.
     *
     * @param validationSupport the ValidationSupport for the Add Dialog
     * @param addModel          the Model for the Add Dialog
     * @return the Node that is used as the content of the dialog created in the
     *         onAddAction method
     */
    private Node createAddNode(ValidationSupport validationSupport, MainModel.AddModel addModel) {
        GridPane gridPane = (GridPane) createEntryNode(validationSupport, addModel);

        return gridPane;
    }

    private Node createEditNode(ValidationSupport validationSupport, MainModel.EditModel editModel) {
        GridPane gridPane = (GridPane) createEntryNode(validationSupport, editModel);

        Label totalPaymentLabel = new Label();
        Label alreadyPaidLabel = new Label();

        DoubleField totalPaymentNumberField = new DoubleField();
        DoubleField alreadyPaidNumberField = new DoubleField();

        gridPane.add(totalPaymentLabel, 0, 3);
        gridPane.add(alreadyPaidLabel, 0, 4);
        gridPane.add(totalPaymentNumberField, 1, 3);
        gridPane.add(alreadyPaidNumberField, 1, 4);

        totalPaymentLabel.setText(resourceBundle.getString("totalPaymentLabel.text"));
        alreadyPaidLabel.setText(resourceBundle.getString("alreadyPaidLabel.text"));

        Tooltip totalPaymentTooltip = new Tooltip(resourceBundle.getString("totalPaymentTooltip.text"));
        Tooltip alreadyPaidTooltip = new Tooltip(resourceBundle.getString("alreadyPaidTooltip.text"));

        totalPaymentLabel.setTooltip(totalPaymentTooltip);
        alreadyPaidLabel.setTooltip(alreadyPaidTooltip);

        totalPaymentNumberField.valueProperty().bindBidirectional(editModel.totalPaymentProperty());
        alreadyPaidNumberField.valueProperty().bindBidirectional(editModel.alreadyPaidProperty());

        Validator<String> totalPaymentValidator = Validator.createEmptyValidator(resourceBundle.getString("idValidator.text"));
        Validator<String> alreadyPaidValidator = Validator.createEmptyValidator(resourceBundle.getString("firstNameValidator.text"));

        Platform.runLater(() -> {
            validationSupport.registerValidator(totalPaymentNumberField, totalPaymentValidator);
            validationSupport.registerValidator(alreadyPaidNumberField, alreadyPaidValidator);
        });

        if (alreadyPaidNumberField.getText().isEmpty()) {
            alreadyPaidNumberField.setText("0");
        }

        if (totalPaymentNumberField.getText().isEmpty()) {
            totalPaymentNumberField.setText("0");
        }

        return gridPane;
    }

    private Node createEntryNode(ValidationSupport validationSupport, MainModel.EntryModel entryModel) {
        GridPane gridPane = new GridPane();

        Label idLabel = new Label();
        Label firstNameLabel = new Label();
        Label lastNameLabel = new Label();

        LongField idNumberField = new LongField();
        CheckBox autoIncrementCheckBox = new CheckBox();

        TextField firstNameTextField = new TextField();
        TextField lastNameTextField = new TextField();

        HBox idHBox = new HBox();

        idHBox.getChildren().add(idNumberField);
        idHBox.getChildren().add(autoIncrementCheckBox);

        idHBox.setAlignment(Pos.CENTER);
        idHBox.setPrefWidth(150);
        idHBox.setSpacing(10);

        autoIncrementCheckBox.setMinWidth(Region.USE_PREF_SIZE);

        long defaultID = entryModel.getId();

        autoIncrementCheckBox.setOnAction(event -> {
            if (autoIncrementCheckBox.isSelected()) {
                Dataset dataset = datasetManager.getCurrentDataset();

                long id = dataset.generateId(defaultID);

                entryModel.setId(id);
            }
        });

        autoIncrementCheckBox.fire();

        gridPane.add(idLabel, 0, 0);
        gridPane.add(firstNameLabel, 0, 1);
        gridPane.add(lastNameLabel, 0, 2);
        gridPane.add(idHBox, 1, 0);
        gridPane.add(firstNameTextField, 1, 1);
        gridPane.add(lastNameTextField, 1, 2);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Insets insets = new Insets(20, 10, 20, 10);

        gridPane.setPadding(insets);

        idLabel.setText(resourceBundle.getString("idLabel.text"));
        firstNameLabel.setText(resourceBundle.getString("firstNameLabel.text"));
        lastNameLabel.setText(resourceBundle.getString("lastNameLabel.text"));
        autoIncrementCheckBox.setText(resourceBundle.getString("autoIncrementCheckBox.text"));

        Tooltip idTooltip = new Tooltip(resourceBundle.getString("idTooltip.text"));
        Tooltip firstNameTooltip = new Tooltip(resourceBundle.getString("firstNameTooltip.text"));
        Tooltip lastNameTooltip = new Tooltip(resourceBundle.getString("lastNameTooltip.text"));
        Tooltip autoIncrementTooltip = new Tooltip(resourceBundle.getString("autoIncrementTooltip.text"));

        idLabel.setTooltip(idTooltip);
        firstNameLabel.setTooltip(firstNameTooltip);
        lastNameLabel.setTooltip(lastNameTooltip);
        autoIncrementCheckBox.setTooltip(autoIncrementTooltip);

        idNumberField.valueProperty().bindBidirectional(entryModel.idProperty());
        firstNameTextField.textProperty().bindBidirectional(entryModel.firstNameProperty());
        lastNameTextField.textProperty().bindBidirectional(entryModel.lastNameProperty());

        idNumberField.disableProperty().bind(autoIncrementCheckBox.selectedProperty());

        Validator<String> idValidator = Validator.createEmptyValidator(resourceBundle.getString("idValidator.text"));
        Validator<String> firstNameValidator = Validator.createEmptyValidator(resourceBundle.getString("firstNameValidator.text"));
        Validator<String> lastNameValidator = Validator.createEmptyValidator(resourceBundle.getString("lastNameValidator.text"));

        Platform.runLater(() -> {
            validationSupport.registerValidator(idNumberField, idValidator);
            validationSupport.registerValidator(firstNameTextField, firstNameValidator);
            validationSupport.registerValidator(lastNameTextField, lastNameValidator);

            firstNameTextField.requestFocus();
        });

        return gridPane;
    }

    private Node createRequestNode(ValidationSupport validationSupport, MainModel.RequestModel requestModel) {
        GridPane gridPane = (GridPane) createAmountNode(validationSupport, requestModel);

        return gridPane;
    }

    private Node createDepositNode(ValidationSupport validationSupport, MainModel.DepositModel depositModel) {
        GridPane gridPane = (GridPane) createAmountNode(validationSupport, depositModel);

        return gridPane;
    }

    private Node createAmountNode(ValidationSupport validationSupport, MainModel.AmountModel amountModel) {
        GridPane gridPane = new GridPane();

        Label amountLabel = new Label();

        DoubleField amountNumberField = new DoubleField();

        gridPane.add(amountLabel, 0, 0);
        gridPane.add(amountNumberField, 1, 0);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Insets insets = new Insets(20, 10, 20, 10);

        gridPane.setPadding(insets);

        Settings.Currency currency = settingsManager.getSettings().getCurrency();

        amountLabel.setText(resourceBundle.getString("amountLabel.text") + (currency == Settings.Currency.USD ? " [$]" : " [€]"));

        Tooltip amountTooltip = new Tooltip(resourceBundle.getString("amountTooltip.text"));

        amountLabel.setTooltip(amountTooltip);

        amountNumberField.valueProperty().bindBidirectional(amountModel.amountProperty());

        Validator<String> amountValidator = Validator.createEmptyValidator(resourceBundle.getString("amountValidator.text"));

        Platform.runLater(() -> {
            validationSupport.registerValidator(amountNumberField, amountValidator);

            amountNumberField.requestFocus();
        });

        return gridPane;
    }

    private Window findParentWindow(ActionEvent event) {
        return ((Node) event.getTarget()).getScene().getWindow();
    }
}