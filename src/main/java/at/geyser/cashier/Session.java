package at.geyser.cashier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Session class of the application. This is the class which will be saved when
 * saving the Session.
 */
public class Session {

    /**
     * List of files which are currently opened in the application.
     */
    private List<File> files;

    /**
     * Constructor for Session. Initializes the list of files.
     */
    public Session() {
        files = new ArrayList<File>();
    }

    /**
     * Constructor for Session. Initializes the list of files with the given list.
     * 
     * @param files the list of files
     */
    public Session(List<File> files) {
        this.files = files;
    }

    /**
     * Adds a file to the list of files.
     * 
     * @param file the file to add
     */
    public void addFile(File file) {
        files.add(file);
    }

    /**
     * Getter for the list of files.
     * 
     * @return the list of files
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Setter for the list of files.
     * 
     * @param files the value to set
     */
    public void setFiles(List<File> files) {
        this.files = files;
    }
}