package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ErrorModel {

    @SerializedName("ErrorCode")
    @Expose
    private String ErrorCode;

    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    public String getErrorCode() {
        return ErrorCode;
    }


    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    private ArrayList<ErrorModel> error;

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
