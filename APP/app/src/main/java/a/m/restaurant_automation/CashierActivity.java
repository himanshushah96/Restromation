package a.m.restaurant_automation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class CashierActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public BottomNavigationView bottomNavigationView;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        setUpNavigation();


    }

    public void setUpNavigation(){

        bottomNavigationView= findViewById(R.id.BottomnavigateMenuCashier);
        navController= Navigation.findNavController(this,R.id.cashierHostFragment);
        bottomNavigationView.setSelectedItemId(R.id.NewPaymentsCashier);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        NavigationUI.setupActionBarWithNavController(this,navController);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        Fragment fragment=null;
        menuItem.setCheckable(true);

        int id = menuItem.getItemId();

        switch (id){
            case R.id.DashboardCashier:
                navController.navigate(R.id.cashierDashboardFragment);
                return true;

            case R.id.NewPaymentsCashier:
               navController.navigate(R.id.cashierReadyForPaymentFragment);
                return true;


            case R.id.MoremenuCashier:
                navController.navigate(R.id.cashierMoreItemsFragment);
                return true;
        }
        return false;
    }


}
