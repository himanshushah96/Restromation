package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.SerializedName;

public class StatusCheckResponse {
    @SerializedName("StatusCode")
    public String statusCode;

    @SerializedName("StatusMessage")
    public String statusMessage;

    @SerializedName("Total")
    public double total;

    @SerializedName("Quantity")
    public String quantity;

    @SerializedName("ItemTotal")
    public double itemTotal;

}
