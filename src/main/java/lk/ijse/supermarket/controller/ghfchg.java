package lk.ijse.supermarket.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.dto.HigabillDto;
import lk.ijse.supermarket.model.DashbordManageModel;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ghfchg {
    private AnchorPane anchorPane;
    private DashbordManageModel DASHBORD;

    @FXML
    private Button update;

    @FXML
    private Button deleteBtn;

    @FXML
    private TextField addPrice;

    @FXML
    private TableView<HigabillDto> tblHiga;

    @FXML
    private TableColumn<HigabillDto, Integer> clOrderId;

    @FXML
    private TableColumn<HigabillDto, String> clVehicleId;

    @FXML
    private TableColumn<HigabillDto, String> clTel;

    @FXML
    private TableColumn<HigabillDto, String> clDate;

    @FXML
    private TableColumn<HigabillDto, Double> clTotalPrice;

    @FXML
    private TableColumn<HigabillDto, Double> clHiga;

    @FXML
    private TextField txtSearc;

    private int ordid;
    private double higamoney;

    public void ghfchg(){

    }

    public void initialize(){
        deleteBtn.setDisable(true);
        update.setDisable(true);
        DASHBORD = new DashbordManageModel();
        clOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        clVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        clTel.setCellValueFactory(new PropertyValueFactory<>("customerTel"));
        clDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        clHiga.setCellValueFactory(new PropertyValueFactory<>("higa"));

        loadTableHIga();
    }

    private void loadTableHIga() {
        ObservableList<HigabillDto> dtos = FXCollections.observableArrayList();
        try {
            List<HigabillDto> sss = DASHBORD.loadTableHIga();
            dtos.addAll(sss);
            tblHiga.setItems(dtos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void tblMouseClick(MouseEvent mouseEvent) {
        update.setDisable(false);
        deleteBtn.setDisable(false);
        ordid = tblHiga.getSelectionModel().getSelectedItem().getOrderId();
        higamoney = tblHiga.getSelectionModel().getSelectedItem().getHiga();

    }

    public void OnactionSearch(ActionEvent actionEvent) {
        String  id =txtSearc.getText();
        ObservableList<HigabillDto> ss = FXCollections.observableArrayList();
        try {
                List<HigabillDto> rsps = DASHBORD.cheackIDHigav(id);
                ss.addAll(rsps);
                tblHiga.setItems(ss);
        } catch (Exception e) {

        }
    }

    public void updateBtn(ActionEvent actionEvent) {
        double ss = Double.parseDouble(addPrice.getText());
       // System.out.println(higamoney);
        double tot = ss+(higamoney);
        try {
            String rsp = DASHBORD.updateHiga(ordid,tot);
            loadTableHIga();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletebtn(ActionEvent actionEvent) {
        try {
            String rsp = DASHBORD.deleteHiga(ordid);
            JOptionPane.showMessageDialog(null,"Sucssess Delete"+rsp+JOptionPane.INFORMATION_MESSAGE);
            loadTableHIga();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error SAVE Orders","orders Save",JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Error SAVE Orders","orders Save",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void BackBtn(MouseEvent mouseEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("DASHBORD");
        stage.centerOnScreen();
        stage.setResizable(true);
    }

  

    public void AddPrice(ActionEvent actionEvent) {
        updateBtn(actionEvent);
    }

    public void resetBtns(ActionEvent actionEvent) {
        deleteBtn.setDisable(true);
        update.setDisable(true);
    }

    public void resetBtn(MouseEvent mouseEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/rf.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}
