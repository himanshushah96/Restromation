package a.m.restaurant_automation.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import a.m.restaurant_automation.model.AppStaticData;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmployeeMenuItemsFragment extends Fragment {

    int category;
    MenuItemAdapter menuItemAdapter;
    TextView emptyText;
    View viewMenu;
    Button removeItemButton;
    OnChangePricePress onChangePricePress;
    OnDeleteItemPress onDeleteItemPress;
    Call<ResponseModel<ArrayList<MenuItemResponse>>> call;

    ArrayList<MenuItemResponse> menuItemResponse;

    public EmployeeMenuItemsFragment(){}

    public EmployeeMenuItemsFragment(int category) {
        // Required empty public constructor
        this.category = category;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_menu_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyText = view.findViewById(R.id.textView_empty);



        viewMenu = view;
        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        call =dataService.getMenuItem(category);

        call.enqueue(new Callback<ResponseModel<ArrayList<MenuItemResponse>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<MenuItemResponse>>> call, Response<ResponseModel<ArrayList<MenuItemResponse>>> response) {
                ResponseModel<ArrayList<MenuItemResponse>> responseModel =response.body();

                if (responseModel != null && responseModel.getError() != null){
                    Toast.makeText(getActivity().getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                }
                else if (responseModel!= null && responseModel.getData() != null){
                    menuItemResponse =responseModel.getData();
                    if (menuItemResponse == null || menuItemResponse.size() == 0){
                        emptyText.setVisibility(View.VISIBLE);
                    }else {
                        emptyText.setVisibility(View.GONE);
                    }
                    generateRecyclerView(menuItemResponse,viewMenu);

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<MenuItemResponse>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong!" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }

    public void generateRecyclerView(ArrayList<MenuItemResponse> menuItemResponse, View viewMenu) {
        menuItemAdapter =new MenuItemAdapter(menuItemResponse,getActivity().getApplication());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView =viewMenu.findViewById(R.id.recyclerView_menuItem);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(menuItemAdapter);
        menuItemAdapter.setOnItemClickListener(onClickListener);
        menuItemAdapter.notifyDataSetChanged();
    }

    public View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            int id = v.getId();
            if (id == R.id.updateItemButton){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Update Item");
                alertDialog.setMessage("Enter Updated Price :");
                final EditText editText_udaptePrice = new EditText(getActivity().getApplicationContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                editText_udaptePrice.setLayoutParams(layoutParams);
                alertDialog.setView(editText_udaptePrice);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double price = Double.parseDouble(editText_udaptePrice.getText().toString());
                        int userType = AppStaticData.USERTYPE_MANAGER;
                        int positionItem = (int) v.getTag();
                        boolean isDelete = false;
                        onChangePricePress.onChangePricePress(price,userType,positionItem,isDelete);
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();
                alertDialog.show();
            }
            else if (id == R.id.deleteItem){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Delete Item");
                alertDialog.setMessage("Do you want to delete this item?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double price = 0;
                        int userType = 0;
                        int positionItem = (int) v.getTag();
                        boolean isDelete = true;
                        onDeleteItemPress.onDeleteItemPress(price,userType,positionItem,isDelete, menuItemAdapter);
                        menuItemAdapter.notifyDataSetChanged();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        })
                        .create();
                alertDialog.show();

            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        if(call != null){
            call.cancel();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onChangePricePress= (EmployeeMenuItemsFragment.OnChangePricePress) context;
        onDeleteItemPress = (OnDeleteItemPress) context;
    }

    public interface OnChangePricePress {
        void onChangePricePress(double price, int UserType, int positionItem,boolean isDelete);
    }

    public interface OnDeleteItemPress {
        void onDeleteItemPress(double price, int UserType, int positionItem, boolean isDelete, MenuItemAdapter menuItemAdapter);
    }
}
