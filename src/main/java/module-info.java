module nl.inholland.librarysystemjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens nl.inholland.librarysystemjavafx to javafx.fxml;
    exports nl.inholland.librarysystemjavafx;
    exports nl.inholland.librarysystemjavafx.controller;
    exports nl.inholland.librarysystemjavafx.model;
    exports nl.inholland.librarysystemjavafx.data;
    opens nl.inholland.librarysystemjavafx.controller to javafx.fxml;
}