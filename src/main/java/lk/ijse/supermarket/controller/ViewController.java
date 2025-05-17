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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.dto.PaymentDto;
import lk.ijse.supermarket.dto.StockManageDto;
import lk.ijse.supermarket.model.StockManageModel;
import lk.ijse.supermarket.model.ViewModel;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class ViewController extends DashbordController{
    private AnchorPane anchorPane;
    private ViewModel VIEW_MODEL;
    private StockManageModel ST_MANAGE_MODEL;

    @FXML
    private Label netAmount;

    @FXML
    private TableView<StockManageDto> tblView;
    @FXML
    private TableColumn<StockManageDto,String>colId;
    @FXML
    private TableColumn<StockManageDto,String>colType;
    @FXML
    private TableColumn<StockManageDto,Integer>colcount;
    @FXML
    private TableColumn<StockManageDto,Double>colPrice;
    @FXML
    private TableColumn<StockManageDto,String>colPlace;



    @FXML
    private TableView<PaymentDto> tblDaily;
    @FXML
    private TableColumn<PaymentDto,String>clItemId;
    @FXML
    private TableColumn<PaymentDto,String>clDate;
    @FXML
    private TableColumn<PaymentDto,Double>clPrice;
    @FXML
    private TableColumn<PaymentDto, Integer>clCount;

    @FXML
    private Label lblTotDaily;

    @FXML
    private TextField txtSearchId;

    public ViewController(){
        VIEW_MODEL = new ViewModel();
        ST_MANAGE_MODEL = new StockManageModel();
    }


    public void initialize(){
        intializeDaily();
        intializes();


    }

    private void intializes() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colcount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPlace.setCellValueFactory(new PropertyValueFactory<>("placeHolder"));

        colId.setStyle("-fx-font-size: 17px;");
        colType.setStyle("-fx-font-size: 17px;");
        colcount.setStyle("-fx-font-size: 17px;");
        colPlace.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        colPrice.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");

        try {
            loadTableView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void intializeDaily() {
        clItemId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        clDate.setCellValueFactory(new PropertyValueFactory<>("type"));
        clCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        clPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        clPrice.setStyle("-fx-alignment: CENTER-RIGHT;"+"-fx-font-size: 17px;");
        clItemId.setStyle("-fx-font-size: 17px;");
        clDate.setStyle("-fx-font-size: 17px;");
        clCount.setStyle("-fx-font-size: 17px;");


        try {
            loadDailyTable();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadDailyTable() throws SQLException, ClassNotFoundException {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        ObservableList<PaymentDto> OLis = FXCollections.observableArrayList();
        try {
            List<PaymentDto> pp = VIEW_MODEL.loadDailyTable();
            OLis.addAll(pp);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }


        double prc = VIEW_MODEL.getTotalPrice();
            double net = VIEW_MODEL.netProfit();
            String pprc = formatter.format(prc);
            lblTotDaily.setText(""+pprc+".00  ");
            String pp = formatter.format(net);
            netAmount.setText(""+pp+".00");
            tblDaily.setItems(OLis);

    }

    private void loadTableView() throws SQLException, ClassNotFoundException {
        ObservableList<StockManageDto> empList = FXCollections.observableArrayList();

            List<StockManageDto> empDtos=VIEW_MODEL.getAllDetails();
            empList.addAll(empDtos);
            tblView.setItems(empList);

    }

    public void BackIconOnAction(ActionEvent event) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("DASHBORD");
        stage.centerOnScreen();
        stage.setResizable(true);
    }

    public void RefershIconOnAction(ActionEvent event) throws IOException {
        btnOnActionView(event);
    }

    public void OnActionSearch(ActionEvent event) {
        String id = txtSearchId.getText();

        ObservableList<StockManageDto> SS = FXCollections.observableArrayList();
        try {
            List<StockManageDto> DD = ST_MANAGE_MODEL.searchItem(id);
            SS.addAll(DD);
            tblView.setItems(SS);
            txtSearchId.setText("");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnActionRefarsh(ActionEvent event) {
        intializes();
    }
}
