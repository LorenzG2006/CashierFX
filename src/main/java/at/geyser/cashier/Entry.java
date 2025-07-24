package at.geyser.cashier;

/**
 * Entry class of the application.
 */
public class Entry {

    /**
     * ID of the entry.
     */
    private long id;

    /**
     * FirstName of the entry.
     */
    private String firstName;

    /**
     * LastName of the entry.
     */
    private String lastName;

    /**
     * TotalPayment of the entry.
     */
    private double totalPayment;

    /**
     * AlreadyPaid of the entry.
     */
    private double alreadyPaid;

    /**
     * Constructor for Entry. Initializes with the default values.
     */
    public Entry() {
        this.id = 0;
        this.firstName = "";
        this.lastName = "";
        this.totalPayment = 0.0d;
        this.alreadyPaid = 0.0d;
    }

    /**
     * Requests a payment from the entry.
     * 
     * @param amount the amount of the payment
     */
    public void request(double amount) {
        this.totalPayment += amount;

        if (this.totalPayment > 9999999999999.99d) {
            this.totalPayment = 9999999999999.99d;
        }

        this.totalPayment = (double) Math.round(this.totalPayment * 100) / 100;
    }

    /**
     * Deposits a payment to the entry.
     * 
     * @param amount the amount of the payment
     */
    public void deposit(double amount) {
        this.alreadyPaid += amount;

        if (this.alreadyPaid > 9999999999999.99d) {
            this.alreadyPaid = 9999999999999.99d;
        }

        this.alreadyPaid = (double) Math.round(this.alreadyPaid * 100) / 100;
    }

    /**
     * Calculates the amount of money the entry has to pay.
     * 
     * @return the result of the calculation
     */
    public double calculateOpenToPay() {
        double openToPay = totalPayment - alreadyPaid;

        openToPay = (double) Math.round(openToPay * 100) / 100;

        return openToPay;
    }

    /**
     * Getter for id.
     * 
     * @return the id of the entry
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for id.
     * 
     * @param id the value to set
     */
    public void setId(long id) {
        if (id > 999999999999999999L) {
            id = 999999999999999999L;
        }

        this.id = id;
    }

    /**
     * Getter for firstName.
     * 
     * @return the firstName of the entry
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for firstName.
     * 
     * @param firstName the value to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for lastName.
     * 
     * @return the lastName of the entry
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for lastName.
     * 
     * @param lastName the value to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for totalPayment.
     * 
     * @return the totalPayment of the entry
     */
    public double getTotalPayment() {
        return totalPayment;
    }

    /**
     * Setter for totalPayment.
     * 
     * @param totalPayment the value to set
     */
    public void setTotalPayment(double totalPayment) {
        if (totalPayment > 9999999999999.99d) {
            totalPayment = 9999999999999.99d;
        }

        this.totalPayment = (double) Math.round(totalPayment * 100) / 100;
    }

    /**
     * Getter for alreadyPaid.
     * 
     * @return the alreadyPaid of the entry
     */
    public double getAlreadyPaid() {
        return alreadyPaid;
    }

    /**
     * Setter for alreadyPaid.
     * 
     * @param alreadyPaid the value to set
     */
    public void setAlreadyPaid(double alreadyPaid) {
        if (alreadyPaid > 9999999999999.99d) {
            alreadyPaid = 9999999999999.99d;
        }

        this.alreadyPaid = (double) Math.round(alreadyPaid * 100) / 100;
    }

    @Override
    public String toString() {
        return "Entry [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", totalPayment=" + totalPayment + ", alreadyPaid=" + alreadyPaid + "]";
    }

    @Override
    public boolean equals(Object object) {
        // Check if the object is the same as this
        if (this == object) {
            return true;
        }

        // Check if the object is null
        if (object == null) {
            return false;
        }

        // Check if the object is an instance of Settings
        if (this.getClass() != object.getClass()) {
            return false;
        }

        // Cast the object to Settings because it is an instance of Settings
        Entry other = (Entry) object;

        // Check if all values are the same
        boolean idIsSame = this.id == other.id;
        boolean firstNameIsSame = this.firstName.equals(other.firstName);
        boolean lastNameIsSame = this.lastName.equals(other.lastName);
        boolean totalPaymentIsSame = this.totalPayment == other.totalPayment;
        boolean alreadyPaidIsSame = this.alreadyPaid == other.alreadyPaid;

        // Return true if all values are the same
        return idIsSame && firstNameIsSame && lastNameIsSame && totalPaymentIsSame && alreadyPaidIsSame;
    }
}