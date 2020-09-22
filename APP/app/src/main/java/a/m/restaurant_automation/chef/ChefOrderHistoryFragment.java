package a.m.restaurant_automation.chef;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.responseModel.GetOrderResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChefOrderHistoryFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EditText fromDateET, toDateET;
    ImageButton fromDateIB, toDateIB;
    Button doneIB;
    TextView noResultTV;
    ConstraintLayout progressWindow;
    public ChefOrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chef_order_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated( view,savedInstanceState );
        recyclerView = view.findViewById( R.id.recyclerviewChefOrderHistory );
        layoutManager = new LinearLayoutManager( getActivity().getApplicationContext() );
        fromDateET = view.findViewById( R.id.editTextFromDate );
        toDateET = view.findViewById( R.id.editTextToDate );
        fromDateIB = view.findViewById( R.id.imageButtonFromDate );
        toDateIB = view.findViewById( R.id.imageButtonToDate );
        doneIB = view.findViewById( R.id.doneIB );
        noResultTV = view.findViewById(R.id.textViewNoResult);
        progressWindow = view.findViewById(R.id.loadingLayout);
        noResultTV.setVisibility(View.GONE);

        doneIB.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkEmptyData()){
                    progressWindow.setVisibility(View.VISIBLE);
                    IDataService iDataService = RetrofitClient.getRetrofitInstance().create( IDataService.class );
                    Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call = iDataService.getOrders( false,0, fromDateET.getText().toString(), toDateET.getText().toString(), "0" , false);
                    call.enqueue( new Callback<ResponseModel<ArrayList<GetOrderResponseModel>>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call,Response<ResponseModel<ArrayList<GetOrderResponseModel>>> response) {
                            ResponseModel<ArrayList<GetOrderResponseModel>> responseModel = response.body();
                            if(responseModel.getError() != null){
                                Toast.makeText( getActivity().getApplicationContext(),""+responseModel.getError().getErrorMessage(),Toast.LENGTH_SHORT ).show();
                            }
                            else if(responseModel != null && !responseModel.getData().isEmpty()){
                                ChefOrderHistoryAdapter chefOrderHistoryAdapter = new ChefOrderHistoryAdapter(responseModel.getData());
                                recyclerView.setAdapter( chefOrderHistoryAdapter );
                                recyclerView.setLayoutManager( layoutManager );
                                recyclerView.setVisibility(View.VISIBLE);
                                noResultTV.setVisibility(View.GONE);
                                Log.i("info", responseModel.getData().toString());
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                                noResultTV.setVisibility(View.VISIBLE);
                                noResultTV.setText("No result found between these two dates!!!");
                            }
                            progressWindow.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<ArrayList<GetOrderResponseModel>>> call,Throwable t) {
                            Toast.makeText(getActivity().getApplicationContext(), "Failed: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressWindow.setVisibility(View.GONE);
                        }
                    } );
                }
            }
        } );
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        fromDateIB.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,int year,int month,int dayOfMonth) {
                        Calendar tempCalendar = Calendar.getInstance();
                        tempCalendar.set(year, month, dayOfMonth);
                        fromDateET.setText(simpleDateFormat.format(tempCalendar.getTime()));
                    }
                }, calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ));
                datePickerDialog.show();
            }
        } );

        toDateIB.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,int year,int month,int dayOfMonth) {
                        Calendar tempCalendar = Calendar.getInstance();
                        tempCalendar.set(year, month, dayOfMonth);
                        toDateET.setText(simpleDateFormat.format(tempCalendar.getTime()));
                    }
                }, calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ));
                datePickerDialog.show();
            }
        } );
    }

    public boolean checkEmptyData(){
        if(TextUtils.isEmpty( fromDateET.getText().toString() )){
            fromDateET.setError("From Date Cannot be Empty!");
            fromDateET.requestFocus();
            return true;
        }
        else if(TextUtils.isEmpty( toDateET.getText().toString() )){
            toDateET.setError("From Date Cannot be Empty!");
            toDateET.requestFocus();
            return true;
        }
        return false;
    }
}
