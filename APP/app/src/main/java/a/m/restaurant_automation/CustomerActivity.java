package a.m.restaurant_automation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import a.m.restaurant_automation.customer.MoreOptionsFragment;
import a.m.restaurant_automation.requestModel.ChangePasswordRequestModel;
import a.m.restaurant_automation.service.IUserService;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import a.m.restaurant_automation.customer.CartFragmentCustomer;
import a.m.restaurant_automation.customer.CustomerCatergoryItemNavigate;
import a.m.restaurant_automation.customer.CustomerNotificationService;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.requestModel.AddToCartRequestModel;
import a.m.restaurant_automation.requestModel.OrderCartItemRequestModel;
import a.m.restaurant_automation.responseModel.OrderCartItemResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.StatusCheckResponse;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, CustomerCatergoryItemNavigate.OnaddToCartPress, CartFragmentCustomer.OnCheckoutPress, MoreOptionsFragment.OnChangePasswordPress {

    public BottomNavigationView bottomNavigationView;
    public NavController navController;
    UserSession session;
    private int Itemid, Quantity, AddedBy;
    private int cartIdCart, cartQuantity;
    private boolean cartIsDelete;
    private int OrderBy;
    private boolean IsDiningIn, IsCardPayment;

    private String newPassword;
    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        session = UserSession.getInstance();
        setUpNavigation();
        getNotifications();
    }

    public void getNotifications() {
        Intent serviceIntent = new Intent(this, CustomerNotificationService.class);
        serviceIntent.putExtra("capacity", 0);
        if (!CustomerNotificationService.isRunning) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Android O requires a Notification Channel.
            if (Build.VERSION.SDK_INT >= 26) {
                CharSequence name = getString(R.string.app_name);
                // Create the channel for the notification

                NotificationChannel mChannel = new NotificationChannel("channel_customer", name, NotificationManager.IMPORTANCE_HIGH);
                // Set the Notification Channel for the Notification Manager.
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(mChannel);
                }

                startForegroundService(new Intent(CustomerActivity.this, CustomerNotificationService.class));
            }
        }
    }

    public void setUpNavigation() {
        bottomNavigationView = findViewById(R.id.BottomnavigateMenuCustomer);
        navController = Navigation.findNavController(this, R.id.customerhostfragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setCheckable(true);
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.tables:
                navController.navigate(R.id.customerTableViewFragment);
                return true;

            case R.id.menu:
                navController.navigate(R.id.customerMenuItemsFragment);
                return true;


            case R.id.cart:
                navController.navigate(R.id.orderFragment);
                return true;

            case R.id.moreMenu:
                navController.navigate(R.id.moreOptionsFragment);
                return true;
        }
        return false;
    }

    public void addToCart() {
        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        AddToCartRequestModel addToCartRequestModel = new AddToCartRequestModel();
        addToCartRequestModel.itemId = Itemid;
        addToCartRequestModel.quantity = Quantity;
        addToCartRequestModel.addedby = AddedBy;

        Call<ResponseModel<StatusCheckResponse>> call = dataService.addToCart(addToCartRequestModel);
        call.enqueue(new Callback<ResponseModel<StatusCheckResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<StatusCheckResponse>> call, Response<ResponseModel<StatusCheckResponse>> response) {
                ResponseModel<StatusCheckResponse> responseModel = response.body();
                if (responseModel != null) {
                    if (responseModel.getError() != null) {
                        Toast.makeText(getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        if (responseModel.getData().statusCode.equalsIgnoreCase("1")) {
                            Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "" + responseModel.getData().statusMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<StatusCheckResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "something went wrong" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onaddToCartPress(int itemId, int quantity, int addedby) {
        Itemid = itemId;
        Quantity = quantity;
        AddedBy = addedby;
        addToCart();
    }

    public void onOrder() {
        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        OrderCartItemRequestModel orderCartItemRequestModel = new OrderCartItemRequestModel();
        orderCartItemRequestModel.orderBy = OrderBy;
        orderCartItemRequestModel.isDiningIn = IsDiningIn;
        orderCartItemRequestModel.isCardPayment = IsCardPayment;

        Call<ResponseModel<OrderCartItemResponseModel>> call = dataService.addOrder(orderCartItemRequestModel);
        call.enqueue(new Callback<ResponseModel<OrderCartItemResponseModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<OrderCartItemResponseModel>> call, Response<ResponseModel<OrderCartItemResponseModel>> response) {
                ResponseModel<OrderCartItemResponseModel> responseModel = response.body();
                if (responseModel != null) {
                    if (responseModel.getError() != null) {
                        Toast.makeText(getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), " Added to orders", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<OrderCartItemResponseModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "something went wrong" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCheckoutPress(int orderBy, boolean isDiningIn, boolean isCardPayment) {
        OrderBy = orderBy;
        IsDiningIn = isDiningIn;
        IsCardPayment = isCardPayment;
        onOrder();
    }


    public void changePassword (){

        IUserService userService = RetrofitClient.getRetrofitInstance().create(IUserService.class);
        ChangePasswordRequestModel changePasswordRequestModel = new ChangePasswordRequestModel();
        changePasswordRequestModel.password = newPassword;
        changePasswordRequestModel.userId = customerId;

        Call<ResponseModel<StatusCheckResponse>> call = userService.changePassword(changePasswordRequestModel);
        call.enqueue(new Callback<ResponseModel<StatusCheckResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<StatusCheckResponse>> call, Response<ResponseModel<StatusCheckResponse>> response) {
                ResponseModel<StatusCheckResponse> responseModel = response.body();

                if (responseModel != null) {
                    if (responseModel.getError() != null) {
                        Toast.makeText(getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), responseModel.getData().statusMessage + "Password Changed", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<StatusCheckResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "something went wrong" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void OnChangePasswordPress(String password, int userId) {
        newPassword = password;
        customerId = userId;
        changePassword();
    }
}
