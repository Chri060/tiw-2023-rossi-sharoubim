package com.christian.rossi.progetto_tiw_2023.Beans;

public class OfferBean {

    private Long offerID = null;
    private int offering;
    private String date;
    private Long userID = null;
    private String userName = null;
    private Long auctionID = null;

    public Long getOfferID() {
        return offerID;
    }
    public void setOfferID(Long offerID) {
        this.offerID = offerID;
    }
    public int getOffering() {
        return offering;
    }
    public void setOffering(int offering) {
        this.offering = offering;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Long getUserID() {
        return userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getAuctionID() {
        return auctionID;
    }
    public void setAuctionID(Long auctionID) {
        this.auctionID = auctionID;
    }
}