package a.m.restaurant_automation.service;
import java.util.ArrayList;

import a.m.restaurant_automation.requestModel.AddToCartRequestModel;
import a.m.restaurant_automation.requestModel.ChangePaymentStatusRequest;
import a.m.restaurant_automation.requestModel.OrderCartItemRequestModel;
import a.m.restaurant_automation.requestModel.OrderStatusUpdateRequest;
import a.m.restaurant_automation.requestModel.ReadyForPaymentRequestModel;
import a.m.restaurant_automation.requestModel.ReserveTableRequest;
import a.m.restaurant_automation.responseModel.ChangePaymentStatusResponse;
import a.m.restaurant_automation.responseModel.CustomerReserveTableResponse;
import a.m.restaurant_automation.responseModel.GetCartItemResponseModel;
import a.m.restaurant_automation.responseModel.GetOrderResponseModel;
import a.m.restaurant_automation.responseModel.GetReadyForPaymentResponseModel;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.OrderCartItemResponseModel;
import a.m.restaurant_automation.responseModel.OrderStatusUpdateResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.StatusCheckResponse;
import a.m.restaurant_automation.responseModel.TableReservationStatusForCustomerModel;
import a.m.restaurant_automation.responseModel.TableResponseModel;
import a.m.restaurant_automation.responseModel.UsersResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IDataService {

    @GET("menu/{categoryId}")
    Call<ResponseModel<ArrayList<MenuItemResponse>>> getMenuItem(@Path(value = "categoryId")int categoryId);

    @GET("users")
    Call<ResponseModel<ArrayList<UsersResponseModel>>> getEmployees(/*@Query(value = "userType")int[] userType*/);

    @GET("table")
    Call<ResponseModel<ArrayList<TableResponseModel>>> getTable ();

    @GET("table/isTableReserved/{clientId}")
    Call<TableReservationStatusForCustomerModel> getReservationStatus(@Path(value = "clientId")int clientId);

    @GET("table/getTables/{tableId}/{capacity}")
    Call<ResponseModel<ArrayList<CustomerReserveTableResponse>>> getTables(@Path(value = "tableId")int tableId, @Path(value = "capacity")int capacity);

    @POST("reserveTable")
    Call<ResponseModel<StatusCheckResponse>> reserveTable(@Body ReserveTableRequest reserveTableRequest);

    @POST("cart/addToCart")
    Call<ResponseModel<StatusCheckResponse>> addToCart (@Body AddToCartRequestModel addToCartRequestModel);

    @GET("cart/cartItems/{userId}")
    Call<ResponseModel<ArrayList<GetCartItemResponseModel>>> getCartItems(@Path(value = "userId") String userId);

    @POST("cart/deleteorModifyCartItems")
    Call<ResponseModel<StatusCheckResponse>> deleteOrModifyCartItems(@Query(value = "cartId") int cartId, @Query(value = "quantity") int quantity, @Query(value="isDelete") boolean isDelete);

    @GET("orders/getOrders/{chefOrCashier}/{customerId}/{fromDate}/{toDate}/{email}/{needUnpaidOnly}")
    Call<ResponseModel<ArrayList<GetOrderResponseModel>>> getOrders(@Path(value="chefOrCashier") boolean chefOrCashier,@Path(value = "customerId")int customerId, @Path(value = "fromDate") String fromDate, @Path(value = "toDate") String toDate, @Path(value = "email") String email, @Path(value = "needUnpaidOnly") boolean needUnpaidOnly);

    @POST ("orders/addOrders")
    Call<ResponseModel<OrderCartItemResponseModel>> addOrder (@Body OrderCartItemRequestModel orderCartItemRequestModel);

    @POST("orders/updateOrderStatus")
    Call<ResponseModel<OrderStatusUpdateResponse>> updateOrderStatus(@Body OrderStatusUpdateRequest orderStatusUpdateRequest);

    @POST("order/changePaymentStatus")
    Call<ResponseModel<ChangePaymentStatusResponse>> changePaymentStatus(@Body ChangePaymentStatusRequest changePaymentStatusRequest);

    @POST("orders/readyToPay")
    Call<ResponseModel<StatusCheckResponse>> readyForPayment (@Body ReadyForPaymentRequestModel readyForPaymentRequestModel);

    @GET("orders/getReadyForPayment")
    Call<ResponseModel<ArrayList<GetReadyForPaymentResponseModel>>> getReadyForPayment();


}
