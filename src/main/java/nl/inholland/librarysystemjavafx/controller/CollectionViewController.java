package nl.inholland.librarysystemjavafx.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
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
import nl.inholland.librarysystemjavafx.model.Item;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionViewController implements Initializable {

    @FXML
    private TextField txtAuthorUpdate;
    @FXML
    private TextField txtTitleUpdate;
    @FXML
    private Label lblError;
    @FXML
    private TextField txtSearchField;
    @FXML
    private TableColumn<Item, String> tblcolAuthor;
    @FXML
    private TableColumn<Item, String> tblcolAvailable;
    @FXML
    private TableColumn<Item, Integer> tblcolItemCode;
    @FXML
    private TableColumn<Item, String> tblcolTitle;
    @FXML
    private TableView<Item> collectionTableView;

    private Database database;
    private ObservableList<Item> collection;
    private ObservableList<Item> currentTableData;
    private Item clickedItem;

    public CollectionViewController(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collection = FXCollections.observableArrayList(LoginController.database.getCollection());

        tblcolItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblcolAvailable.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
        tblcolAvailable.setCellValueFactory(cellData -> {
            boolean available = cellData.getValue().getIsAvailable();
            String availableAsString;
            if (available) {
                availableAsString = "Yes";
            } else {
                availableAsString = "No";
            }
            return new ReadOnlyStringWrapper(availableAsString);
        });
        tblcolTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblcolAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));

        collectionTableView.setItems(collection);

        txtSearchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (txtSearchField.getText().length() >= 2 && txtSearchField != null) {
                ObservableList<Item> searchItems = collectionTableView.getItems();
                collectionTableView.setItems(sortCollection(searchItems, txtSearchField.getText()));
            } else {
                collectionTableView.setItems(collection);
            }
        });
    }

    @FXML
    public void onAddItemButtonClicked(ActionEvent actionEvent) {
        int itemId;
        database = LoginController.database;
        if(!database.getMembers().isEmpty()){
            itemId = database.getCollection().get(database.getCollection().size() - 1).getItemCode() + 1;
        }
        else {
            itemId = 1;
        }

        Item item = new Item(itemId, true, txtTitleUpdate.getText(), txtAuthorUpdate.getText());
        collection = collectionTableView.getItems();

        if(txtTitleUpdate.getText().equals("") || txtAuthorUpdate.getText().equals("")){
            lblError.setText("* Please fill both title and author fields *");
        }
        else {
            LoginController.database.getCollection().add(item);
            collection.add(item);
            collectionTableView.setItems(collection);

            clearFields();
        }
    }

    @FXML
    public void onDeleteItemButtonClicked(ActionEvent actionEvent) {
        ObservableList<Item> selectedItems = collectionTableView.getSelectionModel().getSelectedItems();

        LoginController.database.getCollection().removeAll(selectedItems);
        collection.removeAll(selectedItems);

        clearFields();
        refreshCollection();
    }

    @FXML
    public void onEditItemButtonClicked(ActionEvent actionEvent) {
        //all data in the table
        currentTableData = collectionTableView.getItems();
        //selection made on tableview
        clickedItem = collectionTableView.getSelectionModel().getSelectedItem();
        int currentItemId = clickedItem.getItemCode();

        for (Item item : currentTableData) {
            if (item.getItemCode() == currentItemId) {
                item.setTitle(txtTitleUpdate.getText());
                item.setAuthor(txtAuthorUpdate.getText());

                collectionTableView.setItems(currentTableData);

                clearFields();
                refreshCollection();

                break;
            }
        }
    }

    @FXML
    void onRowButtonClicked(MouseEvent event) {
        clickedItem = collectionTableView.getSelectionModel().getSelectedItem();

        if (clickedItem != null) {
            txtTitleUpdate.setText(String.valueOf(clickedItem.getTitle()));
            txtAuthorUpdate.setText(String.valueOf(clickedItem.getAuthor()));
            lblError.setText("");
        }
        else {
            lblError.setText("* No item clicked on the table view. *");
        }
    }

    private ObservableList<Item> sortCollection(ObservableList<Item> searchCollection, String searchText) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : searchCollection) {
            if (item.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    item.getAuthor().toLowerCase().contains(searchText.toLowerCase())){
                filteredList.add(item);
            }
        }
        return FXCollections.observableList(filteredList);
    }

    public void clearFields() {
        txtTitleUpdate.clear();
        txtAuthorUpdate.clear();
        lblError.setText("");
    }

    public void refreshCollection(){
        collectionTableView.refresh();
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
    public void onLendingReceivingTabButtonClicked(ActionEvent actionEvent) throws IOException {
        loadScene("main-view.fxml", actionEvent);
    }

    public void loadScene(String name, ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(name));

        Stage stage = new Stage();
        stage.setTitle("Collection   - Library System");
        Scene scene = new Scene(fxmlLoader.load());

        ((Node) actionEvent.getSource()).getScene().getWindow().hide();

        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> {
            LoginController.database.writeDataToFile();
        });

        stage.show();
    }

}
