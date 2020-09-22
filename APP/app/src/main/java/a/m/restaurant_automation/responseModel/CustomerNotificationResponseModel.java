package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CustomerNotificationResponseModel {
    public boolean istableAvailable;
    @SerializedName("orderChange")
    public ArrayList<CustomerNotificationOrders> orders;
}
