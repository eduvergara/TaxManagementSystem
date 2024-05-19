module com.eduvergara.taxmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.eduvergara.taxmanagementsystem to javafx.fxml;
    exports com.eduvergara.taxmanagementsystem;
}