package lk.ijse.supermarket.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.model.LoginModel;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class ForeignController extends HelloController{
    private LoginModel lg;
    @FXML
    private TextField CU;
    @FXML
    private TextField CP;
    @FXML
    private TextField CPC;
    @FXML
    private TextField NU;
    @FXML
    private TextField NP;
    @FXML
    private TextField NPC;

    public void ForeignController(){

    }

    public void initialize(){
        lg = new LoginModel();
    }

    public void SaveOnAction(ActionEvent event) {
        String cu = CU.getText();
        String cp = CP.getText();
        String cpc = CPC.getText();
        String np = NP.getText();
        String nu = NU.getText();
        String npc = NPC.getText();

        try {
            Boolean Sli = lg.cheach(cu,cp,cpc);
            if(Sli == true){
                if(np.equals(np)){
                    String resp =lg.updateCreadtitaonal(cu,np,nu);
                    JOptionPane.showMessageDialog(null, "Save Status: " + resp, "Save Status", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null,"cheack Password","Validation Eroor",JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"Incorect USerName OR Password","Validation Eroor",JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error occurred" + e.getMessage());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Error occurred" + e.getMessage());
        }

    }



    public void Logount(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
        stage.setResizable(true);
    }
}
