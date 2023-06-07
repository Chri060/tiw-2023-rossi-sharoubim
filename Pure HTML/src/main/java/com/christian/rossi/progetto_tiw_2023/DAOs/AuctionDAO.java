package com.christian.rossi.progetto_tiw_2023.DAOs;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Utils.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.christian.rossi.progetto_tiw_2023.DAOs.DBConnectionPool.getConnection;

public class AuctionDAO {

    private final String imgPath = "http://localhost:8080/getImage/";

    public Long createAuction(int price, int rise, Timestamp expiry, Long userID) throws SQLException{
        String query = "INSERT INTO auction (price, rise , expiry, active, userID) " +
                       "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setInt(1, price);
            request.setInt(2, rise);
            request.setTimestamp(3, expiry);
            request.setInt(4, 1);
            request.setLong(5, userID);
            request.execute();
        }

        String query1 = "SELECT max(auctionID) AS max " +
                        "FROM auction";
        try (PreparedStatement pstatement = getConnection().prepareStatement(query1);) {

            try (ResultSet result1 = pstatement.executeQuery()) {
                if (!result1.isBeforeFirst())
                    return null;
                else {
                    result1.next();
                    AuctionBean auctionBean = new AuctionBean();
                    auctionBean.setAuctionID(result1.getLong("max"));
                    return auctionBean.getAuctionID();
                }
            }
        }
    }

    public List<AuctionBean> getAuctions(Long userID, int active, long loginTime) throws SQLException{
        String query = "SELECT * " +
                       "FROM auction LEFT JOIN winner ON auction.auctionID = winner.auctionID " +
                       "             JOIN product on auction.auctionID = product.auctionID " +
                       "WHERE auction.active=? AND auction.userID=? " +
                       "ORDER BY auction.auctionID ";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, active);
            request.setLong(2, userID);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    List<AuctionBean> auctionBeanList = new ArrayList<>();
                    while (result.next()) {
                        AuctionBean auctionBean = new AuctionBean();
                        auctionBean.setAuctionID(result.getLong("auctionID"));
                        if (result.getString("max") != null) {
                            auctionBean.setPrice(result.getInt("max"));
                        }
                        else {
                            auctionBean.setPrice(0);
                        }
                        auctionBean.setName(result.getString("name"));
                        auctionBean.setProductID(result.getLong("productID"));
                        auctionBean.setImage(imgPath + result.getString("productID") + ".jpeg");
                        auctionBean.setExpiry(result.getTimestamp("expiry"));
                        List<Integer> timeRem = TimeHandler.getTimeDifference(result.getTimestamp("expiry"), loginTime);
                        auctionBean.setRemainingDays(String.valueOf(timeRem.get(0)));
                        auctionBean.setRemainingHours(timeRem.get(1) + "h " + timeRem.get(2)+ "m");
                        auctionBeanList.add(auctionBean);
                    }
                    return auctionBeanList;
                }
            }
        }
    }

    public List<AuctionBean> getAuctionsByID(String details, long loginTime) throws SQLException{
        String query = "SELECT * " +
                       "FROM auction JOIN product on auction.auctionID = product.auctionID " +
                       "             LEFT JOIN winner on auction.auctionID = winner.auctionID " +
                       "WHERE auction.auctionID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, details);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    List<AuctionBean> auctionBeanList = new ArrayList<>();
                    while (result.next()) {
                        AuctionBean auctionBean = new AuctionBean();
                        auctionBean.setAuctionID(result.getLong("auctionID"));
                        auctionBean.setPrice(result.getInt("price"));
                        auctionBean.setName(result.getString("name"));
                        auctionBean.setActive((result.getInt("active")));
                        auctionBean.setProductID(result.getLong("productID"));
                        auctionBean.setDescription(result.getString("description"));
                        auctionBean.setImage(imgPath + result.getString("productID") + ".jpeg");
                        auctionBean.setRise(result.getInt("rise"));
                        List<Integer> timeRem = TimeHandler.getTimeDifference(result.getTimestamp("expiry"), loginTime);
                        auctionBean.setRemainingDays(String.valueOf(timeRem.get(0)));
                        auctionBean.setRemainingHours(timeRem.get(1) + "h " + timeRem.get(2)+ "m");
                        auctionBeanList.add(auctionBean);
                    }
                    return auctionBeanList;
                }
            }
        }
    }

    public AuctionBean getWinner(String details) throws SQLException {
        String query = "SELECT winner.userID " +
                       "FROM auction LEFT JOIN winner ON auction.auctionID = winner.auctionID " +
                       "WHERE active = 0 AND max != 0 AND auction.auctionID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, details);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    result.next();
                    AuctionBean auctionBean = new AuctionBean();
                    auctionBean.setUserID(result.getLong("userID"));
                    return auctionBean;
                }
            }
        }
    }

    public List<AuctionBean> getWonAuctions(Long userID) throws SQLException{
        String query = "SELECT * " +
                       "FROM winner LEFT JOIN auction on winner.auctionID = auction.auctionID " +
                       "            LEFT JOIN product on auction.auctionID = product.auctionID " +
                       "WHERE winner.userID=? AND auction.active=0";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, userID);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    List<AuctionBean> auctionBeanList = new ArrayList<>();
                    while (result.next()) {
                        AuctionBean auctionBean = new AuctionBean();
                        auctionBean.setAuctionID(result.getLong("auctionID"));
                        if (result.getString("max") != null) {
                            auctionBean.setPrice(result.getInt("max"));
                        }
                        else {
                            auctionBean.setPrice(0);
                        }
                        auctionBean.setName(result.getString("name"));
                        auctionBean.setProductID(result.getLong("productID"));
                        auctionBean.setDescription(result.getString("description"));
                        auctionBean.setImage(imgPath + result.getString("productID") + ".jpeg");
                        auctionBeanList.add(auctionBean);
                    }
                    return auctionBeanList;
                }
            }
        }
    }

    public List<AuctionBean> getAuctionByKeyword(String article, Long userID, Long loginTime) throws SQLException {
        String details = "%" + article + "%";
        String query = "SELECT * " +
                       "FROM auction LEFT JOIN product ON auction.auctionID = product.auctionID " +
                       "WHERE auction.active=1 AND (product.name LIKE ? OR product.description LIKE ?) AND product.userID!=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, details);
            request.setString(2, details);
            request.setLong(3, userID);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    List<AuctionBean> auctionBeanList = new ArrayList<>();
                    while (result.next()) {
                        AuctionBean auctionBean = new AuctionBean();
                        auctionBean.setAuctionID(result.getLong("auctionID"));
                        auctionBean.setName(result.getString("name"));
                        auctionBean.setProductID(result.getLong("productID"));
                        auctionBean.setDescription(result.getString("description"));
                        auctionBean.setImage(imgPath + result.getString("productID") + ".jpeg");
                        auctionBean.setPrice(result.getInt("price"));
                        List<Integer> timeRem = TimeHandler.getTimeDifference(result.getTimestamp("expiry"), loginTime);
                        auctionBean.setRemainingDays(String.valueOf(timeRem.get(0)));
                        auctionBean.setRemainingHours(timeRem.get(1) + "h " + timeRem.get(2)+ "m");
                        auctionBeanList.add(auctionBean);
                    }
                    return auctionBeanList;
                }
            }
        }
    }

    public void close(String auctionID, String userID) throws SQLException {
        String query = "UPDATE auction SET active=0 WHERE auctionID=? AND userID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, auctionID);
            request.setString(2, userID);
            request.executeUpdate();
        }
    }
}