package a.m.restaurant_automation.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import a.m.restaurant_automation.RetrofitClient;
import a.m.restaurant_automation.model.AppStaticData;
import a.m.restaurant_automation.requestModel.MenuItemRequestModel;
import a.m.restaurant_automation.responseModel.MenuItemResponse;
import a.m.restaurant_automation.responseModel.ResponseModel;
import a.m.restaurant_automation.service.IUserService;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import a.m.restaurant_automation.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeMenuItemActivity extends AppCompatActivity implements EmployeeMenuItemsFragment.OnChangePricePress, EmployeeMenuItemsFragment.OnDeleteItemPress {


    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    TabItem appetizerItem, mainCourseItem, beverageItem, dessertItem;
    PagerAdapter pagerAdapter;
    public NavController navController;
    FloatingActionButton floatingActionButton;
    MenuItemAdapter menuItemAdapter;

    private double changePrice;
    private int mUserType = AppStaticData.USERTYPE_MANAGER;
    private int itemPosition;
    private boolean removeItem;
    private int usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_menu_item);



        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablayout);

        appetizerItem = findViewById(R.id.appetizerItem);
        mainCourseItem = findViewById(R.id.mainCourseItem);
        beverageItem = findViewById(R.id.beverageItem);
        dessertItem = findViewById(R.id.dessertItem);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount());
        mViewPager.setAdapter(pagerAdapter);



        floatingActionButton =findViewById(R.id.floatingButton_addMenuItem);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(EmployeeMenuItemActivity.this, AddMenuItem.class));

            }
        });
        
        
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




    private class PagerAdapter extends FragmentPagerAdapter{
        private int tabNumber;

       public PagerAdapter(@NonNull FragmentManager fm,int behavior, int mTabs) {
           super(fm,behavior);
           this.tabNumber = mTabs;
       }

       @NonNull
       @Override
       public Fragment getItem(int position) {
           switch (position){
               case 0:
                   return new EmployeeMenuItemsFragment(1);
               case 1:
                   return new EmployeeMenuItemsFragment(2);
               case 2:
                   return new EmployeeMenuItemsFragment(3);
               case 3:
                   return new EmployeeMenuItemsFragment(4);

               default:
                   return null;

           }
       }

       @Override
       public int getCount() {
           return tabNumber;
       }
   }


    public void UpdateMenuItemPrice(){

        IUserService userService = RetrofitClient.getRetrofitInstance().create(IUserService.class);
        MenuItemRequestModel menuItemRequestModel = new MenuItemRequestModel();
        menuItemRequestModel.price =changePrice;
        menuItemRequestModel.updatedBy = mUserType;
        menuItemRequestModel.itemId = itemPosition;
        menuItemRequestModel.isDelete = removeItem;

        Call<ResponseModel<MenuItemResponse>> call = userService.changePrice(menuItemRequestModel);
        call.enqueue(new Callback<ResponseModel<MenuItemResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<MenuItemResponse>> call, Response<ResponseModel<MenuItemResponse>> response) {
                ResponseModel<MenuItemResponse> responseModel = response.body();
                if (responseModel != null) {
                    if (responseModel.getError() != null) {
                        Toast.makeText(getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getApplicationContext(), responseModel.getData().getMenuItemName() + " Price Changed", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<MenuItemResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "something went wrong" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onChangePricePress(double price, int UserType, int positionItem,boolean isDelete) {
        changePrice =price;
        mUserType = UserType;
        itemPosition = positionItem;
        removeItem = isDelete;
        UpdateMenuItemPrice();
    }

    public void DeletItem(){

        IUserService userService = RetrofitClient.getRetrofitInstance().create(IUserService.class);
        final MenuItemRequestModel menuItemRequestModel = new MenuItemRequestModel();
        menuItemRequestModel.price =changePrice;
        menuItemRequestModel.updatedBy = mUserType;
        menuItemRequestModel.itemId = itemPosition;
        menuItemRequestModel.isDelete = removeItem;

        Call<ResponseModel<MenuItemResponse>> call = userService.changePrice(menuItemRequestModel);
        call.enqueue(new Callback<ResponseModel<MenuItemResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<MenuItemResponse>> call, Response<ResponseModel<MenuItemResponse>> response) {
                ResponseModel<MenuItemResponse> responseModel = response.body();
                if (responseModel != null) {
                    if (responseModel.getError() != null) {
                        Toast.makeText(getApplicationContext(), responseModel.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), responseModel.getData().getMenuItemName() + " Item Deleted", Toast.LENGTH_LONG).show();
                        for(int i = 0; i < menuItemAdapter.menuItemResponse.size(); i++){
                            if(menuItemAdapter.menuItemResponse.get(i).getMenuItemId() == menuItemRequestModel.itemId){
                                menuItemAdapter.menuItemResponse.remove(i);
                                menuItemAdapter.size--;
                                menuItemAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel<MenuItemResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "something went wrong" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDeleteItemPress(double price, int UserType, int positionItem, boolean isDelete, MenuItemAdapter menuItemAdapter) {
        changePrice = price;
        usertype = UserType;
        itemPosition = positionItem;
        removeItem = isDelete;
        this.menuItemAdapter = menuItemAdapter;
        DeletItem();


    }
}
