package lk.ijse.supermarket.model;

import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.dto.PaymentDto;
import lk.ijse.supermarket.dto.StockManageDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    public List<StockManageDto> getAllDetails() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM stock";
        PreparedStatement statement = connection.prepareStatement(sql);

        List<StockManageDto>SList = new ArrayList<>();

        ResultSet rst = statement.executeQuery();

        while (rst.next()){
            StockManageDto sDto = new StockManageDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4),
                    rst.getString(5)
            );
            SList.add(sDto);
        }
        return SList;
    }

    public List<PaymentDto> loadDailyTable() throws SQLException, ClassNotFoundException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(dateFormatter);

        Connection connection = DBConnection.getInstance().getConnection();
        // Use a placeholder for the date in the SQL query
        String sql = "SELECT s.id, c.productId, SUM(c.count) AS count, SUM(c.price) AS price\n" +
                "FROM payments c\n" +
                "LEFT JOIN stock s ON c.productId = s.type\n" +
                "WHERE c.date = ? \n" +
                "GROUP BY c.productId, c.date;\n";

        PreparedStatement statement = connection.prepareStatement(sql);
        // Set the formatted date parameter to the prepared statement
        statement.setString(1, formattedDate);

        List<PaymentDto> SList = new ArrayList<>();
        ResultSet rst = statement.executeQuery();

        while (rst.next()){
            PaymentDto pp = new PaymentDto(
                    rst.getString(1),  // Assuming s.id is a string
                    rst.getString(2),  // Assuming c.productId is a string
                    rst.getInt(3),     // Assuming c.count is an integer
                    rst.getDouble(4)   // Assuming c.price is a double
            );
            SList.add(pp);
        }
        return SList;
    }


    public double getTotalPrice() throws SQLException, ClassNotFoundException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(dateFormatter);
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT SUM(price)as price FROM payments WHERE date = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,formattedDate);

        double pc = 0;
         ResultSet resultSet = statement.executeQuery();

         if(resultSet.next()){
             pc = resultSet.getDouble("price");
         }
         return pc;
    }

    public double netProfit() throws SQLException, ClassNotFoundException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(dateFormatter);
        Connection connection = DBConnection.getInstance().getConnection();

        double totalProfit = 0;

        // 1. Get all product IDs for today's payments
        String sql = "SELECT productId FROM payments WHERE date = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, formattedDate);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String productId = resultSet.getString("productId");

            // 2. For each productId, get the sellPrice from stock
            String sql2 = "SELECT sellPrice FROM stock WHERE type = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, productId);
            ResultSet resultSet2 = statement2.executeQuery();

            if (resultSet2.next()) {
                double sellPrice = resultSet2.getDouble("sellPrice");
                totalProfit += sellPrice; // add to total
            }

            // Close inner statement and resultSet
            resultSet2.close();
            statement2.close();
        }

        // Close outer statement and resultSet
        resultSet.close();
        statement.close();

        return totalProfit;
    }

}
