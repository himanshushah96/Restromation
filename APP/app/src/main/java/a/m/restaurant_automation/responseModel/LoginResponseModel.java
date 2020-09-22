package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoginResponseModel {
    @SerializedName("UserId")
    @Expose
    private String userId;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Token")
    @Expose
    private String token;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;



    @SerializedName("UserTypeId")
    @Expose
    private Integer userTypeId;
    @SerializedName("TokenCreatedDate")
    @Expose
    private String tokenCreatedDate;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getTokenCreatedDate() {
        return tokenCreatedDate;
    }

    public void setTokenCreatedDate(String tokenCreatedDate) {
        this.tokenCreatedDate = tokenCreatedDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @SerializedName("ExpireDate")
    @Expose
    private String expireDate;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}

