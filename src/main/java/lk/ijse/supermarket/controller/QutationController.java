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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.dto.QtDetaiilsDto;
import lk.ijse.supermarket.dto.QtyDto;
import lk.ijse.supermarket.model.QtatinModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class QutationController extends DashbordController {
    private AnchorPane anchorPane;
    private QtatinModel QT_MODEL;

    private String stockId;

    @FXML
    private Button savebtn;
    @FXML
    private ToggleButton Qutation;
    @FXML
    private TextField txtQtId;

    @FXML
    private TableView<QtyDto> tblQt;

    @FXML
    private TableColumn<QtyDto, String > clDis;

    @FXML
    private TableColumn<QtyDto, String> clCount;

    @FXML
    private TableColumn<QtyDto, String> clPrice;

    @FXML
    private TextField txtDiscription;

    @FXML
    private TextField txtCount;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtVID;

    @FXML
    private TextField xtName;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtAdvance;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblBalance;

    @FXML
    private Button btnAdd;

    public void initialize() {
        savebtn.setDisable(true);
        btnAdd.setDisable(false);
        cearFull();
        QT_MODEL = new QtatinModel();
        clDis.setCellValueFactory(new PropertyValueFactory<>("type"));
        clCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        clPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(dateFormatter);
        txtDate.setText(formattedDate);
        loadNextQtID();
        loadTableQT();
        Qutation.setDisable(true);

    }

    private void loadTableQT() {

        int id = Integer.parseInt(txtQtId.getText());


        ObservableList<QtyDto> qDto = FXCollections.observableArrayList();

        try {
            List<QtyDto> ss = QT_MODEL.loadTableQt(id);
            qDto.addAll(ss);
            tblQt.setItems(qDto);
        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {

        }

        try {
            double price = QT_MODEL.getPriceQR(id);
            lblPrice.setText(""+price);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

    }

    private void loadNextQtID() {
        try {
            String id = QT_MODEL.getQtId();
            txtQtId.setText(id);
        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {

        }


    }


    public void onClickBack(MouseEvent mouseEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("DASHBORD");
        stage.setResizable(true);
    }

    public void RefershMouse(MouseEvent mouseEvent) {
        initialize();
    }




    public void DisOnAction(javafx.event.ActionEvent actionEvent) {
        txtCount.requestFocus();
//        String stID = txtDiscription.getText();
//        stockId = stID;
//        try {
//            String name = QT_MODEL.getType(stID);
//            if (name.equals("")||name == ""){
//
//            }else {
//                txtDiscription.setText(name);
//
//            }
//        } catch (SQLException e) {
//
//        } catch (ClassNotFoundException e) {
//
//        }
    }

    public void QtyOnAction(javafx.event.ActionEvent actionEvent) {
        txtPrice.requestFocus();
//        int count = Integer.parseInt(txtCount.getText());
//        String stID = txtDiscription.getText();
//
//        try {
//            double price = QT_MODEL.getPrice(stockId);
//            price = (double)price*count;
//            txtPrice.setText(""+price);
//
//        } catch (SQLException e) {
//
//        } catch (ClassNotFoundException e) {
//
//        }
    }

    public void PriceOnAction(javafx.event.ActionEvent actionEvent) {
        AddOnActionQty(actionEvent);
    }

    public void PayUpOnAction(javafx.event.ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int id = Integer.parseInt(txtQtId.getText());

        String vId = txtVID.getText();
        String name = xtName.getText();
        String Date = txtDate.getText();
        String tel = txtTel.getText();



        if(vId == null || vId.isEmpty() || name == null || name.isEmpty() || Date == null || Date.isEmpty() || tel == null || tel.isEmpty()){
            JOptionPane.showMessageDialog(null,"All field must be not compelted","Validation Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        QtDetaiilsDto dto = new QtDetaiilsDto(id,vId,name,Date,tel);


        try {
            String rsp = QT_MODEL.insertQtDetails(dto);
            JOptionPane.showMessageDialog(null,"SAVE "+rsp+JOptionPane.INFORMATION_MESSAGE);
            loadTableQT();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"SAVE "+e.getMessage()+JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"SAVE "+e.getMessage()+JOptionPane.INFORMATION_MESSAGE);
        }
        cllJasperQ();


    }

    private void cllJasperQ() {
        Thread thread = new Thread(() ->{
            String QId = txtQtId.getText();
            String date = txtDate.getText();
            String vnb = txtVID.getText();
            String tel = txtTel.getText();
            String name = xtName.getText();
            String tot = lblPrice.getText();
            try {
                JasperDesign design = JRXmlLoader.load("src/main/resources/Riport/sathi.jrxml");

                JasperReport jasperReport = JasperCompileManager.compileReport(design);

                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("OID",QId);
                parameters.put("Date",date);
                parameters.put("VNB",vnb);
                parameters.put("Tel",tel);
                parameters.put("TOT",tot);
                parameters.put("Name",name);


                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, DBConnection.getInstance().getConnection());
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

        loadNextQtID();
        cearFull();
        loadTableQT();

    }

    private void cearFull() {
        txtDiscription.requestFocus();
        txtDiscription.setText("");
        txtCount.setText("");
        txtPrice.setText("");
        txtVID.setText("");
        xtName.setText("");
        txtTel.setText("");

        txtAdvance.setText("");
        lblBalance.setText("0.00");


    }

    public void PrintBillOnAction(javafx.event.ActionEvent actionEvent) {
        cllJasperQ();
    }

    public void SaveOnaction(javafx.event.ActionEvent actionEvent) {
        int id = Integer.parseInt(txtQtId.getText());

        String vId = txtVID.getText();
        String name = xtName.getText();
        String Date = txtDate.getText();
        String tel = txtTel.getText();

        if(vId == null || vId.isEmpty() || name == null || name.isEmpty() || Date == null || Date.isEmpty() || tel == null || tel.isEmpty()){
            JOptionPane.showMessageDialog(null,"All field must be not compelted","Validation Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        QtDetaiilsDto dto = new QtDetaiilsDto(id,vId,name,Date,tel);

        try {
            String rsps = QT_MODEL.deleteQt(id);
            String rsp = QT_MODEL.insertQtDetails(dto);
            JOptionPane.showMessageDialog(null,"SAVE "+rsp+JOptionPane.INFORMATION_MESSAGE);
            loadTableQT();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"SAVE "+e.getMessage()+JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"SAVE "+e.getMessage()+JOptionPane.INFORMATION_MESSAGE);
        }
        cearFull();
        loadNextQtID();

    }

    public void ReturnOnAction(javafx.event.ActionEvent actionEvent) {

    }

    public void AddOnActionQty(javafx.event.ActionEvent actionEvent) {
        int id = Integer.parseInt(txtQtId.getText());
        String dis = txtDiscription.getText();
        int count = Integer.parseInt(txtCount.getText());
        double price = Double.parseDouble(txtPrice.getText());
        String stID = txtDiscription.getText();

        QtyDto dto = new QtyDto(id,dis,count,price);
        try {
            String rsp = QT_MODEL.insertQt(dto,stockId,count);
            JOptionPane.showMessageDialog(null,"SAVE "+rsp+JOptionPane.INFORMATION_MESSAGE);
            loadTableQT();
            double tot = QT_MODEL.getTotal(id);
            lblPrice.setText(""+tot);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"SAVE "+e.getMessage()+JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        clearHaf();

    }

    private void clearHaf() {
        txtDiscription.requestFocus();
        txtDiscription.setText("");
        txtCount.setText("");
        txtPrice.setText("");
    }

    public void InvoiceOnAction(ActionEvent actionEvent) {
        int id = Integer.parseInt(txtQtId.getText());


        ObservableList<QtyDto> qDto = FXCollections.observableArrayList();

        try {
            double price = QT_MODEL.getPriceQR(id);
            lblPrice.setText(""+price);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        try {
            List<QtyDto> ss = QT_MODEL.loadTableQt(id);
            qDto.addAll(ss);
            tblQt.setItems(qDto);
            String vid = QT_MODEL.getVNB(id);
            txtVID.setText(vid);
            String tel = QT_MODEL.getTel(id);
            txtTel.setText(tel);
            String date = QT_MODEL.getDate(id);
            txtDate.setText(date);
            String Name = QT_MODEL.getName(id);
            xtName.setText(Name);



        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {

        }
    }

    public void OnActionBalance(ActionEvent actionEvent) {
        double bb = Double.parseDouble(txtAdvance.getText());
        int oo = Integer.parseInt(txtQtId.getText());
        try {
            double price = QT_MODEL.getPriceQR(oo);
            lblBalance.setText(""+(bb-price));
        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {

        }
    }

    public void AddOnAction(ActionEvent actionEvent) {
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        String tt = txtDiscription.getText();
        int count = Integer.parseInt(txtCount.getText());
        String type = txtDiscription.getText();

        try {
            String rsp = QT_MODEL.delete(tt,count,type);
            JOptionPane.showMessageDialog(null,"SAVE "+rsp+JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {

        }
        loadTableQT();
    }

    public void ResetOnActioon(ActionEvent actionEvent) {
        btnAdd.setDisable(false);
        cearFull();
    }

    public void MouseClickOn(MouseEvent mouseEvent) {
        btnAdd.setDisable(false);
        txtDiscription.setText(tblQt.getSelectionModel().getSelectedItem().getType());
        txtCount.setText(""+tblQt.getSelectionModel().getSelectedItem().getCount());
        txtPrice.setText(""+tblQt.getSelectionModel().getSelectedItem().getPrice());

    }

    public void VNBOnaction(ActionEvent event) {
        xtName.requestFocus();
    }

    public void NameOnaction(ActionEvent event) {
        txtTel.requestFocus();
    }

    public void TelOnAction(ActionEvent event) {
    }
}
