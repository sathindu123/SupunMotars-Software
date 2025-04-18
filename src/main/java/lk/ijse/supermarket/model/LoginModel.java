package lk.ijse.supermarket.model;

import lk.ijse.supermarket.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    public Boolean cheach(String cu, String cp , String cpc) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "Select userName from admin WHERE userName = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,cu);
        ResultSet resultSet = statement.executeQuery();
        String sss = "s";
        while (resultSet.next()){
            sss = resultSet.getString("userName");

        }
        if(sss.equals(cu)){
            String sql1 = "Select password from admin WHERE password = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1,cp);
            ResultSet resultSet1 = statement1.executeQuery();
            String sss1 = "s";
            while (resultSet1.next()){
                sss1 = resultSet1.getString("password");
            }
            if (sss1.equals(cp) && sss1.equals(cpc) ){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public String updateCreadtitaonal(String cu, String np, String nu) throws SQLException, ClassNotFoundException {
        Connection connection =DBConnection.getInstance().getConnection();
        String sql = "UPDATE admin SET userName = ? , password = ? WHERE userName = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,nu);
        statement.setString(2,np);
        statement.setString(3,cu);

        int resp = statement.executeUpdate();
        return resp > 0 ? "Sucsses" : "Fail";

    }

    public Boolean cheack(String un, String up) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "Select userName from admin WHERE userName = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,un);
        ResultSet resultSet = statement.executeQuery();
        String sss = "s";
        System.out.println(sss);
        while (resultSet.next()){
            System.out.println(sss);
            sss = resultSet.getString("userName");

        }
        if(sss.equals(un)){
            String sql1 = "Select password from admin WHERE password = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1,up);
            ResultSet resultSet1 = statement1.executeQuery();
            String sss1 = "s";
            while (resultSet1.next()){
                System.out.println(sss1);
                sss1 = resultSet1.getString("password");
            }
            if (sss1.equals(up)){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public String getUderName() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "Select userName from admin";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            return   resultSet.getString("userName");

        }
        return "ADMIN";
    }

    public String getPAssword() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "Select password from admin ";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            return   resultSet.getString("password");

        }
        return "ADMIN";
    }
}
