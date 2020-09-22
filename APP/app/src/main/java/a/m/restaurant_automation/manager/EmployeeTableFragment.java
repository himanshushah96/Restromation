package a.m.restaurant_automation.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.manager.MenuItemAdapter;
import a.m.restaurant_automation.responseModel.CustomerReserveTableResponse;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.responseModel.TableResponseModel;
import a.m.restaurant_automation.service.IDataService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class EmployeeTableFragment extends Fragment {
    int tableId;
    MenuItemAdapter menuItemAdapter;
    TextView emptyText;
    View viewTable;

    FloatingActionButton button_addTable;
    OnTableAddPress onTableAddPress;
    AlertDialog.Builder alertDialog;
    EditText addCapacity;
    OnDeleteTablePress onDeleteTablePress;

    ArrayList<CustomerReserveTableResponse> tableResponseModel;

    public EmployeeTableFragment() {
        // Required empty public constructor

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyText = view.findViewById(R.id.textView_emptyTable);
        viewTable = view;
        button_addTable = view.findViewById(R.id.button_addTable);
        //addCapacity = view.findViewById(R.id.editText_addCapacity);

        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);

        Call<ResponseModel<ArrayList<CustomerReserveTableResponse>>> call =dataService.getTables(0,0);

        call.enqueue(new Callback<ResponseModel<ArrayList<CustomerReserveTableResponse>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<CustomerReserveTableResponse>>> call, Response<ResponseModel<ArrayList<CustomerReserveTableResponse>>> response) {
                ResponseModel<ArrayList<CustomerReserveTableResponse>> responseModel =response.body();

                if (responseModel != null && responseModel.getError() != null){
                    Toast.makeText(getActivity().getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                }
                else if (responseModel!= null && responseModel.getData() != null){
                    tableResponseModel =responseModel.getData();
                    if (tableResponseModel == null || tableResponseModel.size() == 0){
                        emptyText.setVisibility(View.VISIBLE);
                    }else {
                        emptyText.setVisibility(View.GONE);
                        generateRecyclerView(tableResponseModel,viewTable);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<CustomerReserveTableResponse>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong!" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        button_addTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("ADD TABLE");
                    alertDialog.setMessage("Enter Table Capacity");
                    final EditText editText_tableCapacity = new EditText(getActivity().getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    editText_tableCapacity.setLayoutParams(layoutParams);
                    alertDialog.setView(editText_tableCapacity);
                    alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int capacity = Integer.parseInt(editText_tableCapacity.getText().toString());
                            onTableAddPress.OnTableAddPress(capacity);

                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
                    alertDialog.show();
                }

        });

    }


    public void generateRecyclerView(ArrayList<CustomerReserveTableResponse> tableResponseModel, View viewTable) {
        menuItemAdapter =new MenuItemAdapter(tableResponseModel,getActivity().getApplication(),0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView =viewTable.findViewById(R.id.recyclerView_viewTable);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(menuItemAdapter);
        menuItemAdapter.setOnItemClickListener(onClickListener);
        menuItemAdapter.notifyDataSetChanged();

    }

    public View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            int id = v.getId();
            if (id == R.id.imageDelete) {
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Delete Table")
                        .setMessage("Are you sure you want to delete this Table?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int tableId = (int) v.getTag();
                                boolean delete = true;
                                onDeleteTablePress.onDeleteTablePress(tableId, delete);
                                

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
        }
    };

    public interface OnDeleteTablePress {
        void onDeleteTablePress (int tableId,boolean isDelete);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_table, container, false);

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onTableAddPress = (EmployeeTableFragment.OnTableAddPress) context;
        onDeleteTablePress= (EmployeeTableFragment.OnDeleteTablePress) context;

    }

    public interface OnTableAddPress {
        void OnTableAddPress(int capacity);
    }

}



