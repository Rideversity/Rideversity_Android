package cab.com.rideversitydriver.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cab.com.rideversitydriver.Adapters.CreateCarpoolSrcArrayAdapter;
import cab.com.rideversitydriver.Dialogs.CustomDialog;
import cab.com.rideversitydriver.Fragments.Carpool_Fragment;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Interfaces.DialogDismisser;
import cab.com.rideversitydriver.Models.InviteRiderModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.DatePicker;
import cab.com.rideversitydriver.Utils.GPSTracker;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by KEERTHINI on 8/4/2016.
 */

public class CarPoolCreate_Activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, TimePickerDialog.OnTimeSetListener {
    LinearLayout inviteRider;
    EditText edtDonation, edtSeatNo, edtComments;
    // TextView textViewInviteRider;
    public static TextView txtDate, txtTime, txtInviteRider;
    AutoCompleteTextView SourceAutocompleteTextView, DestinationAutocompleteTextView;
    Context context;
    String validDate, validTime;
    private static SharedPref sharedPref;
    private int currentTime = 0;
    String date;
    ScrollView scrollView;
    private CreateCarpoolSrcArrayAdapter sourcePlaceArrayAdapter, destinationPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    public static String SourceAddress = "", DestinationAddress = "";
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final String LOG_TAG = "CarPoolCreate";
    public static LatLng SourcePosition, DestinationPosition;
    GPSTracker gpsTracker;
    Snackbar snackbar;
    LinearLayout layoutMain;
    public static String strDate, strTime, strSeatNo, strDonation, stsComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carpool_create);
        context = this;
        sharedPref = new SharedPref(context);
        RelativeLayout backArrow = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        Button createcarPool = (Button) findViewById(R.id.button_create_carpool);
        inviteRider = (LinearLayout) findViewById(R.id.layout_invite_rider);
        edtSeatNo = (EditText) findViewById(R.id.edittext_seatnumber);
        edtDonation = (EditText) findViewById(R.id.edittext_donation);
        edtComments = (EditText) findViewById(R.id.edittext_comments);
        // inviteRider.setEnabled(false);
        inviteRider.setOnClickListener(this);
        txtDate = (TextView) findViewById(R.id.textview_date);
        txtTime = (TextView) findViewById(R.id.textview_time);
        txtInviteRider = (TextView) findViewById(R.id.textView_invite_rider);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        createcarPool.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        scrollView = (ScrollView) findViewById(R.id.scrollView_carpool);
        layoutMain = (LinearLayout) findViewById(R.id.layout_carpoolcreate);

        if (Utilities.isOnline(context)) {
            inviteRiderAPI();
        } else {
            snackbar = Snackbar.make(layoutMain, Constants.NO_CONNECTION, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            snackbar.show();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(CarPoolCreate_Activity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        SourceAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView_source);
        SourceAutocompleteTextView.setThreshold(3);

        SourceAutocompleteTextView.setInputType(InputType.TYPE_NULL);
        SourceAutocompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //scrollviewAdvanceRide.scrollTo(0, SourceAutocompleteTextView.getBaseline());
                scrollView.smoothScrollTo(0, txtTime.getBottom());
                SourceAutocompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                SourceAutocompleteTextView.onTouchEvent(event); // call native handler
                SourceAutocompleteTextView.setCursorVisible(true);
                SourceAutocompleteTextView.requestFocus();
                SourceAutocompleteTextView.setActivated(true);
                SourceAutocompleteTextView.setPressed(true);
                SourceAutocompleteTextView.setTextIsSelectable(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(SourceAutocompleteTextView, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        SourceAutocompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Log.e("source actionNext-->", "actionNext");
                    SourceAutocompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                    SourceAutocompleteTextView.setCursorVisible(true);
                    SourceAutocompleteTextView.requestFocus();
                    SourceAutocompleteTextView.setActivated(true);
                    SourceAutocompleteTextView.setPressed(true);
                    SourceAutocompleteTextView.setTextIsSelectable(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(SourceAutocompleteTextView, InputMethodManager.SHOW_IMPLICIT);
                    CarPoolCreate_Activity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
                }
                return false;
            }
        });
        SourceAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_GEOCODE)
                .build();
        sourcePlaceArrayAdapter = new CreateCarpoolSrcArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, filter);
        SourceAutocompleteTextView.setAdapter(sourcePlaceArrayAdapter);


        //DestinationAutocompleteTextView
        DestinationAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView_destination);
        DestinationAutocompleteTextView.setThreshold(3);

        DestinationAutocompleteTextView.setInputType(InputType.TYPE_NULL);
        DestinationAutocompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //scrollviewAdvanceRide.scrollTo(0, DestinationAutocompleteTextView.getBaseline());
                scrollView.smoothScrollTo(0, SourceAutocompleteTextView.getBottom());
                DestinationAutocompleteTextView.setCursorVisible(true);
                DestinationAutocompleteTextView.requestFocus();
                DestinationAutocompleteTextView.setActivated(true);
                DestinationAutocompleteTextView.setPressed(true);
                DestinationAutocompleteTextView.setTextIsSelectable(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(DestinationAutocompleteTextView, InputMethodManager.SHOW_IMPLICIT);
                CarPoolCreate_Activity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

                // scrollView.smoothScrollTo(0,txtTime.getBottom());
                DestinationAutocompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                DestinationAutocompleteTextView.onTouchEvent(event); // call native handler
                return true; // consume touch even
            }
        });

        DestinationAutocompleteTextView.setOnItemClickListener(destinationAutocompleteClickListener);
        AutocompleteFilter filters = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_GEOCODE)
                .build();
        destinationPlaceArrayAdapter = new CreateCarpoolSrcArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, filters);
        DestinationAutocompleteTextView.setAdapter(destinationPlaceArrayAdapter);
        SourceAutocompleteTextView.setHorizontallyScrolling(false);
        SourceAutocompleteTextView.setMaxLines(Integer.MAX_VALUE);
        DestinationAutocompleteTextView.setHorizontallyScrolling(false);
        DestinationAutocompleteTextView.setMaxLines(Integer.MAX_VALUE);
    }

    //Source Address Auto complete place API
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SourceAutocompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            final CreateCarpoolSrcArrayAdapter.PlaceAutocomplete item = sourcePlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.e("Selected: ", "" + item.description);
            SourceAddress = item.description.toString();
            Log.e("SourceAddress", "" + SourceAddress);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            // Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            SourcePosition = place.getLatLng();
            Constants.Current_Lattitude = SourcePosition.latitude;
            Constants.Current_landitude = SourcePosition.longitude;
            // Log.e("SourcePosition", "" + SourcePosition);
        }
    };

    //Destination Address Auto complete place API
    private AdapterView.OnItemClickListener destinationAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DestinationAutocompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            final CreateCarpoolSrcArrayAdapter.PlaceAutocomplete item = destinationPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            DestinationAddress = item.description.toString();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(destinationUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> destinationUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            DestinationPosition = place.getLatLng();
            Constants.Destination_Lattitue = DestinationPosition.latitude;
            Constants.Destination_Landitude = DestinationPosition.longitude;

            // getPriceSuggestion();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.button_create_carpool:

                strDate = txtDate.getText().toString().trim();
                strTime = txtTime.getText().toString().trim();
                SourceAddress = SourceAutocompleteTextView.getText().toString().trim();
                DestinationAddress = DestinationAutocompleteTextView.getText().toString().trim();
                strSeatNo = edtSeatNo.getText().toString().trim();
                strDonation = edtDonation.getText().toString().trim();
                stsComments = edtComments.getText().toString().trim();


                if (strDate.isEmpty() || strTime.isEmpty() || SourceAddress.isEmpty() || DestinationAddress.isEmpty() || strSeatNo.isEmpty() || strDonation.isEmpty()) {
                    Utilities.snackbarMessageTwo(context, layoutMain, "Please enter all values");
                } else if (currentTime == 1) {
                    Utilities.snackbarMessageTwo(context, layoutMain, "Please book for future rides only");
                } else {
                    if (Utilities.isOnline(context)) {

                        CreateCarpool();
                    } else {
                        Utilities.snackbarNoInternetTwo(context, layoutMain);
                    }
                }

                break;
            case R.id.layout_invite_rider:
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    Utilities.hideKeyboard(CarPoolCreate_Activity.this);
                }
                Intent intent = new Intent(CarPoolCreate_Activity.this, InviteRiderActivity.class);
                startActivity(intent);
                break;
            case R.id.textview_date:
                DatePicker dialog = new DatePicker(txtDate);
                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                dialog.show(fragmentTransaction, "DatePicker");
                break;

            case R.id.textview_time:
                Calendar timeNow = Calendar.getInstance();
                TimePickerDialog dpdTime = TimePickerDialog.newInstance(CarPoolCreate_Activity.this,
                        timeNow.get(Calendar.HOUR_OF_DAY),
                        timeNow.get(Calendar.MINUTE),
                        false);
                dpdTime.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpdTime.show(getFragmentManager(), "Datepickerdialog");
                break;

            default:
                break;
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        if (minute < 10) {
            minute = Integer.parseInt("0" + minute);
        }
        validTime = "" + hourOfDay + minute;
        if (hourOfDay > 12) {
            txtTime.setText(String.valueOf(hourOfDay - 12) + ":" + (String.valueOf(minute) + "PM"));
        } else if (hourOfDay == 12) {
            txtTime.setText("12" + ":" + (String.valueOf(minute) + "PM"));
        } else if (hourOfDay < 12) {
            if (hourOfDay != 0) {
                txtTime.setText(String.valueOf(hourOfDay) + ":" + (String.valueOf(minute) + "AM"));
            } else {
                txtTime.setText("12" + ":" + (String.valueOf(minute) + "AM"));
            }
        }

        Calendar datetime = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String todayDate = df.format(cal.getTime());

        if (datetime.getTimeInMillis() > c.getTimeInMillis()) {
            currentTime = 0;
        } else {
            if (todayDate.equals(txtDate.getText().toString().trim())) {
                currentTime = 1;
                Log.e("currentTime3", "currentTime3");
            } else {
                currentTime = 0;
                Log.e("currentTime4", "currentTime4");
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        sourcePlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        destinationPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        sourcePlaceArrayAdapter.setGoogleApiClient(null);
        destinationPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    private void inviteRiderAPI() {
        gpsTracker = new GPSTracker(context, CarPoolCreate_Activity.this);
        String currentLat = String.valueOf(gpsTracker.getLatitude());
        String currentLong = String.valueOf(gpsTracker.getLongitude());
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.LATITUDE, currentLat);
            objData.putOpt(Constants.LONGITUDE, currentLong);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CommonAsynTask driverAsync = new CommonAsynTask(CarPoolCreate_Activity.this, Constants.INVITE_RIDER_URL, objData.toString(), Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (jsonObject.has(Constants.RESULT)) {
                        Singleton.getInstance().inviteRiderArray.clear();
                        if (jsonObject.optString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            JSONArray dataArray = jsonObject.optJSONArray(Constants.DATA);
                            for (int i = 0; i < dataArray.length(); i++) {
                                InviteRiderModel inviteRiderModel = new InviteRiderModel();
                                inviteRiderModel.setRiderId(dataArray.optJSONObject(i).optString(Constants.ACTIVE_RIDE_RIDER_ID));

                                if (!dataArray.optJSONObject(i).optString(Constants.CURRENT_LATITUDE).isEmpty()) {
                                    inviteRiderModel.setCurrentLat(Double.valueOf(dataArray.optJSONObject(i).optString(Constants.CURRENT_LATITUDE)));
                                } else {
                                    inviteRiderModel.setCurrentLat(Double.valueOf("0"));
                                }

                                if (!dataArray.optJSONObject(i).optString(Constants.CURRENT_LONGITUDE).isEmpty()) {
                                    inviteRiderModel.setCurrentLong(Double.valueOf(dataArray.optJSONObject(i).optString(Constants.CURRENT_LONGITUDE)));
                                } else {
                                    inviteRiderModel.setCurrentLong(Double.valueOf("0"));
                                }

                                if (!dataArray.optJSONObject(i).optString(Constants.PREVIOUS_LATITUDE).isEmpty()) {
                                    inviteRiderModel.setPrevLat(Double.valueOf(dataArray.optJSONObject(i).optString(Constants.PREVIOUS_LATITUDE)));
                                } else {
                                    inviteRiderModel.setPrevLat(Double.valueOf("0"));
                                }

                                if (!dataArray.optJSONObject(i).optString(Constants.PREVIOUS_LONGITUDE).isEmpty()) {
                                    inviteRiderModel.setPrevLong(Double.valueOf(dataArray.optJSONObject(i).optString(Constants.PREVIOUS_LONGITUDE)));
                                } else {
                                    inviteRiderModel.setPrevLong(Double.valueOf("0"));
                                }

                                inviteRiderModel.setFirstName(dataArray.optJSONObject(i).optString(Constants.FIRST_NAME));
                                inviteRiderModel.setEmail(dataArray.optJSONObject(i).optString(Constants.EMAIL));
                                inviteRiderModel.setPhoneNo(dataArray.optJSONObject(i).optString(Constants.PHONENO));
                                inviteRiderModel.setGender(dataArray.optJSONObject(i).optString(Constants.GENDER));
                                inviteRiderModel.setProfileImage(dataArray.optJSONObject(i).optString(Constants.PROFILE_IMAGE));
                                inviteRiderModel.setRiderStatus(dataArray.optJSONObject(i).optString(Constants.RIDER_STATUS));
                                inviteRiderModel.setUserType(dataArray.optJSONObject(i).optString(Constants.USER_TYPE));
                                inviteRiderModel.setPickupTime(dataArray.optJSONObject(i).optString(Constants.PICKUP_TIME));
                                inviteRiderModel.setDistance(dataArray.optJSONObject(i).optString(Constants.DISTANCE));
                                inviteRiderModel.setAddress(dataArray.optJSONObject(i).optString(Constants.ADDRESS));
                                inviteRiderModel.setSelected(false);
                                Singleton.getInstance().inviteRiderArray.add(inviteRiderModel);
                            }
                        }
                    }
                }

            }
        });
        driverAsync.execute();
    }

    public void CreateCarpool() {
        CommonAsynTask rideconfirmAsyncTask = new CommonAsynTask(CarPoolCreate_Activity.this, Constants.CREATE_CARPOOL_URL, getDetails(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            JSONObject userObj = jsonObject.getJSONObject(Constants.DATA);
                            Carpool_Fragment.isCarpoolAdded = true;
                            CustomDialog customDialog = new CustomDialog();
                            customDialog.setDialogTitle("Carpool Created", new DialogDismisser() {
                                @Override
                                public void dismissDialog(int dismiss) {
                                    finish();
                                }
                            });
                            customDialog.setDialogDescription(jsonObject.getString(Constants.MESSAGE));
                            customDialog.show(getSupportFragmentManager(), "carpool");

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            Snackbar snackbar = Snackbar
                                    .make(layoutMain, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }
        });
        rideconfirmAsyncTask.execute();
    }

    private static String getDetails() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.RIDE_TIME, strTime);
            objData.putOpt(Constants.RIDE_DATE, strDate);
            objData.putOpt(Constants.PICKUP_LOCATION, SourceAddress);
            objData.putOpt(Constants.PICKUP_LAT, Constants.Current_Lattitude);
            objData.putOpt(Constants.PICKUP_LONG, Constants.Current_landitude);
            objData.putOpt(Constants.DESTINATION, DestinationAddress);
            objData.putOpt(Constants.DESTINATION_LAT, Constants.Destination_Lattitue);
            objData.putOpt(Constants.DESTINATION_LONG, Constants.Destination_Landitude);
            objData.putOpt(Constants.TRIP_TYPE, "One-way");
            objData.putOpt(Constants.SEAT_NO, strSeatNo);
            objData.putOpt(Constants.DONATION, strDonation);
            objData.putOpt(Constants.COMMENTS, stsComments);


            JSONArray inviteriderArray = new JSONArray();
            JSONObject invitedriverObj = new JSONObject();
            boolean isSelect = false;
            for (InviteRiderModel model : Singleton.getInstance().inviteRiderArray) {
                if (model.isSelected()) {
                    isSelect = true;
                    invitedriverObj = new JSONObject();
                    invitedriverObj.putOpt(Constants.RIDER_ID, model.getRiderId());
                    inviteriderArray.put(invitedriverObj);
                }
            }

            if (isSelect == false) {
                invitedriverObj = new JSONObject();
                invitedriverObj.putOpt(Constants.RIDER_ID, "");
                inviteriderArray.put(invitedriverObj);
            }

            objData.putOpt(Constants.INVITE_RIDER, inviteriderArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objData.toString();
    }
}
