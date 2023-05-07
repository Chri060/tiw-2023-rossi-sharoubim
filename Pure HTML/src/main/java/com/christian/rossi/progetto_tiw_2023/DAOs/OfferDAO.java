package com.christian.rossi.progetto_tiw_2023.DAOs;


import com.christian.rossi.progetto_tiw_2023.Beans.OfferBean;
import static com.christian.rossi.progetto_tiw_2023.DAOs.DBConnectionPool.getConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OfferDAO {

    public List<OfferBean> getOffers(String details) throws SQLException {
        String query = "SELECT * FROM offer WHERE auctionID=? ORDER BY offering DESC";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, details);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    List<OfferBean> offerBeanList = new ArrayList<>();
                    while (result.next()) {
                        OfferBean offerBean = new OfferBean();
                        offerBean.setUserID(Long.valueOf(result.getString("userID")));
                        offerBean.setOffering(Integer.parseInt(result.getString("offering")));
                        offerBean.setDate(result.getString("date"));
                        offerBeanList.add(offerBean);
                    }
                    return offerBeanList;
                }
            }
        }
    }


    public void addOffer(String offer, String userID, String auctionID) throws SQLException {
        String query = "INSERT INTO offer (offering, date, userID, auctionID) values (?, ?, ?, ?)";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, offer);
            request.setString(2, "data");
            request.setString(3, userID);
            request.setString(4, auctionID);
            request.execute();
        }
    }




}
