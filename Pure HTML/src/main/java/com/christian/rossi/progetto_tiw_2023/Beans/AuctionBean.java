package com.christian.rossi.progetto_tiw_2023.Beans;

import java.sql.Timestamp;

public class AuctionBean {
    private Long auctionID = null;
    private int price;
    private int rise;
    private Timestamp expiry = null;
    private boolean active;
    private Long userID = null;
    private String name = null;
    private Long productID = null;
    private String description = null;
    private String image = null;
    private String remainingDays = null;
    private String remainingHours = null;

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

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
