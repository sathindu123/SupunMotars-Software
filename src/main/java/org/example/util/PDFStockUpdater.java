
package org.example.util;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PDFStockUpdater {
    // Database connection details (modify these according to your database)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    public void updateStockFromPDF(String pdfPath) {
        try {
            // Load PDF

            File file = new File(pdfPath);
            PDDocument document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();


            // Split text into lines
            String[] lines = text.split("\n");

            // Establish database connection
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Process each line
                for (String line : lines) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length >= 3) {
                        String pdfId = parts[0];
                        String name = parts[1];
                        int quantity = Integer.parseInt(parts[2]);

                        // Check if stock exists
                        String selectSQL = "SELECT quantity FROM stock WHERE id = ?";
                        try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {
                            selectStmt.setString(1, pdfId);
                            ResultSet rs = selectStmt.executeQuery();

                            if (rs.next()) {
                                // Update existing stock
                                int currentQuantity = rs.getInt("quantity");
                                String updateSQL = "UPDATE stock SET quantity = ? WHERE id = ?";
                                try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
                                    updateStmt.setInt(1, currentQuantity + quantity);
                                    updateStmt.setString(2, pdfId);
                                    updateStmt.executeUpdate();
                                }
                            } else {
                                // Insert new stock
                                String insertSQL = "INSERT INTO stock (id, name, quantity) VALUES (?, ?, ?)";
                                try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
                                    insertStmt.setString(1, pdfId);
                                    insertStmt.setString(2, name);
                                    insertStmt.setInt(3, quantity);
                                    insertStmt.executeUpdate();
                                }
                            }
                        }
                    }
                }
                System.out.println("Stock updated successfully from PDF!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error updating stock: " + e.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading PDF: " + e.getMessage());
        }
    }

    private PDFTextStripper newStockUpdater() {
        return null;
    }

    public static void main(String[] args) {
        PDFStockUpdater updater = new PDFStockUpdater();
        updater.updateStockFromPDF("C:/path/to/your/supplier.pdf");
    }
}