package com.christian.rossi.progetto_tiw_2023;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;

import java.util.ArrayList;
import java.util.List;

public class SellPageData {
    List<ProductBean> myProducts;
    List<AuctionBean> myOpenAuctions;
    List<AuctionBean> myClosedAuctions;

    public List<ProductBean> getMyProducts() {
        return myProducts;
    }
    public void setMyProducts(List<ProductBean> myProducts) {
        this.myProducts = myProducts;
    }

    public List<AuctionBean> getMyOpenAuctions() {
        return myOpenAuctions;
    }

    public void setMyOpenAuctions(List<AuctionBean> myOpenAuctions) {
        this.myOpenAuctions = myOpenAuctions;
    }

    public List<AuctionBean> getMyClosedAuctions() {
        return myClosedAuctions;
    }

    public void setMyClosedAuctions(List<AuctionBean> myClosedAuctions) {
        this.myClosedAuctions = myClosedAuctions;
    }
}
