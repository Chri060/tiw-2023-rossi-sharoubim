package com.christian.rossi.progetto_tiw_2023.Beans;

import java.sql.Timestamp;
import java.util.List;

public class AuctionBean {

    private Long auctionID = null;
    private Long userID = null;
    private boolean active;
    private int price;
    private int rise;
    private int maxOffer;
    private List<ProductBean> productList = null;
    private Timestamp expiry = null;
    private String remainingDays = null;
    private String remainingHours = null;
    private UserBean winner = null;

    public Long getAuctionID() {
        return auctionID;
    }
    public void setAuctionID(Long auctionID) {
        this.auctionID = auctionID;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public Timestamp getExpiry() {
        return expiry;
    }
    public void setExpiry(Timestamp expiry) {
        this.expiry = expiry;
    }
    public int getRise() {
        return rise;
    }
    public void setRise(int rise) {
        this.rise = rise;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(int active) { this.active= active == 1; }
    public Long getUserID() {
        return userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public List<ProductBean> getProductList() {
        return productList;
    }
    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }
    public String getRemainingDays() {
        return remainingDays;
    }
    public void setRemainingDays(String remainingDays) {
        this.remainingDays = remainingDays;
    }
    public String getRemainingHours() {
        return remainingHours;
    }
    public void setRemainingHours(String remainingHours) {
        this.remainingHours = remainingHours;
    }
    public int getMaxOffer() {
        return maxOffer;
    }
    public void setMaxOffer(int maxOffer) {
        this.maxOffer = maxOffer;
    }
    public UserBean getWinner() {
        return winner;
    }
    public void setWinner(UserBean winner) {
        this.winner = winner;
    }
}