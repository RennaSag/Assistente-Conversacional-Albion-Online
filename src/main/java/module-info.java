module com.albion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires org.postgresql.jdbc;
    requires com.google.gson;

    opens com.albion to javafx.fxml;
    exports com.albion;
}