package lk.ijse.supermarket.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.dto.StockManageDto;
import lk.ijse.supermarket.model.StockManageModel;


import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class StockManageController extends DashbordController {
    private StockManageModel STOCK_MANGE_MODEL;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtCount;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtPlaceHolder;
    private AnchorPane anchorPane;

    public StockManageController(){
        STOCK_MANGE_MODEL = new StockManageModel();
    }
    public void HomePageOnAction(ActionEvent event) throws IOException {
//
//        try {
//            anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"));
//            Scene scene = new Scene(anchorPane);
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(scene);
//            stage.setTitle("DASHBORD");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

//    public void ViewStockOnAction(ActionEvent event) throws IOException {
////        anchorPane = FXMLLoader.load(getClass().getResource("/view/StockViewForm.fxml"));
////        Stage stage1 = new Stage();
////        stage1.setScene(new Scene(anchorPane));
////        stage1.show();
////        stage1.setResizable(false);
////        stage1.setTitle("Stock view form");
//    }

    public void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String type = txtType.getText();
        String count = txtCount.getText();
        String price = txtPrice.getText();
        String plaseHolder = txtPlaceHolder.getText();

        if(id == null || id.isEmpty() || type == null || type.isEmpty() ||
        count == null || count.isEmpty() || price == null || price.isEmpty()
        || plaseHolder == null || plaseHolder.isEmpty()){
            JOptionPane.showMessageDialog(null,"All filed must be not Completed","Validation Error ",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cont = 0;
        double prc = 0;
        try{
            cont = Integer.parseInt(count);
            prc = Double.parseDouble(price);

        } catch (NumberFormatException e) {

        }

        StockManageDto stockManageDto = new StockManageDto(id,type,cont,prc,plaseHolder);
        ObservableList<StockManageDto> list = FXCollections.observableArrayList();
        try {
            String resp = STOCK_MANGE_MODEL.InsertData(stockManageDto);
            JOptionPane.showMessageDialog(null, "Save Status: " + resp, "Save Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        clearDataStock();
    }

    public void onActionStockID(ActionEvent event) {
        String id = txtId.getText();
        try{
            List<StockManageDto> Cdto = STOCK_MANGE_MODEL.getDetailsSEtId(id);
            if(Cdto.size() > 0){
                StockManageDto data = Cdto.get(0);

                txtType.setText(String.valueOf(data.getType()));
                txtCount.setText(String.valueOf(data.getCount()));
                txtPrice.setText(String.valueOf(data.getPrice()));
                txtPlaceHolder.setText(String.valueOf(data.getPlaceHolder()));

            }
            else {
                txtType.requestFocus();
                txtType.setText("");
                txtPlaceHolder.setText("");
                txtCount.setText("");
                txtPrice.setText("");
            }
        } catch (Exception e) {
            System.out.println("null");
        }
    }

    public void btnUpdateOnActon(ActionEvent event) {
        String id = txtId.getText();
        String type = txtType.getText();
        String count = txtCount.getText();
        String price = txtPrice.getText();
        String plaseHolder = txtPlaceHolder.getText();

        if(id == null || id.isEmpty() || type == null || type.isEmpty() ||
                count == null || count.isEmpty() || price == null || price.isEmpty()
                || plaseHolder == null || plaseHolder.isEmpty()){
            JOptionPane.showMessageDialog(null,"All filed must be not Completed","Validation Error ",JOptionPane.ERROR_MESSAGE);
        }

        int cont = 0;
        double prc = 0;
        try{
            cont = Integer.parseInt(count);
            prc = Double.parseDouble(price);

        } catch (NumberFormatException e) {

        }

        StockManageDto stockManageDto = new StockManageDto(id,type,cont,prc,plaseHolder);
        //ObservableList<StockManageDto> list = FXCollections.observableArrayList();
        try {
            String resp = STOCK_MANGE_MODEL.UpdateData(stockManageDto);
            JOptionPane.showMessageDialog(null, "Save Status: " + resp, "Save Status", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        clearDataStock();
    }

    private void clearDataStock() {
        txtId.setText("");
        txtType.setText("");
        txtCount.setText("");
        txtPrice.setText("");
        txtPlaceHolder.setText("");
    }


    public void onActionType(ActionEvent event) {
        txtCount.requestFocus();
    }

    public void onActionCount(ActionEvent event) {
        txtPrice.requestFocus();
    }

    public void onActionPlace(ActionEvent event) {
        btnAddOnAction(event);
    }

    public void OnACtionPrice(ActionEvent event) {
        txtPlaceHolder.requestFocus();
    }


}
