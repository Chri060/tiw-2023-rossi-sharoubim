package com.christian.rossi.progetto_tiw_2023.DAOs;

import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Constants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends AbstractDAO{

    public long addProduct(String name, String description, int price, Long userID) throws SQLException {
        String query = "INSERT INTO product (name, description, price, sellable, userID, auctionID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement request = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            request.setString(1, name);
            request.setString(2, description);
            request.setInt(3, price);
            request.setInt(4, 1);
            request.setLong(5, userID);
            request.setLong(6, 0L);
            request.execute();
            ResultSet resultSet = request.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(1);
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
                        productBean.setProductID(result.getLong("productID"));
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

    public void update(Long productID, Long auctionID) throws SQLException {
        String query = "UPDATE product " +
                       "SET sellable=0, auctionID=? " +
                       "WHERE productID=? ";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, auctionID);
            request.setLong(2, productID);
            request.executeUpdate();
        }
    }

    public int GetPrice (Long productID) throws SQLException {
        String query = "SELECT price " +
                       "FROM product " +
                       "WHERE productID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, productID);
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


    public boolean CheckProduct(Long productID, Long userID) throws SQLException {
        String query = "SELECT * " +
                       "FROM product " +
                       "WHERE productID=? AND userID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, productID);
            request.setLong(2, userID);
            try (ResultSet result = request.executeQuery()) {

                ProductBean productBean = new ProductBean();
                if (result.next()) {
                    productBean.setName(result.getString("name"));
                }
                return productBean.getName() == null;
            }
        }
    }

    public List<ProductBean> getProductFromAuction(Long auctionID) throws SQLException {
        String query = "SELECT * " +
                       "FROM product " +
                       "WHERE auctionID=?";
        try (PreparedStatement request = getConnection().prepareStatement(query)) {
            request.setLong(1, auctionID);
            try (ResultSet result = request.executeQuery()) {
                List<ProductBean> productBeanList = new ArrayList<>();
                while(result.next()) {
                    ProductBean productBean = new ProductBean();
                    productBean.setName(result.getString("name"));
                    productBean.setDescription(result.getString("description"));
                    productBean.setPrice(result.getInt("price"));
                    productBean.setSellable(result.getBoolean("sellable"));
                    productBean.setUserID(result.getLong("userID"));
                    productBean.setAuctionID(result.getLong("auctionID"));
                    productBean.setImage("http://localhost:8080/getImage/" + result.getLong("productID") + ".jpeg");
                    productBeanList.add(productBean);
                }
                return productBeanList;
            }
        }
    }
}