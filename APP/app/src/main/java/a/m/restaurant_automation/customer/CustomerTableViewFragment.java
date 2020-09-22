package a.m.restaurant_automation.customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.CustomerReserveTableResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomerTableViewFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Call<ResponseModel<ArrayList<CustomerReserveTableResponse>>> call;
    TextView textViewCapacity;
    Button addCapacity, subcapacity, doneButton;
    int capacity = 0;
    UserSession session;
    BottomNavigationView bottomNavigationView;
    public CustomerTableViewFragment() {
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
        return inflater.inflate(R.layout.fragment_customer_table_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigationView = getActivity().findViewById(R.id.BottomnavigateMenuCustomer);
        bottomNavigationView.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.recyclerviewReserveTable);
        textViewCapacity = view.findViewById(R.id.textViewCapacity);
        addCapacity = view.findViewById(R.id.btnPlusCapacityTable);
        subcapacity = view.findViewById(R.id.btnMinusCapacityTable);
        doneButton =view.findViewById(R.id.btnDoneCapacity);
        session = UserSession.getInstance();
        if(session.getIsTableReserved().equalsIgnoreCase("Y")){
            Toast.makeText(getActivity().getApplicationContext(), "You already have a table reserved!!! To reserve other table you must end reservation for existing table!!!", Toast.LENGTH_LONG).show();
            bottomNavigationView.setSelectedItemId(R.id.menu);
            Navigation.findNavController(view).navigate(R.id.customerMenuItemsFragment);
        }
        if(session.getDiningInOrTakeOut().equalsIgnoreCase("FALSE")){
            final View v = view;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You opt Take-Out before, Do you want to reserve a table?");
            builder.setTitle("Reserve table!!!");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    session.setDiningInOrTakeOut("D");
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    bottomNavigationView.setSelectedItemId(R.id.menu);
                    Navigation.findNavController(v).navigate(R.id.customerMenuItemsFragment);
                }
            });
            builder.show();
        }
        addCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewCapacity.setText(""+(Integer.parseInt(textViewCapacity.getText().toString())+1));
            }
        });
        subcapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(textViewCapacity.getText().toString()) > 1)
                textViewCapacity.setText(""+(Integer.parseInt(textViewCapacity.getText().toString())-1));
                else
                    Toast.makeText(getActivity().getApplicationContext(), "Capacity must be greator than 0", Toast.LENGTH_SHORT).show();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capacity = Integer.parseInt(textViewCapacity.getText().toString());
                if(capacity < 1){
                    Toast.makeText(getActivity().getApplicationContext(), "Capacity must be greator than 0", Toast.LENGTH_SHORT).show();
                }
                else{
                    IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
                    call = dataService.getTables(0, capacity);
                    call.enqueue(new Callback<ResponseModel<ArrayList<CustomerReserveTableResponse>>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<ArrayList<CustomerReserveTableResponse>>> call, Response<ResponseModel<ArrayList<CustomerReserveTableResponse>>> response) {
                            ResponseModel<ArrayList<CustomerReserveTableResponse>> responseModel = response.body();
                            if (responseModel != null) {
                                if (responseModel != null) {
                                    if (responseModel.getError() != null) {
                                        Toast.makeText(getActivity().getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        CustomerTablesAdapter tablesAdapter = new CustomerTablesAdapter(responseModel.getData(), capacity);
                                        recyclerView.setAdapter(tablesAdapter);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        if(responseModel.getData().isEmpty()){
                                            CustomerNotificationService.capacity = capacity;
                                            CustomerNotificationService.userNotifiedForTableAvailability = false;
                                        }
                                    }
                                } else {

                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseModel<ArrayList<CustomerReserveTableResponse>>> call, Throwable t) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }
}
