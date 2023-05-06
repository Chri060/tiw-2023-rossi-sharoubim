package com.christian.rossi.progetto_tiw_2023.DAOs;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
}
