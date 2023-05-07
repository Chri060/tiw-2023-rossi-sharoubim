package com.christian.rossi.progetto_tiw_2023.Beans;

public class AuctionBean {
    private Long auctionID = null;
    private int price;
    private int rise;
    private String expiry = null;
    private boolean active;
    private Long UserID = null;
    private String name = null;
    private Long productID = null;
    private Long articleID = null;
    private String description = null;

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

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getUserID() {
        return UserID;
    }

    public void setUserID(Long userID) {
        UserID = userID;
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

    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
