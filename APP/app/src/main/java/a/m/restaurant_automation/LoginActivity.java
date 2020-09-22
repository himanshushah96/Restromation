package a.m.restaurant_automation;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import a.m.restaurant_automation.manager.AddEmployeeFragment;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import a.m.restaurant_automation.model.AppStaticData;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.requestModel.LoginRequestModel;
import a.m.restaurant_automation.requestModel.RegisterRequestModel;
import a.m.restaurant_automation.responseModel.LoginResponseModel;
import a.m.restaurant_automation.responseModel.RegisterResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements OnLoginPress,RegisterFragment.OnRegisterPress{

    private int UserType = AppStaticData.USERTYPE_CUSTOMER;
    private int mUserType;

    private String registerEmail, registerPassword, registerFirstName, registerLastName;
    private String loginEmail;
    private String loginPassword;
    Intent intentFromMain;



    @Override
    public void OnEmailSet(String email) {
        loginEmail = email;

    }

    @Override
    public void OnPasswordSet(String password) {
        loginPassword = password;
        login(loginEmail, loginPassword);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }


    public void OnRegisterPress(String firstName, String lastName, String email, String password, int userType) {

        registerEmail = email;
        registerFirstName = firstName;
        registerLastName = lastName;
        registerPassword = password;
        UserType = userType;
        Register();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void Register() {
        IUserService userService = RetrofitClient.getRetrofitInstance().create(IUserService.class);
        RegisterRequestModel registerRequestModel = new RegisterRequestModel();
        registerRequestModel.FirstName = registerFirstName;
        registerRequestModel.lastName = registerLastName;
        registerRequestModel.Email = registerEmail;
        registerRequestModel.Password = registerPassword;
        registerRequestModel.UserType = UserType;

        Call<ResponseModel<RegisterResponseModel>> call = userService.register(registerRequestModel);

        call.enqueue(new Callback<ResponseModel<RegisterResponseModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<RegisterResponseModel>> call, Response<ResponseModel<RegisterResponseModel>> response) {
                ResponseModel<RegisterResponseModel> responseModel = response.body();

                if (responseModel != null) {
                    if (responseModel.getError() != null) {
                        Toast.makeText(getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else if (responseModel.getData() != null) {
                        RegisterResponseModel registerRequestModel = responseModel.getData();
                        Toast.makeText(getApplicationContext(), responseModel.getData().getEmail() + " registered", Toast.LENGTH_LONG).show();
                        login(registerRequestModel.getEmail(), loginPassword);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<RegisterResponseModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "something went wrong" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


    public void login(String email, String password)
    {
        final IUserService service = RetrofitClient.getRetrofitInstance().create(IUserService.class);

        LoginRequestModel request = new LoginRequestModel();
        request.Email = email;
        request.Password = password;


        Call<ResponseModel<LoginResponseModel>> call = service.login(request);

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            call.enqueue(new Callback<ResponseModel<LoginResponseModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<LoginResponseModel>> call, Response<ResponseModel<LoginResponseModel>> response) {
                    ResponseModel<LoginResponseModel> responseModel = response.body();

                    if (responseModel != null && responseModel.getError() != null) {
                        Toast.makeText(getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else if (responseModel != null && responseModel.getData() != null) {
                        LoginResponseModel loginResponseModel = responseModel.getData();
                        if (loginResponseModel != null) {
                            UserSession session = UserSession.getInstance();
                            session.setEmail(loginResponseModel.getEmail());
                            session.setToken(loginResponseModel.getToken());
                            session.setUserId(loginResponseModel.getUserId());
                            session.setFirstName(loginResponseModel.getFirstName());
                            session.setLastName(loginResponseModel.getLastName());
                            session.setPhoneNumber(loginResponseModel.getPhoneNo());
                            session.setGender(loginResponseModel.getGender());
                            session.setUserTypeId(loginResponseModel.getUserTypeId());
                            session.setTokenCreatedDate(loginResponseModel.getTokenCreatedDate());
                            session.setExpireDate(loginResponseModel.getExpireDate());
                            session.setIsTableReserved("");

                            int usertypevalue = loginResponseModel.getUserTypeId();
                            switch (usertypevalue) {


                                case AppStaticData.USERTYPE_CUSTOMER:
                                    intentFromMain = new Intent(LoginActivity.this, CustomerActivity.class);
                                    startActivity(intentFromMain);
                                    break;

                                case AppStaticData.USERTYPE_MANAGER:
                                    intentFromMain = new Intent(LoginActivity.this, ManagerActivity.class);
                                    startActivity(intentFromMain);
                                    break;

                                case AppStaticData.USERTYPE_CHEF:
                                    intentFromMain = new Intent(LoginActivity.this, ChefActivity.class);
                                    startActivity(intentFromMain);
                                    break;

                                case AppStaticData.USERTYPE_CASHIER:
                                    intentFromMain = new Intent(LoginActivity.this, CashierActivity.class);
                                    startActivity(intentFromMain);
                                    break;

                            }
                            finish();


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<LoginResponseModel>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong!" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public long getSessionTimeOut(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);
        date = calendar.getTime();
        return date.getTime();
    }

}

