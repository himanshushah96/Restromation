package a.m.restaurant_automation.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.GetOrderResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderFragment extends Fragment {
    RecyclerView recyclerView;
    TextView emptyText;

    ArrayList<GetOrderResponseModel> getOrderResponseModel;
    View viewOrders;
    OrderAdapterCustomer orderAdapterCustomer;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.BottomnavigateMenuCustomer);
        bottomNavigationView.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.recyclerView_customerOrder);
        emptyText = view.findViewById(R.id.emptyTextCustomerOrder);
        viewOrders = view;

        UserSession session =  UserSession.getInstance();
        int UserId = Integer.parseInt(session.getUserId());
        String emailId = session.getEmail();
        boolean isPayment = false;


        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call =dataService.getOrders(false,UserId,"0","0",emailId,isPayment);

        call.enqueue(new Callback<ResponseModel<ArrayList<GetOrderResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call, Response<ResponseModel<ArrayList<GetOrderResponseModel>>> response) {
                ResponseModel<ArrayList<GetOrderResponseModel>> responseModel =response.body();

                if (responseModel != null && responseModel.getError() != null){
                    Toast.makeText(getActivity().getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                }
                else if (responseModel!= null && responseModel.getData() != null){
                    getOrderResponseModel =responseModel.getData();
                    if (getOrderResponseModel == null || getOrderResponseModel.size() == 0){
                        emptyText.setVisibility(View.VISIBLE);
                    }else {
                        emptyText.setVisibility(View.GONE);
                        generateRecyclerView(getOrderResponseModel,viewOrders);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong! could not get the orders." + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void generateRecyclerView(ArrayList<GetOrderResponseModel> getOrderResponseModel, View viewOrders) {
      orderAdapterCustomer =new OrderAdapterCustomer(getOrderResponseModel,getActivity().getApplication());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView =viewOrders.findViewById(R.id.recyclerView_customerOrder);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(orderAdapterCustomer);
        orderAdapterCustomer.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history_customer, container, false);
    }
}
