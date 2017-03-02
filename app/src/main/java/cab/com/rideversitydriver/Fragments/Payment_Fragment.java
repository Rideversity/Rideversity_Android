package cab.com.rideversitydriver.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cab.com.rideversitydriver.Adapters.Payment_PageAdapter;
import cab.com.rideversitydriver.R;

/**
 * Created by Kalidoss on 03/08/16.
 */

public class Payment_Fragment extends Fragment implements TabLayout.OnTabSelectedListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    public static final String EXTRA_FRAG_TYPE = "extraFragType";
    public static final int CONTACTS_FRAG = 0;
    Context context;
    Payment_PageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.payment, container, false);
        context = this.getActivity();
        tabLayout = (TabLayout) rootView.findViewById(R.id.payment_tabs);

        tabLayout.addTab(tabLayout.newTab().setText("Credit Card"));
        tabLayout.addTab(tabLayout.newTab().setText("Bank Account"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) rootView.findViewById(R.id.payment_viewpager);


        adapter = new Payment_PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());


        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(this);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);
                tabLayout.getTabAt(position).select();
                adapter.getItem(position).isVisible();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return rootView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        TabLayout.Tab selectedTab = tabLayout.getTabAt(tab.getPosition());
        selectedTab.select();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}