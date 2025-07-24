package at.geyser.cashier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

/**
 * Controller class for the chart view.
 */
public class ChartController implements Controller {

    /**
     * ContentBarChart of the view.
     */
    @FXML
    private BarChart<String, Double> contentBarChart;

    /**
     * ResourceBundle for the controller.
     */
    private final ResourceBundle resourceBundle;

    /**
     * List of all entries.
     */
    private final ObservableList<Entry> entries;

    /**
     * Series of the total payment.
     */
    private final XYChart.Series<String, Double> totalPaymentSeries;

    /**
     * Series of the already paid.
     */
    private final XYChart.Series<String, Double> alreadyPaidSeries;

    /**
     * Constructor for ChartController.
     */
    public ChartController() {
        this.resourceBundle = ResourceBundle.getBundle(App.GROUP_ID + ".properties.ChartController");

        this.entries = FXCollections.observableArrayList(new ArrayList<Entry>());

        this.totalPaymentSeries = new XYChart.Series<String, Double>();
        this.alreadyPaidSeries = new XYChart.Series<String, Double>();
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void init() {
        initChart();
    }

    /**
     * Refreshes the controller.
     * 
     * @param dataset The dataset to refresh the controller with
     */
    public void refresh(Dataset dataset) {
        entries.clear();
        entries.addAll(dataset.getEntries());

        totalPaymentSeries.getData().clear();
        alreadyPaidSeries.getData().clear();

        updateData();
    }

    /**
     * Updates the data of the chart.
     */
    private void updateData() {
        Map<String, List<Entry>> map = new LinkedHashMap<String, List<Entry>>();

        for (Entry entry : entries) {
            String name = entry.getFirstName() + " " + entry.getLastName() + " [" + entry.getId() + "]";

            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
            }

            map.get(name).add(entry);
        }

        for (String name : map.keySet()) {
            XYChart.Data<String, Double> totalPaymentData;
            XYChart.Data<String, Double> alreadyPaidData;

            List<Entry> entries = map.get(name);

            for (int i = 0; i < entries.size(); i++) {
                Entry entry = entries.get(i);

                if (i == 0) {
                    totalPaymentData = new XYChart.Data<String, Double>(name, entry.getTotalPayment());
                    alreadyPaidData = new XYChart.Data<String, Double>(name, entry.getAlreadyPaid());
                } else {
                    totalPaymentData = new XYChart.Data<String, Double>(name + " [" + i + "]", entry.getTotalPayment());
                    alreadyPaidData = new XYChart.Data<String, Double>(name + " [" + i + "]", entry.getAlreadyPaid());
                }

                this.totalPaymentSeries.getData().add(totalPaymentData);
                this.alreadyPaidSeries.getData().add(alreadyPaidData);
            }
        }
    }

    /**
     * Initializes the chart.
     */
    private void initChart() {
        this.totalPaymentSeries.setName(resourceBundle.getString("totalPayment.text"));
        this.alreadyPaidSeries.setName(resourceBundle.getString("alreadyPaid.text"));

        this.contentBarChart.getData().add(this.totalPaymentSeries);
        this.contentBarChart.getData().add(this.alreadyPaidSeries);
    }
}