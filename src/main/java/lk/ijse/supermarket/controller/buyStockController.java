package lk.ijse.supermarket.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.supermarket.dto.DashBordManageDto;
import lk.ijse.supermarket.dto.StockManageDto;
import lk.ijse.supermarket.model.StockManageModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class buyStockController implements Initializable {

    private StockManageModel ST_MANAGE_MODEL = new StockManageModel();

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtCount;

    @FXML
    private ListView<String> ListView;



    @FXML
    private TableView<StockManageDto> tbl;

    @FXML
    private TableColumn<StockManageDto, String> clproduct;

    @FXML
    private TableColumn<StockManageDto, Integer> clqty;

    @FXML
    private TableColumn<StockManageDto, String> clplace;

    @FXML
    private TableColumn<StockManageDto, Double> clprice;


    public void SearchOnAction(javafx.event.ActionEvent event) {
        txtCount.requestFocus();
    }

    public void countOnAction(javafx.event.ActionEvent event) {
        PayUp(event);
        txtSearch.requestFocus();
    }

    public void PayUp(javafx.event.ActionEvent event) {
        String id = txtSearch.getText();
        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid Stock ID.").show();
            return;
        }

        int count;
        try {
            count = Integer.parseInt(txtCount.getText());
            if (count <= 0) {
                new Alert(Alert.AlertType.ERROR, "Please enter a valid count greater than 0.").show();
                return;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid number for the count.").show();
            return;
        }

        try {
            boolean isUpdated = ST_MANAGE_MODEL.downcount(id, count);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Stock count updated successfully!").show();
                tblLoad(); // Refresh the table
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to update stock: Insufficient stock or invalid ID.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating stock: " + e.getMessage()).show();
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadTableSET();

        txtSearch.setOnKeyReleased(this::onKeyReleased);


        ListView.setVisible(false);

        ListView.setOnMouseClicked(event -> {
            String selected = ListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtSearch.setText(selected);
                ListView.getItems().clear();
                ListView.setVisible(false);
            }
        });


        txtSearch.setOnKeyPressed(this::handleKeyPress);
    }

    private void loadTableSET() {
        clproduct.setCellValueFactory(new PropertyValueFactory<>("id"));
        clqty.setCellValueFactory(new PropertyValueFactory<>("count"));
        clplace.setCellValueFactory(new PropertyValueFactory<>("placeHolder"));
        clprice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    private void onKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.ENTER) {
            return;
        }

        String searchText = txtSearch.getText();
        if (searchText.isEmpty()) {
            ListView.getItems().clear();
            ListView.setVisible(false);
            return;
        }

        List<String> suggestions = new ArrayList<>();
        try {
            suggestions = ST_MANAGE_MODEL.searchListItam(searchText);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (!suggestions.isEmpty()) {
            ListView.getItems().setAll(suggestions);
            ListView.setVisible(true);
            // Select the first item by default
            ListView.getSelectionModel().selectFirst();
        } else {
            ListView.getItems().clear();
            ListView.setVisible(false);
        }
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        if (!ListView.isVisible() || ListView.getItems().isEmpty()) {
            return;
        }

        int currentIndex = ListView.getSelectionModel().getSelectedIndex();

        switch (keyEvent.getCode()) {
            case DOWN:
                // Move selection down
                if (currentIndex < ListView.getItems().size() - 1) {
                    ListView.getSelectionModel().select(currentIndex + 1);
                    ListView.scrollTo(currentIndex + 1);
                }
                keyEvent.consume();
                break;

            case UP:
                // Move selection up
                if (currentIndex > 0) {
                    ListView.getSelectionModel().select(currentIndex - 1);
                    ListView.scrollTo(currentIndex - 1);
                }
                keyEvent.consume();
                break;

            case ENTER:
                // Select the item and populate the TextField
                String selected = ListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    txtSearch.setText(selected);
                    ListView.getItems().clear();
                    ListView.setVisible(false);
                }
                keyEvent.consume();
                tblLoad();
                break;

            case ESCAPE:
                // Hide the ListView when Escape is pressed
                ListView.getItems().clear();
                ListView.setVisible(false);
                keyEvent.consume();
                break;
        }
    }

    private void tblLoad() {
        String oID = txtSearch.getText();
        ObservableList<StockManageDto> OLis = FXCollections.observableArrayList();
        try {

            List<StockManageDto> dd = ST_MANAGE_MODEL.loadTbl(oID);

            OLis.addAll(dd);
            tbl.setItems(OLis);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}