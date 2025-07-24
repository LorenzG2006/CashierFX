package at.geyser.cashier;

import java.util.ResourceBundle;

public class Info {

    private final String header;
    private final String content;

    /**
     * Constructor for Info.
     */
    public Info() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(App.GROUP_ID + ".properties.Info");

        this.header = resourceBundle.getString("info.header");
        this.content = resourceBundle.getString("info.content");
    }

    public String getHeader() {
        return this.header;
    }

    public String getContent() {
        return this.content;
    }
}