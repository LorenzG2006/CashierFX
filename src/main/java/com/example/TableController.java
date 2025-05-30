package com.example;

import java.util.ArrayList;
import java.util.List;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller class for the table view.
 */
public class TableController implements Controller {

    /**
     * 
     */
    @FXML
    private TableView<Entry> contentTableView;

    @FXML
    private TableColumn<Entry, Integer> idTableColumn;

    @FXML
    private TableColumn<Entry, String> firstNameTableColumn;

    @FXML
    private TableColumn<Entry, String> lastNameTableColumn;

    @FXML
    private TableColumn<Entry, Double> totalPaymentTableColumn;

    @FXML
    private TableColumn<Entry, Double> alreadyPaidTableColumn;

    @FXML
    private TableColumn<Entry, Double> openToPayTableColumn;

    @FXML
    private TableColumn<Entry, FontAwesomeIconView> statusTableColumn;

    private final ObservableList<Entry> entries;

    public TableController() {
        this.entries = FXCollections.observableArrayList(new ArrayList<Entry>());
    }

    @Override
    public void init() {
        initTable();
    }

    public TableView<Entry> getTableView() {
        return contentTableView;
    }

    public void refresh(Dataset dataset) {
        entries.clear();

        entries.addAll(dataset.getEntries());

        contentTableView.refresh();
    }

    public List<Entry> findSelectedEntries() {
        List<Entry> selectedItems = contentTableView.getSelectionModel().getSelectedItems();
        List<Entry> selectedItemsList = new ArrayList<>(selectedItems);

        return selectedItemsList;
    }

    private void initTable() {
        contentTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        contentTableView.setItems(entries);

        idTableColumn.setCellValueFactory(new PropertyValueFactory<Entry, Integer>("id"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("lastName"));
        totalPaymentTableColumn.setCellValueFactory(new PropertyValueFactory<Entry, Double>("totalPayment"));
        alreadyPaidTableColumn.setCellValueFactory(new PropertyValueFactory<Entry, Double>("alreadyPaid"));

        openToPayTableColumn.setCellValueFactory(param -> {
            Entry entry = param.getValue();
            Double openToPay = entry.calculateOpenToPay();

            return new SimpleObjectProperty<Double>(openToPay);
        });

        statusTableColumn.setCellValueFactory(param -> {
            Entry entry = param.getValue();
            FontAwesomeIconView iconView = new FontAwesomeIconView();

            if (entry.calculateOpenToPay() <= 0) {
                iconView.setGlyphName("CHECK");
                iconView.setFill(javafx.scene.paint.Color.GREEN);
            } else {
                iconView.setGlyphName("EXCLAMATION");
                iconView.setFill(javafx.scene.paint.Color.RED);
            }

            return new SimpleObjectProperty<FontAwesomeIconView>(iconView);
        });
    }
}