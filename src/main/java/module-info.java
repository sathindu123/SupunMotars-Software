module lk.ijse.supermarket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires jasperreports;
    //  requires jasperreports;

    // Open the DTO package to javafx.base for reflection
    opens lk.ijse.supermarket.dto to javafx.base;
    opens lk.ijse.supermarket.model to javafx.base;

    // Open the controller package to javafx.fxml if using FXML
    opens lk.ijse.supermarket.controller to javafx.fxml;

    // Export packages if necessary
    exports lk.ijse.supermarket;
    exports lk.ijse.supermarket.controller;
    exports lk.ijse.supermarket.model; // If accessed externally
}

