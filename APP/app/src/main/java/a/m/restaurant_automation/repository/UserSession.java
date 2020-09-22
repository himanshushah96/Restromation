package a.m.restaurant_automation.repository;

import android.content.SharedPreferences;

public class UserSession {

    private UserSession() {
    }

    private static SharedPreferences userPreferences;
    private static UserSession userSession;

    public static UserSession getInstance(SharedPreferences preferences) {

        if (userPreferences == null) {
            userPreferences = preferences;
        }

        if (userSession == null)
            userSession = new UserSession();
        return userSession;
    }

    public static UserSession getInstance() {
        if (userSession == null)
            userSession = new UserSession();
        return userSession;
    }

    public void clearSession() {
        userPreferences.edit().putString("token", "").commit();
        userPreferences.edit().putString("userId", "").commit();
        userPreferences.edit().putString("email", "").commit();
        userPreferences.edit().putString("firstName", "").commit();
        userPreferences.edit().putString("lastName", "").commit();
        userPreferences.edit().putString("gender", "").commit();
        userPreferences.edit().putString("phoneNumber", "").commit();
        userPreferences.edit().putString("tokenCreatedDate", "").commit();
        userPreferences.edit().putString("expireDate", "").commit();
        userPreferences.edit().putInt("userTypeId", 0).commit();
        userPreferences.edit().putString("isDineIn", "").commit();
        userPreferences.edit().putString("isTableReserved", "").commit();
    }

    public String getIsTableReserved(){
        return userPreferences.getString("isTableReserved", "");
    }

    public void setIsTableReserved(String status){
        userPreferences.edit().putString("isTableReserved", status).commit();
    }

    public String getDiningInOrTakeOut(){
        return userPreferences.getString("isDineIn", "");
    }

    public void setDiningInOrTakeOut(String dineInOrTakeOut){
        userPreferences.edit().putString("isDineIn", dineInOrTakeOut).commit();
    }

    public String getToken() {
        return userPreferences.getString("token", "");
    }

    public void setToken(String token) {
        userPreferences.edit().putString("token", token).apply();
    }

    public void setEmail(String email) {
        userPreferences.edit().putString("email", email).apply();
    }

    public String getEmail() {
        return userPreferences.getString("email", "");
    }

    public void setUserId(String userId) {
        userPreferences.edit().putString("userId", userId).apply();
    }

    public String getUserId() {
        return userPreferences.getString("userId", "").trim();
    }

    public void setUserTypeId(int userTypeId) {
        userPreferences.edit().putInt("userTypeId",userTypeId).apply();
    }
    public int getUserTypeId(){return userPreferences.getInt("userTypeId",0);}


    public void setTokenCreatedDate(String tokenCreatedDate) {
        userPreferences.edit().putString("tokenCreatedDate",tokenCreatedDate).commit();
    }
    public String getTokenCreatedDate(){return userPreferences.getString("tokenCreatedDate","");}

    public void setExpireDate(String expireDate) {
        userPreferences.edit().putString("expireDate",expireDate).commit();
    }
    public String getExpireDate(){return userPreferences.getString("expireDate","");}


    public void setFirstName(String firstName) {
        userPreferences.edit().putString("firstName",firstName).apply();
    }
    public String getFirstName(){return userPreferences.getString("firstName","");}

    public void setLastName(String lastName) {
        userPreferences.edit().putString("lastName",lastName).apply();
    }
    public String getLastName(){return userPreferences.getString("lastName","");}

    public void setGender(String gender) {
        userPreferences.edit().putString("gender",gender).apply();
    }
    public String getGender(){return userPreferences.getString("gender","");}

    public void setPhoneNumber(String phoneNumber) {
        userPreferences.edit().putString("phoneNumber",phoneNumber).apply();
    }
    public String getPhoneNumber(){return userPreferences.getString("phoneNumber","");}
}
