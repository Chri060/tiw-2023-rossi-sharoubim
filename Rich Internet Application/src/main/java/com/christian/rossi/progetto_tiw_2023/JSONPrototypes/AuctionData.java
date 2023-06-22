package com.christian.rossi.progetto_tiw_2023.JSONPrototypes;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.OfferBean;
import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;

import java.util.List;

public class AuctionData {
    List<ProductBean> productList;
    List<OfferBean> offersList;
    UserBean winner;

    public List<ProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }

    public List<OfferBean> getOffersList() {
        return offersList;
    }

    public void setOffersList(List<OfferBean> offersList) {
        this.offersList = offersList;
    }

    public UserBean getWinner() {
        return winner;
    }

    public void setWinner(UserBean winner) {
        this.winner = winner;
    }
}
