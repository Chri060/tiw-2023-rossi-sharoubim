package com.christian.rossi.progetto_tiw_2023.Beans;

public class UserBean {
    private Long userID = null;
    private String username = null;
    private String email = null;
    private String city = null;
    private String address = null;
    private String province = null;

    public Long getUserID(){
        return userID;
    }
    public void setUserID(Long userID){
        this.userID = userID;
    }
    public String getUsername (){
        return username;
    }
    public void setUsername (String username){
        this.username = username;
    }
    public String getEmail (){
        return email;
    }
    public void setEmail (String email){
        this.email = email;
    }
    public String getCity (){
        return city;
    }
    public void setCity (String city){
        this.city = city;
    }
    public String getAddress (){
        return address;
    }
    public void setAddress (String address){
        this.address = address;
    }
    public String getProvince (){
        return province;
    }
    public void setProvince (String province){
        this.province = province;
    }
}