package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsersResponseModel {


        @SerializedName("UserType")
        @Expose
        private Integer userType;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("UserId")
        @Expose
        private Integer userId;
        @SerializedName("LastName")
        @Expose
        private String lastName;
        @SerializedName("Phone")
        @Expose
        private String phone;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("Image")
        @Expose
        private String image;

    public String getUserTypeTitle() {
        return userTypeTitle;
    }

    public void setUserTypeTitle(String userTypeTitle) {
        this.userTypeTitle = userTypeTitle;
    }

    @SerializedName("UserTypeTitle")
    @Expose
    private String userTypeTitle;
        public Integer getUserType() {
            return userType;
        }

        public void setUserType(Integer userType) {
            this.userType = userType;
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

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


    }
