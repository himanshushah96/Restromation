package a.m.restaurant_automation.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import a.m.restaurant_automation.R;
import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.TableReservationStatusForCustomerModel;
import a.m.restaurant_automation.service.IDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerOverviewFragment extends Fragment implements View.OnClickListener {
    Button buttonDineIn, buttonTakeOut;
    UserSession session;
    Call<TableReservationStatusForCustomerModel> call;
    BottomNavigationView bottomNavigationView;
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = getActivity().findViewById(R.id.BottomnavigateMenuCustomer);
        bottomNavigationView.setVisibility(View.GONE);
        buttonDineIn = view.findViewById(R.id.btnDineIn);
        buttonTakeOut = view.findViewById(R.id.btnTakeOut);
        buttonDineIn.setOnClickListener(this);
        buttonTakeOut.setOnClickListener(this);
        session = UserSession.getInstance();
        IDataService dataService = RetrofitClient.getRetrofitInstance().create(IDataService.class);
        call = dataService.getReservationStatus(Integer.parseInt(session.getUserId()));
        if (session.getIsTableReserved().equalsIgnoreCase("") || session.getIsTableReserved().equalsIgnoreCase("Y"))
            call.enqueue(new Callback<TableReservationStatusForCustomerModel>() {
                @Override
                public void onResponse(Call<TableReservationStatusForCustomerModel> call, Response<TableReservationStatusForCustomerModel> response) {
                    if (response.body().Response)
                    {
                        session.setIsTableReserved("Y");
                        session.setDiningInOrTakeOut("FALSE");
                        session.setDiningInOrTakeOut("TRUE");
                        bottomNavigationView.setSelectedItemId(R.id.menu);
                        Navigation.findNavController(view).navigate(R.id.customerMenuItemsFragment);
                    } else {
                        session.setIsTableReserved("N");

                        if (session.getDiningInOrTakeOut().equalsIgnoreCase("TRUE")) {
                            bottomNavigationView.setSelectedItemId(R.id.tables);
                            Navigation.findNavController(view).navigate(R.id.customerTableViewFragment);
                        }
                        if (session.getDiningInOrTakeOut().equalsIgnoreCase("FALSE")) {
                            bottomNavigationView.setSelectedItemId(R.id.menu);
                            Navigation.findNavController(view).navigate(R.id.customerMenuItemsFragment);
                        }
                    }
                }

                @Override
                public void onFailure(Call<TableReservationStatusForCustomerModel> call, Throwable t) {

                }
            });
        else{
            if(session.getIsTableReserved().equalsIgnoreCase("Y") || session.getDiningInOrTakeOut().equalsIgnoreCase("FALSE")){
                bottomNavigationView.setSelectedItemId(R.id.menu);
                Navigation.findNavController(view).navigate(R.id.customerMenuItemsFragment);
            }
            else if (session.getDiningInOrTakeOut().equalsIgnoreCase("TRUE")) {
                bottomNavigationView.setSelectedItemId(R.id.tables);
                Navigation.findNavController(view).navigate(R.id.customerTableViewFragment);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_overview, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public interface OnDineInPress {
        void OnDineInPress();
    }

    public interface OnTakeOutPress {
        void OnTakeOutPress();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDineIn:
                session.setDiningInOrTakeOut("TRUE");
                bottomNavigationView.setSelectedItemId(R.id.tables);
                Navigation.findNavController(view).navigate(R.id.customerTableViewFragment);
                break;
            case R.id.btnTakeOut:
                bottomNavigationView.setSelectedItemId(R.id.menu);
                session.setDiningInOrTakeOut("FALSE");
                Navigation.findNavController(view).navigate(R.id.customerMenuItemsFragment);
                break;
        }
    }
}
