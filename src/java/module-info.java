module tp.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.calendarfx.view;
    requires java.sql;

    opens tp.javafx to javafx.fxml;
    exports tp.javafx;
    opens tp.javafx.data to javafx.fxml;
    exports tp.javafx.data;

    opens tp.javafx.models to javafx.fxml;
    exports tp.javafx.models;
    opens tp.javafx.controllers to javafx.fxml;
    exports tp.javafx.controllers;
}