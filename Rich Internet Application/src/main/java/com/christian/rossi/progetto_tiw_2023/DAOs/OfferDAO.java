package com.christian.rossi.progetto_tiw_2023.DAOs;

import com.christian.rossi.progetto_tiw_2023.Beans.OfferBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class OfferDAO extends AbstractDAO{

    public List<OfferBean> getOffers(Long auctionID) throws SQLException {
        String query = "SELECT * " +
                       "FROM offer " +
                       "WHERE auctionID=? " +
                       "ORDER BY offering DESC";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, auctionID);
            List<OfferBean> offerBeanList = new ArrayList<>();
            try (ResultSet result = request.executeQuery()) {
                if (result.isBeforeFirst()) {
                    while (result.next()) {
                        OfferBean offerBean = new OfferBean();
                        offerBean.setUserID(result.getLong("userID"));
                        offerBean.setOffering(result.getInt("offering"));
                        offerBean.setDate(result.getString("date"));
                        offerBeanList.add(offerBean);
                    }
                }
            }
            return offerBeanList;
        }
    }

    public void addOffer(String offer, Long userID, Long auctionID, Timestamp date) throws SQLException {
        String query = "INSERT INTO offer (offering, date, userID, auctionID) " +
                       "VALUES (?, ?, ?, ?)";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, offer);
            request.setTimestamp(2, date);
            request.setLong(3, userID);
            request.setLong(4, auctionID);
            request.execute();
        }
    }

    public int getMaxOffer (Long auctionID) throws SQLException {
        String query = "SELECT max " +
                       "FROM winner " +
                       "WHERE auctionID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, auctionID);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst()) return 0;
                else {
                    result.next();
                    return result.getInt(1);
                }
            }
        }
    }
}