package lk.ijse.supermarket.model;

import javafx.collections.ObservableList;
import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.dto.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockManageModel {
    public String InsertData(StockManageDto stockManageDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO stock VALUES(?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, stockManageDto.getId());
        statement.setString(2, stockManageDto.getType());
        statement.setInt(3, stockManageDto.getCount());
        statement.setDouble(4, stockManageDto.getSellPrice());
        statement.setDouble(5, stockManageDto.getPrice());
        statement.setString(6, stockManageDto.getPlaceHolder());

        int resp = statement.executeUpdate();
        return resp > 0 ? "sucsess" : "fail";

    }

    public List<StockManageDto> LoadTableStock() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT id,type,count,sellPrice,price,plase_Holder FROM stock";
        PreparedStatement statement = connection.prepareStatement(sql);

        List<StockManageDto> SList = new ArrayList<>();

        ResultSet rst = statement.executeQuery();

        while (rst.next()) {
            StockManageDto sDto = new StockManageDto(
                    rst.getString("id"),
                    rst.getString("type"),
                    rst.getInt("count"),
                    rst.getDouble("sellPrice"),
                    rst.getDouble("price"),
                    rst.getString("plase_Holder")
            );
            SList.add(sDto);
        }
        return SList;
    }

    public List<StockManageDto> getDetailsSEtId(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM stock WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        List<StockManageDto> SList = new ArrayList<>();
        statement.setString(1, id);

        ResultSet rst = statement.executeQuery();

        while (rst.next()) {
            StockManageDto sDto = new StockManageDto(
                    rst.getString("id"),
                    rst.getString("type"),
                    rst.getInt("count"),
                    rst.getDouble("price"),
                    rst.getString("plase_Holder")
            );
            SList.add(sDto);
        }
        return SList;
    }

    public String UpdateData(StockManageDto stockManageDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE stock SET type = ? ,count = ?, sellPrice = ?, price = ? ,plase_Holder = ? WHERE id = ? ";
        PreparedStatement statement = connection.prepareStatement(sql);


        statement.setString(1, stockManageDto.getType());
        statement.setInt(2, stockManageDto.getCount());
        statement.setDouble(3, stockManageDto.getSellPrice());
        statement.setDouble(4, stockManageDto.getPrice());
        statement.setString(5, stockManageDto.getPlaceHolder());
        statement.setString(6, stockManageDto.getId());

        int resp = statement.executeUpdate();
        return resp > 0 ? "sucsess" : "fail";
    }


    public List<DashBordManageDto> loadTb(int oId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT c.type,o.count,o.onePrice,o.oneDiscount,o.price FROM stock c " +
                "JOIN orders o ON c.id = o.productId where orderId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, oId);
        List<DashBordManageDto> SList = new ArrayList<>();

        ResultSet rst = statement.executeQuery();

        while (rst.next()) {
            DashBordManageDto sDto = new DashBordManageDto(
                    rst.getString("type"),
                    rst.getInt("count"),
                    rst.getDouble("onePrice"),
                    rst.getDouble("oneDiscount"),
                    rst.getDouble("price")
            );
            SList.add(sDto);
        }
        return SList;
    }


    public String paymentMethod() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement statement = null;
        ResultSet rst = null;
        try {
            connection.setAutoCommit(false);


            String sql = "SELECT productId, count FROM orders LIMIT 1";
            statement = connection.prepareStatement(sql);

            rst = statement.executeQuery();

            if (!rst.next()) {
                connection.rollback();
                return "Product not found"; // Highlighted: Changed to 'Product not found'
            }

            int count = rst.getInt("count");
            String name = rst.getString("productId");

            if (count > 0) {
                // Update the stock count
                String sql1 = "UPDATE stock SET count = count - ? WHERE id = ?";
                PreparedStatement statement1 = connection.prepareStatement(sql1);
                statement1.setInt(1, count);
                statement1.setString(2, name);

                if (statement1.executeUpdate() > 0) {
                    connection.commit();

                    // Corrected: Use executeUpdate() for DELETE statement
                    String sqlq = "DELETE FROM orders ORDER BY orderId LIMIT 1"; // Highlighted: Added ORDER BY to ensure it deletes the correct row
                    PreparedStatement statement2 = connection.prepareStatement(sqlq);
                    int rowsAffected = statement2.executeUpdate(); // Changed to executeUpdate

                    if (rowsAffected > 0) {
                        return "Order saved and deleted successfully"; // Highlighted: Improved success message
                    } else {
                        connection.rollback();
                        return "Failed to delete order"; // Highlighted: Added error message for DELETE failure
                    }
                } else {
                    connection.rollback();
                    return "Insufficient stock"; // Highlighted: Kept the existing message
                }

            } else {
                connection.rollback();
                return "Order save Error"; // Highlighted: Kept the existing message
            }
        } catch (Exception e) {
            connection.rollback();
            throw e; // Highlighted: Rethrow exception to handle it outside
        } finally {
            // Ensure that we close resources
            connection.setAutoCommit(true);
            statement.close(); // Highlighted: Close the statement
            rst.close(); // Highlighted: Close the result set
        }
    }

    public String insertDataStock(DashBordManageDto dto, StockManageDto sto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Check if stock exists and get the current count
            String sql = "SELECT count FROM stock WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, sto.getId());

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                connection.rollback();
                return "Item Not Found";
            }

            int currentStock = resultSet.getInt("count");

            // Check if sufficient stock is available
            if (currentStock < dto.getCount()) {
                connection.rollback();
                return "Insufficient Stock";
            }

            // Update stock count
            String sql2 = "UPDATE stock SET count = count - ? WHERE id = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, dto.getCount());
            statement2.setString(2, sto.getId());
            int stockUpdateResult = statement2.executeUpdate();

            if (stockUpdateResult <= 0) {
                connection.rollback();
                return "Failed to Update Stock";
            }

            // Insert order into `orders` table
            String sql1 = "INSERT INTO orders (orderId, productId, count, date, onePrice, oneDiscount, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement1 = connection.prepareStatement(sql1);

            statement1.setInt(1, dto.getOrderId());
            statement1.setString(2, dto.getProductId());
            statement1.setInt(3, dto.getCount());
            statement1.setString(4, dto.getDate());
            statement1.setDouble(5, dto.getOnePrice());
            statement1.setDouble(6, dto.getDiscount());
            statement1.setDouble(7, dto.getPrice());

            int orderInsertResult = statement1.executeUpdate();

            if (orderInsertResult > 0) {
                // Commit transaction if everything is successful
                connection.commit();
                return "Saved Successfully";
            } else {
                connection.rollback();
                return "Order Save Error";
            }
        } catch (Exception e) {
            // Rollback transaction on exception
            connection.rollback();
            throw e;
        } finally {
            // Reset auto-commit to true
            connection.setAutoCommit(true);
        }
    }

    public String EqalsDetailCheack(String id, int oId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM orders WHERE productId = ? and orderId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, id);
        statement.setInt(2, oId);

        ResultSet rst = statement.executeQuery();

        if (!rst.next()) {
            return "Sucsess";
        }

        return "fail";
    }

    public String deleteData(String id, int count, double sellPrice, double dis, double price) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            // Begin transaction
            connection.setAutoCommit(false);

            // Get stock ID based on the product type
            String sql1 = "SELECT id FROM stock WHERE type = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, id);
            ResultSet resultSet = statement1.executeQuery();
            String stockId = "";

            if (resultSet.next()) {
                stockId = resultSet.getString("id");
            } else {
                connection.rollback();
                return "Item not found in stock";
            }

            // Update stock count by adding back the quantity
            String sql2 = "UPDATE stock SET count = count + ? WHERE id = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, count);
            statement2.setString(2, stockId);

            int stockUpdateResult = statement2.executeUpdate();
            if (stockUpdateResult <= 0) {
                connection.rollback();
                return "Failed to update stock count";
            }

            // Delete the order from the `orders` table
            String sql3 = "DELETE FROM orders WHERE productId = ? AND count = ? AND onePrice = ? AND oneDiscount = ? AND price = ?";
            PreparedStatement statement3 = connection.prepareStatement(sql3);

            statement3.setString(1, stockId);
            statement3.setInt(2, count);
            statement3.setDouble(3, sellPrice);
            statement3.setDouble(4, dis);
            statement3.setDouble(5, price);

            int orderDeleteResult = statement3.executeUpdate();
            if (orderDeleteResult > 0) {
                connection.commit();
                return "SUCCESS DELETE";
            } else {
                connection.rollback();
                return "Failed to delete order";
            }
        } catch (SQLException e) {
            // Rollback transaction on error
            connection.rollback();
            throw new RuntimeException("Error deleting data: " + e.getMessage(), e);
        } finally {
            // Restore auto-commit mode
            connection.setAutoCommit(true);
        }
    }

    public List<DashBordManageDto> pastDetailPayment() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM orders";
        PreparedStatement statement = connection.prepareStatement(sql);

        List<DashBordManageDto> DLs = new ArrayList<>();

        ResultSet rst = statement.executeQuery();

        while (rst.next()) {
            DashBordManageDto dd = new DashBordManageDto(
                    rst.getInt(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getDouble(6),
                    rst.getDouble(7)
            );
            DLs.add(dd);
        }
        return DLs;
    }


    public double getTotalPrice(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT SUM(price) as price FROM orders where orderId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rst = statement.executeQuery();

        double pric = 0.00;

        if (rst.next()) {
            pric = rst.getInt("price");
        }
        return pric;
    }

    public List<StockManageDto> searchItem(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM stock WHERE type = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id);

        List<StockManageDto> Ss = new ArrayList<>();
        ResultSet rst = statement.executeQuery();

        while (rst.next()) {
            StockManageDto sto = new StockManageDto(rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4));

            Ss.add(sto);
        }
        return Ss;
    }

    public String UpdateDataStock(DashBordManageDto dto, StockManageDto sto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            String sql = "SELECT count from stock WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, sto.getId());

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                connection.rollback();
                return "Item Not Fround";
            }

            int countss = resultSet.getInt("count");

            String sql1 = "UPDATE orders SET count = ?,date = ?,onePrice = ?,oneDiscount = ? ,price = ? WHERE productId = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);


            statement1.setInt(1, dto.getCount());
            statement1.setString(2, dto.getDate());
            statement1.setDouble(3, dto.getOnePrice());
            statement1.setDouble(4, dto.getDiscount());
            statement1.setDouble(5, dto.getPrice());
            statement1.setString(6, dto.getProductId());

            boolean isord = statement1.executeUpdate() > 0;

            if (isord) {
                if (countss >= dto.getCount()) {
                    connection.commit();
                    return "Saved";
                } else {
                    connection.rollback();
                    return "Insufficient stock";
                }

            } else {
                connection.rollback();
                return "Order save Error";
            }
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);

        }
    }

    public String updateData(String id, int count, double sellPrice, double dis, double price) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "select id from stock where type = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, id);
        ResultSet resultSet = statement1.executeQuery();
        String name = "";
        if (resultSet.next()) {
            name = resultSet.getString(1);
            System.out.println(name);
        }

        String sql = "update FROM orders SET count = ? AND onePrice = ? AND oneDiscount = ? AND price = ? WHERE productId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);


        statement.setInt(1, count);
        statement.setDouble(2, sellPrice);
        statement.setDouble(3, dis);
        statement.setDouble(4, price);
        statement.setString(5, name);

        int rst = statement.executeUpdate();

        return rst > 0 ? "SUCSESS DELETE " : "FAIL";
    }

    public String InsertPaymentDetails(CustomerVehicelDto dtos, int odId, ObservableList<DashBordManageDto> dds, String vehicleNb, HigabillDto higabillDto, double tots, String ml, String ml1, String tel) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        String sql = "INSERT INTO payments (orderId, productId, count, date, onePrice, discount, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(sql);

            for (DashBordManageDto dd : dds) {
                // Set the parameters for the current payment record
                statement.setInt(1, odId);
                statement.setString(2, dd.getProductId());
                statement.setInt(3, dd.getCount());
                statement.setString(4, dd.getDate());
                statement.setDouble(5, dd.getOnePrice());
                statement.setDouble(6, dd.getDiscount());
                statement.setDouble(7, dd.getPrice());


                int result = statement.executeUpdate();


                if (result <= 0) {
                    connection.rollback();
                    return "FAILURE: Unable to insert payment details.";
                }


            }

                if (tots < 0) {
                    String query = "INSERT INTO higabill VALUES (?,?,?,?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setInt(1, higabillDto.getOrderId());
                    preparedStatement.setString(2, higabillDto.getVehicleId());
                    preparedStatement.setString(3, higabillDto.getCustomerTel());
                    preparedStatement.setString(4, higabillDto.getDate());
                    preparedStatement.setDouble(5, higabillDto.getTotalPrice());
                    preparedStatement.setDouble(6, higabillDto.getHiga());

                    int resultSet = preparedStatement.executeUpdate();
                    if (resultSet <= 0) {
                        connection.rollback();
                        return "FAILURE: Unable to insert payment details.";
                    }
                }


           paybtn(odId);

            String sql2 = "insert into orderdetails VALUES(?,?,?,?,?)";
            PreparedStatement statement1 = connection.prepareStatement(sql2);
            statement1.setString(1, vehicleNb);
            statement1.setString(2, ml);
            statement1.setString(3, ml1);
            statement1.setString(4, tel);
            statement1.setInt(5, odId);
            int results = statement1.executeUpdate();
            if (results <= 0) {
                connection.rollback();
                return "FAILURE: Unable to insert payment details.";
            }




            connection.commit();
            return "SUCCESS";
        } catch (SQLException e) {

            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e; // Rethrow the exception to let the caller handle it
        } finally {
            if (statement != null) {
                statement.close(); // Close the prepared statement
            }
            if (connection != null) {
                connection.setAutoCommit(true); // Reset autocommit to true
            }
        }
    }

    private void paybtn(int odId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "delete from orderdetails where orderId = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setInt(1, odId);
        int rst = statement1.executeUpdate();

    }

    public List<DashBordManageDto> pastDetailPayments(int odId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        String sql = "SELECT o.orderId,s.type,o.count,o.date,o.onePrice,o.oneDiscount,o.price FROM orders o JOIN stock s ON o.productId = s.id where orderId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, odId);
        List<DashBordManageDto> DLs = new ArrayList<>();

        ResultSet rst = statement.executeQuery();

        while (rst.next()) {
            DashBordManageDto dd = new DashBordManageDto(
                    rst.getInt("orderId"),
                    rst.getString("type"),
                    rst.getInt("count"),
                    rst.getString("date"),
                    rst.getDouble("onePrice"),
                    rst.getDouble("oneDiscount"),
                    rst.getDouble("price")
            );
            DLs.add(dd);
        }
        return DLs;

    }

    public double getAlLPrice() throws SQLException, ClassNotFoundException {
        double tot = 0;

        // Get database connection
        Connection connection = DBConnection.getInstance().getConnection();

        // SQL query to calculate the total price (count * sellPrice)
        String sql = "SELECT count, sellPrice FROM stock";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        // Iterate through the result set and calculate the total price
        while (resultSet.next()) {
            int count = resultSet.getInt("count");
            double sellPrice = resultSet.getDouble("sellPrice");

            // Multiply count by sellPrice and add to total
            tot += count * sellPrice;
        }


        // Return the total price
        return tot;
    }


    public String deletes(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "delete from stock where id = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, id);
        int resp = statement1.executeUpdate();
        return resp > 0 ? "sucsess" : "fail";
    }

    public List<String> searchListItam(String sc) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "select type from stock where type LIKE ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, sc + "%");
        ResultSet rst = statement1.executeQuery();

        List<String> sugest = new ArrayList<>();
        while (rst.next()) {
            sugest.add(rst.getString("type"));
        }
        if (sugest.isEmpty()){
            String sql11 = "select id from stock where id LIKE ?";
            PreparedStatement statement11 = connection.prepareStatement(sql11);
            statement11.setString(1, sc + "%");
            ResultSet rst1 = statement11.executeQuery();


            while (rst1.next()) {
                sugest.add(rst1.getString("id"));
            }
        }
        return sugest;
    }

    public List<StockManageDto> getDetailsEqualType(String type) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "select * from stock where type = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, type);
        ResultSet resultSet = statement1.executeQuery();
        List<StockManageDto> ss = new ArrayList<>();
        while (resultSet.next()) {
            StockManageDto sDto = new StockManageDto(
                    resultSet.getString("id"),
                    resultSet.getString("type"),
                    resultSet.getInt("count"),
                    resultSet.getDouble("sellPrice"),
                    resultSet.getDouble("price"),
                    resultSet.getString("plase_Holder")
            );
            ss.add(sDto);
        }
        return ss;
    }

    public int getAllCount() throws SQLException, ClassNotFoundException {
//        Connection connection = DBConnection.getInstance().getConnection();
//        String sql1 = "select  from stock where type = ?";
//        PreparedStatement statement1 = connection.prepareStatement(sql1);
//        statement1.setString(1, type);
//        ResultSet resultSet = statement1.executeQuery();
        return 0;
    }

    public List<StockManageDto> getDetailsEqualType1(String type) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "select * from stock where id = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, type);
        ResultSet resultSet = statement1.executeQuery();
        List<StockManageDto> ss = new ArrayList<>();
        while (resultSet.next()) {
            StockManageDto sDto = new StockManageDto(
                    resultSet.getString("id"),
                    resultSet.getString("type"),
                    resultSet.getInt("count"),
                    resultSet.getDouble("sellPrice"),
                    resultSet.getDouble("price"),
                    resultSet.getString("plase_Holder")
            );
            ss.add(sDto);
        }
        return ss;
    }

    public List<StockManageDto> searchItems(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM stock WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id);

        List<StockManageDto> Ss = new ArrayList<>();
        ResultSet rst = statement.executeQuery();

        while (rst.next()) {
            StockManageDto sto = new StockManageDto(rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4));

            Ss.add(sto);
        }
        return Ss;
    }

    public String getItemid(String ids) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT id FROM stock WHERE type = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, ids);
        String ss = "";
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            ss = resultSet.getString(1);
        }
        return ss;
    }

    public boolean deleteOrders(int odId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        String sql2 = "delete from payments where orderId = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql2);
        statement1.setInt(1,odId);
        int rst1 = statement1.executeUpdate();

        return rst1 > 0 ;



    }

    public List<StockManageDto> loadTbl(String oID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        String sql = "select id, count, plase_Holder, price from stock where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, oID);
        ResultSet resultSet = statement.executeQuery();
        List<StockManageDto> ss = new ArrayList<>();
        while (resultSet.next()) {
            StockManageDto sDto = new StockManageDto(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            );
            ss.add(sDto);
        }

        if (ss.isEmpty()){
            String sql1 = "select id, count, plase_Holder, price from stock where type = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, oID);
            ResultSet resultSet1 = statement1.executeQuery();
            while (resultSet1.next()) {
                StockManageDto sDto = new StockManageDto(
                        resultSet1.getString(1),
                        resultSet1.getInt(2),
                        resultSet1.getString(3),
                        resultSet1.getDouble(4)
                );
                ss.add(sDto);
            }
        }
        return ss;
    }
    public boolean downcount(String id, int inputCount) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        String selectSql = "SELECT count FROM stock WHERE id = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setString(1, id);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                int currentCount = resultSet.getInt("count");

                if (currentCount < inputCount) {
                    return false;
                }
                String updateSql = "UPDATE stock SET count = count - ? WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, inputCount);
                    updateStmt.setString(2, id);
                    int rowsAffected = updateStmt.executeUpdate();

                    return rowsAffected > 0;
                }

            }

        }
        return up(id, inputCount);
    }
    private boolean up(String id, int inputCount) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        String selectSql = "SELECT count FROM stock WHERE type = ?";
        PreparedStatement selectStmt = connection.prepareStatement(selectSql);
        selectStmt.setString(1, id);
        ResultSet resultSet = selectStmt.executeQuery();

        int currentCount = 0;
        if (resultSet.next()) {
            currentCount = resultSet.getInt("count");
        } else {
            return false;
        }

        if (currentCount < inputCount) {
            return false;
        }
        // Step 3: Proceed with the update if the check passes
        String updateSql = "UPDATE stock SET count = count - ? WHERE type = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateSql);
        updateStmt.setInt(1, inputCount);
        updateStmt.setString(2, id);
        int rowsAffected = updateStmt.executeUpdate();

        return rowsAffected > 0;
    }

    public boolean selectCount(String partNo, int qty, double price, double sellPrice) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String selectSQL = "SELECT count FROM stock WHERE id = ?";
        PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
        selectStmt.setString(1, partNo);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            int currentCount = rs.getInt("count");
            int newCount = currentCount + qty;


            // update record
            String updateSQL = "UPDATE stock SET count = ?, sellPrice = ?, price = ? WHERE id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSQL);
            updateStmt.setInt(1, newCount);
            updateStmt.setDouble(2, sellPrice);
            updateStmt.setDouble(3, price);
            updateStmt.setString(4, partNo);
            updateStmt.executeUpdate();

            return updateStmt.executeUpdate() > 0 ;
        }
        return false;
    }
}


