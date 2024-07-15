module org.linx.appck {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires java.sql;

    opens org.linx.appck to javafx.fxml;
    opens org.linx.appck.controller to javafx.fxml;
    opens org.linx.appck.model to com.fasterxml.jackson.databind;
    opens org.linx.appck.service to com.fasterxml.jackson.databind;

    exports org.linx.appck;
    exports org.linx.appck.controller;
    exports org.linx.appck.model;
    exports org.linx.appck.service;
}
