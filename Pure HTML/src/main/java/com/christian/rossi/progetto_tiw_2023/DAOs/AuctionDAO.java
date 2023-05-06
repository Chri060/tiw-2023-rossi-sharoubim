package com.christian.rossi.progetto_tiw_2023.DAOs;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.christian.rossi.progetto_tiw_2023.DAOs.DBConnectionPool.getConnection;

public class AuctionDAO {

    public AuctionDAO() throws SQLException {
        super();
    }


    public Long createAuction(int price, int rise, String expiry, Long userID) throws SQLException{
        //TODO: restituisci auct
        String query = "INSERT INTO auction (price, rise , expiry, active, userID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setInt(1, price);
            request.setInt(2, rise);
            request.setString(3, expiry);
            request.setInt(4, 1);
            request.setLong(5, userID);
            request.execute();
        }

        String query1 = "SELECT max(auctionID) AS max FROM auction";
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





    public List<AuctionBean> getAuctions(Long userID, int active) throws SQLException{
        String query = "SELECT auction.auctionID, winner.`max(offering)`, product.name, product.articleID " +
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
                        auctionBean.setAuctionID(Long.valueOf(result.getString("auctionID")));
                        if (result.getString("max(offering)") != null) {
                            auctionBean.setPrice(Integer.parseInt(result.getString("max(offering)")));
                        }
                        else {
                            auctionBean.setPrice(0);
                        }
                        auctionBean.setName(result.getString("name"));
                        auctionBean.setProductID(Long.valueOf(result.getString("articleID")));
                        auctionBeanList.add(auctionBean);
                    }
                    return auctionBeanList;
                }
            }
        }
    }



}
