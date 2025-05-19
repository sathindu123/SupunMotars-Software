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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.supermarket.dto.StockManageDto;
import lk.ijse.supermarket.model.StockManageModel;


import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;


public class StockControoler extends DashbordController {
    private static StockManageModel STOCK_MANGE_MODEL;

    @FXML
    private TableView<StockManageDto> tbleStock;
    @FXML
    private TableColumn<StockManageDto, String> clId;
    @FXML
    private TableColumn<StockManageDto, String> clType;
    @FXML
    private TableColumn<StockManageDto, Integer> clCount;
    @FXML
    private TableColumn<StockManageDto, Double> clPrice;
    @FXML
    private TableColumn<StockManageDto, String> clPlaceHolder;

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

    public StockControoler() {
        STOCK_MANGE_MODEL = new StockManageModel();
    }

    public void initialize() {
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


        clPrice.setStyle("-fx-alignment: CENTER-RIGHT;" + "-fx-font-size: 17px;");
        PP.setStyle("-fx-alignment: CENTER-RIGHT;" + "-fx-font-size: 17px;");
        clPlaceHolder.setStyle("-fx-alignment: CENTER-RIGHT;" + "-fx-font-size: 17px;");
        clCount.setStyle("-fx-alignment: CENTER-RIGHT;" + "-fx-font-size: 17px;");
        clId.setStyle("-fx-font-size: 17px;");
        clType.setStyle("-fx-font-size: 17px;");


        loadTableStock();

        ListSearch.setVisible(false);

        ListSearch.setOnMouseClicked(event -> {
            String slect = ListSearch.getSelectionModel().getSelectedItem();
            if (slect != null) {
                txtSeacrhBar.setText(slect);
                ListSearch.getItems().clear();
                ListSearch.setVisible(false);
            }
        });


    }

    private void handleKeyRelesed(KeyEvent keyEvent) {
        String sc = txtSeacrhBar.getText();
        if (sc.isEmpty()) {
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

        if (!sgest.isEmpty()) {
            ListSearch.getItems().setAll(sgest);
            AllGETITEM();
            ListSearch.setVisible(true);
            // AllGETITEM();
        } else {
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

        if (id == null || id.isEmpty() || type == null || type.isEmpty() ||
                count == null || count.isEmpty() || price == null || price.isEmpty()
                || plaseHolder == null || plaseHolder.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All filed must be not Completed", "Validation Error ", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cont = 0;
        double prc = 0;
        try {
            cont = Integer.parseInt(count);
            prc = Double.parseDouble(price);

        } catch (NumberFormatException e) {

        }

        StockManageDto stockManageDto = new StockManageDto(id, type, cont, sell, prc, plaseHolder);
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
        try {
            List<StockManageDto> Cdto = STOCK_MANGE_MODEL.getDetailsSEtId(id);
            if (Cdto.size() > 0) {
                StockManageDto data = Cdto.get(0);

                txtType.setText(String.valueOf(data.getType()));
                txtCount.setText(String.valueOf(data.getCount()));
                txtPrice.setText(String.valueOf(data.getPrice()));
                txtPlaceHolder.setText(String.valueOf(data.getPlaceHolder()));

            } else {
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

        if (id == null || id.isEmpty() || type == null || type.isEmpty() ||
                count == null || count.isEmpty() || price == null || price.isEmpty()
                || plaseHolder == null || plaseHolder.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All filed must be not Completed", "Validation Error ", JOptionPane.ERROR_MESSAGE);
        }

        int cont = 0;
        double prc = 0;
        try {
            cont = Integer.parseInt(count);
            prc = Double.parseDouble(price);

        } catch (NumberFormatException e) {

        }

        StockManageDto stockManageDto = new StockManageDto(id, type, cont, sell, prc, plaseHolder);
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
        double x = ((di * price) / 100);

        txtPrice.setText("" + (x + price));
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
            if (ss.isEmpty()) {
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

    public void fileOpen(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        // FileChooser ekak hadanawa
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File ekak select karanna");

        // (Optional) mula folder ekak denna
        // fileChooser.setInitialDirectory(new File("C:/"));

        // User kenek file ekak select karaddi

        File selectedFile = fileChooser.showOpenDialog(stage);

        // File ekak select kala nam
        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("File ekak select karanna athi bava nawatha balanna.");
        }

        processExcel(selectedFile.getAbsolutePath());


    }



    public void processExcel(String excelPath) {
        List<String[]> notFoundItems = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(excelPath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null || row.getCell(1) == null) continue;

                String partNo = row.getCell(1).getStringCellValue().trim();
                String type = row.getCell(2).getStringCellValue().trim();
                String netPrices = String.valueOf(row.getCell(4).getNumericCellValue());
                String qtys = String.valueOf(row.getCell(5).getNumericCellValue());

                double netPrice = 0;
                int qty = 0;
                try {
                    netPrice = Double.parseDouble(netPrices);
                    qty =(int) Double.parseDouble(qtys);

                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }

                double sellPrice = netPrice;
                double price = ((netPrice * 30)/100);
                price = price+netPrice;
                // check if partNo (id) exists
                boolean v1s = STOCK_MANGE_MODEL.selectCount(partNo, qty, price, sellPrice);
                if (v1s == false) {
                    notFoundItems.add(new String[]{partNo, type, String.valueOf(netPrice), String.valueOf(qty)});
                    for (String[] item : notFoundItems) {
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Write missing items to new Excel file
        if (!notFoundItems.isEmpty()) {
            try (Workbook newWorkbook = new XSSFWorkbook()) {
                Sheet sheet = newWorkbook.createSheet("Missing Items");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("PART NO");
                header.createCell(1).setCellValue("TYPE");
                header.createCell(2).setCellValue("NET PRICE");
                header.createCell(3).setCellValue("QTY");

                int rowIdx = 1;
                for (String[] item : notFoundItems) {
                    Row row = sheet.createRow(rowIdx++);
                    for (int j = 0; j < item.length; j++) {
                        row.createCell(j).setCellValue(item[j]);
                    }
                }

                String outputPath = System.getProperty("user.home") + "/Desktop/missing_parts.xlsx";
                File file = new File(outputPath);
                System.out.println("Writing missing parts Excel to: " + file.getAbsolutePath());

                try (FileOutputStream out = new FileOutputStream(file)) {
                    newWorkbook.write(out);
                }

                System.out.println("Missing items saved to 'missing_parts.xlsx'");
            } catch (IOException e) {
                System.err.println("Error while writing missing parts Excel file:");
                e.printStackTrace();
            }

            loadTableStock();
            openxcel();
        }

    }

    private static void openxcel() {
        try {
            String outputPath = System.getProperty("user.home") + "/Desktop/missing_parts.xlsx";
            File file = new File(outputPath);

            if (file.exists()) {
                Desktop.getDesktop().open(file); // This will open the file with default application (e.g., MS Excel)
            } else {
                System.out.println("File not found: " + outputPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


