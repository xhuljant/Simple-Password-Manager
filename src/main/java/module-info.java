module com.example.pm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.smartcardio;
    requires java.desktop;


    opens com.example.pm to javafx.fxml;
    exports com.example.pm;
    exports com.example.pm.Controller;
    opens com.example.pm.Controller to javafx.fxml;
}