package lk.ijse.supermarket.model;

import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.dto.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashbordManageModel {
    public List<StockManageDto> getDetails(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT type,price FROM stock WHERE id = ? ";
        PreparedStatement statement = connection.prepareStatement(sql);

        List<StockManageDto> SList = new ArrayList<>();
        statement.setString(1, id);

        ResultSet rst = statement.executeQuery();

        while (rst.next()) {
            StockManageDto sDto = new StockManageDto(
                    rst.getString("type"),
                    rst.getDouble("price")
            );
            SList.add(sDto);
        }
        if (SList.isEmpty()) {
            String sql1 = "SELECT type,price FROM stock WHERE type = ? ";
            PreparedStatement statement1 = connection.prepareStatement(sql1);

            //    List<StockManageDto>SList = new ArrayList<>();
            statement1.setString(1, id);

            ResultSet rst1 = statement1.executeQuery();

            while (rst1.next()) {
                StockManageDto sDto = new StockManageDto(
                        rst1.getString("type"),
                        rst1.getDouble("price")
                );
                SList.add(sDto);
            }
        }
        return SList;
    }

    public String getNextCustomerId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1";
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

    public String getOrderID() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT orderId FROM orders order by orderId DESC LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String customerId = resultSet.getString(1);
            String supString = customerId.substring(1);
            int i = Integer.parseInt(supString);
            int newIDInt = i + 1;
            String newID = String.format("", newIDInt);
            // System.out.println(newID);
            return newID;
        }
        return "1";
    }

    public String custopmerReturn(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "delete  FROM orders where orderId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, id);
        int resultSet = preparedStatement.executeUpdate();

        if(resultSet > 0) {
            String query1 = "delete FROM payments where orderId = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

            preparedStatement1.setString(1, id);
            int resultSet1 = preparedStatement1.executeUpdate();

            return resultSet1>0 ? "Sucsess" : "Fail" ;
        }
        return "Fail";

    }

    public String insertVehicleNb(orderDetails customerVehicelDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "INSERT INTO orderdetails VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, customerVehicelDto.getVehicleNb());
        preparedStatement.setString(2, customerVehicelDto.getMilagage());
        preparedStatement.setString(3, customerVehicelDto.getNextmilagage());
        preparedStatement.setString(4, customerVehicelDto.getTel());
        preparedStatement.setInt(5, customerVehicelDto.getOrderId());

        int resultSet = preparedStatement.executeUpdate();
        return resultSet > 0 ? "SUCSESS" : "Fails";
    }

    public List<DashBordManageDto> getDetailCHECKVEHICLENB(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select o.productId,o.count,o.onePrice,o.oneDiscount,o.price from orders o join orderdetails d on o.orderId = d.orderId where d.vehicleNb = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, id);
        List<DashBordManageDto> ss = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            DashBordManageDto dd = new DashBordManageDto(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5)
            );
            ss.add(dd);
        }
        if (ss.isEmpty() || ss == null) {
            String query1 = "SELECT o.productId, o.count, o.onePrice, o.oneDiscount, o.price FROM orders o JOIN orderdetails d ON o.orderId = d.orderId WHERE o.orderId = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

            preparedStatement1.setString(1, id);
            ResultSet resultSet1 = preparedStatement1.executeQuery(); // Corrected: Use preparedStatement1 to execute query

            while (resultSet1.next()) {
                DashBordManageDto dd = new DashBordManageDto(
                        resultSet1.getString(1),    // productId
                        resultSet1.getInt(2),       // count
                        resultSet1.getDouble(3),    // onePrice
                        resultSet1.getDouble(4),    // oneDiscount
                        resultSet1.getDouble(5)     // price
                );
                ss.add(dd);
            }

        }
        return ss;
    }

    public Boolean cheackOrderID(int odId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select orderId  FROM payments where orderId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, odId);
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 0;
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        if (id == odId) {
            return true;
        }
        return false;
    }

    public String higabillInsert(HigabillDto higabillDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "INSERT INTO higabill VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, higabillDto.getOrderId());
        preparedStatement.setString(2, higabillDto.getVehicleId());
        preparedStatement.setString(3, higabillDto.getCustomerTel());
        preparedStatement.setString(4, higabillDto.getDate());
        preparedStatement.setDouble(5, higabillDto.getTotalPrice());
        preparedStatement.setDouble(6, higabillDto.getHiga());

        int resultSet = preparedStatement.executeUpdate();
        return resultSet > 0 ? "SUCSESS" : "Fails";
    }

    public List<HigabillDto> loadTableHIga() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select * from higabill";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        List<HigabillDto> ss = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            HigabillDto dd = new HigabillDto(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6)
            );
            ss.add(dd);
        }
        return ss;
    }

    public String deleteHiga(int ordid) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "delete  FROM higabill where orderId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, ordid);
        int resultSet = preparedStatement.executeUpdate();
        return resultSet > 0 ? "SUCSESS" : "Fails";
    }

    public String updateHiga(int ordid, double higamoney) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "UPDATE higabill SET higa = ? WHERE orderId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setDouble(1, higamoney);
        preparedStatement.setInt(2, ordid);
        int resultSet = preparedStatement.executeUpdate();
        return resultSet > 0 ? "SUCSESS" : "Fails";
    }

    public List<HigabillDto> cheackIDHiga(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select * from higabill where orderId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        List<HigabillDto> ss = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            HigabillDto dd = new HigabillDto(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6)
            );
            ss.add(dd);
        }
        return ss;
    }

    public List<HigabillDto> cheackIDHigav(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select * from higabill where vehicleId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        List<HigabillDto> ss = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            HigabillDto dd = new HigabillDto(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getDouble(6)
            );
            ss.add(dd);
        }
        return ss;
    }

    public double cekakHigaPrice(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select SUM(higa)as higa from higabill where vehicleId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        double mo = 0;
        if (resultSet.next()) {
            mo = resultSet.getDouble("higa");

        }
        return mo;
    }

    public String geCutID(int oId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select vehicleNb,milagage,nextmilagage from orderdetails where orderId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, oId);
        ResultSet resultSet = preparedStatement.executeQuery();
        String mo = "";
        if (resultSet.next()) {
            mo = resultSet.getString(1);

        }
        return mo;
    }

    public List<String> geCutIDs(int oId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select vehicleNb, milagage, nextmilagage,tel from orderdetails where orderId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, oId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> ss = new ArrayList<>();

        // Iterate through all result rows
        while (resultSet.next()) {
            ss.add(resultSet.getString(1));  // vehicleNb
            ss.add(resultSet.getString(2));  // milagage
            ss.add(resultSet.getString(3));  // nextmilagage
            ss.add(resultSet.getString(4));  // nextmilagage
        }

        // Return the list of values
        return ss;
    }

    public List<String> geCutIDss(String oId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select vehicleNb, milagage, nextmilagage,tel from orderdetails where vehicleNb = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, oId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> ss = new ArrayList<>();

        // Iterate through all result rows
        while (resultSet.next()) {
            ss.add(resultSet.getString(1));  // vehicleNb
            ss.add(resultSet.getString(2));  // milagage
            ss.add(resultSet.getString(3));  // nextmilagage
            ss.add(resultSet.getString(4));  // nextmilagage
        }
        return ss;
    }

    public List<String> geCutI(String oId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select vehicleNb, milagage, nextmilagage,tel from orderdetails where orderId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, oId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> ss = new ArrayList<>();

        // Iterate through all result rows
        while (resultSet.next()) {
            ss.add(resultSet.getString(1));  // vehicleNb
            ss.add(resultSet.getString(2));  // milagage
            ss.add(resultSet.getString(3));  // nextmilagage
            ss.add(resultSet.getString(4));  // nextmilagage
        }

        // Return the list of values
        return ss;
    }

    public List<String> searchListItam(String sc) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "select vehicleNb from orderdetails where vehicleNb LIKE ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, sc + "%");
        ResultSet rst = statement1.executeQuery();

        List<String> sugest = new ArrayList<>();
        while (rst.next()) {
            sugest.add(rst.getString("vehicleNb"));
        }
        return sugest;
    }

    public boolean cheakAnother(int odId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "select COUNT(productId) from payments where orderId = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setInt(1, odId);
        ResultSet rst = statement1.executeQuery();

        int count = 0;
        if (rst.next()) {
            count = rst.getInt(1);
        }


        String sql11 = "select COUNT(productId) from payments where orderId = ?";
        PreparedStatement statement = connection.prepareStatement(sql11);
        statement.setInt(1, odId);
        ResultSet rst1 = statement.executeQuery();

        int counts = 0;
        if (rst1.next()) {
            counts = rst1.getInt(1);
        }



        return (counts == count);

    }

    public String deleteOrdDetails(int oId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "delete from orderdetails where orderId = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setInt(1, oId);
        int rst = statement1.executeUpdate();


        return rst > 0 ? "Scusses" : "Fail";
    }

    public List<String> ListDate(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql1 = "select o.date from orders o JOIN orderdetails od on o.orderId = od.orderId where od.vehicleNb = ?";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, id);
        ArrayList list = new ArrayList();
        ResultSet rst = statement1.executeQuery();
        while (rst.next()) {
            list.add(rst.getString(1));
        }
        return list;
    }

    public List<DashBordManageDto> getDetailCHECKVEHICLENBDATE(String id, String selected) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "select o.productId,o.count,o.onePrice,o.oneDiscount,o.price from orders o join orderdetails d on o.orderId = d.orderId where d.vehicleNb = ? AND date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, id);
        preparedStatement.setString(2, selected);
        List<DashBordManageDto> ss = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            DashBordManageDto dd = new DashBordManageDto(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5)
            );
            ss.add(dd);
        }
        if (ss.isEmpty() || ss == null) {
            String query1 = "SELECT o.productId, o.count, o.onePrice, o.oneDiscount, o.price FROM orders o JOIN orderdetails d ON o.orderId = d.orderId WHERE o.orderId = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

            preparedStatement1.setString(1, id);
            ResultSet resultSet1 = preparedStatement1.executeQuery(); // Corrected: Use preparedStatement1 to execute query

            while (resultSet1.next()) {
                DashBordManageDto dd = new DashBordManageDto(
                        resultSet1.getString(1),    // productId
                        resultSet1.getInt(2),       // count
                        resultSet1.getDouble(3),    // onePrice
                        resultSet1.getDouble(4),    // oneDiscount
                        resultSet1.getDouble(5)     // price
                );
                ss.add(dd);
            }

        }
        return ss;
    }

    public int getOderId(String vid) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select orderId from orderdetails where vehicleNb = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, vid);
        ResultSet resultSet = statement.executeQuery();
        int orderId = 0;
        if (resultSet.next()){
            orderId = resultSet.getInt(1);
        }
        return orderId;
    }

    public List<String> getPrintDetailsJusper(int orderId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select price,date from payments where orderId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> ss = new ArrayList<>();
        while (resultSet.next()){
            ss.add(String.valueOf(resultSet.getDouble(1)));
            ss.add(resultSet.getString(2));
        }
        return ss;
    }

    public int getOderIdDate(String vid, String dateprint) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select o.orderId from orderdetails o JOIN payments p on o.orderId = p.orderId where o.vehicleNb = ? AND p.date = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, vid);
        statement.setString(2, dateprint);
        ResultSet resultSet = statement.executeQuery();
        int orderId = 0;
        if (resultSet.next()){
            orderId = resultSet.getInt(1);
        }
        return orderId;
    }

    public int getCount(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT count FROM stock WHERE id = ? ";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, id);

        ResultSet rst = statement.executeQuery();

        int cc = 0;

        if(rst.next()){
           cc = rst.getInt("count");
        }


        String sql1 = "SELECT count FROM stock WHERE type = ? ";
        PreparedStatement statement1 = connection.prepareStatement(sql1);
        statement1.setString(1, id);

        ResultSet rst1 = statement1.executeQuery();

        if (rst1.next()){
            cc = rst1.getInt("count");
        }

        return cc;
    }
}

