module com.example.labo5 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.labo5 to javafx.fxml;
    exports com.example.labo5;
}