package cab.com.rideversitydriver.Activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Adapters.SchoolSpinnerAdapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ActiveRiderModel;
import cab.com.rideversitydriver.Models.SchoolListModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.MyApplication;
import cab.com.rideversitydriver.Utils.MySpinner;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;


/**
 * Created by Kalidoss on 04/08/16.
 */
public class RideFilter_Activity extends AppCompatActivity implements View.OnClickListener {

    MyApplication application = null;
    TextView txtTermofService;
    RelativeLayout imgBack;
    Button btnUpdate;
    private MySpinner mSchoolSpinner;
    private LayoutInflater layoutInflater;
    private AppCompatSeekBar mSeekBar;
    private TextView mMilesTextView;
    private EditText mZipCodeEditText;
    private String mMiles = "10";
    private String mSchoolName;
    Snackbar snackbar;
    int iCurrentSelection;
    boolean spinnerTouch = false;
    RelativeLayout layoutFull;
    Context context;
    String schoolName, strSchool = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.ride_filter);
        context = this;
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        btnUpdate = (Button) findViewById(R.id.button_update);
        mSchoolSpinner = (MySpinner) findViewById(R.id.spinner_school);
        mSeekBar = (AppCompatSeekBar) findViewById(R.id.seekBar);
        mMilesTextView = (TextView) findViewById(R.id.milesTextView);
        mZipCodeEditText = (EditText) findViewById(R.id.edittext_zip);
        layoutFull = (RelativeLayout) findViewById(R.id.layout_filter);
        imgBack.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mMilesTextView.setText(progress + "mi");
                mMiles = progress + "";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (Utilities.isOnline(this)) {
            schoolListAPI();
        } else {
        }

        //spnSchool
        mSchoolSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (spinnerTouch == false) {
                        spinnerTouch = true;
                        mSchoolSpinner.setBackgroundResource(R.drawable.spinner_up);
                    } else {
                        spinnerTouch = false;
                        mSchoolSpinner.setBackgroundResource(R.drawable.spinner_down);
                    }
                }
                return false;
            }
        });

        iCurrentSelection = mSchoolSpinner.getSelectedItemPosition();
        mSchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTouch = false;
                if (iCurrentSelection != position) {
                    mSchoolSpinner.setBackgroundResource(R.drawable.spinner_down);
                    strSchool = Singleton.getInstance().schoolArray.get(position).schoolId;
                    mSchoolName = Singleton.getInstance().schoolArray.get(position).schoolName;
                    Constants.schoolId = strSchool;
                } else {
                    mSchoolSpinner.setBackgroundResource(R.drawable.spinner_down);
                }
                iCurrentSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSchoolSpinner.setBackgroundResource(R.drawable.spinner_up);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.button_update:
                schoolName = mSchoolSpinner.getSelectedItem().toString();
                String zipCode = mZipCodeEditText.getText().toString();
                Constants.zipcode = zipCode;
                Constants.distance = "10";
                parseActiveRiderList(strSchool, zipCode, mMiles);
                parseAdvancedBookings(strSchool, zipCode, mMiles);
                this.finish();
                break;
            default:
                break;
        }
    }

    //data for school spinner
    private void schoolListAPI() {
        if (Utilities.isOnline(this)) {
            CommonAsynTask schoolListAsync = new CommonAsynTask(this, Constants.SCHOOLLIST_URL, "", Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
                @Override
                public void onTaskCompleted(JSONObject jsonObject) {

                    if (jsonObject != null) {
                        if (jsonObject.has(Constants.MESSAGE)) {
                            String message = jsonObject.optString(Constants.MESSAGE);
                            if (message.equals("message success")) {
                                JSONArray jsonArray = jsonObject.optJSONArray(Constants.DATA);
                                Singleton.getInstance().schoolArray.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    SchoolListModel schoolListModel = new SchoolListModel();
                                    JSONObject dataObject = jsonArray.optJSONObject(i);
                                    schoolListModel.schoolId = dataObject.optString(Constants.SCHOOLID);
                                    schoolListModel.schoolName = dataObject.optString(Constants.SCHOOLNAME);
                                    Singleton.getInstance().schoolArray.add(schoolListModel);

                                }
                                SchoolListModel schoolModelDef = new SchoolListModel();
                                schoolModelDef.schoolId = "0";
                                schoolModelDef.schoolName = "Select";
                                Singleton.getInstance().schoolArray.add(schoolModelDef);
                                SchoolSpinnerAdapter adapterSchool = new SchoolSpinnerAdapter(layoutInflater, Singleton.getInstance().schoolArray);
                                mSchoolSpinner.setAdapter(adapterSchool);
                                //spnSchool.setSelection(0);
                                mSchoolSpinner.setSelection(adapterSchool.getCount());
                            }
                        }
                    }

                }
            });
            schoolListAsync.execute();
        }
    }

    private void parseActiveRiderList(String schoolName, String zipCode, String miles) {

        if (Utilities.isOnline(context)) {


            CommonAsynTask activeRiderTask = new CommonAsynTask(this, Constants.ACTIVE_RIDER_API, getData(schoolName, zipCode, miles),
                    Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {

                @Override
                public void onTaskCompleted(JSONObject jsonObject) {

                    if (jsonObject != null) {
                        Singleton.getInstance().mActiveRiderArray.clear();

                        try {
                            String message = jsonObject.getString(Constants.ACTIVE_RIDE_MESSAGE);

                            if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    ActiveRiderModel model = new ActiveRiderModel();
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    model.setRideId(dataObject.getString(Constants.ACTIVE_RIDE_RIDE_ID));
                                    model.setRiderId(dataObject.getString(Constants.ACTIVE_RIDE_RIDER_ID));
                                    model.setDriverId(dataObject.getString(Constants.ACTIVE_RIDE_DRIVER_ID));
                                    model.setRideTime(dataObject.getString(Constants.ACTIVE_RIDE_RIDE_TIME));
                                    model.setRideType(dataObject.getString(Constants.ACTIVE_RIDE_TYPE));
                                    model.setRideTypeId(dataObject.getString(Constants.ACTIVE_RIDE_TYPE_ID));
                                    model.setRideDate(dataObject.getString(Constants.ACTIVE_RIDE_DATE));
                                    model.setPickUpLocation(dataObject.getString(Constants.ACTIVE_RIDE_PICK_UP_LOCATION));
                                    model.setDestination(dataObject.getString(Constants.ACTIVE_RIDE_DESTINATION));
                                    model.setSeatNo(dataObject.getString(Constants.ACTIVE_RIDE_SEAT_NO));
                                    model.setTripType(dataObject.getString(Constants.ACTIVE_RIDE_TRIP_TYPE));
                                    model.setDonation(dataObject.getString(Constants.ACTIVE_RIDE_DONATION));
                                    model.setLadiesOnly(dataObject.getString(Constants.ACTIVE_RIDE_LADIES_ONLY));
                                    model.setDiscountId(dataObject.getString(Constants.ACTIVE_RIDE_DISCOUNT_ID));
                                    //model.setDriverReqCount(dataObject.getString(Constants.ACTIVE_RIDE_DRIVER_REQ_COUNT));
                                    model.setComments(dataObject.getString(Constants.ACTIVE_RIDE_COMMENTS));
                                    //model.setDriverRequest(dataObject.getString(Constants.ACTIVE_RIDE_DRIVER_REQUEST));
                                    model.setCarpoolId(dataObject.getString(Constants.ACTIVE_RIDE_CAR_POOL_ID));
                                    model.setRiderImage(dataObject.getString(Constants.ACTIVE_RIDE_RIDER_IMAGE));
                                    model.setPostedTime(dataObject.getString(Constants.ACTIVE_RIDE_POSTED_TIME));
                                    model.setEstimatedMI(dataObject.getString(Constants.ACTIVE_RIDE_ESTIMATE_MI));
                                    model.setRiderName(dataObject.getString(Constants.RIDER_NAME));
                                    Singleton.getInstance().mActiveRiderArray.add(model);
                                }
                            } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                                snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                                snackbar.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            activeRiderTask.execute();
        } else {
            Toast.makeText(application, "" + Constants.NO_CONNECTION, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Dialog dialog = null;

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void parseAdvancedBookings(String schoolName, String zipCode, String miles) {

        if (Utilities.isOnline(context)) {
            CommonAsynTask advancedRiderTask = new CommonAsynTask(this, Constants.ADVANCED_BOOKINGS_URL, getData(schoolName, zipCode, miles),
                    Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
                @Override
                public void onTaskCompleted(JSONObject jsonObject) {
                    if (jsonObject != null) {
                        Singleton.getInstance().mAdvancedRiderArray.clear();
                        try {
                            String message = jsonObject.getString(Constants.ACTIVE_RIDE_MESSAGE);
                            String result = jsonObject.getString(Constants.ACTIVE_RIDE_RESULT);
                            if (result.equals(Constants.SUCCESS)) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    ActiveRiderModel model = new ActiveRiderModel();
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    model.setRideId(dataObject.getString(Constants.ACTIVE_RIDE_RIDE_ID));
                                    model.setRiderId(dataObject.getString(Constants.ACTIVE_RIDE_RIDER_ID));
                                    model.setDriverId(dataObject.getString(Constants.ACTIVE_RIDE_DRIVER_ID));
                                    model.setRideTime(dataObject.getString(Constants.ACTIVE_RIDE_RIDE_TIME));
                                    model.setRideType(dataObject.getString(Constants.ACTIVE_RIDE_TYPE));
                                    model.setRideTypeId(dataObject.getString(Constants.ACTIVE_RIDE_TYPE_ID));
                                    model.setRideDate(dataObject.getString(Constants.ACTIVE_RIDE_DATE));
                                    model.setPickUpLocation(dataObject.getString(Constants.ACTIVE_RIDE_PICK_UP_LOCATION));
                                    model.setDestination(dataObject.getString(Constants.ACTIVE_RIDE_DESTINATION));
                                    model.setSeatNo(dataObject.getString(Constants.ACTIVE_RIDE_SEAT_NO));
                                    model.setTripType(dataObject.getString(Constants.ACTIVE_RIDE_TRIP_TYPE));
                                    model.setDonation(dataObject.getString(Constants.ACTIVE_RIDE_DONATION));
                                    model.setLadiesOnly(dataObject.getString(Constants.ACTIVE_RIDE_LADIES_ONLY));
                                    model.setDiscountId(dataObject.getString(Constants.ACTIVE_RIDE_DISCOUNT_ID));
                                    //model.setDriverReqCount(dataObject.getString(Constants.ACTIVE_RIDE_DRIVER_REQ_COUNT));
                                    model.setComments(dataObject.getString(Constants.ACTIVE_RIDE_COMMENTS));
                                    //model.setDriverRequest(dataObject.getString(Constants.ACTIVE_RIDE_DRIVER_REQUEST));
                                    model.setCarpoolId(dataObject.getString(Constants.ACTIVE_RIDE_CAR_POOL_ID));
                                    model.setRiderImage(dataObject.getString(Constants.ACTIVE_RIDE_RIDER_IMAGE));
                                    model.setPostedTime(dataObject.getString(Constants.ACTIVE_RIDE_POSTED_TIME));
                                    model.setEstimatedMI(dataObject.getString(Constants.ACTIVE_RIDE_ESTIMATE_MI));
                                    model.setRiderName(dataObject.getString(Constants.RIDER_NAME));
                                    Singleton.getInstance().mAdvancedRiderArray.add(model);
                                }
                                setResult(101);
                                finish();
                            } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                                Snackbar snackbar = Snackbar
                                        .make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            advancedRiderTask.execute();
        } else {
            Toast.makeText(application, Constants.NO_CONNECTION, Toast.LENGTH_SHORT).show();
        }

    }

    private String getData(String schoolName, String zipCode, String miles) {
        JSONObject activeRiderData = new JSONObject();
        SharedPref pref = new SharedPref(this);
        String userId = pref.getString("userId");
        String femaleMode = pref.getString("LadiesMode");
        if (!TextUtils.isEmpty(userId)) {
            try {
                activeRiderData.put("driverId", userId);
                activeRiderData.put("zipcode", zipCode);
                if (femaleMode != null) {
                    activeRiderData.put("female_mode", femaleMode);
                }
                if (schoolName != null) {
                    activeRiderData.put("schoolId", schoolName);
                }
                if (mMiles != null) {
                    activeRiderData.put("distance", mMiles);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return activeRiderData.toString();
    }
}