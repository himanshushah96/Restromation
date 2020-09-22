package a.m.restaurant_automation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import a.m.restaurant_automation.model.AppStaticData;
import a.m.restaurant_automation.repository.UserSession;
import a.m.restaurant_automation.responseModel.LoginResponseModel;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Intent intentFromMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String userTypeValue = getIntent().getStringExtra("usertype");
//        LoginResponseModel loginResponseModel = responseModel.getData();
        // String userType = loginResponseModel.getUserType();
        //SharedPreferences preferences = getApplicationContext().getSharedPreferences("session", 0);
        //UserSession session = UserSession.getInstance(preferences);
        //String userType = getUserType().toS;

//        switch (userTypeValue) {
////
////
////        case "customer":
////            intentFromMain = new Intent(MainActivity.this, CustomerActivity.class);
////            startActivity(intentFromMain);
////            break;
////
////        case "manager":
////            intentFromMain = new Intent(MainActivity.this, EmployeeMenuItemActivity.class);
////            startActivity(intentFromMain);
////            break;
////
////        case "chef":
////            intentFromMain = new Intent(MainActivity.this, ChefActivity.class);
////            startActivity(intentFromMain);
////            break;
////
////        case "cashier":
////            intentFromMain = new Intent(MainActivity.this, CashierActivity.class);
////            startActivity(intentFromMain);
////            break;
////
////    }
}


}
