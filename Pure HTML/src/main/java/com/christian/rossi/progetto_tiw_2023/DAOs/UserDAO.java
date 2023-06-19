package com.christian.rossi.progetto_tiw_2023.DAOs;

import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import static com.christian.rossi.progetto_tiw_2023.DAOs.DBConnectionPool.getConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO {

    public UserBean authenticate(String username, String password) throws SQLException {
        String query = "SELECT username, userID " +
                       "FROM user WHERE username=? AND password=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, username);
            request.setString(2, password);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    result.next();
                    UserBean userBean = new UserBean();
                    userBean.setUsername(result.getString("username"));
                    userBean.setUserID(result.getLong("userID"));
                    return userBean;
                }
            }
        }
    }

    public UserBean addUser(String username, String email, String city, String address, String province, String password) throws SQLException {
        String query = "INSERT INTO user (username, email, city, address, province, password) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, username);
            request.setString(2, email);
            request.setString(3, city);
            request.setString(4, address);
            request.setString(5, province);
            request.setString(6, password);
            request.execute();
        }
        return authenticate(username, password);
    }

    public UserBean getUser(String userID) throws SQLException {
        String query = "SELECT * " +
                       "FROM user " +
                       "WHERE userID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, userID);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    result.next();
                    UserBean userBean = new UserBean();
                    userBean.setUsername(result.getString("username"));
                    userBean.setUserID(result.getLong("userID"));
                    userBean.setEmail(result.getString("email"));
                    userBean.setCity(result.getString("city"));
                    userBean.setAddress(result.getString("address"));
                    userBean.setProvince(result.getString("province"));
                    return userBean;
                }
            }
        }
    }

    public String GetUserByUsername(String username) throws SQLException {
        String query = "SELECT * " +
                       "FROM user " +
                       "WHERE username=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, username);
            try (ResultSet result = request.executeQuery()) {

                UserBean userBean = new UserBean();
                if (result.next()) {
                    userBean.setUsername(result.getString("username"));
                }
                return userBean.getUsername();
            }
        }
    }

    public String GetUserByEmail(String email) throws SQLException {
        String query = "SELECT * " +
                       "FROM user " +
                       "WHERE email=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, email);
            try (ResultSet result = request.executeQuery()) {
                UserBean userBean = new UserBean();
                if (result.next()) {
                    userBean.setEmail(result.getString("email"));
                }
                return userBean.getEmail();
            }
        }
    }
}