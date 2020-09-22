package a.m.restaurant_automation.service;

import java.util.ArrayList;

import a.m.restaurant_automation.responseModel.CustomerNotificationResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface INotificationService {
    @GET("notification/customer/{customerId}/{capacity}")
    Call<ResponseModel<CustomerNotificationResponseModel>> getNotificationDataCustomer(@Path(value = "customerId") int customerId, @Path(value = "capacity") int capacity);
}
