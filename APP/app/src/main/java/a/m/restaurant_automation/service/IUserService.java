package a.m.restaurant_automation.service;

import a.m.restaurant_automation.requestModel.AddMenuItemRequestModel;
import a.m.restaurant_automation.requestModel.AddTableRequestModel;
import a.m.restaurant_automation.requestModel.ChangePasswordRequestModel;
import a.m.restaurant_automation.requestModel.EditProfileRequestModel;
import a.m.restaurant_automation.requestModel.EmployeeDeleteRequestModel;
import a.m.restaurant_automation.requestModel.LoginRequestModel;
import a.m.restaurant_automation.requestModel.MenuItemRequestModel;
import a.m.restaurant_automation.requestModel.RegisterRequestModel;
import a.m.restaurant_automation.requestModel.TableDeleteRequestModel;
import a.m.restaurant_automation.responseModel.CustomerReserveTableResponse;
import a.m.restaurant_automation.responseModel.EditProfileReponseModel;
import a.m.restaurant_automation.responseModel.ErrorModel;
import a.m.restaurant_automation.responseModel.LoginResponseModel;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.RegisterResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.StatusCheckResponse;
import a.m.restaurant_automation.responseModel.TableResponseModel;
import a.m.restaurant_automation.responseModel.UsersResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserService {

    @POST ("auth/login")
    Call<ResponseModel<LoginResponseModel>> login (@Body LoginRequestModel loginRequestModel);

    @POST ("users/add")
    Call<ResponseModel<RegisterResponseModel>> register (@Body RegisterRequestModel registerRequestModel);

    @POST ("menu/addMenuItem")
    Call<ResponseModel<MenuItemResponse>> addMenuItem (@Body AddMenuItemRequestModel addMenuItemRequestModel);

    @POST ("addTable")
    Call<ResponseModel<TableResponseModel>> addTable (@Body AddTableRequestModel addTableRequestModel);

    @POST ("menu/changePrice")
    Call<ResponseModel<MenuItemResponse>> changePrice (@Body MenuItemRequestModel menuItemRequestModel);

    @POST ("deleteorModifyEmployee")
    Call<ResponseModel<UsersResponseModel>> deleteEmployee (@Body EmployeeDeleteRequestModel employeeDeleteRequestModel);

    @POST ("deleteorModifyTable")
    Call<ResponseModel<CustomerReserveTableResponse>> deleteTable (@Body TableDeleteRequestModel tableDeleteRequestModel);

    @POST("users/editProfile")
    Call<ResponseModel<EditProfileReponseModel>> updateProfile(@Body EditProfileRequestModel editProfileRequestModel);

    @POST("users/changePassword")
    Call<ResponseModel<StatusCheckResponse>> changePassword (@Body ChangePasswordRequestModel changePasswordRequestModel);


}
