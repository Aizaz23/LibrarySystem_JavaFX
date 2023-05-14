package nl.inholland.librarysystemjavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.inholland.librarysystemjavafx.HelloApplication;
import nl.inholland.librarysystemjavafx.data.Database;
import nl.inholland.librarysystemjavafx.model.Member;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class MembersViewController implements Initializable {

    @FXML
    private Label lblError;
    @FXML
    private DatePicker txtDatePicker;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtSearchField;
    @FXML
    private TableColumn<Member, LocalDate> tblcolBirthDate;
    @FXML
    private TableColumn<Member, String> tblcolFirstName;
    @FXML
    private TableColumn<Member, Integer> tblcolIdentifier;
    @FXML
    private TableColumn<Member, String> tblcolLastName;
    @FXML
    private TableView<Member> membersTableView;

    private Member clickedItem;
    private ObservableList<Member> currentTableData;
    private Database database;
    private ObservableList<Member> members;

    public MembersViewController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        members = FXCollections.observableArrayList(LoginController.database.getMembers());

        tblcolIdentifier.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        tblcolFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tblcolLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tblcolBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        membersTableView.setItems(members);

        txtSearchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (txtSearchField.getText().length() >= 2 && txtSearchField != null) {
                ObservableList<Member> searchItems = membersTableView.getItems();
                membersTableView.setItems(sortMember(searchItems, txtSearchField.getText()));
            } else {
                membersTableView.setItems(members);
            }
        });
    }


    @FXML
    public void onAddMemberButtonClicked(ActionEvent actionEvent) {

        int memberId;
        database = LoginController.database;
        if(!database.getMembers().isEmpty()){
            memberId = database.getMembers().get(database.getMembers().size() - 1).getMemberId() + 1;
        }
        else{
            memberId = 1;
        }

        Member member = new Member(memberId, txtFirstName.getText(), txtLastName.getText(), allowManualTextFieldDate());
        members = membersTableView.getItems();

        if(txtFirstName.getText().equals("") || txtLastName.getText().equals("") || allowManualTextFieldDate() == null){
            lblError.setText("* Please fill all the text fields including the date picker *");
        }
        else {

            allowManualTextFieldDate();
            LoginController.database.getMembers().add(member);
            members.add(member);

            membersTableView.setItems(members);
            clearFields();
            refreshMembers();
        }
    }

    @FXML
    public void onDeleteMemberButtonClicked(ActionEvent actionEvent) {
        ObservableList<Member> selectedMembers = membersTableView.getSelectionModel().getSelectedItems();

        LoginController.database.getMembers().removeAll(selectedMembers);
        members.removeAll(selectedMembers);

        clearFields();
        refreshMembers();
    }

    @FXML
    public void onEditMemberButtonClicked(ActionEvent actionEvent) {
        //all data in the table
        currentTableData = membersTableView.getItems();
        //selection made on tableview
        clickedItem = membersTableView.getSelectionModel().getSelectedItem();
        int currentMemberId = clickedItem.getMemberId();

        for (Member member : currentTableData) {
            if (member.getMemberId() == currentMemberId) {
                member.setFirstName(txtFirstName.getText());
                member.setLastName(txtLastName.getText());
                member.setBirthDate(allowManualTextFieldDate());

                membersTableView.setItems(currentTableData);

                clearFields();
                refreshMembers();
                break;
            }
        }
    }

    @FXML
    void onRowButtonClicked(MouseEvent event) {
        clickedItem = membersTableView.getSelectionModel().getSelectedItem();

        if (clickedItem != null){
            txtFirstName.setText(String.valueOf(clickedItem.getFirstName()));
            txtLastName.setText(String.valueOf(clickedItem.getLastName()));
            txtDatePicker.setValue(clickedItem.getBirthDate());
            lblError.setText("");
        }
        else {
            lblError.setText("* No item clicked on the table view. *");
        }
    }

    private ObservableList<Member> sortMember(ObservableList<Member> searchMembers, String searchText) {
        List<Member> filteredList = new ArrayList<>();
        for (Member member : searchMembers) {
            if (member.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                    member.getLastName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(member);
            }
        }
        return FXCollections.observableList(filteredList);
    }

    private LocalDate allowManualTextFieldDate(){
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");

        LocalDate birthDateValue = null;
        try{
            if(txtDatePicker.getEditor().getText().contains("/")) {
                Date birthDate = simpleDateFormat.parse(txtDatePicker.getEditor().getText());
                birthDateValue = Instant.ofEpochMilli(birthDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            }
            else if(txtDatePicker.getValue() != null){
                birthDateValue = txtDatePicker.getValue();
            }
        } catch (ParseException e){
            lblError.setText("Please, enter date in the format dd/mm/yyyy");
        }

        return birthDateValue;
    }

    public void clearFields() {
        txtFirstName.clear();
        txtLastName.clear();
        txtDatePicker.setValue(null);
        lblError.setText("");
    }

    private void refreshMembers(){
        membersTableView.refresh();
    }

    @FXML
    public void onCollectionTabButtonClicked(ActionEvent actionEvent) throws IOException {
        loadScene("collection-view.fxml", actionEvent);
    }
    @FXML
    public void onMembersTabButtonClicked(ActionEvent actionEvent) throws IOException {
        loadScene("members-view.fxml", actionEvent);
    }
    @FXML
    public void onLendingReceivingTabButtonClicked(ActionEvent actionEvent) {
        loadScene("main-view.fxml", actionEvent);
    }
    public void loadScene(String name, ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(name));

            Stage stage = new Stage();
            stage.setTitle("Members   - Library System");
            Scene scene = new Scene(fxmlLoader.load());

            ((Node) actionEvent.getSource()).getScene().getWindow().hide();

            stage.setScene(scene);

            stage.setOnCloseRequest(windowEvent -> LoginController.database.writeDataToFile());

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
