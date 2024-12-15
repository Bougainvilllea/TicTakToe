module org.example.tictaktoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.example.tictaktoe to javafx.fxml;
    exports org.example.tictaktoe;
    exports org.example.tictaktoe.GameUnits;
    opens org.example.tictaktoe.GameUnits to javafx.fxml;
    exports org.example.tictaktoe.Internet;
    opens org.example.tictaktoe.Internet to javafx.fxml;
}