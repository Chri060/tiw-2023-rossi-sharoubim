package com.christian.rossi.progetto_tiw_2023.Utils;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;

import java.sql.SQLException;
import java.util.List;

public class StringFormatter {

    public static void getProductsWithBreakLine(List<AuctionBean> auctions) throws SQLException {
        ProductDAO productDAO = new ProductDAO();
        if (auctions != null) {
            for (AuctionBean auctionBean : auctions) {
                String products = "";
                List<ProductBean> productBeanList = productDAO.getProductFromAuction(auctionBean.getAuctionID());
                auctionBean.setProductList(productBeanList);
                for (ProductBean productBean : productBeanList) products += productBean.getName() + "</br>";
                auctionBean.setProductNames(products);
            }
        }
    }
}