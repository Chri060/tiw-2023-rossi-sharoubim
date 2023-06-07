package com.christian.rossi.progetto_tiw_2023.Utils;

import java.sql.Timestamp;

public class InputChecker {
    private static final String emailRegex = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";
    private static final String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,16}$";
    private static final String usernameRegex = "^[a-zA-Z0-9_-]{4,16}$";
    private static final String cityProvinceRegex = "^[a-zA-Z]{1,32}$";
    private static final String nameRegex = "^[a-zA-Z]{1,32}$";


    public static boolean checkUsername(String username) { return username.matches(usernameRegex); }
    public static boolean checkEmail(String email) { return email.length() <= 256 && email.matches(emailRegex); }
    public static boolean checkPassword(String password) { return password.matches(passwordRegex); }
    public static boolean checkCity(String city) {
        return city.matches(cityProvinceRegex);
    }
    public static boolean checkAddress(String address) {
        return address.length() <= 64;
    }
    public static boolean checkProvince(String province) {
        return province.matches(cityProvinceRegex);
    }
    public static boolean checkName(String name) { return name.matches(nameRegex); }
    public static boolean checkDescription(String description) { return description.length() <= 256; }
    public static boolean checkPrice(int price) { return price >= 0; }
    public static boolean checkRise(int rise) { return rise > 0; }
    public static boolean checkExpiry(Timestamp date) {
        long expiry = date.getTime();
        long now = System.currentTimeMillis();
        return expiry - now >= 0; }
    public static boolean checkOffer (int offer, int start, int rise, int actualOffer) {
        if (actualOffer == 0) {
            return offer >= start;
        }
        else {
            return offer >= actualOffer + rise;
        }
    }
}
