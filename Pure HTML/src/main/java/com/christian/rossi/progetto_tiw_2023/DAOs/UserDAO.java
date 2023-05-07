package com.christian.rossi.progetto_tiw_2023.DAOs;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.christian.rossi.progetto_tiw_2023.DAOs.DBConnectionPool.getConnection;

public class UserDAO {

    public UserDAO() throws SQLException {
        super();
    }

    public UserBean authenticate(String username, String password) throws SQLException {
        String query = "SELECT username, userID FROM user WHERE username=? AND password=?";
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
                    userBean.setUserID(Long.valueOf(result.getString("userID")));
                    return userBean;
                }
            }
        }
    }

    public UserBean addUser(String username, String email, String city, String address, String province, String password) throws SQLException {
        String query = "INSERT INTO user (username, email, city, address, province, password) values (?, ?, ?, ?, ?, ?)";
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
        String query = "SELECT * FROM user WHERE userID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, userID);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    result.next();
                    UserBean userBean = new UserBean();
                    userBean.setUsername(result.getString("username"));
                    userBean.setUserID(Long.valueOf(result.getString("userID")));
                    userBean.setEmail(result.getString("email"));
                    userBean.setCity(result.getString("city"));
                    userBean.setAddress(result.getString("address"));
                    userBean.setProvince(result.getString("province"));
                    return userBean;
                }
            }
        }
    }




}