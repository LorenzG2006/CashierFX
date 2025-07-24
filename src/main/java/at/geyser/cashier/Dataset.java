package at.geyser.cashier;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.geyser.BCrypt;

/**
 * Dataset class of the application. This is the class which will be saved when
 * saving the Dataset.
 */
public class Dataset {

    /**
     * List of all entries.
     */
    private List<Entry> entries;

    /**
     * Map of all authorizations.
     */
    private Map<String, String> authorizations;

    /**
     * Hash of the file.
     */
    private String hash;

    /**
     * File of the dataset.
     */
    @JsonIgnore
    private File file;

    /**
     * Authorized of the dataset.
     */
    @JsonIgnore
    private boolean authorized;

    /**
     * Modified of the dataset.
     */
    @JsonIgnore
    private boolean modified;

    /**
     * Verified of the dataset.
     */
    @JsonIgnore
    private boolean verified;

    /**
     * Constructor for Dataset.
     */
    public Dataset() {
        this.entries = new ArrayList<Entry>();
        this.authorizations = new HashMap<String, String>();

        this.verified = true;
    }

    public long generateId(long defaultId) {
        if (defaultId != 0) {
            return defaultId;
        }

        List<Long> ids = new ArrayList<>();

        for (Entry entry : this.entries) {
            ids.add(entry.getId());
        }

        Collections.sort(ids);

        long newId = 1;

        for (long id : ids) {
            if (id > newId) {
                break;
            } else {
                newId = id + 1;
            }
        }

        if (newId > 999999999999999999L) {
            newId = 999999999999999999L;
        }

        return newId;
    }

    public boolean zero() {
        if (this.entries.size() != 0) {
            return false;
        } else if (this.authorizations.size() != 0) {
            return false;
        } else if (this.hash != null) {
            return false;
        } else if (this.file != null) {
            return false;
        } else if (this.authorized) {
            return false;
        } else if (this.modified) {
            return false;
        } else {
            return true;
        }
    }

    public boolean permission() {
        boolean permission = authorized || !containsAuthorizations();

        return permission;
    }

    public boolean containsAuthorizations() {
        boolean containsAuthorizations = !(authorizations == null || authorizations.isEmpty());

        return containsAuthorizations;
    }

    public boolean checkAuthorization(Map.Entry<String, String> authorization) {
        String key = authorization.getKey();

        String value = authorization.getValue();

        String hashedValue = this.authorizations.get(key);

        return BCrypt.checkpw(value, hashedValue);
    }

    public Map<String, String> hashAuthorizations(Map<String, String> authorizations) {
        Map<String, String> hashedAuthorizations = new HashMap<String, String>();

        for (Map.Entry<String, String> authorization : authorizations.entrySet()) {
            String key = authorization.getKey();
            String value = authorization.getValue();

            String hashedValue;

            if (value.equals("********")) {
                if (this.authorizations.containsKey(key)) {
                    hashedValue = this.authorizations.get(key);
                } else {
                    continue;
                }
            } else {
                hashedValue = BCrypt.hashpw(value, BCrypt.gensalt());
            }

            hashedAuthorizations.put(key, hashedValue);
        }

        return hashedAuthorizations;
    }

    public void calculateHash() {
        if (!permission()) {
            return;
        }

        this.hash = Hasher.hash(this);

        this.verified = true;
    }

    public void compareHash() {
        // Calculates the hash of the current dataset
        String hash = Hasher.hash(this);

        // Checks if the hash is null
        if (this.hash != null) {
            // If the hash is not null, it can be compared
            this.verified = this.hash.equals(hash);
        } else {
            // If the hash is null, it can't be compared and is therefore not verified
            this.verified = false;
        }

        if (permission() && !this.verified) {
            this.modified = true;
        }
    }

    public void addEntry(Entry entry) {
        this.entries.add(entry);

        // Sorts the entries by id
        entries.sort(Comparator.comparingLong(Entry::getId));
    }

    public void removeEntry(Entry entry) {
        this.entries.remove(entry);
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public Map<String, String> getAuthorizations() {
        return this.authorizations;
    }

    public void setAuthorizations(Map<String, String> authorizations) {
        this.authorizations = authorizations;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean getAuthorized() {
        return this.authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean getModified() {
        return this.modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean getVerified() {
        return this.verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "Dataset [entries=" + entries + ", authorizations=" + authorizations + "]";
    }
}