package a.m.restaurant_automation.cashier;

import android.os.Bundle;

import a.m.restaurant_automation.CashierActivity;
import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.customer.CustomerMenuItemAdapter;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.GetReadyForPaymentResponseModel;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class CashierReadyForPaymentFragment extends Fragment {
    Button btnPay;
    CashierGetReadyPaymentAdapter cashierGetReadyPaymentAdapter;
    ArrayList<GetReadyForPaymentResponseModel> getReadyForPaymentResponseModel;
    View viewMenu;
    UserSession userSession;
    Call<ResponseModel<ArrayList<GetReadyForPaymentResponseModel>>> call;


    public CashierReadyForPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cashier_ready_for_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewMenu=view;
        btnPay=view.findViewById(R.id.button_paymentGRP);


        final IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        call = dataService.getReadyForPayment();
        call.enqueue(new Callback<ResponseModel<ArrayList<GetReadyForPaymentResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<GetReadyForPaymentResponseModel>>> call, Response<ResponseModel<ArrayList<GetReadyForPaymentResponseModel>>> response) {
                ResponseModel<ArrayList<GetReadyForPaymentResponseModel>> responseModel = response.body();

                if (responseModel != null && responseModel.getError() != null) {
                    Toast.makeText(getActivity().getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                } else if (responseModel != null && responseModel.getData() != null) {
                    getReadyForPaymentResponseModel = responseModel.getData();

                    generateRecyclerView(getReadyForPaymentResponseModel, viewMenu);

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<GetReadyForPaymentResponseModel>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong!" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void generateRecyclerView(ArrayList<GetReadyForPaymentResponseModel> getReadyForPaymentResponse, View viewMenu) {
        cashierGetReadyPaymentAdapter = new CashierGetReadyPaymentAdapter(getReadyForPaymentResponse, getActivity().getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = viewMenu.findViewById(R.id.recyclerView_cashier_getReadyRecycler);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cashierGetReadyPaymentAdapter);
        //cashierGetReadyPaymentAdapter.setOnItemClickListener(onClickListener);
        cashierGetReadyPaymentAdapter.notifyDataSetChanged();
    }
}
