package com.christian.rossi.progetto_tiw_2023.Utils;

public class InputChecker {
    public static boolean checkUsername(String username) { return username.length() <= 32; }
    public static boolean checkEmail(String email) { return email.length() <= 256; }
    public static boolean checkPassword(String password) { return password.length() <= 64; }
    public static boolean checkCity(String city) {
        return city.length() <= 32;
    }
    public static boolean checkAddress(String address) {
        return address.length() <= 64;
    }
    public static boolean checkProvince(String province) {
        return province.length() <= 32;
    }
    public static boolean checkArticleID(String articleID) { return articleID.length() == 6; }
    public static boolean checkName(String name) { return name.length() <=32; }
    public static boolean checkDescription(String description) { return description.length() <= 256; }
}
