package a.m.restaurant_automation.cashier;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import a.m.restaurant_automation.LoginActivity;
import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.chef.ChefDashboard;
import a.m.restaurant_automation.chef.ChefDashboardAdapter;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.GetOrderResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class CashierDashboardFragment extends Fragment {
    static RecyclerView recyclerView;
    ConstraintLayout progressWindow;
    Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call;
    Set<Integer> ordersSet;
    CashierDashboardAdapter dashboardAdapter;
    LinearLayoutManager layoutManager;
    Context context;
    static Timer timer;
    static TimerTask timertask;
    TextView noOrdersTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cashier_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerviewCashier);
        noOrdersTV = view.findViewById(R.id.textViewNoOrdersCashier);
        noOrdersTV.setVisibility(View.GONE);
        context = getActivity().getApplicationContext();

    }

    public void fetchOrders() {
        call.enqueue(new Callback<ResponseModel<ArrayList<GetOrderResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call, Response<ResponseModel<ArrayList<GetOrderResponseModel>>> response) {
                ResponseModel<ArrayList<GetOrderResponseModel>> orders = response.body();
                if (orders != null && orders.getData().isEmpty()) {
                    noOrdersTV.setText("No Orders!!!");
                    noOrdersTV.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else if (orders != null && orders.getData() != null && !orders.getData().isEmpty()) {
                    ArrayList<GetOrderResponseModel> ordersArrayList = orders.getData();
                   /* if (ordersSet != null) {
                        Set<Integer> tempSet = new HashSet<>();
                        HashMap<Integer, GetOrderResponseModel> hashMap = new HashMap<>();
                        for (int i = 0; i < ordersArrayList.size(); i++) {
                            tempSet.add(ordersArrayList.get(i).orderId);
                            hashMap.put(ordersArrayList.get(i).orderId, ordersArrayList.get(i));
                        }
                        tempSet.removeAll(ordersSet);
                        for (int x : tempSet) {
                            dashboardAdapter.orders.add(hashMap.get(x));
                            ordersSet.add(x);
                        }
                        if (!tempSet.isEmpty()) {
                            Uri alarmSound =
                                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            MediaPlayer mp = MediaPlayer.create(context, alarmSound);
                            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(1000);
                            }
                            mp.start();
                            dashboardAdapter.notifyDataSetChanged();
                        }
                    } */if (ordersSet == null)  {
                        //ArrayList<GetOrderResponseModel> ordersArrayList = orders.getData();
                        dashboardAdapter = new CashierDashboardAdapter(ordersArrayList);
                        recyclerView.setAdapter(dashboardAdapter);
                        layoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setVisibility(View.VISIBLE);
                        noOrdersTV.setVisibility(View.GONE);
                        ordersSet = new HashSet<>();
                        for (int i = 0; i < ordersArrayList.size(); i++) {
                            ordersSet.add(ordersArrayList.get(i).orderId);
                        }
                        dashboardAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(context, "" + orders.getError(), Toast.LENGTH_SHORT).show();
                }
                //   progressWindow.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call, Throwable t) {
                // progressWindow.setVisibility(View.GONE);
                Toast.makeText(context, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createTimerTask() {
        final Handler handler = new Handler();
        timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
                        call = dataService.getOrders(true, 0, "0", "0", "0", false);
                        fetchOrders();
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timertask, 0, 10000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            ordersSet = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //progressWindow.setVisibility(View.VISIBLE);
        createTimerTask();
    }

}
