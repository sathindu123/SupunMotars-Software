package lk.ijse.supermarket.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.HelloApplication;
import lk.ijse.supermarket.model.LoginModel;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class HelloController extends HelloApplication {
    @FXML

    public Boolean sssss = false;
    public int sass = 10;
    public Button btnTest;
    private AnchorPane anchorPane;
    public int nbcont = 1;

    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtPassword;

    private LoginModel lg;

    public void initialize(){
        txtUserName.requestFocus();
        lg = new LoginModel();
    }

    public void HelloController(){
        lg = new LoginModel();
    }

    public void btnLogin(ActionEvent event) throws IOException {
        String un = txtUserName.getText();
        String up = txtPassword.getText();
        try {
            String userName = lg.getUderName();
            String password = lg.getPAssword();

            // Boolean ssss = lg.cheack(un,up);
            if(userName.equals(un) && password.equals(up)){
                // System.out.println("login");
                anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"));
                Scene scene = new Scene(anchorPane);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.setResizable(true);
                stage.setTitle("DASHBORD");
            }else {
                JOptionPane.showMessageDialog(null,"Incorect USerName OR Password","Validation Eroor",JOptionPane.ERROR_MESSAGE);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    public void onActionPassword(ActionEvent event) throws IOException {
        btnLogin(event);
    }

    public void onActionUser(ActionEvent event) {
        txtPassword.requestFocus();
    }

    public void OnMouseClick(MouseEvent mouseEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Forign.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
        stage.setResizable(true);
    }
}