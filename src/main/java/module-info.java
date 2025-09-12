module com.example.javahospitalmanagementsystemproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javahospitalmanagementsystemproject to javafx.fxml;
    exports com.example.javahospitalmanagementsystemproject;
}