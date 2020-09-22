package a.m.restaurant_automation.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.GetCartItemResponseModel;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragmentCustomer extends Fragment implements View.OnClickListener {

    TextView textView_itemName_cart, textView_itemPrice_cart, emptyTextCart, textView_quantity_cart;
    Button addCartItem, subtractCartItem, RemoveItem, checkoutButton;
    int userId;
    View viewCartItems;
    RadioGroup radioGroupPaymentMethod;
    RadioButton radioButtoncardPayment, radioButtoncashPayment, radioButtonType;
    int selectedRadioType;
    Button buttonCheckout;
    OnCheckoutPress onCheckoutPress;
    UserSession session;
    CustomerMenuItemAdapter customerMenuItemAdapter;
    ArrayList<GetCartItemResponseModel> getCartItemResponseModels;
    CustomerMenuItemAdapter.ItemsChangedListener listener;

    public CartFragmentCustomer() {
        // Required empty public constructor
    }

    //    public CartFragmentCustomer(int userId) {
//        this.userId = userId;
//        //  public constructor to get the parameter
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.BottomnavigateMenuCustomer);
        bottomNavigationView.setVisibility(View.GONE);
        emptyTextCart = view.findViewById(R.id.emptyTextCart);
        textView_itemName_cart = view.findViewById(R.id.textView_itemName_cart);
       // textView_itemPrice_cart = view.findViewById(R.id.textView_itemPrice_cart);
        addCartItem = view.findViewById(R.id.buttonAddQuantity);
        subtractCartItem = view.findViewById(R.id.buttonSubtractQuantity);
        textView_quantity_cart = view.findViewById(R.id.textviewQuantityCart);
        viewCartItems = view;
        radioGroupPaymentMethod = view.findViewById(R.id.radioGroupPaymentmethod);
        radioButtoncardPayment = view.findViewById(R.id.cardPayment_radioButton);
        radioButtoncashPayment = view.findViewById(R.id.cashPayment_radioButton);
        radioGroupPaymentMethod.check(R.id.cardPayment_radioButton);
        radioButtonType = view.findViewById(selectedRadioType);
        buttonCheckout = view.findViewById(R.id.checkoutButton);
        buttonCheckout.setOnClickListener(this);
        UserSession session = UserSession.getInstance();
        String userId = session.getUserId();

        listener = new CustomerMenuItemAdapter.ItemsChangedListener() {
            @Override
            public void onItemsChanged(double sum) {
                buttonCheckout.setText("Checkout: ( " + String.format("%.2f", sum) + " $ ) + tax(14.975%)");
            }
        };

        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        Call<ResponseModel<ArrayList<GetCartItemResponseModel>>> call = dataService.getCartItems(userId);
        call.enqueue(new Callback<ResponseModel<ArrayList<GetCartItemResponseModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<GetCartItemResponseModel>>> call, Response<ResponseModel<ArrayList<GetCartItemResponseModel>>> response) {
                ResponseModel<ArrayList<GetCartItemResponseModel>> responseModel = response.body();

                if (responseModel != null && responseModel.getError() != null) {
                    Toast.makeText(getActivity().getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                } else if (responseModel != null && responseModel.getData() != null) {
                    getCartItemResponseModels = responseModel.getData();
                    if (getCartItemResponseModels == null || getCartItemResponseModels.size() == 0) {
                        emptyTextCart.setVisibility(View.VISIBLE);
                    } else {
                        emptyTextCart.setVisibility(View.GONE);
                        generateRecyclerView(getCartItemResponseModels, viewCartItems);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<GetCartItemResponseModel>>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong!" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateRecyclerView(ArrayList<GetCartItemResponseModel> getCartItemResponseModels, View viewCartItems) {
        customerMenuItemAdapter = new CustomerMenuItemAdapter(getCartItemResponseModels, getActivity().getApplication(), listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = viewCartItems.findViewById(R.id.recyclerView_cart);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(customerMenuItemAdapter);
        //customerMenuItemAdapter.setOnItemClickListener(onClickListener);
        customerMenuItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.checkoutButton) {
            if (radioButtoncardPayment.isChecked()) {
                int orderBy = Integer.parseInt(session.getInstance().getUserId());
                boolean isDiningIn = Boolean.parseBoolean(session.getInstance().getDiningInOrTakeOut());
                boolean isCardPayment = true;
                onCheckoutPress.onCheckoutPress(orderBy, isDiningIn, isCardPayment);
            } else if (radioButtoncashPayment.isChecked()) {
                //OrderCartItemRequestModel tag = (OrderCartItemRequestModel) v.getTag();
                int orderBy = Integer.parseInt(session.getInstance().getUserId());
                boolean isDiningIn = Boolean.parseBoolean(session.getInstance().getDiningInOrTakeOut());
                boolean isCardPayment = false;
                onCheckoutPress.onCheckoutPress(orderBy, isDiningIn, isCardPayment);
            }
            Navigation.findNavController(v).navigate(R.id.orderFragment);
        }
    }

    public interface OnCheckoutPress {
        void onCheckoutPress(int orderBy, boolean isDiningIn, boolean isCardPayment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onCheckoutPress = (OnCheckoutPress) context;
    }
}


