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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.dto.DashBordManageDto;
import lk.ijse.supermarket.model.DashbordManageModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyController {
    private AnchorPane anchorPane;
    private DashbordManageModel DASHBORD_MANAGE_MODEL;
    private String dateprint = "";
    private int orderId = 0;
    String pricePrint = "";

    @FXML
    private TextField txtSearch;

    @FXML
    private ListView<String> ListView2;

    @FXML
    private TextField txtvehicleId;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField customerTel;

    @FXML
    private TextField Ml;

    @FXML
    private TextField Ml1;

    @FXML
    private Label lblNameHiga;

    @FXML
    private Label lblHigaCount;

    @FXML
    private ListView<String> ListView;

    @FXML
    private TableView<DashBordManageDto> tblLoad;

    @FXML
    private TableColumn<DashBordManageDto, String> clType;

    @FXML
    private TableColumn<DashBordManageDto, Double> clSellPrice;

    @FXML
    private TableColumn<DashBordManageDto, Integer> clCount;

    @FXML
    private TableColumn<DashBordManageDto, Integer> clDis;

    @FXML
    private TableColumn<DashBordManageDto, Double> clPrice;

    @FXML
    private TableColumn<DashBordManageDto, String> clNb;

    public void initialize() {
        ListView2.setVisible(false);
        DASHBORD_MANAGE_MODEL = new DashbordManageModel();

        // TextField key released event for suggestions
        txtSearch.setOnKeyReleased(this::handleKeyRelesed);

        // Table column setup
        clNb.setCellValueFactory(new PropertyValueFactory<>("nb"));
        clType.setCellValueFactory(new PropertyValueFactory<>("type"));
        clCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        clSellPrice.setCellValueFactory(new PropertyValueFactory<>("onePrice"));
        clDis.setCellValueFactory(new PropertyValueFactory<>("discount"));
        clPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Table column styles
        clCount.setStyle("-fx-alignment: CENTER-LEFT;-fx-font-size: 21px;");
        clDis.setStyle("-fx-alignment: CENTER-LEFT;-fx-font-size: 21px;");
        clPrice.setStyle("-fx-alignment: CENTER-LEFT;-fx-font-size: 21px;");
        clSellPrice.setStyle("-fx-font-size: 21px;");
        clNb.setStyle("-fx-font-size: 21px;");
        clType.setStyle("-fx-font-size: 21px;");

        // ListView setup
        ListView.setVisible(false);
        ListView.setOnMouseClicked(event -> {
            String slect = ListView.getSelectionModel().getSelectedItem();
            if (slect != null) {
                txtSearch.setText(slect);
                ListView.getItems().clear();
                ListView.setVisible(false);
                sudjectAuto(); // Load table or show ListView2
            }
        });

        // Key press for navigation and selection
        txtSearch.setOnKeyPressed(this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        if (!ListView.isVisible() || ListView.getItems().isEmpty()) {
            return;
        }

        int currentIndex = ListView.getSelectionModel().getSelectedIndex();

        switch (keyEvent.getCode()) {
            case DOWN:
                if (currentIndex < ListView.getItems().size() - 1) {
                    ListView.getSelectionModel().select(currentIndex + 1);
                    ListView.scrollTo(currentIndex + 1);
                }
                keyEvent.consume();
                break;

            case UP:
                if (currentIndex > 0) {
                    ListView.getSelectionModel().select(currentIndex - 1);
                    ListView.scrollTo(currentIndex - 1);
                }
                keyEvent.consume();
                break;

            case ENTER:
                String selected = ListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    txtSearch.setText(selected);
                    ListView.getItems().clear();
                    ListView.setVisible(false);
                    sudjectAuto(); // Trigger table load or ListView2
                }
                keyEvent.consume();
                break;

            case ESCAPE:
                ListView.getItems().clear();
                ListView.setVisible(false);
                keyEvent.consume();
                break;
        }
    }

    private void handleKeyRelesed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN ||
                keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.ESCAPE) {
            return;
        }

        String sc = txtSearch.getText().trim();
        if (sc.isEmpty()) {
            ListView.getItems().clear();
            ListView.setVisible(false);
            return;
        }

        List<String> sgest = new ArrayList<>();
        try {
            sgest = DASHBORD_MANAGE_MODEL.searchListItam(sc);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load suggestions: " + e.getMessage()).showAndWait();
        }

        if (!sgest.isEmpty()) {
            ListView.getItems().setAll(sgest);
            ListView.setVisible(true);
            ListView.getSelectionModel().selectFirst(); // Auto-select first suggestion
            // Do NOT request focus to keep it on txtSearch
        } else {
            ListView.getItems().clear();
            ListView.setVisible(false);
        }
    }

    private void sudjectAuto() {
        String id = txtSearch.getText().trim();
        if (id.isEmpty()) {
            tblLoad.getItems().clear();
            ListView2.setVisible(false);
            return;
        }

        ObservableList<DashBordManageDto> sli = FXCollections.observableArrayList();
        ObservableList<String> date = FXCollections.observableArrayList();

        try {
            List<String> listDate = DASHBORD_MANAGE_MODEL.ListDate(id);
            date.addAll(listDate);
            if (date.size() > 1) {
                ListView2.setVisible(true);
                ListView2.setItems(date);

                ListView2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        try {
                            dateprint = newSelection;
                            List<DashBordManageDto> sd = DASHBORD_MANAGE_MODEL.getDetailCHECKVEHICLENBDATE(id, newSelection);
                            sli.setAll(sd);
                            tblLoad.setItems(sli);
                         //   ListView2.setVisible(false);
                            tblLoad.refresh();
                        } catch (SQLException | ClassNotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, "Failed to load table data: " + e.getMessage()).showAndWait();
                        }
                    }
                });


                ListView2.getSelectionModel().selectFirst();
            } else {
                ListView2.setVisible(false);
                List<DashBordManageDto> sd = DASHBORD_MANAGE_MODEL.getDetailCHECKVEHICLENB(id);
                sli.addAll(sd);
                tblLoad.setItems(sli);
                tblLoad.refresh();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading data: " + e.getMessage()).showAndWait();
            tblLoad.getItems().clear();
        }
    }

    @FXML
    public void SearchNBOnAction(ActionEvent actionEvent) {
        sudjectAuto();
        refesh(actionEvent);
    }

    private void refesh(ActionEvent event) {
        String oId = txtSearch.getText().trim();
        ObservableList<String> ss = FXCollections.observableArrayList();
        try {
            List<String> xx = DASHBORD_MANAGE_MODEL.geCutI(oId);
            if (xx != null && xx.size() >= 4) {
                txtvehicleId.setText(xx.get(0));
                Ml.setText(xx.get(1));
                Ml1.setText(xx.get(2));
                customerTel.setText(xx.get(3));
            } else {
                xx = DASHBORD_MANAGE_MODEL.geCutIDss(oId);
                if (xx != null && xx.size() >= 4) {
                    txtvehicleId.setText(xx.get(0));
                    Ml.setText(xx.get(1));
                    Ml1.setText(xx.get(2));
                    customerTel.setText(xx.get(3));
                }
            }
            OnActionVehicle(event);
        } catch (SQLException | ClassNotFoundException e) {

        }
    }

    @FXML
    private void OnActionVehicle(ActionEvent actionEvent) {
        String id = txtvehicleId.getText();
        try {
            double hig = DASHBORD_MANAGE_MODEL.cekakHigaPrice(id);
            if (hig == 0) {
                lblNameHiga.setText("");
                lblHigaCount.setText("");
            } else {
                lblNameHiga.setText("old Arries");
                lblHigaCount.setText(String.valueOf(hig));
            }
        } catch (SQLException | ClassNotFoundException e) {

        }
    }

    public void OnMouseClick(MouseEvent mouseEvent) {
        // Add any specific mouse click logic if needed
    }

    public void ClickBack(MouseEvent mouseEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("DASHBORD");
        stage.setResizable(true);
    }

    public void clickRefarsh(MouseEvent mouseEvent) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource("/view/Daily.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void PayupbtnonAction(ActionEvent event) {
        ObservableList<String> ss = FXCollections.observableArrayList();
        String Vid = txtSearch.getText();
        if (dateprint == null || dateprint.equals("")){

            try {
                int oId = DASHBORD_MANAGE_MODEL.getOderId(Vid);
                orderId = oId;
                List<String> arry = DASHBORD_MANAGE_MODEL.getPrintDetailsJusper(orderId);
                if (arry != null && arry.size() >= 2) {
                    pricePrint = arry.get(0);
                    dateprint = arry.get(1);
                }
                clJusperRiport();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        else {

            try {
                int oId = DASHBORD_MANAGE_MODEL.getOderIdDate(Vid,dateprint);
                orderId = oId;
                List<String> arry = DASHBORD_MANAGE_MODEL.getPrintDetailsJusper(orderId);
                if (arry != null && arry.size() >= 2) {
                    pricePrint = arry.get(0);
                    dateprint = arry.get(1);
                }
            clJusperRiport();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void clJusperRiport() throws SQLException, ClassNotFoundException {

        Thread thread = new Thread(() -> {


            String vNb = txtvehicleId.getText();
            String tel = customerTel.getText();
            String aa = lblHigaCount.getText();
            String ml = Ml.getText();
            String ml1 = Ml1.getText();


            String invoice = ""+orderId;

            String numericString = pricePrint.replaceAll(",", "");
            double total;
            String ttt;
            try {
                total = Double.parseDouble(numericString);
                ttt = "" + total;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
            String higa = "" + total;
            String hh = "0";
            String id = txtvehicleId.getText();
            try {
                double hig = DASHBORD_MANAGE_MODEL.cekakHigaPrice(id);
                if (hig == 0) {

                } else {
                    hig = Math.abs(hig);
                    hh = "" + hig;
                    higa = "" + (total - hig);

                }
            } catch (SQLException e) {

            } catch (ClassNotFoundException e) {

            }

            try {
                JasperDesign design = JRXmlLoader.load("src/main/resources/Riport/Blank_A4.jrxml");

                JasperReport jasperReport = JasperCompileManager.compileReport(design);

                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("oId", orderId);
                parameters.put("Date", dateprint);
                parameters.put("VNb", vNb);
                parameters.put("Tel", tel);
                parameters.put("Total", higa);
                parameters.put("Ariyas", hh);
                parameters.put("SubTotal", ttt);
                parameters.put("InNb", invoice);
                parameters.put("ML", ml);
                parameters.put("ML1", ml1);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DBConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
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
    }

}