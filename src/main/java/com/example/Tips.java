package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Tips {

    /**
     * ResourceBundle for the tips.
     */
    private final ResourceBundle resourceBundle;

    private final List<String> headers;
    private final List<String> contents;

    /**
     * Constructor for Tips.
     */
    public Tips() {
        this.resourceBundle = ResourceBundle.getBundle(App.GROUP_ID + ".properties.Tips");

        this.headers = new ArrayList<String>();
        this.contents = new ArrayList<String>();

        this.loadTips();
    }

    public String findHeader(int index) {
        String header = this.headers.get(index);

        return header;
    }

    public String findContent(int index) {
        String content = this.contents.get(index);

        return content;
    }

    public int size() {
        return this.headers.size();
    }

    private void loadTips() {
        // Instead of looping through all the keys, I decided to hardcode them
        // Guarantees that the order of the tips is correct

        // Adds all the tips
        addTip("ad");
        addTip("tip");
        addTip("settingsTip");
        addTip("generalTip");
    }

    private void addTip(String name) {
        // Gets the header
        String header = this.resourceBundle.getString(name + ".header");

        // Gets the content
        String content = this.resourceBundle.getString(name + ".content");

        // Adds the header to the list
        this.headers.add(header);

        // Adds the content to the list
        this.contents.add(content);
    }
}