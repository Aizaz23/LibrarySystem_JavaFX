package nl.inholland.librarysystemjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.inholland.librarysystemjavafx.data.Database;
import nl.inholland.librarysystemjavafx.model.User;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label loginStatus;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

    public static Database database;
    public static User loggedUser;

    public LoginController() {}

    @FXML
    public void onLoginButtonClicked(ActionEvent actionEvent) throws IOException {
        database = new Database();
            for (User user : database.getUsers()) {
                if (usernameField.getText().equals(user.getUsername()) &&
                        (passwordField.getText().equals(user.getPassword()))) {

                    Stage loginStage = (Stage) loginButton.getScene().getWindow();
                    loginStage.close();

                    loggedUser = new User(user.getFullName());

                    MainWindowController mainWindowController = new MainWindowController();
                    mainWindowController.loadScene("main-view.fxml", actionEvent);

                } else if (usernameField.getText().isEmpty() && passwordField.getText().isEmpty()) {
                    loginStatus.setText("Please enter your username and password.");
                } else {
                    loginStatus.setText("Wrong credentials, please try again.");
                }
            }
    }

}
