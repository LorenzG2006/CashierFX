module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires de.jensd.fx.glyphs.fontawesome;

    requires at.geyser.bcrypt;

    opens com.example to javafx.fxml;

    exports com.example;
}