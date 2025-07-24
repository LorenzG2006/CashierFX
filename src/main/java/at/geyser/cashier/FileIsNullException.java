package at.geyser.cashier;

/**
 * Exception for when the file is null. Used when saving a dataset which has no
 * file.
 */
public class FileIsNullException extends Exception {

    /**
     * Constructor for FileIsNullException.
     */
    public FileIsNullException() {
        super();
    }
}