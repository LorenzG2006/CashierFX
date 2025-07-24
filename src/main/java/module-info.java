module at.geyser {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires de.jensd.fx.glyphs.fontawesome;

    requires at.geyser.bcrypt;

    opens at.geyser.cashier to javafx.fxml;

    exports at.geyser.cashier;
}