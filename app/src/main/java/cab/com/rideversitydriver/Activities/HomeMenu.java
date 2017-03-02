package cab.com.rideversitydriver.Activities;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Fragments.BankAccount;
import cab.com.rideversitydriver.Fragments.HomeRide_Fragment;
import cab.com.rideversitydriver.Fragments.Profile_Fragment;
import cab.com.rideversitydriver.Fragments.Rewards_Fragment;
import cab.com.rideversitydriver.Fragments.RideHistory_Fragment;
import cab.com.rideversitydriver.Fragments.Settings_Fragment;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ReportModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.PolygonImageView;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SendLatLongService;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 01/08/16.
 */

public class HomeMenu extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    public ImageView imgMenu, imgSetting, imgLogout;
    public static ImageView imgCreateCarPool;
    Context context;
    NavigationView navigationView;
    TextView txtHeader, txtUserName;
    public TextView txtViewProfile;
    public static PolygonImageView imgProfile;
    public static ImageView imgRidefilter, homeRideFilter;
    public static TextView txtEdit;
    SharedPref sharedPref;
    public static RelativeLayout toolbar;
    private String mSuccessMessage;
    public static int isDriverMode;
    public static int isLadiesOnly = 0;
    private Dialog pDialog;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        context = this;
        sharedPref = new SharedPref(context);
        imgMenu = (ImageView) findViewById(R.id.imageView_menu);


        imgMenu.setOnClickListener(this);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        imgLogout = (ImageView) findViewById(R.id.imageView_logout);
        imgCreateCarPool = (ImageView) findViewById(R.id.imageView_create_carpool);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        imgCreateCarPool.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
        txtHeader = (TextView) findViewById(R.id.textView_screenheader);
        txtEdit = (TextView) findViewById(R.id.textView_edit);
        imgRidefilter = (ImageView) findViewById(R.id.imageView_ride_filter);
        homeRideFilter = (ImageView) findViewById(R.id.home_ride_filter);
        homeRideFilter.setOnClickListener(this);
        imgRidefilter.setOnClickListener(this);
        imgRidefilter.setVisibility(View.GONE);
        txtEdit.setVisibility(View.GONE);
        imgLogout.setVisibility(View.GONE);
        imgCreateCarPool.setVisibility(View.GONE);

        if (RideDetails_Activity.activityRideDetails_Activity != null) {
            RideDetails_Activity.activityRideDetails_Activity.finish();
        }


        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        initNavigationDrawer();

        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        if (intent != null) {
            mSuccessMessage = intent.getStringExtra("message");
            try {
                if (!TextUtils.isEmpty(mSuccessMessage)) {
                    bundle.putString(Constants.MESSAGE, mSuccessMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HomeRide_Fragment requestride_fragment = new HomeRide_Fragment();
        requestride_fragment.setArguments(bundle);
        start_fragment(requestride_fragment, "HOMERIDE");
        txtHeader.setText(getResources().getString(R.string.home));

        // navigation drawer SwitchCompat
        SwitchCompat piano = (SwitchCompat) navigationView.getMenu().getItem(5).getActionView().findViewById(R.id.toggle);
        //piano.setChecked(true);
        piano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    isDriverMode = 1;
                } else {
                    isDriverMode = 0;
                }
                DriverMode();
            }
        });

        final SwitchCompat ladiesOnly = (SwitchCompat) navigationView.getMenu().getItem(6).getActionView().findViewById(R.id.toggle);
        ladiesOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    isLadiesOnly = 1;
                } else {
                    isLadiesOnly = 0;
                }
                ladiesMode();
            }
        });

        if (Constants.PERSONAL_CAMERA_RECALL == true) {
            Constants.BECOME_DRIVER = false;
            Profile_Fragment fragment = new Profile_Fragment();
            start_fragment(fragment, "PROFILE");
        }

        //Check Box Initial Setup
        try {
            if (sharedPref.getString("DriverMode").equals("1")) {
                piano.setChecked(true);
            } else if (sharedPref.getString("DriverMode").equals("0")) {
                piano.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (sharedPref.getString("LadiesMode") != null) {
                if (sharedPref.getString("LadiesMode").equals("1")) {
                    ladiesOnly.setChecked(true);
                } else if (sharedPref.getString("LadiesMode").equals("0")) {
                    ladiesOnly.setChecked(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (RideDetails_Activity.toPayment) {
            txtHeader.setText(getResources().getString(R.string.payment));
            imgCreateCarPool.setVisibility(View.GONE);
            imgRidefilter.setVisibility(View.GONE);
            txtEdit.setVisibility(View.VISIBLE);
            //Payment_Fragment.isCardAdded=false;
            imgLogout.setVisibility(View.GONE);
            homeRideFilter.setVisibility(View.GONE);
                        /*Payment_Fragment payment_fragment = new Payment_Fragment();
                        start_fragment(payment_fragment, "PAYMENT");*/
            BankAccount bankAccount = new BankAccount();
            start_fragment(bankAccount, "bankaccount");
            drawerLayout.closeDrawers();
        }
    }

    public void initNavigationDrawer() {
        navigationView.setItemIconTintList(null);
        MenuItem first_menu = navigationView.getMenu().getItem(0);
        first_menu.setChecked(true);
        MenuItem item = navigationView.getMenu().getItem(5);

        item.setCheckable(false);

        MenuItem itemLadiesOnly = navigationView.getMenu().getItem(6);
        if (sharedPref.getString("gender") != null) {
            if (sharedPref.getString("gender").equals("Female")) {
                itemLadiesOnly.setVisible(true);
            } else if (sharedPref.getString("gender").equals("Male")) {
                itemLadiesOnly.setVisible(false);
            }
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {

                    case R.id.ride_home:
                        menuItem.setCheckable(true);
                        txtHeader.setText(getResources().getString(R.string.home));
                        imgCreateCarPool.setVisibility(View.GONE);
                        imgLogout.setVisibility(View.GONE);
                        Constants.RIDE_ACCPET = false;
                        homeRideFilter.setVisibility(View.VISIBLE);
                        imgRidefilter.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.GONE);
                        HomeRide_Fragment requestride_fragment = new HomeRide_Fragment();
                        start_fragment(requestride_fragment, "HomeRide");
                        drawerLayout.closeDrawers();
                        break;
                    /*case R.id.carpool:
                        txtHeader.setText(getResources().getString(R.string.carpool));
                        imgCreateCarPool.setVisibility(View.VISIBLE);
                        imgLogout.setVisibility(View.GONE);
                        homeRideFilter.setVisibility(View.GONE);
                        imgRidefilter.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.GONE);
                        Carpool_Fragment carPool_fragment = new Carpool_Fragment();
                        start_fragment(carPool_fragment);
                        drawerLayout.closeDrawers();
                        break;*/
                    case R.id.payments:
                        txtHeader.setText(getResources().getString(R.string.payment));
                        imgCreateCarPool.setVisibility(View.GONE);
                        imgRidefilter.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.VISIBLE);
                        //Payment_Fragment.isCardAdded=false;
                        imgLogout.setVisibility(View.GONE);
                        homeRideFilter.setVisibility(View.GONE);
                        /*Payment_Fragment payment_fragment = new Payment_Fragment();
                        start_fragment(payment_fragment, "PAYMENT");*/
                        BankAccount bankAccount = new BankAccount();
                        start_fragment(bankAccount, "bankaccount");
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.rewards:
                        imgLogout.setVisibility(View.GONE);
                        imgCreateCarPool.setVisibility(View.GONE);
                        txtHeader.setText(getResources().getString(R.string.rewards));
                        imgRidefilter.setVisibility(View.VISIBLE);
                        homeRideFilter.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.GONE);
                        Rewards_Fragment rewards_fragment = new Rewards_Fragment();
                        start_fragment(rewards_fragment, "REWARDS");
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.ride_history:
                        imgLogout.setVisibility(View.GONE);
                        imgCreateCarPool.setVisibility(View.GONE);
                        txtHeader.setText(getResources().getString(R.string.ride_history));
                        imgRidefilter.setVisibility(View.GONE);
                        homeRideFilter.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.GONE);
                        RideHistory_Fragment ridehistory_fragment = new RideHistory_Fragment();
                        start_fragment(ridehistory_fragment, "RIDEHISTORY");
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        txtViewProfile = (TextView) header.findViewById(R.id.text_view_profile);
        imgSetting = (ImageView) header.findViewById(R.id.imageView_setting);
        txtUserName = (TextView) header.findViewById(R.id.textView_name);
        imgProfile = (PolygonImageView) header.findViewById(R.id.imageView_profile);
        imgProfile.setBackgroundResource(android.R.color.transparent);
        imgProfile.setBackgroundColor(Color.TRANSPARENT);
        imgProfile.setAlpha(127);
        txtViewProfile.setOnClickListener(this);
        imgSetting.setOnClickListener(this);

        Log.e("userProfileImage ---> ", "" + sharedPref.getString("userProfileImage"));
        if (!sharedPref.getString("userProfileImage").isEmpty()) {
            Picasso.with(context).load(sharedPref.getString("userProfileImage")).fit().centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image).
                    into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.no_image);
        }
        txtUserName.setText(sharedPref.getString("userName"));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_menu:

                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(drawerLayout.getWindowToken(), 0);

                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.text_view_profile:
                txtHeader.setText("PROFILE");
                imgLogout.setVisibility(View.GONE);
                homeRideFilter.setVisibility(View.GONE);
                imgCreateCarPool.setVisibility(View.GONE);
                imgRidefilter.setVisibility(View.GONE);
                txtEdit.setVisibility(View.GONE);
                Profile_Fragment profile_fragment = new Profile_Fragment();
                start_fragment(profile_fragment, "PROFILE");
                drawerLayout.closeDrawers();
                break;

            case R.id.imageView_setting:
                imgLogout.setVisibility(View.GONE);
                imgCreateCarPool.setVisibility(View.GONE);
                txtHeader.setText(getResources().getString(R.string.settings));
                imgRidefilter.setVisibility(View.GONE);
                txtEdit.setVisibility(View.GONE);
                Settings_Fragment setting_fragment = new Settings_Fragment();
                start_fragment(setting_fragment, "SETTINGS");
                drawerLayout.closeDrawers();
                break;


            case R.id.imageView_create_carpool:
                /*isCarpoolAdded=false;
                Intent intent = new Intent(HomeMenu.this, CarPoolCreate_Activity.class);
                startActivity(intent);*/
                break;

            case R.id.home_ride_filter:
                Intent rideIntent = new Intent(context, RideFilter_Activity.class);
                startActivityForResult(rideIntent, 101);
                break;

            default:
                break;
        }
    }

    private void start_fragment(Fragment frag, String Tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, frag, Tag);
        fragmentTransaction.commit();
    }

    public void UnselectableNavigationrows() {
        MenuItem first_menu = navigationView.getMenu().getItem(0);
        MenuItem second_menu = navigationView.getMenu().getItem(1);
        MenuItem third_menu = navigationView.getMenu().getItem(2);
        MenuItem fourth_menu = navigationView.getMenu().getItem(3);
        MenuItem fifth_menu = navigationView.getMenu().getItem(4);
        MenuItem seventh_menu = navigationView.getMenu().getItem(6);
        MenuItem eigth_menu = navigationView.getMenu().getItem(7);

        first_menu.setChecked(false);
        second_menu.setChecked(false);
        third_menu.setChecked(false);
        fourth_menu.setChecked(false);
        fifth_menu.setChecked(false);
        seventh_menu.setChecked(false);
        eigth_menu.setChecked(false);
    }

    private void ladiesMode() {
        JSONObject ladiesData = new JSONObject();
        try {
            ladiesData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
            ladiesData.putOpt(Constants.LADIES_ONLY, isLadiesOnly);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CommonAsynTask ladiesModeAsyncTask = new CommonAsynTask(HomeMenu.this, Constants.LADIES_MODE_URL, ladiesData.toString(), Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.i("JSONObject", "" + jsonObject);
                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            jsonObject.getString(Constants.MESSAGE);
                            jsonObject.getString(Constants.RESULT);
                            if (jsonObject.getString(Constants.MESSAGE).equals("ladies only enabled successfully")) {
                                sharedPref.setString("LadiesMode", "1");
                            } else if (jsonObject.getString(Constants.MESSAGE).equals("ladies only disabled successfully")) {
                                sharedPref.setString("LadiesMode", "0");
                            }
                        } else {
                            Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        ladiesModeAsyncTask.execute();
    }

    public void DriverMode() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.DRIVER_MODE, isDriverMode);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CommonAsynTask drivermodeAsyncTask = new CommonAsynTask(HomeMenu.this, Constants.DRIVER_MODE_URL, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.e("jsonObject--->", "" + jsonObject);

                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            jsonObject.getString(Constants.MESSAGE);
                            jsonObject.getString(Constants.RESULT);
                            if (jsonObject.getString(Constants.MESSAGE).equals("Drivermode enabled successfully")) {
                                Log.e("Enabled--->", "Enabled");
                                sharedPref.setString("DriverMode", "1");

                                if (context.startService(new Intent(context, SendLatLongService.class)) == null) {
                                    Intent serviceIntent = new Intent(context, SendLatLongService.class);
                                    serviceIntent.putExtra("UserId", sharedPref.getString("userId"));
                                    context.startService(serviceIntent);
                                }
                            } else if (jsonObject.getString(Constants.MESSAGE).equals("Drivermode disabled successfully")) {
                                Log.e("Disabled--->", "Disabled");
                                sharedPref.setString("DriverMode", "0");
                                context.stopService(new Intent(context, SendLatLongService.class));
                            }
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        drivermodeAsyncTask.execute();
    }

    private String getData() {
        JSONObject activeRiderData = new JSONObject();
        SharedPref pref = new SharedPref(this);
        String userId = pref.getString("userId");
        if (!TextUtils.isEmpty(userId)) {
            try {
                activeRiderData.put("driverId", userId);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return activeRiderData.toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (fragment instanceof Rewards_Fragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (fragment instanceof HomeRide_Fragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode != RESULT_CANCELED) {
            if (requestCode == 100) {

            }
        }
    }
}

