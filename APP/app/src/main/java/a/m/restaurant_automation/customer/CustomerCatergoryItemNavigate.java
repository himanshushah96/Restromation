package a.m.restaurant_automation.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import a.m.restaurant_automation.requestModel.AddToCartRequestModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerCatergoryItemNavigate extends Fragment  {

    Button addItemButton;
    FloatingActionButton floatingActionButton;
    int category;
    CustomerMenuItemAdapter menuItemAdaptercustomer;
    View viewMenu;
    ArrayList<MenuItemResponse> menuItemResponsecustomer;


    Call<ResponseModel<ArrayList<MenuItemResponse>>> call;
    UserSession session;
    OnaddToCartPress onaddToCartPress;


    public CustomerCatergoryItemNavigate(int category) {
        this.category = category;
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
        return inflater.inflate(R.layout.fragment_customer_catergory_item_navigate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewMenu = view;
        addItemButton = view.findViewById(R.id.customer_addItemButton);
        floatingActionButton = view.findViewById(R.id.floatingButton_cartFragment);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.cartFragmentCustomer);
            }
        });



        final IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        call = dataService.getMenuItem(category);
        call.enqueue(new Callback<ResponseModel<ArrayList<MenuItemResponse>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<MenuItemResponse>>> call, Response<ResponseModel<ArrayList<MenuItemResponse>>> response) {
                ResponseModel<ArrayList<MenuItemResponse>> responseModel = response.body();

                if (responseModel != null && responseModel.getError() != null) {
                    Toast.makeText(getActivity().getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                } else if (responseModel != null && responseModel.getData() != null) {
                    menuItemResponsecustomer = responseModel.getData();

                    generateRecyclerView(menuItemResponsecustomer, viewMenu);

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<MenuItemResponse>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong!" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void generateRecyclerView(ArrayList<MenuItemResponse> menuItemResponse, View viewMenu) {
        ArrayList<MenuItemResponse> temp = new ArrayList<>();
        for(int i = 0; i < menuItemResponse.size(); i++){
            if(menuItemResponse.get(i).getAvailablequantity() > 0){
                temp.add(menuItemResponse.get(i));
            }
        }
        menuItemAdaptercustomer = new CustomerMenuItemAdapter(temp, getActivity().getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = viewMenu.findViewById(R.id.recyclerView_customer_menuItem);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(menuItemAdaptercustomer);
        menuItemAdaptercustomer.setOnItemClickListener(onClickListener);
        menuItemAdaptercustomer.notifyDataSetChanged();
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.customer_addItemButton) {
                //int itemid = (int) v.getTag();
                AddToCartRequestModel tag = (AddToCartRequestModel) v.getTag();
                int addedby = Integer.parseInt(session.getInstance().getUserId());
                if(tag != null) {
                    if(tag.quantity != 0)
                    onaddToCartPress.onaddToCartPress(tag.itemId, tag.quantity, addedby);
                    else
                        Toast.makeText(getContext().getApplicationContext(), "Please add atleast a single quantity of item!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext().getApplicationContext(), "Please add atleast a single quantity of item!!!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onaddToCartPress= (OnaddToCartPress) context;
    }

    public interface OnaddToCartPress {
            void onaddToCartPress(int itemId, int quantity, int addedby);
        }

}
