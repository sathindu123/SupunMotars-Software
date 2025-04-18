package lk.ijse.supermarket.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.dto.*;
import lk.ijse.supermarket.model.DashbordManageModel;
import lk.ijse.supermarket.model.StockManageModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashbordController extends HelloController{
    private AnchorPane anchorPane;
    private StockManageModel ST_MANAGE_MODEL;
    private DashbordManageModel DASHBORD_MANAGE_MODEL;


    @FXML
    private ListView<String> ListView;
    @FXML
    private Label lblHigaCount;
    @FXML
    private Label lblNameHiga;


    @FXML
    private TextField customerTel;

    @FXML
    private TextField txtvehicleId;
    @FXML
    private Label lblBalance;
    @FXML
    private TextField txtcashPay;
    public Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;
   @FXML
   private TextField OrderIDLBL;

    private String id = "";
    private int lbPrice = 0 ;
    @FXML
    private TableView<DashBordManageDto> tblLoad;
    @FXML
    private TableColumn<DashBordManageDto,String>clType;
    @FXML
    private TableColumn<DashBordManageDto,Double>clSellPrice;
    @FXML
    private TableColumn<DashBordManageDto,Integer> clCount;
    @FXML
    private TableColumn<DashBordManageDto,Integer> clDis;
    @FXML
    private TableColumn<DashBordManageDto,Double> clPrice;

    @FXML
    private TableColumn clNb;

    @FXML
    private Label lblTotPrice;

    @FXML
    private TextField Ml;
    @FXML
    private TextField Ml1;



    @FXML
    private TextField txId;
    @FXML
    private TextField txtDate;
    @FXML
    private TextField txCount;
    @FXML
    private TextField txPresantage;
    @FXML
    private TextField txDiscount;
    @FXML
    private TextField txPrice;
    @FXML
    private Label lblName;
    @FXML
    private Label lblPrice;

    public DashbordController(){
        DASHBORD_MANAGE_MODEL = new DashbordManageModel();
        ST_MANAGE_MODEL = new StockManageModel();
    }

    public void initialize(){
      //  OrderIDLBL.setText("sds");
        //deleteBtn.setDisable(true);
       // addBtn.setDisable(false);
       // setcellValues();



        txId.setOnKeyReleased(this::onKeyReleased);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(dateFormatter);
        txtDate.setText(formattedDate);

        try {
            loadNextOrderId();
            loadTablse();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ListView.setVisible(false);

        ListView.setOnMouseClicked(event -> {
            String selected = ListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txId.setText(selected);
                ListView.getItems().clear();
                ListView.setVisible(false);
            }
        });

        txId.setOnKeyPressed(this::handleKeyPress);
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        // Ignore arrow keys and Enter key to avoid interference with handleKeyPress
        if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.ENTER) {
            return;
        }

        String searchText = txId.getText();
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
                    txId.setText(selected);
                    ListView.getItems().clear();
                    ListView.setVisible(false);
                }
                keyEvent.consume();
                break;

            case ESCAPE:
                // Hide the ListView when Escape is pressed
                ListView.getItems().clear();
                ListView.setVisible(false);
                keyEvent.consume();
                break;
        }
    }


    private void focusCount(KeyEvent keyEvent) {
        IdCheack();
    }



    private void loadNextOrderId() throws SQLException, ClassNotFoundException {
        try {
            String nextCustomerID = DASHBORD_MANAGE_MODEL.getNextCustomerId();

            if(nextCustomerID == null){

            }else {
                OrderIDLBL.setText(""+nextCustomerID);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }


    }



    private void setcellValues() {

    }

    public void logoutOnAction(ActionEvent event) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);

    }

    public void OnActionID(ActionEvent event) {
       IdCheack();
    }

    private void IdCheack() {
        id = txId.getText();

        try{
            List<StockManageDto> sdto = DASHBORD_MANAGE_MODEL.getDetails(id);
            if(sdto.size() > 0){
                StockManageDto data = sdto.get(0);
                // lblPrice.setText(String.valueOf(data.getPrice()));
                lbPrice = (int) data.getPrice();
                txId.setText(String.valueOf(data.getType()));

                txDiscount.setText("0.00");
                txCount.requestFocus();

            }
            else {
                lblName.setText("");
                lblPrice.setText("");
                txPresantage.setText("");
                //   txtDate.setText("");
                txCount.setText("");
                txPrice.setText("");
                txDiscount.setText("");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void OnActionPresantage(ActionEvent event) {
        String presantage = txPresantage.getText();
        String lbPrice = lblPrice.getText();
        String count = txCount.getText();

        int pp = 0;
        double lbprc = 0;
        int cont = 0;
        try {
            pp = Integer.parseInt(presantage);
            lbprc = Double.parseDouble(lbPrice);
            cont = Integer.parseInt(count);
        }catch (NumberFormatException e){
            System.out.println("Error");
        }
        double dic = pp+100;
        if(pp != 0){
           dic = ((lbprc * 100)/dic);
            txPrice.setText(""+dic);
            dic = ((lbprc)-(dic));
            txDiscount.setText(""+dic);
        }
        txPrice.requestFocus();
    }

    public void OnActionTxCount(ActionEvent event) {
        IdCheack();


        String cont = txCount.getText();
        txPresantage.requestFocus();
        int counts = 0;
        try {
            counts = Integer.parseInt(cont);
            lblPrice.setText(""+(counts*lbPrice));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    public void OnActionDiscount(ActionEvent event) {
        String discount = txDiscount.getText();
        String price = lblPrice.getText();
        String count = txCount.getText();

        int dic = 0;
        double pric = 0;
        int cont = 0;
        try{
            dic = Integer.parseInt(discount);
            pric = Double.parseDouble(price);
            cont = Integer.parseInt(count);
        }catch (NumberFormatException e){
            System.out.println("Error");
        }
        double prc = 0;
        if(dic != 0){
            prc = ((pric)-(dic));
            txPrice.setText(""+prc);
        }

        txPrice.requestFocus();
    }

    public void btnOnActionView(ActionEvent event) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/View.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void StockManageOnAction(ActionEvent event) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/StockMange.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void AddAonAction(ActionEvent event) {
        String ids = txId.getText();

        int oID = Integer.parseInt(OrderIDLBL.getText());
        String type = lblName.getText();
        String price = txPrice.getText();
        String date = txtDate.getText();
        String count = txCount.getText();
        String discount = txDiscount.getText();
        String rate = lblPrice.getText();


        int cont = 0;
        double dis = 0;
        double prc = 0;
        double rat = 0.0;
        String ItemId = "";
        try {
             ItemId = ST_MANAGE_MODEL.getItemid(ids);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            cont = Integer.parseInt(count);
            dis = Double.parseDouble(discount);
            prc = Double.parseDouble(price);
            rat = Double.parseDouble(rate);

          //  dis = (rat-dis);

            if (cont == 0){
                JOptionPane.showMessageDialog(null, "Count,  is not a 0 ", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String rsp = ST_MANAGE_MODEL.EqalsDetailCheack(ItemId,oID);

                if (rsp.equals("fail")){
                    txId.setText("");
                    lblName.setText("");
                    txPrice.setText("");
                    lblPrice.setText("");
                 //   txtDate.setText("");
                    txCount.setText("");
                    txPresantage.setText("");
                    txDiscount.setText("");
                    JOptionPane.showMessageDialog(null, "ProductId is Availble", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Count, Price, or Discount is not a valid number", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DashBordManageDto dto = new DashBordManageDto(oID,ItemId,cont,date,rat,dis,prc);

        StockManageDto sto = new StockManageDto(ItemId,type,cont,prc);
        try {
            String resp = ST_MANAGE_MODEL.insertDataStock(dto,sto);
            JOptionPane.showMessageDialog(null, "Save Status: " + resp, "Save Status", JOptionPane.INFORMATION_MESSAGE);
               clearDataAll();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage());
        }

        loadTablse();


    }

    private void clearDataAll() {
        txId.setText("");
        lblName.setText("");
        lblPrice.setText("");
        txPrice.setText("");
     //   txtDate.setText("");
        txCount.setText("");
        txDiscount.setText("");
        txPresantage.setText("");

    }

    private void loadTablse() {


        DecimalFormat formatter = new DecimalFormat("#,###,###");
        if (tblLoad == null) {
            // System.out.println("tblLoad is null. FXML not linked correctly.");
        } else {
            // System.out.println("tblLoad is properly initialized.");
        }
        if (clType != null && clCount != null && clDis != null && clPrice != null) {
            clNb.setCellValueFactory(new PropertyValueFactory<>("nb"));
            clType.setCellValueFactory(new PropertyValueFactory<>("type"));
            clCount.setCellValueFactory(new PropertyValueFactory<>("count"));
            clSellPrice.setCellValueFactory(new PropertyValueFactory<>("onePrice"));
            clDis.setCellValueFactory(new PropertyValueFactory<>("discount"));
            clPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


            clCount.setStyle("-fx-alignment: CENTER-LEFT;"+"-fx-font-size: 21px;");
            clDis.setStyle("-fx-alignment: CENTER-LEFT;"+"-fx-font-size: 21px;");
            clPrice.setStyle("-fx-alignment: CENTER-LEFT;"+"-fx-font-size: 21px;");
            clSellPrice.setStyle("-fx-font-size: 21px;");
            clNb.setStyle("-fx-font-size: 21px;");
            clType.setStyle("-fx-font-size: 21px;");

        } else {
            //System.out.println("TableColumns not initialized properly.");
        }

        int oID = Integer.parseInt(OrderIDLBL.getText());
        ObservableList<DashBordManageDto> OLis = FXCollections.observableArrayList();
        try {
            lblCountsset();
            List<DashBordManageDto> dd = ST_MANAGE_MODEL.loadTb(oID);
            nbcont = 1;
            for (DashBordManageDto dtos : dd) {
                dtos.setNb(nbcont++);
                OLis.add(dtos);
            }

            tblLoad.setItems(OLis);
        } catch (Exception e) {
            System.out.println(e);
        }

    }


    private void lblCountsset() throws SQLException, ClassNotFoundException {
        int id = Integer.parseInt(OrderIDLBL.getText());
        double prc = 0.00;
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        prc = ST_MANAGE_MODEL.getTotalPrice(id);

        String pp = formatter.format(prc);
        if(pp == null){
            pp = "0";
            lblTotPrice.setText("" + pp + " ");
        }else {
            lblTotPrice.setText("" + pp + " ");
        }

    }

    Label L1;

    public void onActionPayUpbtn(ActionEvent event) throws SQLException, ClassNotFoundException {



        boolean condition = false;

        int odId = Integer.parseInt(OrderIDLBL.getText());
        String id = txId.getText();
        String type = lblName.getText();
        String price = txPrice.getText();
        String date = txtDate.getText();
        String count = txCount.getText();
        String discount = txDiscount.getText();
        String rate = lblPrice.getText();
        String vehicleNb = txtvehicleId.getText();


        String ml = Ml.getText();
        String ml1 = Ml1.getText();


            String pays = txtcashPay.getText();
            String  tot = lblTotPrice.getText();
            String tel = customerTel.getText();
            String higas = lblBalance.getText();
        String numericString = higas.replaceAll(",", "");
        double pay;
        double higa = 0;
        try {
             higa = Double.parseDouble(numericString);

            pay = Double.parseDouble(pays);
        } catch (NumberFormatException e) {

        }


        if(vehicleNb == null || vehicleNb.isEmpty() ){
            JOptionPane.showMessageDialog(null, "Vehicle NB is Empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return ;
        }
        if(tel == null || tel.isEmpty() ){
            JOptionPane.showMessageDialog(null, "Customer Tell NB is empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Boolean ss = DASHBORD_MANAGE_MODEL.cheackOrderID(odId);

            if (ss == true){
                boolean ssk = DASHBORD_MANAGE_MODEL.cheakAnother(odId);

                if (ssk == false){
                        JOptionPane.showMessageDialog(null, "this bill is pay bill ", "Validation Error", JOptionPane.ERROR_MESSAGE);
                         return;
                }else {
                    payaupbtns();
                }

                return;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (HeadlessException e) {
            throw new RuntimeException(e);
        }

        payaupbtn();



    }

    private void payaupbtns() throws SQLException, ClassNotFoundException {
        boolean condition = false;

        int odId = Integer.parseInt(OrderIDLBL.getText());
        String id = txId.getText();
        String type = lblName.getText();
        String price = txPrice.getText();
        String date = txtDate.getText();
        String count = txCount.getText();
        String discount = txDiscount.getText();
        String rate = lblPrice.getText();
        String vehicleNb = txtvehicleId.getText();


        String ml = Ml.getText();
        String ml1 = Ml1.getText();


        String pays = txtcashPay.getText();
        String  tot = lblTotPrice.getText();
        String tel = customerTel.getText();
        String higas = lblBalance.getText();
        String numericString = higas.replaceAll(",", "");
        double pay;
        double higa = 0;
        try {
            higa = Double.parseDouble(numericString);

            pay = Double.parseDouble(pays);
        } catch (NumberFormatException e) {

        }


        if(vehicleNb == null || vehicleNb.isEmpty() ){
            JOptionPane.showMessageDialog(null, "Vehicle NB is Empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(tel == null || tel.isEmpty() ){
            JOptionPane.showMessageDialog(null, "Customer Tell NB is empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }



        int cont = 0;
        double dis = 0;
        double prc = 0;
        double rat = 0;

        try {
            cont = Integer.parseInt(count);
            dis = Double.parseDouble(discount);
            prc = Double.parseDouble(price);
            rat = Double.parseDouble(rate);

            if (cont == 0) {
                JOptionPane.showMessageDialog(null, "Count,  is not a 0 ", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }catch (NumberFormatException e){
            //  System.out.println("NUll");
        }
        ObservableList<DashBordManageDto> DL = FXCollections.observableArrayList();
        CustomerVehicelDto customerVehicelDto = new CustomerVehicelDto(odId,vehicleNb);

        String numericStrings = tot.replaceAll(",", "");
        double number = Double.parseDouble(numericStrings);

        HigabillDto higabillDto = new HigabillDto(odId,vehicleNb,tel,date,number,higa);


        try {
            boolean ssk = ST_MANAGE_MODEL.deleteOrders(odId);
            List<DashBordManageDto> DD = ST_MANAGE_MODEL.pastDetailPayments(odId);

            DL.addAll(DD);
            try {

                String rsp = ST_MANAGE_MODEL.InsertPaymentDetails(customerVehicelDto,odId,DL,vehicleNb,higabillDto,higa,ml,ml1,tel);
                JOptionPane.showMessageDialog(null, "Sucsess " + rsp + JOptionPane.ERROR_MESSAGE);



            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Fail " + e.getMessage() + JOptionPane.ERROR_MESSAGE);

            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Fail " + e.getMessage() + JOptionPane.ERROR_MESSAGE);

            }
        } catch (HeadlessException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }



        clJusperRiport();







    }

    private void payaupbtn() throws SQLException, ClassNotFoundException {
        boolean condition = false;

        int odId = Integer.parseInt(OrderIDLBL.getText());
        String id = txId.getText();
        String type = lblName.getText();
        String price = txPrice.getText();
        String date = txtDate.getText();
        String count = txCount.getText();
        String discount = txDiscount.getText();
        String rate = lblPrice.getText();
        String vehicleNb = txtvehicleId.getText();


        String ml = Ml.getText();
        String ml1 = Ml1.getText();


        String pays = txtcashPay.getText();
        String  tot = lblTotPrice.getText();
        String tel = customerTel.getText();
        String higas = lblBalance.getText();
        String numericString = higas.replaceAll(",", "");
        double pay;
        double higa = 0;
        try {
            higa = Double.parseDouble(numericString);

            pay = Double.parseDouble(pays);
        } catch (NumberFormatException e) {

        }


        if(vehicleNb == null || vehicleNb.isEmpty() ){
            JOptionPane.showMessageDialog(null, "Vehicle NB is Empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(tel == null || tel.isEmpty() ){
            JOptionPane.showMessageDialog(null, "Customer Tell NB is empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }



        int cont = 0;
        double dis = 0;
        double prc = 0;
        double rat = 0;

        try {
            cont = Integer.parseInt(count);
            dis = Double.parseDouble(discount);
            prc = Double.parseDouble(price);
            rat = Double.parseDouble(rate);

            if (cont == 0) {
                JOptionPane.showMessageDialog(null, "Count,  is not a 0 ", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }catch (NumberFormatException e){
            //  System.out.println("NUll");
        }
        ObservableList<DashBordManageDto> DL = FXCollections.observableArrayList();
        CustomerVehicelDto customerVehicelDto = new CustomerVehicelDto(odId,vehicleNb);

        String numericStrings = tot.replaceAll(",", "");
        double number = Double.parseDouble(numericStrings);

        HigabillDto higabillDto = new HigabillDto(odId,vehicleNb,tel,date,number,higa);


        try {
            boolean ssk = ST_MANAGE_MODEL.deleteOrders(odId);
            List<DashBordManageDto> DD = ST_MANAGE_MODEL.pastDetailPayments(odId);

            DL.addAll(DD);
            try {

                String rsp = ST_MANAGE_MODEL.InsertPaymentDetails(customerVehicelDto,odId,DL,vehicleNb,higabillDto,higa,ml,ml1,tel);
                JOptionPane.showMessageDialog(null, "Sucsess " + rsp + JOptionPane.ERROR_MESSAGE);



            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Fail " + e.getMessage() + JOptionPane.ERROR_MESSAGE);

            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Fail " + e.getMessage() + JOptionPane.ERROR_MESSAGE);

            }
        } catch (HeadlessException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }


        clJusperRiport();



    }

    private void clJusperRiport() throws SQLException, ClassNotFoundException {
        Thread thread = new Thread(() -> {
            int oId = Integer.parseInt(OrderIDLBL.getText());
            String date = txtDate.getText();
            String vNb = txtvehicleId.getText();
            String tel = customerTel.getText();
            String tot = lblTotPrice.getText();
            String aa = lblHigaCount.getText();
            String ml = Ml.getText();
            String ml1 = Ml1.getText();


            String invoice = ""+oId;

            String numericString = tot.replaceAll(",", "");
            double total;
            String ttt ;
            try {
                total = Double.parseDouble(numericString);
                ttt = ""+total;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
            String higa = ""+total;
            String hh = "0";
            String id = txtvehicleId.getText();
            try {
                double hig = DASHBORD_MANAGE_MODEL.cekakHigaPrice(id);
                if(hig == 0 ){

                }else {
                    hig = Math.abs(hig);
                    hh = ""+hig;
                    higa = ""+(total-hig);

                }
            } catch (SQLException e) {

            } catch (ClassNotFoundException e) {

            }

            try {
                JasperDesign design = JRXmlLoader.load("src/main/resources/Riport/Blank_A4.jrxml");

                JasperReport jasperReport =JasperCompileManager.compileReport(design);

                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("oId",oId);
                parameters.put("Date",date);
                parameters.put("VNb",vNb);
                parameters.put("Tel",tel);
                parameters.put("Total",higa);
                parameters.put("Ariyas",hh);
                parameters.put("SubTotal",ttt);
                parameters.put("InNb",invoice);
                parameters.put("ML",ml);
                parameters.put("ML1",ml1);

                JasperPrint jasperPrint =JasperFillManager.fillReport(jasperReport,parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint,false);
            } catch (Exception e) {
                System.out.println(e);
            }



        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        loadNextOrderId();
        clClear();
        clearDataAll();
        loadTablse();

    }

    private void clClear() {                                                                                                                                                                                                                                                                                              
        customerTel.setText("");
        txtvehicleId.setText("");
        Ml.setText("");
        Ml1.setText("");
        lblBalance.setText("0.0");
        txtcashPay.setText("");
        lblBalance.setText("0.00");
    }

    public void btnOnActionDaily(ActionEvent event) {
    }

    public void onActionPrice(ActionEvent event)throws IOException  {
        AddAonAction(event);
    }

    public void OnActionUpdateBtn(ActionEvent event) throws SQLException, ClassNotFoundException {

        loadTablse();
        clearDataAll();
        addBtn.setDisable(false);
        txtcashPay.setText("");
        lblBalance.setText("");
      //  txtvehicleId.setText("");
        clSellPrice.setText("");




    }

        public void OnActionDeleteBtn(ActionEvent event) {
        String id = txId.getText();
        int count = Integer.parseInt(txCount.getText());
        double sellPrice = Double.parseDouble(clSellPrice.getText());
        double dis = Double.parseDouble(txDiscount.getText());
        double price = Double.parseDouble(txPrice.getText());


        try{
            String resp = ST_MANAGE_MODEL.deleteData(id,count,sellPrice,dis,price);
            JOptionPane.showMessageDialog(null, "Save Status: " + resp, "Save Status", JOptionPane.INFORMATION_MESSAGE);

            loadTablse();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

    }

    public void searchOnAction(ActionEvent event) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Qutation.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void ONActionBillQutation(ActionEvent event) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Qutation.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void OnMouseClick(MouseEvent mouseEvent) {
        lblPrice.setText("");
        addBtn.setDisable(true);
        deleteBtn.setDisable(false);
   //     updateBtn.setDisable(false);
        try {
            txId.setText(tblLoad.getSelectionModel().getSelectedItem().getType());
            txCount.setText(""+tblLoad.getSelectionModel().getSelectedItem().getCount());
            clSellPrice.setText(""+tblLoad.getSelectionModel().getSelectedItem().getOnePrice());
            txDiscount.setText(""+tblLoad.getSelectionModel().getSelectedItem().getDiscount());
            txPrice.setText(""+tblLoad.getSelectionModel().getSelectedItem().getPrice());
        } catch (Exception e) {

        }


    }

    public void OnActioncashPay(ActionEvent event) {
        double pay = Double.parseDouble(txtcashPay.getText());
        String  tot = lblTotPrice.getText();
        String tel = customerTel.getText();
        int oID = Integer.parseInt(OrderIDLBL.getText());
        String vehicleId = txtvehicleId.getText();

        String date = txtDate.getText();





        String numericString = tot.replaceAll(",", "");

        try {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            double number = Double.parseDouble(numericString);
            String pp = formatter.format(pay-number);
            double tots = pay-number;
            lblBalance.setText(pp);
        } catch (NumberFormatException e) {

        }

    }

    public void CustomerReturnOnAction(ActionEvent event) {
        String id = OrderIDLBL.getText();

        try {
            String rsp = DASHBORD_MANAGE_MODEL.custopmerReturn(id);
            JOptionPane.showMessageDialog(null, "Save Status: " + rsp, "Save Status", JOptionPane.INFORMATION_MESSAGE);

            loadTablse();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Save Status: " + e.getMessage(), "Save Status", JOptionPane.INFORMATION_MESSAGE);

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Save Status: " + e.getMessage(), "Save Status", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    public void savebtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        int oId = Integer.parseInt(OrderIDLBL.getText());
        String date = txtDate.getText();
        String nb = txtvehicleId.getText();
        String id = txId.getText();
        String type = lblName.getText();
        String price = txPrice.getText();
        String count = txCount.getText();
        String discount = txDiscount.getText();
        String rate = lblPrice.getText();
        String vehicleNb = txtvehicleId.getText();

        String ml = Ml.getText();
        String ml1 = Ml1.getText();
        String tel = customerTel.getText();


        orderDetails orderDetails = new orderDetails(nb,ml,ml1,tel,oId);

        if(nb == null || nb.isEmpty()){
            JOptionPane.showMessageDialog(null,"VEHICLE NB IS EMPTY","VALDATION ERROR",JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String rsps = DASHBORD_MANAGE_MODEL.deleteOrdDetails(oId);
            String rsp = DASHBORD_MANAGE_MODEL.insertVehicleNb(orderDetails);
            JOptionPane.showMessageDialog(null,"SAVE Orders"+rsp+JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null,"Error SAVE Orders","orders Save",JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException e) {

            JOptionPane.showMessageDialog(null,"Error SAVE Orders","orders Save",JOptionPane.INFORMATION_MESSAGE);
        }
        loadNextOrderId();
        loadTablse();
        clClear();



    }

    public void onActionOrderID(ActionEvent event) throws SQLException, ClassNotFoundException {
        clearDataAll();
        clClear();
        int oId = Integer.parseInt(OrderIDLBL.getText());
        ObservableList<String > ss = FXCollections.observableArrayList();
        try {
            List<String > xx = DASHBORD_MANAGE_MODEL.geCutIDs(oId);
            if (xx != null && xx.size() > 2) {
                txtvehicleId.setText(xx.get(0));  // First item: vehicleNb
                Ml.setText(xx.get(1));
                Ml1.setText(xx.get(2));
                customerTel.setText(xx.get(3));// Third item: nextmilagage

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        OnActionVehicle(event);
        OnActionUpdateBtn(event);
    }

    public void BillSearch(ActionEvent actionEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Daily.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void higaBillOnAction(ActionEvent actionEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/rf.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);

    }

    public void OnActionVehicle(ActionEvent actionEvent) {
        String id = txtvehicleId.getText();
        try {
            double hig = DASHBORD_MANAGE_MODEL.cekakHigaPrice(id);
            if(hig == 0 ){
                lblNameHiga.setText("");
                lblHigaCount.setText("");
            }else {
                lblNameHiga.setText("old Arries");
                lblHigaCount.setText(""+hig);
            }
        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {

        }
        Ml.requestFocus();
    }

    public void RePrintBill(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clJusperRiport();
    }

    public void ONMOuseRe(MouseEvent mouseEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("DASHBORD");
        stage.setResizable(true);
    }

    public void ViewStockOnAction(ActionEvent event) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/buystock.fxml"));
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(anchorPane));
        stage1.show();
        stage1.setResizable(false);
        stage1.setTitle("NET PROFIT");
    }

    public void onActionTel(ActionEvent event) {
        txtvehicleId.requestFocus();
    }

    public void onActionMileage(ActionEvent event) {
        Ml1.requestFocus();
    }

    public void OnActionNextMileage(ActionEvent event) {
    }
}