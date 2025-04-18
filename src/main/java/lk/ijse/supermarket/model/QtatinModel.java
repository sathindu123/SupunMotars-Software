package lk.ijse.supermarket.model;

import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.dto.QtDetaiilsDto;
import lk.ijse.supermarket.dto.QtyDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QtatinModel {
    public String insertQt(QtyDto dto,String StID,int count) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
            String sql1 = "INSERT INTO qtation VALUES(?,?,?,?)";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1,dto.getQtId());
            statement1.setString(2,dto.getType());
            statement1.setInt(3,dto.getCount());
            statement1.setDouble(4,dto.getPrice());

        int resp = statement1.executeUpdate();
        return resp > 0 ? "sucsess" : "fail";
    }

    public String getType(String stID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select type from stock where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,stID);
        ResultSet rst = statement.executeQuery();
        String name = "";
        if (rst.next()){
            name = rst.getString(1);
        }
        return name;
    }

    public double getPrice(String stID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select price from stock where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,stID);
        ResultSet rst = statement.executeQuery();
        double name = 0;
        if (rst.next()){
            name = rst.getDouble("price");
        }
        return name;
    }

    public String insertQtDetails(QtDetaiilsDto dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "INSERT INTO qtdetails VALUES(?,?,?,?,?)";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setInt(1,dto.getQtId());
        statement1.setString(2,dto.getVehicleId());
        statement1.setString(3,dto.getName());
        statement1.setString(4,dto.getDate());
        statement1.setString(5,dto.getTel());

        int resp = statement1.executeUpdate();
        return resp > 0 ? "sucsess" : "fail";
    }

    public String getQtId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT qtId FROM qtation ORDER BY qtId DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String lastOrderId = resultSet.getString(1);
            int lastIdNumber = Integer.parseInt(lastOrderId); // Convert to int
            int newIdNumber = lastIdNumber + 1; // Increment the ID
            return String.valueOf(newIdNumber); // Return the new ID as a String
        }
        return "1";
    }

    public List<QtyDto> loadTableQt(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select type,count,price from qtation where qtId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        ResultSet rst = statement.executeQuery();
        List<QtyDto> ss = new ArrayList<>();

       while (rst.next()){
           QtyDto qt = new QtyDto(rst.getString(1),
                   rst.getInt(2),
                   rst.getDouble(3));
           ss.add(qt);
       }
        return ss;
    }

    public double getTotal(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select SUM(price) from qtation where qtId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        ResultSet rst = statement.executeQuery();

        double dd = 0;
        if (rst.next()){
            dd = rst.getDouble(1);
        }
        return dd;
    }

    public String getVNB(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT vehicleNB FROM qtdetails where qtyId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,id);

        ResultSet resultSet = preparedStatement.executeQuery();
        String mm = "";
        if (resultSet.next()) {
            mm = resultSet.getString(1);
        }
        return mm;
    }

    public String getTel(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT tel FROM qtdetails where qtyId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,id);

        ResultSet resultSet = preparedStatement.executeQuery();
        String mm = "";
        if (resultSet.next()) {
            mm = resultSet.getString(1);
        }
        return mm;
    }

    public String getDate(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT Date FROM qtdetails where qtyId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,id);

        ResultSet resultSet = preparedStatement.executeQuery();
        String mm = "";
        if (resultSet.next()) {
            mm = resultSet.getString(1);
        }
        return mm;
    }

    public String getName(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT name FROM qtdetails where qtyId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,id);

        ResultSet resultSet = preparedStatement.executeQuery();
        String mm = "";
        if (resultSet.next()) {
            mm = resultSet.getString(1);
        }
        return mm;
    }

    public double getPriceQR(int oo) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT SUM(price) as price FROM qtation where qtId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,oo);

        ResultSet resultSet = preparedStatement.executeQuery();
        double mm = 0;
        if (resultSet.next()) {
            mm = resultSet.getDouble(1);
        }
        return mm;
    }

    public String delete(String tt,int count,String type) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
            // Delete the order from the `orders` table
            String sql3 = "DELETE FROM qtation WHERE type = ?";
            PreparedStatement statement3 = connection.prepareStatement(sql3);

            statement3.setString(1, tt);

        int resp = statement3.executeUpdate();
        return resp > 0 ? "sucsess" : "fail";
    }

    public boolean check(int ids) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select qtId  FROM qtation where qtId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1,ids);
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 0;
        if(resultSet.next()){
            id = resultSet.getInt(1);
        }
        if(id == ids){
            return true;
        }
        return false;
    }

    public String deleteQt(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "delete from qtation where qtId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1,id);
        int rst = statement.executeUpdate();

        return rst > 0 ? "sucsess" : "fail";
    }



}
