package a.m.restaurant_automation.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.RegisterResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.UsersResponseModel;
import a.m.restaurant_automation.service.IDataService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class EmployeeFragment extends Fragment{
    //int userType=2 ;
    //int [] userTypeM=new int[4];
    View viewMenu;
    AddEmployeeAdapter addEmployeeAdapter;
    ArrayList<UsersResponseModel> registerResponse;
    FloatingActionButton floatingActionButton;

    OnDeleteEmployeePress onDeleteEmployeePress;

    public EmployeeFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewMenu = view;
        floatingActionButton=view.findViewById(R.id.floatingButton_addEmployee);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addEmployeeFragment);
            }
        });

        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);

        Call<ResponseModel<ArrayList<UsersResponseModel>>> call = dataService.getEmployees();

        call.enqueue(new Callback<ResponseModel<ArrayList<UsersResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<UsersResponseModel>>> call, Response<ResponseModel<ArrayList<UsersResponseModel>>> response) {
                ResponseModel<ArrayList<UsersResponseModel>> responseModel = response.body();

                if (responseModel != null && responseModel.getError() != null) {
                    Toast.makeText(getActivity().getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                } else if (responseModel != null && responseModel.getData() != null) {
                    registerResponse = responseModel.getData();
                   /* if (registerResponse == null || registerResponse.size() == 0){
                        emptyText.setVisibility(View.VISIBLE);
                    }else {
                        emptyText.setVisibility(View.GONE);
                    }*/
                    generateRecyclerView(registerResponse, viewMenu);

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<UsersResponseModel>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong!" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

        public void generateRecyclerView(ArrayList<UsersResponseModel> registerResponse, View viewMenu) {
            addEmployeeAdapter =new AddEmployeeAdapter(registerResponse,getActivity().getApplication());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            RecyclerView recyclerView =viewMenu.findViewById(R.id.recyclerView_employee);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(addEmployeeAdapter);
            addEmployeeAdapter.setOnItemClickListener(onClickListener);
            addEmployeeAdapter.notifyDataSetChanged();
        }
        public View.OnClickListener onClickListener =new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Employee")
                        .setMessage("Are you sure you want to delete this Employee?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                             int userId = (int) v.getTag();
                             boolean delete = true;
                             onDeleteEmployeePress.onDeleteEmployeePress(userId,delete);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();
            }
        };

    public interface OnDeleteEmployeePress {
        void onDeleteEmployeePress (int userId,boolean isDelete);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onDeleteEmployeePress= (EmployeeFragment.OnDeleteEmployeePress) context;
    }
}
