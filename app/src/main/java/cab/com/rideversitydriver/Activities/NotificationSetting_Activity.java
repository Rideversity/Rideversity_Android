package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.NotificationModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.MyApplication;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 01/08/16.
 */
public class NotificationSetting_Activity extends AppCompatActivity implements View.OnClickListener {

    MyApplication application = null;
    TextView txtTermofService;
    RelativeLayout imgBack;
    static Context context;
    static LinearLayout layoutFull;
    static Snackbar snackbar;
    static SharedPref sharedPref;
    public static View.OnClickListener notificationClickListener;
    private static RecyclerView recyclerView;
    private static ExpandableListView expandableListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView.Adapter adapter;
    public static HashMap<String, List<NotificationModel>> stringArrayListHashMap = new HashMap<>();
    public static ArrayList<String> stringsTitle;

    private TextView notify30MinutesBefore, notify1HourBefore, notify1DayBefore, notify3DaysBefore, newRidesPostedHeader, riderDoesntAcceptRealANdADvance,
            requestHasBeenAccepted, newRideHasBeenPosted, newRewardhasPosted, rewardExpire24hours, textHeaderAdvancedBooking, newRidesPosted;
    private SeekBar noticationSeekbar;
    private SwitchCompat notify30MinutesBeforeToggle, notify1HourBeforeToggle, notify1DayBeforeToggle, notify3DaysBeforeToggle, riderDoesntAcceptRealAndAdvanceToggle,
            riderHasBeenAcceptedToggle, newRideHasBeenPostedToggle, newRewardhasPostedToggle, rewardExpire24hoursToggle, newRidesPostedtoggle;

    static int intSeekbarMinValue = 0;
    static int mMinimumDonation = 0;
    static int mMaximumDonation = 0;
    int mileage_limit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_notification_layout);
        context = this;
        application = (MyApplication) getApplication();
        sharedPref = new SharedPref(context);
        notificationClickListener = new NotificationsOnClick(context);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        imgBack.setOnClickListener(this);
        layoutFull = (LinearLayout) findViewById(R.id.layout_notification);

        expandableListAdapter = (ExpandableListView) findViewById(R.id.recyclerView_notification);
        initViews();

        noticationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar arg0) {

            }

            public void onStartTrackingTouch(SeekBar arg0) {

            }

            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                intSeekbarMinValue = (arg1);

            }
        });

        if (Utilities.isOnline(context)) {
            Notification();
        } else {
            Toast.makeText(application, "" + Constants.NO_CONNECTION, Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        notify30MinutesBefore = (TextView) findViewById(R.id.notify_30_minutes_before);
        notify1HourBefore = (TextView) findViewById(R.id.notify_1_hour_before);
        notify1DayBefore = (TextView) findViewById(R.id.notify_1_day_before);
        notify3DaysBefore = (TextView) findViewById(R.id.notify_3_days_before);
        riderDoesntAcceptRealANdADvance = (TextView) findViewById(R.id.rider_doesnt_accept_real_advance);
        requestHasBeenAccepted = (TextView) findViewById(R.id.request_has_been_accepted);
        newRideHasBeenPosted = (TextView) findViewById(R.id.new_ride_has_been_posted);
        newRewardhasPosted = (TextView) findViewById(R.id.new_reward_has_posted);
        rewardExpire24hours = (TextView) findViewById(R.id.reward_expire_24_hours);
        textHeaderAdvancedBooking = (TextView) findViewById(R.id.text_header_advanced_booking);
        newRidesPostedHeader = (TextView) findViewById(R.id.new_rides_posted_header);
        newRidesPosted = (TextView) findViewById(R.id.new_rides_posted);
        notify30MinutesBeforeToggle = (SwitchCompat) findViewById(R.id.notify_30_minutes_before_toggle);
        notify1HourBeforeToggle = (SwitchCompat) findViewById(R.id.notify_1_hour_before_toggle);
        notify1DayBeforeToggle = (SwitchCompat) findViewById(R.id.notify_1_day_before_toggle);
        notify3DaysBeforeToggle = (SwitchCompat) findViewById(R.id.notify_3_days_before_toggle);
        riderDoesntAcceptRealAndAdvanceToggle = (SwitchCompat) findViewById(R.id.rider_doesnt_accept_real_advance_toggle);
        riderHasBeenAcceptedToggle = (SwitchCompat) findViewById(R.id.rider_has_been_accepted_toggle);
        newRideHasBeenPostedToggle = (SwitchCompat) findViewById(R.id.new_ride_has_been_posted_toggle);
        newRewardhasPostedToggle = (SwitchCompat) findViewById(R.id.new_reward_has_posted_toggle);
        rewardExpire24hoursToggle = (SwitchCompat) findViewById(R.id.reward_expire_24_hours_toggle);
        newRidesPostedtoggle = (SwitchCompat) findViewById(R.id.new_rides_posted_toggle);
        noticationSeekbar = (SeekBar) findViewById(R.id.seekbar_notification);
        noticationSeekbar.setMax(20);
    }

    private void setData() {

        textHeaderAdvancedBooking.setText(stringsTitle.get(0).replace("_", " "));
        newRidesPostedHeader.setText(stringsTitle.get(1).replace("_", " "));

        if (stringArrayListHashMap.size() > 0) {
        }
        notify30MinutesBefore.setText(stringArrayListHashMap.get(stringsTitle.get(0)).get(0).getNotification());
        notify1HourBefore.setText(stringArrayListHashMap.get(stringsTitle.get(0)).get(1).getNotification());
        notify1DayBefore.setText(stringArrayListHashMap.get(stringsTitle.get(0)).get(2).getNotification());
        notify3DaysBefore.setText(stringArrayListHashMap.get(stringsTitle.get(0)).get(3).getNotification());

        riderDoesntAcceptRealANdADvance.setText(stringArrayListHashMap.get(stringsTitle.get(1)).get(0).getNotification());
        requestHasBeenAccepted.setText(stringArrayListHashMap.get(stringsTitle.get(1)).get(1).getNotification());
        newRideHasBeenPosted.setText(stringArrayListHashMap.get(stringsTitle.get(1)).get(2).getNotification());
        newRewardhasPosted.setText(stringArrayListHashMap.get(stringsTitle.get(1)).get(3).getNotification());
        rewardExpire24hours.setText(stringArrayListHashMap.get(stringsTitle.get(1)).get(4).getNotification());
        newRidesPosted.setText(stringArrayListHashMap.get(stringsTitle.get(1)).get(5).getNotification());

        if (stringArrayListHashMap.get(stringsTitle.get(0)).get(0).getStatus().equals("1")) {
            notify30MinutesBeforeToggle.setChecked(true);
        } else {
            notify30MinutesBeforeToggle.setChecked(false);
        }

        notify30MinutesBeforeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(0)).get(0).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(0)).get(0).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });

        if (stringArrayListHashMap.get(stringsTitle.get(0)).get(1).getStatus().equals("1")) {
            notify1HourBeforeToggle.setChecked(true);
        } else {
            notify1HourBeforeToggle.setChecked(false);
        }

        notify1HourBeforeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(0)).get(1).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(0)).get(1).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });


        if (stringArrayListHashMap.get(stringsTitle.get(0)).get(2).getStatus().equals("1")) {
            notify1DayBeforeToggle.setChecked(true);
        } else {
            notify1DayBeforeToggle.setChecked(false);
        }

        notify1DayBeforeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(0)).get(2).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(0)).get(2).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });
        if (stringArrayListHashMap.get(stringsTitle.get(0)).get(3).getStatus().equals("1")) {
            notify3DaysBeforeToggle.setChecked(true);
        } else {
            notify3DaysBeforeToggle.setChecked(false);
        }

        notify3DaysBeforeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(0)).get(3).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(0)).get(3).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });

        if (stringArrayListHashMap.get(stringsTitle.get(1)).get(0).getStatus().equals("1")) {
            riderDoesntAcceptRealAndAdvanceToggle.setChecked(true);
        } else {
            riderDoesntAcceptRealAndAdvanceToggle.setChecked(false);
        }

        riderDoesntAcceptRealAndAdvanceToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(0).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(0).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });


        if (stringArrayListHashMap.get(stringsTitle.get(1)).get(1).getStatus().equals("1")) {
            riderHasBeenAcceptedToggle.setChecked(true);
        } else {
            riderHasBeenAcceptedToggle.setChecked(false);
        }

        riderHasBeenAcceptedToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(1).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(1).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });


        if (stringArrayListHashMap.get(stringsTitle.get(1)).get(2).getStatus().equals("1")) {
            newRideHasBeenPostedToggle.setChecked(true);
        } else {
            newRideHasBeenPostedToggle.setChecked(false);
        }

        newRideHasBeenPostedToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(2).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(2).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });


        if (stringArrayListHashMap.get(stringsTitle.get(1)).get(3).getStatus().equals("1")) {
            newRewardhasPostedToggle.setChecked(true);
        } else {
            newRewardhasPostedToggle.setChecked(false);
        }

        newRewardhasPostedToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(3).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(3).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });


        if (stringArrayListHashMap.get(stringsTitle.get(1)).get(4).getStatus().equals("1")) {
            rewardExpire24hoursToggle.setChecked(true);
        } else {
            rewardExpire24hoursToggle.setChecked(false);
        }

        rewardExpire24hoursToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(4).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(4).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });

        if (stringArrayListHashMap.get(stringsTitle.get(1)).get(5).getStatus().equals("1")) {
            newRidesPostedtoggle.setChecked(true);
        } else {
            newRidesPostedtoggle.setChecked(false);
        }

        newRidesPostedtoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (isChecked == true) {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(5).getNotificationId();
                        String isThisChecked = "1";
                        NotificationClick(strID, isThisChecked);
                    } else {
                        String strID = stringArrayListHashMap.get(stringsTitle.get(1)).get(5).getNotificationId();
                        String isThisChecked = "0";
                        NotificationClick(strID, isThisChecked);
                    }
                }
            }
        });


    }

    private class NotificationsOnClick implements View.OnClickListener {

        private final Context context;

        private NotificationsOnClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void Notification() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CommonAsynTask notificationAsyncTask = new CommonAsynTask(context, Constants.NOTIFICATION_LIST_URL, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        Singleton.getInstance().notificationArrayList.clear();
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {

                            try {
                                int mileage_limit = jsonObject.getInt(Constants.MILEAGE_LIMIT);
                                noticationSeekbar.setProgress(mileage_limit);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            stringArrayListHashMap = new HashMap<String, List<NotificationModel>>();
                            stringsTitle = new ArrayList<>();

                            NotificationModel model = new NotificationModel();
                            JSONArray titleArray = jsonObject.getJSONArray(Constants.TITLE);
                            if (titleArray != null && titleArray.length() > 0) {
                                for (int j = 0; j < titleArray.length(); j++) {
                                    stringsTitle.add(titleArray.getString(j));
                                }
                            }

                            JSONObject jsonArrayHeader = null;
                            try {
                                jsonArrayHeader = jsonObject.getJSONObject(Constants.DATA);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                for (int j = 0; j < stringsTitle.size(); j++) {
                                    JSONArray jsonArray = jsonArrayHeader.getJSONArray(stringsTitle.get(j));
                                    List<NotificationModel> listChild = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            NotificationModel notificationModel = new NotificationModel();
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            notificationModel.setNotificationId(jsonObject1.getString(Constants.NOTIFICATION_ID));
                                            notificationModel.setNotification(jsonObject1.getString(Constants.NOTIFICATION));
                                            notificationModel.setStatus(jsonObject1.getString(Constants.STATUS));
                                            listChild.add(notificationModel);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    stringArrayListHashMap.put(stringsTitle.get(j), listChild);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            Toast.makeText(application, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (stringArrayListHashMap.size() > 0) {
                        setData();
                    }
                } else {
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        notificationAsyncTask.execute();
    }

    public static void NotificationClick(String notificationid, String enable) {

        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.NOTIFICATION_ID, notificationid);
            objData.putOpt(Constants.STATUS, enable);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CommonAsynTask notificationAsyncTask = new CommonAsynTask(context, Constants.NOTIFICATION_ENABLE_URL, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            jsonObject.getString(Constants.MESSAGE);
                            Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        notificationAsyncTask.execute();
    }

    public void pickupLimits(int Value) {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.PICKUP_MILES, String.valueOf(Value));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        CommonAsynTask pickUpLimits = new CommonAsynTask(context, Constants.SET_PICKUP_LIMIT, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        pickUpLimits.execute();
    }

    public static void notificationSetPickupLimits() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.PICKUP_MILES, "");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CommonAsynTask advanceRideStart = new CommonAsynTask(context, Constants.ADVANCE_BOOKING_RIDE_START, "",
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        String result = jsonObject.getString(Constants.RESULT);
                        if (result.equals("success")) {
                            Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {

                            Toast.makeText(context, String.valueOf(jsonObject.getString(Constants.MESSAGE).equals(Constants.ERROR)), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        advanceRideStart.execute();
    }

    @Override
    public void onBackPressed() {
        pickupLimits(intSeekbarMinValue);
        super.onBackPressed();
    }
}