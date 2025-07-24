package at.geyser.cashier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * DatasetManager class which manages all datasets and their corresponding
 * files. Also provides methods for building a session from the current
 * datasets. Contains two static methods for loading and saving datasets.
 */
public class DatasetManager {

    private static List<Dataset> loadDir(File dir) {
        List<File> files = new ArrayList<File>();

        Collections.addAll(files, Objects.requireNonNull(dir.listFiles()));

        return load(files);
    }

    private static List<Dataset> load(Session session) {
        List<File> files = session.getFiles();

        return load(files);
    }

    private static List<Dataset> load(List<File> files) {
        List<Dataset> datasets = new ArrayList<Dataset>();

        for (File file : files) {
            if (file != null && file.exists() && file.isFile()) {
                try {
                    datasets.add(load(file));
                } catch (IOException exception) {
                    // Do nothing
                }
            }
        }

        return datasets;
    }

    private static Dataset load(File datasetFile) throws IOException {
        Dataset dataset = JsonMapper.load(datasetFile, Dataset.class);

        dataset.setFile(datasetFile);

        dataset.compareHash();

        return dataset;
    }

    /**
     * The generic save method which saves the given dataset to the associated file.
     * Creates the parent directories if they do not exist.
     * 
     * @throws FileIsNullException if the file is null (no file is selected)
     * @throws IOException         if the file could not be saved
     */
    private static void save(Dataset dataset) throws FileIsNullException, IOException {
        File datasetFile = dataset.getFile();

        if (!datasetFile.exists()) {
            datasetFile.getParentFile().mkdirs();
        }

        dataset.setModified(false);

        dataset.calculateHash();

        JsonMapper.save(datasetFile, dataset);
    }

    /**
     * The generic buildSession method which builds a session from the given
     * datasets.
     * 
     * @param datasets the datasets to build a session from
     * @return a session build from the given datasets
     */
    private static Session buildSession(List<Dataset> datasets) {
        Session session = new Session();

        for (Dataset dataset : datasets) {
            File datasetFile = dataset.getFile();

            if (datasetFile != null) {
                session.addFile(datasetFile);
            }
        }

        return session;
    }

    /**
     * The list of loaded datasets.
     */
    private final List<Dataset> datasets;

    /**
     * The currently selected dataset. This is the dataset which is displayed in the
     * contentPane.
     */
    private Dataset currentDataset;

    /**
     * Constructor for the DatasetManager class. Initializes the datasets list.
     */
    public DatasetManager() {
        this.datasets = new ArrayList<Dataset>();
    }

    public List<Dataset> loadDatasetsDir() throws IOException {
        File file = new File(App.DATASETS_DIR_PATH);

        if (!file.exists()) {
            throw new IOException();
        }

        return loadDir(file);
    }

    public List<Dataset> loadSessionFile() throws IOException {
        File file = new File(App.SESSION_FILE_PATH);

        if (!file.exists()) {
            throw new IOException();
        }

        Session session = JsonMapper.load(file, Session.class);

        return load(session);
    }

    public Dataset loadDataset(File file) throws FileIsLoadedException, IOException {
        for (Dataset dataset : this.datasets) {
            if (dataset.getFile() != null && dataset.getFile().equals(file)) {
                throw new FileIsLoadedException(datasets.indexOf(dataset));
            }
        }

        return load(file);
    }

    /**
     * Save the current dataset to the file it is associated with.
     * 
     * @throws FileIsNullException if the file is null (no file is selected)
     * @throws IOException         if the file could not be saved
     */
    public void saveDataset() throws FileIsNullException, IOException {
        // If the file is null, throw the FileIsNullException to handle it differently
        // than an IOException
        if (this.currentDataset.getFile() == null) {
            throw new FileIsNullException();
        }

        // If the file exists, and it is not modified, there is no need to save it
        if (this.currentDataset.getFile().exists() && !this.currentDataset.getModified()) {
            return;
        }

        // Save the dataset
        save(this.currentDataset);
    }

    /**
     * Save every dataset to the file it is associated with.
     */
    public void saveAll() {
        for (Dataset dataset : this.datasets) {
            try {
                // If the file was modified, and it has a file associated with it, save it
                if (dataset.getModified() && dataset.getFile() != null) {
                    save(dataset);
                }
            } catch (FileIsNullException | IOException exception) {
                // Do nothing
            }
        }
    }

    /**
     * Build a session from the current datasets.
     * 
     * @return the session built from the current datasets
     */
    public Session buildSession() {
        return buildSession(this.datasets);
    }

    public boolean containsFile(File file) {
        for (Dataset dataset : this.datasets) {
            if (dataset.getFile() != null && dataset.getFile().equals(file)) {
                return true;
            }
        }

        return false;
    }

    public int findIndexOfZeroDataset() {
        for (Dataset dataset : this.datasets) {
            if (dataset.zero()) {
                return this.datasets.indexOf(dataset);
            }
        }

        return -1;
    }

    public int findIndexOfCurrentDataset() {
        return this.datasets.indexOf(currentDataset);
    }

    public int findSizeOfDatasets() {
        return this.datasets.size();
    }

    public void addDatasets(List<Dataset> datasets) {
        this.datasets.addAll(datasets);
    }

    public void addDataset(Dataset dataset) {
        this.datasets.add(dataset);
    }

    public void removeDataset(Dataset dataset) {
        this.datasets.remove(dataset);
    }

    public List<Dataset> getDatasets() {
        return this.datasets;
    }

    public Dataset getCurrentDataset() {
        return this.currentDataset;
    }

    public void setCurrentDataset(Dataset currentDataset) {
        this.currentDataset = currentDataset;
    }
}