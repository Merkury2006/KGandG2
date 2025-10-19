module org.example.kgandg2_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.kgandg2_1 to javafx.fxml;
    exports org.example.kgandg2_1;
}