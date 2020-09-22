package a.m.restaurant_automation.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModel<T>{


    @SerializedName("Response")
    @Expose
    private T Data;

    @SerializedName("Error")
    @Expose
    private ErrorModel error;


    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }

}
