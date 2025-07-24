package at.geyser.cashier;

/**
 * Exception for when the file is already loaded. Used when loading a dataset.
 */
public class FileIsLoadedException extends Exception {

    /**
     * The index of the dataset which is already loaded.
     */
    private int index;

    /**
     * Constructor for FileIsLoadedException.
     * 
     * @param index of the dataset which is already loaded.
     */
    public FileIsLoadedException(int index) {
        super();

        this.index = index;
    }

    /**
     * Getter for the index of the dataset which is already loaded.
     * 
     * @return the index of the dataset which is already loaded.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setter for the index of the dataset which is already loaded.
     * 
     * @param index of the dataset which is already loaded.
     */
    public void setIndex(int index) {
        this.index = index;
    }
}