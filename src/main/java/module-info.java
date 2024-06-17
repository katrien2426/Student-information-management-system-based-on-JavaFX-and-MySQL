module com.example.javafx_study {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.web;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.javafx_study to javafx.fxml;
    exports com.example.javafx_study;
    exports StudentInfo;
    opens StudentInfo to javafx.fxml;
}