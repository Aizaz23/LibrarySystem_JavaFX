package nl.inholland.librarysystemjavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.inholland.librarysystemjavafx.HelloApplication;
import nl.inholland.librarysystemjavafx.data.Database;
import nl.inholland.librarysystemjavafx.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{

    @FXML
    private Label lblLendItem;
    @FXML
    private Label lblReceiveItem;
    @FXML
    private Label lblLendError;
    @FXML
    private Label lblReceiveError;
    @FXML
    private Label lblWelcome;
    @FXML
    private TextField txtItemCodeLendItem;
    @FXML
    private TextField txtItemCodeReceiveItem;
    @FXML
    private TextField txtMemberIdentifier;

    private User user;
    private Database database;

    public MainWindowController() {
        database = LoginController.database;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = LoginController.loggedUser;
        lblWelcome.setText("Welcome, " + user.getUsername());
    }

    @FXML
    public void onReceiveButtonClicked(ActionEvent actionEvent) {
        try{
            int itemCode = Integer.parseInt(txtItemCodeReceiveItem.getText());

            if(database.isItemCodeVerified(database.getCollection(), itemCode)){
                if(database.itemReceived(database.getCollection(), itemCode, lblReceiveItem)){
                    txtItemCodeReceiveItem.clear();
                }
                else{
                    lblReceiveItem.setText("The item is already in the library.");
                }
            }
            else{
                lblReceiveItem.setText("Wrong item code, please check item code.");
            }
        }
        catch (NumberFormatException e){
            lblReceiveError.setText("Numerical code required, please type a number.");
        }
    }
    @FXML
    public void onLendButtonClicked(ActionEvent actionEvent) {
        try{
            int itemCode = Integer.parseInt(txtItemCodeLendItem.getText());
            int memberId = Integer.parseInt(txtMemberIdentifier.getText());

            if(database.isMemberVerified(database.getMembers(), memberId)){
                if(database.isItemCodeVerified(database.getCollection(), itemCode)){
                    if(database.isItemLent(itemCode)){
                        clearFields();
                        lblLendItem.setText("Lending Successful!");
                    }
                    else{
                        lblLendItem.setText("The item is already lent.");
                    }
                }
                else{
                    lblLendItem.setText("Wrong item code, please check item code.");
                }
            }
            else{
                lblLendItem.setText("Wrong member identifier, please check member identifier.");
            }
        }
        catch (NumberFormatException e){
            lblLendError.setText("Numerical code required, please type a number.");
        }
    }
    @FXML
    public void onMembersTabButtonClicked(ActionEvent actionEvent) throws IOException {
        loadScene("members-view.fxml", actionEvent);
    }
    @FXML
    public void onCollectionTabButtonClicked(ActionEvent actionEvent) throws IOException {
        loadScene("collection-view.fxml", actionEvent);
    }
    @FXML
    public void onLendingReceivingTabButtonClicked(ActionEvent actionEvent) throws IOException {
        loadScene("main-view.fxml", actionEvent);
    }

    public void clearFields() {
        txtMemberIdentifier.clear();
        txtItemCodeLendItem.clear();
        txtItemCodeReceiveItem.clear();
        lblLendError.setText("");
        lblLendItem.setText("");
        lblReceiveError.setText("");
        lblReceiveItem.setText("");
    }

    public void loadScene(String name, ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(name));

        Stage stage = new Stage();
        stage.setTitle("Lending/Receiving   - Library System");
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();

        stage.setOnCloseRequest(windowEvent -> {
            LoginController.database.writeDataToFile();
        });

        stage.show();
    }
}

