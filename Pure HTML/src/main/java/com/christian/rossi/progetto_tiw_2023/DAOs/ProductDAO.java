package com.christian.rossi.progetto_tiw_2023.DAOs;

import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import static com.christian.rossi.progetto_tiw_2023.DAOs.DBConnectionPool.getConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void addProduct(String articleID, String name, String description, int price, Long userID) throws SQLException {
        String query = "INSERT INTO product (articleID, name, description, price, sellable, userID, auctionID) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setString(1, articleID);
            request.setString(2, name);
            request.setString(3, description);
            request.setInt(4, price);
            request.setInt(5, 1);
            request.setLong(6, userID);
            request.setLong(7, 0L);
            request.execute();
        }
    }

    public List<ProductBean> getUserProducts(Long userID) throws SQLException {
        String query = "SELECT * " +
                       "FROM product " +
                       "WHERE userID=? AND sellable=1";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, userID);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    List<ProductBean> productBeanList = new ArrayList<>();
                    while (result.next()) {
                        ProductBean productBean = new ProductBean();
                        productBean.setProductID(result.getLong("articleID"));
                        productBean.setName(result.getString("name"));
                        productBean.setDescription(result.getString("description"));
                        productBean.setPrice(result.getInt("price"));
                        productBeanList.add(productBean);
                    }
                    return productBeanList;
                }
            }
        }
    }

    public void update(Long articleID, Long auctionID) throws SQLException {
        String query = "UPDATE product " +
                       "SET sellable=0, auctionID=? " +
                       "WHERE articleID=? ";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, auctionID);
            request.setLong(2, articleID);
            request.executeUpdate();
        }
    }

    public int GetPrice (Long articleID) throws SQLException {
        String query = "SELECT price " +
                       "FROM product " +
                       "WHERE articleID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, articleID);
            try (ResultSet result = request.executeQuery()) {
                if (!result.isBeforeFirst())
                    return 0;
                else {
                    ProductBean productBean = new ProductBean();
                    if (result.next()) {
                        productBean.setPrice(result.getInt("price"));
                    }
                    return productBean.getPrice();
                }
            }
        }
    }


    public String GetProductByID (Long productID) throws SQLException {
        String query = "SELECT * " +
                       "FROM product " +
                       "WHERE articleID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, productID);
            try (ResultSet result = request.executeQuery()) {

                ProductBean productBean = new ProductBean();
                if (result.next()) {
                    productBean.setName(result.getString("name"));
                }
                return productBean.getName();
            }
        }
    }
}