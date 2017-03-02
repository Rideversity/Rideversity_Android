package cab.com.rideversitydriver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cab.com.rideversitydriver.Adapters.RideHistory_PageAdapter;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.Constants;

/**
 * Created by Kalidoss on 03/08/16.
 */
public class RideHistory_Fragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String confirmCancelMsg = "";
    Snackbar snackbar;
    static Activity activity;
    LinearLayout layoutFull;
    Context context;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        rootView = inflater.inflate(R.layout.ride_history, container, false);
        context = this.getActivity().getApplicationContext();
        activity = this.getActivity();
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        layoutFull = (LinearLayout) rootView.findViewById(R.id.layout_ridehistory);
        tabLayout.addTab(tabLayout.newTab().setText("ACTIVE RIDES"));
        tabLayout.addTab(tabLayout.newTab().setText("PAST RIDES"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

        //SoftkeyBoard Handles
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                if (heightDiff > 100) {

                    getActivity().getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    InputMethodManager inputManager = (InputMethodManager) rootView
                            .getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    IBinder binder = rootView.getWindowToken();
                    inputManager.hideSoftInputFromWindow(binder,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if (heightDiff < 100) {
                }
            }
        });

        RideHistory_PageAdapter adapter = new RideHistory_PageAdapter(getFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(this);
        return rootView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.HISTORY_FROM.equals("ACTIVE_RIDE")) {
            if (!confirmCancelMsg.equals("")) {
                snackbar = Snackbar.make(layoutFull, confirmCancelMsg, Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                snackbar.show();
            }
        }

        //SoftkeyBoard Handles
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                if (heightDiff > 100) {

                    getActivity().getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    InputMethodManager inputManager = (InputMethodManager) rootView
                            .getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    IBinder binder = rootView.getWindowToken();
                    inputManager.hideSoftInputFromWindow(binder,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if (heightDiff < 100) {
                }
            }
        });

    }
}