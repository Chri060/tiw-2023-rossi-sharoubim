package com.christian.rossi.progetto_tiw_2023.Beans;

public class ProductBean {
    private Long productID = null;
    private String name = null;
    private String description = null;
    private int price;
    private boolean sellable = false;
    private Long userID = null;
    private Long auctionID = null;

    public Long getProductID() { return productID; }
    public void setProductID(Long productID) { this.productID = productID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public boolean isSellable() { return sellable; }
    public void setSellable(boolean sellable) { this.sellable = sellable; }
    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }
    public Long getAuctionID() { return auctionID; }
    public void setAuctionID(Long auctionID) { this.auctionID = auctionID; }
}
