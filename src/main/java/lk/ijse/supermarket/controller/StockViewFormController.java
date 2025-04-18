package lk.ijse.supermarket.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.supermarket.dto.StockManageDto;
import lk.ijse.supermarket.model.StockManageModel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public class StockViewFormController extends StockManageController{
    private StockManageModel ST_MANAGE_MODEL;
    @FXML
    private TableView<StockManageDto> tbleStock;
    @FXML
    private TableColumn<StockManageDto,String >clId;
    @FXML
    private TableColumn<StockManageDto,String >clType;
    @FXML
    private TableColumn<StockManageDto,Integer>clCount;
    @FXML
    private TableColumn<StockManageDto,Double>clPrice;
    @FXML
    private TableColumn<StockManageDto,Double>clPrice1;
    @FXML
    private TableColumn<StockManageDto,String >clPlaceHolder;

    @FXML
    private ListView<String> ListView;
    @FXML
    private TextField txtSearchId;

    public StockViewFormController(){
        ST_MANAGE_MODEL = new StockManageModel();
    }

    public void initialize(){
        txtSearchId.setOnKeyReleased(this::OnKeyHandle);
        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clType.setCellValueFactory(new PropertyValueFactory<>("type"));
        clCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        clPlaceHolder.setCellValueFactory(new PropertyValueFactory<>("placeHolder"));
        clPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        clPrice1.setCellValueFactory(new PropertyValueFactory<>("price"));


        clPrice.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        clPrice1.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        clPlaceHolder.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        clCount.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        clId.setStyle("-fx-font-size: 17px;");
        clType.setStyle("-fx-font-size: 17px;");

        loadTableStock();

        ListView.setVisible(false);

        ListView.setOnMouseClicked(event ->{
            String slect = ListView.getSelectionModel().getSelectedItem();
            if(slect != null){
                txtSearchId.setText(slect);
                ListView.getItems().clear();
                ListView.setVisible(false);
            }
        });
    }

    private void OnKeyHandle(KeyEvent keyEvent) {
        String sc = txtSearchId.getText();
        if(sc.isEmpty()){
            ListView.getItems().clear();
            ListView.setVisible(false);
            return;
        }

        List<String> sgest = new ArrayList<>();
        try {
            sgest = ST_MANAGE_MODEL.searchListItam(sc);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (!sgest.isEmpty()){
            ListView.getItems().setAll(sgest);
            ListView.setVisible(true);
            // AllGETITEM();
        }else {
            ListView.getItems().clear();
            ListView.setVisible(false);
        }
    }

    private void loadTableStock() {
        ObservableList<StockManageDto> StockList = FXCollections.observableArrayList();

        try {
            List<StockManageDto> StockDto = ST_MANAGE_MODEL.LoadTableStock();
            StockList.addAll(StockDto);
            tbleStock.setItems(StockList);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        tbleStock.setItems(StockList);
    }

    public void searchItemIdOnAction(ActionEvent event) {
        String id = txtSearchId.getText();

        ObservableList<StockManageDto> SS = FXCollections.observableArrayList();
        try {
            List<StockManageDto> DD = ST_MANAGE_MODEL.searchItem(id);
            if(DD.isEmpty()){
                DD=ST_MANAGE_MODEL.searchItems(id);
            }
            SS.addAll(DD);
            tbleStock.setItems(SS);
            txtSearchId.setText("");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void refershButtonOnAction(ActionEvent event) {
        loadTableStock();
    }
}


