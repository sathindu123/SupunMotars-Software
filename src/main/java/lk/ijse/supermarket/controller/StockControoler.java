package lk.ijse.supermarket.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.dto.StockManageDto;
import lk.ijse.supermarket.model.StockManageModel;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockControoler extends DashbordController{
    private StockManageModel STOCK_MANGE_MODEL;

    @FXML
    private TableView<StockManageDto> tbleStock;
    @FXML
    private TableColumn<StockManageDto,String > clId;
    @FXML
    private TableColumn<StockManageDto,String >clType;
    @FXML
    private TableColumn<StockManageDto,Integer>clCount;
    @FXML
    private TableColumn<StockManageDto,Double>clPrice;
    @FXML
    private TableColumn<StockManageDto,String >clPlaceHolder;

    @FXML
    private TableColumn<StockManageDto, Double> PP;

    @FXML
    private Label LBLCount;
    @FXML
    private TextField txtSeacrhBar;


    @FXML
    private Button AddBtn;
    @FXML
    private Button UpdateBtn;

    @FXML
    private ToggleButton Stock;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtCount;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtPrice2;
    @FXML
    private TextField txtPrice1;

    @FXML
    private TextField txtPlaceHolder;
    private AnchorPane anchorPane;

    @FXML
    private Label lblFull;

    @FXML
    private ListView<String> ListSearch;

    @FXML
    private Button deletebtn;

    public StockControoler(){
        STOCK_MANGE_MODEL = new StockManageModel();
    }

    public void initialize(){
        txtSeacrhBar.setOnKeyReleased(this::handleKeyRelesed);
        UpdateBtn.setDisable(true);
        AddBtn.setDisable(false);
        deletebtn.setDisable(true);
        Stock.setDisable(true);
        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clType.setCellValueFactory(new PropertyValueFactory<>("type"));
        clCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        clPlaceHolder.setCellValueFactory(new PropertyValueFactory<>("placeHolder"));
        PP.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        clPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        clPrice.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        PP.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        clPlaceHolder.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        clCount.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        clId.setStyle("-fx-font-size: 17px;");
        clType.setStyle("-fx-font-size: 17px;");


        loadTableStock();

        ListSearch.setVisible(false);

        ListSearch.setOnMouseClicked(event ->{
            String slect = ListSearch.getSelectionModel().getSelectedItem();
            if(slect != null){
                txtSeacrhBar.setText(slect);
                ListSearch.getItems().clear();
                ListSearch.setVisible(false);
            }
        });


    }

    private void handleKeyRelesed(KeyEvent keyEvent) {
        String sc = txtSeacrhBar.getText();
        if(sc.isEmpty()){
            ListSearch.getItems().clear();
            ListSearch.setVisible(false);
            return;
        }

        List<String> sgest = new ArrayList<>();
        try {
            sgest = STOCK_MANGE_MODEL.searchListItam(sc);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (!sgest.isEmpty()){
            ListSearch.getItems().setAll(sgest);
            AllGETITEM();
            ListSearch.setVisible(true);
           // AllGETITEM();
        }else {
            ListSearch.getItems().clear();
            ListSearch.setVisible(false);
        }
    }

    private void loadTableStock() {
        ObservableList<StockManageDto> StockList = FXCollections.observableArrayList();

        try {
            getFullTot();
            List<StockManageDto> StockDto = STOCK_MANGE_MODEL.LoadTableStock();
            StockList.addAll(StockDto);
            tbleStock.setItems(StockList);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        tbleStock.setItems(StockList);
    }

    private void getFullTot() {
        try {
            double ss = STOCK_MANGE_MODEL.getAlLPrice();
            int sd = STOCK_MANGE_MODEL.getAllCount();

            // Format the value of ss to two decimal places
            lblFull.setText(String.format("%.2f\n", ss));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }



    public void HomePageOnAction(ActionEvent event) throws IOException {

        try {
            anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"));
            Scene scene = new Scene(anchorPane);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("DASHBORD");
            stage.centerOnScreen();
            stage.setResizable(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String type = txtType.getText();
        String count = txtCount.getText();
        String price = txtPrice.getText();
        String plaseHolder = txtPlaceHolder.getText();
        double sell = Double.parseDouble(txtPrice2.getText());

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

        StockManageDto stockManageDto = new StockManageDto(id,type,cont,sell,prc,plaseHolder);
        ObservableList<StockManageDto> list = FXCollections.observableArrayList();
        try {
            String resp = STOCK_MANGE_MODEL.InsertData(stockManageDto);
            JOptionPane.showMessageDialog(null, "Save Status: " + resp, "Save Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        loadTableStock();
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
        double sell = Double.parseDouble(txtPrice2.getText());

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

        StockManageDto stockManageDto = new StockManageDto(id,type,cont,sell,prc,plaseHolder);
        //ObservableList<StockManageDto> list = FXCollections.observableArrayList();
        try {
            String resp = STOCK_MANGE_MODEL.UpdateData(stockManageDto);
            JOptionPane.showMessageDialog(null, "Save Status: " + resp, "Save Status", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        loadTableStock();
        clearDataStock();
    }

    private void clearDataStock() {
        txtId.setText("");
        txtType.setText("");
        txtCount.setText("");
        txtPrice.setText("");
        txtPlaceHolder.setText("");
        txtPrice2.setText("");
        txtPrice1.setText("");
    }


    public void onActionType(ActionEvent event) {
        txtCount.requestFocus();
    }

    public void onActionCount(ActionEvent event) {
        txtPrice2.requestFocus();
    }

    public void onActionPlace(ActionEvent event) {
        btnAddOnAction(event);
    }

    public void OnACtionPrice(ActionEvent event) {

        txtPrice2.requestFocus();
    }

    public void logoutOnAction(ActionEvent event) {
    }

    public void OnACtionPrice2(ActionEvent event) {
        txtPrice1.requestFocus();
    }

    public void OnACtiondis(ActionEvent event) {
        double price = Double.parseDouble(txtPrice2.getText());
        int di = Integer.parseInt(txtPrice1.getText());
        double x = ((di*price)/100);

        txtPrice.setText(""+(x+price));
        txtPlaceHolder.requestFocus();
    }

    public void OnClickMouse(MouseEvent mouseEvent) {

        AddBtn.setDisable(true);
        UpdateBtn.setDisable(false);
        deletebtn.setDisable(false);

        StockManageDto selectedItem = tbleStock.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {

            txtId.setText(selectedItem.getId());
            txtType.setText(selectedItem.getType());
            txtCount.setText("" + selectedItem.getCount());
            txtPrice.setText("" + selectedItem.getPrice());
            txtPlaceHolder.setText("" + selectedItem.getPlaceHolder());
        } else {


        }
    }


    public void btnResetOnAction(ActionEvent actionEvent) {
        AddBtn.setDisable(false);
        UpdateBtn.setDisable(true);
        clearDataStock();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        try {
            String ss = STOCK_MANGE_MODEL.deletes(id);
            JOptionPane.showMessageDialog(null, "Save Status: " + ss, "Save Status", JOptionPane.INFORMATION_MESSAGE);
            loadTableStock();
        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {

        }

    }

    public void RefreshClick(MouseEvent mouseEvent) {

        loadTableStock();

    }

    public void OnActionSearch(ActionEvent actionEvent) {
        AllGETITEM();
    }

    private void AllGETITEM() {
        String type = txtSeacrhBar.getText();

        ObservableList<StockManageDto> sd = FXCollections.observableArrayList();

        try {
            List<StockManageDto> ss = STOCK_MANGE_MODEL.getDetailsEqualType(type);
            if (ss.isEmpty()){
                ss = STOCK_MANGE_MODEL.getDetailsEqualType1(type);
            }
            sd.addAll(ss);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tbleStock.setItems(sd);
    }

}
