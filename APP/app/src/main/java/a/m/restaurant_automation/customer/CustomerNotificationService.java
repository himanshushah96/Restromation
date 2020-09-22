package a.m.restaurant_automation.customer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import a.m.restaurant_automation.CustomerActivity;
import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.CustomerNotificationOrders;
import a.m.restaurant_automation.responseModel.CustomerNotificationResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import a.m.restaurant_automation.service.INotificationService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerNotificationService extends Service {
    static public boolean isRunning = false;
    static public int capacity = 0;
    static public int userId = Integer.parseInt(UserSession.getInstance().getUserId());
    static public boolean userNotifiedForTableAvailability = false;
    static public boolean firstTime = true;
    static public HashMap<Integer, Integer> hashMap;
    static TimerTask timerTask;
    static Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        hashMap = new HashMap<>();
        capacity = intent.getIntExtra("capacity", 0);
        userId = intent.getIntExtra("userId", 0);
        final Handler handler = new Handler();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        getData(capacity, Integer.parseInt(UserSession.getInstance().getUserId()));
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 10000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service", "End");
        isRunning = false;
        timerTask.cancel();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, "channel_customer");

        b.setOngoing(true)
                .setContentTitle("Restomation")
                .setContentText("Fetching Notifications")
                .setSmallIcon(R.drawable.logo_restromation)
                .setTicker("Fetching");


        startForeground(1, b.build());
    }


    public void getData(int capacity, int userId) {
        Log.i("abcxyz", "UserId: "+userId);
        INotificationService notificationService = RetrofitClient.getRetrofitInstance().create(INotificationService.class);
        Call<ResponseModel<CustomerNotificationResponseModel>> call = notificationService.getNotificationDataCustomer(userId, capacity);
        call.enqueue(new Callback<ResponseModel<CustomerNotificationResponseModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<CustomerNotificationResponseModel>> call, Response<ResponseModel<CustomerNotificationResponseModel>> response) {
                ResponseModel<CustomerNotificationResponseModel> responseModel = response.body();
                Log.i("abcxyz", "Fetched");
                if (responseModel.getError() != null) {
                    Log.i("abcxyz", responseModel.getError().getErrorMessage());
                } else {
                    if (firstTime) {
                        for (int i = 0; i < responseModel.getData().orders.size(); i++) {
                            CustomerNotificationOrders model = responseModel.getData().orders.get(i);
                            hashMap.put(model.orderId, model.statusId);
                        }
                        firstTime = false;
                    } else {
                        for (int i = 0; i < responseModel.getData().orders.size(); i++) {
                            CustomerNotificationOrders model = responseModel.getData().orders.get(i);
                            if (hashMap.containsKey(model.orderId)) {
                                if (hashMap.get(model.orderId) != model.statusId) {
                                    //Notification to user
                                    String status = "";
                                    int statusId = model.statusId;
                                    if (statusId == 1) status = "Ordered";
                                    else if (statusId == 2) status = "In Progress";
                                    else if (statusId == 3) status = "Completed";
                                    else if (statusId == 4) status = "Cancelled";
                                    notifyOrderStatusUpdate(model.orderId, status);
                                    hashMap.put(model.orderId, model.statusId);
                                }
                            } else {
                                hashMap.put(model.orderId, model.statusId);
                            }
                        }
                    }


                    if (!userNotifiedForTableAvailability) {
                        if (responseModel.getData().istableAvailable) {
                            //Notifying user about table
                            notifyTableAvailability();
                            userNotifiedForTableAvailability = true;
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseModel<CustomerNotificationResponseModel>> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });
    }

    public void notifyOrderStatusUpdate(int orderId, String orderStatus) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "channel_customer_order_update";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Orders", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Order Update");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, CustomerActivity.class), 0);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_restromation)
                .setTicker("Order Status Updated")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Order Update")
                .setContentText("Order " + orderId + ": " + orderStatus)
                .setContentInfo("Info");
        notificationBuilder.setContentIntent(contentIntent);
        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }

    public void notifyTableAvailability() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "channel_customer_table_update";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Tables", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Table Update");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, CustomerActivity.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_restromation)
                .setTicker("Table Availability")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Table Availability Update")
                .setContentText("Table for  " + capacity + " is available to reserve.")
                .setContentInfo("Go reserve table before it is reserved by someone else!!!")
                .setContentIntent(contentIntent);

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
        userNotifiedForTableAvailability = true;
    }
}
