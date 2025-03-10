module org.example.ViewProject1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.example.ModelProject;
    requires org.apache.commons.lang3;
    requires java.logging;
    requires java.sql;

    opens org.exampleview to javafx.fxml;
    exports org.exampleview;
}