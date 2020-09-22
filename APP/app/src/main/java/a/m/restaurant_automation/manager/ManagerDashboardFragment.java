package a.m.restaurant_automation.manager;

import android.content.Intent;
import android.os.Bundle;

import a.m.restaurant_automation.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ManagerDashboardFragment extends Fragment {
    CardView viewMenu, viewEmployee, viewTables,currentBookings;
    public ManagerDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.BottomnavigateMenuManager);
        bottomNavigationView.setVisibility(View.VISIBLE);

        viewMenu = view.findViewById(R.id.cardView_viewMenu);
        viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent = new Intent(getActivity(),EmployeeMenuItemActivity.class);
                startActivity(_intent);
            }
        });

        viewEmployee= view.findViewById(R.id.cardView_viewEmployees);
        viewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.employeeFragment);
            }
        });

        viewTables = view.findViewById(R.id.cardView_viewTables);
        viewTables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.employeeTableFragment);
            }
        });
        currentBookings = view.findViewById(R.id.cardView_currentBookings);
        currentBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.cashierDashboardFragment);
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_dashboard, container, false);
    }
}
