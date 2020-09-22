package a.m.restaurant_automation.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import a.m.restaurant_automation.R;


public class CustomerMenuItemsFragment extends Fragment {



    ViewPager mViewPagercustomer;
    TabLayout mTabLayoutcustomer;
    TabItem appetizerItem, mainCourseItem, beverageItem, dessertItem;
    PagerAdapter pagerAdaptercustomer;


    public CustomerMenuItemsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         return  inflater.inflate(R.layout.fragment_customer_menu_items, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.BottomnavigateMenuCustomer);
        bottomNavigationView.setVisibility(View.VISIBLE);

        //tablayout
        mViewPagercustomer =view.findViewById(R.id.viewpagercustomer);
        mTabLayoutcustomer = view.findViewById(R.id.tablayoutcustomer);

        appetizerItem = view.findViewById(R.id.appetizerItemcustomer);
        mainCourseItem = view.findViewById(R.id.mainCourseItemcustomer);
        beverageItem = view.findViewById(R.id.beverageItemcustomer);
        dessertItem = view.findViewById(R.id.dessertItemcustomer);

        pagerAdaptercustomer = new PagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayoutcustomer.getTabCount());
        mViewPagercustomer.setAdapter(pagerAdaptercustomer);


        mTabLayoutcustomer.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPagercustomer.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPagercustomer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayoutcustomer));
    }



    private class PagerAdapter extends FragmentPagerAdapter {
        private int tabNumber;

        public PagerAdapter(@NonNull FragmentManager fm, int behavior, int mTabs) {
            super(fm, behavior);
            this.tabNumber = mTabs;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CustomerCatergoryItemNavigate(1);
                case 1:
                    return new CustomerCatergoryItemNavigate(2);
                case 2:
                    return new CustomerCatergoryItemNavigate(3);
                case 3:
                    return new CustomerCatergoryItemNavigate(4);

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabNumber;
        }
    }



//    @Override
//    public void onDetach() {
//        super.onDetach();
//        if(call != null){
//            call.cancel();
//        }
//    }
}

