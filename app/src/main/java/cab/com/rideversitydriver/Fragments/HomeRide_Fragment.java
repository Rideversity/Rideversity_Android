package cab.com.rideversitydriver.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.DTDHandler;

import java.util.ArrayList;
import java.util.List;

import cab.com.rideversitydriver.Activities.ActiveRideDetail_Activity;
import cab.com.rideversitydriver.Activities.AdvancedBooking_Activity;
import cab.com.rideversitydriver.Activities.HomeMenu;
import cab.com.rideversitydriver.Activities.RideDetails_Activity;
import cab.com.rideversitydriver.Activities.RideLists_Activity;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ActiveRiderModel;
import cab.com.rideversitydriver.Models.Data;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.CommonAsyncWithoutLoader;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.GPSTracker;
import cab.com.rideversitydriver.Utils.SendLatLongService;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

public class HomeRide_Fragment extends Fragment implements View.OnClickListener, LocationListener {

    private static final long LOCATION_REFRESH_TIME = 10000;
    private static final float LOCATION_REFRESH_DISTANCE = 0;
    private static SupportMapFragment mapFragment;
    private GoogleMap map;
    Button btnCash, btnCard, notify_arrival_complete_button, btnAcceptRide, requestPendingBtn, button_cancel_ride, cancelButton, notifyArrivalCompleteButton, btnPickupnow, btnPickuplater, btnNo, btnConfirm, btnCallDriver, btnArrivedConfirm;
    TextView txtClose, text_Rider_Name;
    Dialog select_ride_dialog, cancel_ride_dialog, payment_dialog, arrived_dialog;
    RelativeLayout layoutCalldriver, layoutTextdriver, layoutRealtimerides, layoutAdvachedbooking, layoutSource, layoutDestination, layoutRiderDetails, layoutRiderTextCall;
    // LinearLayout layoutRiderTextCall;
    LinearLayout notifyAndComopleteLayout, layoutDriverCallText;
    ImageView imgClose, imgTick, refreshImage, imageViewDriverImage;
    Context context;
    Double Current_Lattitude = 13.062519, Current_landitude = 80.265293;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Activity activity;
    private Data mData;
    private ActiveRiderModel mActiveRiderModel;
    private TextView mTotalDriversTextView;
    private TextView mAdvancedBookings;
    private TextView textRiderName;
    private static String mTotalDrivers;
    String phoneNo, arrivedStatus = "", approvalStatus = "";
    private SharedPref sharedPref;
    LocationListener locationListener;
    boolean fromRealAdvacne;
    boolean fromComplete = false;
    //myown
    private LocationManager mLocationManager;
    private static double currentLat = 0;
    private static double currentLon = 0;

    int MY_PERMISSIONS_REQUEST_LOCATION = 100;

    private final long FOUR_SECONDS = 4000;
    private boolean cancelAfterApprvd = false, isCanceled = false;
    public static TextView text_Location, textDestinationLocation;
    private static boolean isCancelAfterApproved = false;
    double LocationLat, LocationLong;
    double DesLocationLat, DesLocationLong;

    private Runnable runnable, runnableOnGoing;
    private Handler handler = new Handler();
    private Handler handlerOnGoing = new Handler();


    public HomeRide_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String message = null;
        Bundle bundle = getArguments();
        if (null != bundle) {
            message = bundle.getString(Constants.MESSAGE);
        }

        View rootView = inflater.inflate(R.layout.ride_home, container, false);
        activity = this.getActivity();
        context = this.getActivity();
        sharedPref = new SharedPref(context);
        InitView(rootView);
        SharedPref pref = new SharedPref(getActivity());
        phoneNo = pref.getString("phoneNo");

        //Setting pickup and drop location using sharedpref
        text_Location.setText(pref.getString("userLoc"));
        textDestinationLocation.setText(pref.getString("userDestination"));
        text_Rider_Name.setText("Rider : " + pref.getString("Rider_name"));
        try {
            String image = sharedPref.getString("Rider_Image");
            Picasso.with(context).load(image).
                    error(R.drawable.no_image).
                    placeholder(R.drawable.no_image).into(imageViewDriverImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        text_Location.setSelected(true);//adding marquee
        textDestinationLocation.setSelected(true);


        try {
            String PickupLat = sharedPref.getString("userPickupLat");
            String PickupLong = sharedPref.getString("userPickupLong");
            String DestinationLat = sharedPref.getString("userDestinationLat");
            String DestinationLong = sharedPref.getString("userDestinationLong");


            if (PickupLat != null) {
                LocationLat = Double.parseDouble(PickupLat);
            }

            if (PickupLong != null) {
                LocationLong = Double.parseDouble(PickupLong);
            }

            if (DestinationLat != null) {
                DesLocationLat = (Double.parseDouble(DestinationLat));
            }


            if (DestinationLong != null) {
                DesLocationLong = (Double.parseDouble(DestinationLong));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        View zoomControls = mapFragment.getView().findViewById(0x1);
        if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70,
                    getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(context, "Error - Map Fragment was null !!", Toast.LENGTH_SHORT).show();
        }
        if (sharedPref.getString("DriverMode").equals("1")) {
            if (activity.startService(new Intent(activity, SendLatLongService.class)) == null) {
                Intent serviceIntent = new Intent(activity, SendLatLongService.class);
                serviceIntent.putExtra("UserId", sharedPref.getString("userId"));
                activity.startService(serviceIntent);
            }
        } else {
            activity.stopService(new Intent(activity, SendLatLongService.class));
        }
        Constants.zipcode = "";
        Constants.schoolId = "";
        Constants.distance = "";
        Constants.female_mode = "";
        //Get Location
        CheckLocationPerm();
        setUserLocation();

        String strWhereFrom = null;
        try {
            strWhereFrom = sharedPref.getString(Constants.strCheckFromRealTimeOrAdvBook);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (strWhereFrom.equals(Constants.yesAdvBooking)) {
                callHandlerOnGoingRide();
            } else if (strWhereFrom.equals(Constants.yesRealTime)) {
                RequestPending();
            } else {
                mtdDefault();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (sharedPref.getString("ImageTick").equals("onComplete")) {
                sharedPref.setString("ImageTick", "");
                mtdDefault();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fromRealAdvacne) {
            HomeMenu.txtEdit.setVisibility(View.VISIBLE);
        }
        onGoingRide();
        return rootView;
    }

    private void mtdDefault() {
        layoutRealtimerides.setVisibility(View.VISIBLE);
        layoutAdvachedbooking.setVisibility(View.VISIBLE);
        refreshImage.setVisibility(View.VISIBLE);

        layoutSource.setVisibility(View.GONE);
        layoutDestination.setVisibility(View.GONE);
        layoutRiderTextCall.setVisibility(View.GONE);
        layoutRiderDetails.setVisibility(View.GONE);
        requestPendingBtn.setVisibility(View.GONE);
        button_cancel_ride.setVisibility(View.GONE);
        notifyAndComopleteLayout.setVisibility(View.GONE);
    }

    public void onStatusChecking() {
        try {
            if (sharedPref.getString("status").equals(Constants.PENDING)) {
                realTimeOnPending();

            } else if (sharedPref.getString("status").equals(Constants.APPROVED)) {
                realTimeOnApproved();

            } else if (sharedPref.getString("status").equals(Constants.REJECTED)) {
                mtdDefault();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void advanceBookingDetails() {

        if (ActiveRideDetail_Activity.advanceBookingValue) {
            ActiveRideDetail_Activity.advanceBookingValue = false;
            layoutSource.setVisibility(View.VISIBLE);
            layoutDestination.setVisibility(View.VISIBLE);
            notifyAndComopleteLayout.setVisibility(View.VISIBLE);
            layoutRiderTextCall.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);

            layoutRealtimerides.setVisibility(View.GONE);
            layoutAdvachedbooking.setVisibility(View.GONE);
            layoutRiderDetails.setVisibility(View.GONE);
            requestPendingBtn.setVisibility(View.GONE);
            button_cancel_ride.setVisibility(View.GONE);
            refreshImage.setVisibility(View.GONE);
            btnAcceptRide.setVisibility(View.GONE);
            text_Location.setText(Singleton.getInstance().onGoingArraySingleton.get(0).getPickupLocation());
            textDestinationLocation.setText(Singleton.getInstance().onGoingArraySingleton.get(0).getDestination());

        } else if (RideDetails_Activity.isFromRequestDriver) {
            RideDetails_Activity.isFromRequestDriver = false;

            layoutSource.setVisibility(View.VISIBLE);
            layoutDestination.setVisibility(View.VISIBLE);
            layoutRealtimerides.setVisibility(View.GONE);
            layoutAdvachedbooking.setVisibility(View.GONE);
            layoutRiderDetails.setVisibility(View.GONE);
            layoutRiderTextCall.setVisibility(View.GONE);
            requestPendingBtn.setVisibility(View.VISIBLE);
            button_cancel_ride.setVisibility(View.VISIBLE);
            notifyAndComopleteLayout.setVisibility(View.GONE);
            refreshImage.setVisibility(View.GONE);
            handler = new Handler();
            RequestPending();
            callHandlerOnGoingRide();
        } else {
            ActiveRideDetail_Activity.advanceBookingValue = false;

            layoutRealtimerides.setVisibility(View.VISIBLE);
            layoutAdvachedbooking.setVisibility(View.VISIBLE);
            layoutRiderDetails.setVisibility(View.GONE);
            layoutRiderTextCall.setVisibility(View.GONE);
            requestPendingBtn.setVisibility(View.GONE);
            button_cancel_ride.setVisibility(View.GONE);
            notifyAndComopleteLayout.setVisibility(View.GONE);
            refreshImage.setVisibility(View.VISIBLE);
        }
    }

    void realTimeOnPending() {
        layoutSource.setVisibility(View.VISIBLE);
        layoutDestination.setVisibility(View.VISIBLE);
        layoutRiderTextCall.setVisibility(View.GONE);
        layoutRiderDetails.setVisibility(View.GONE);
        layoutRealtimerides.setVisibility(View.GONE);
        layoutAdvachedbooking.setVisibility(View.GONE);
        requestPendingBtn.setVisibility(View.VISIBLE);
        button_cancel_ride.setVisibility(View.VISIBLE);
        notifyAndComopleteLayout.setVisibility(View.GONE);
        refreshImage.setVisibility(View.GONE);
    }

    void realTimeOnApproved() {
        layoutSource.setVisibility(View.VISIBLE);
        layoutDestination.setVisibility(View.VISIBLE);
        layoutRiderTextCall.setVisibility(View.VISIBLE);
        layoutRiderDetails.setVisibility(View.VISIBLE);
        layoutRealtimerides.setVisibility(View.GONE);
        layoutAdvachedbooking.setVisibility(View.GONE);
        requestPendingBtn.setVisibility(View.GONE);
        button_cancel_ride.setVisibility(View.GONE);
        notifyAndComopleteLayout.setVisibility(View.VISIBLE);
        refreshImage.setVisibility(View.GONE);
    }

    void realTimeOnComplete() {
        layoutSource.setVisibility(View.VISIBLE);
        layoutDestination.setVisibility(View.VISIBLE);
        layoutRiderTextCall.setVisibility(View.VISIBLE);
        layoutRiderDetails.setVisibility(View.VISIBLE);
        layoutRealtimerides.setVisibility(View.GONE);
        layoutAdvachedbooking.setVisibility(View.GONE);
        requestPendingBtn.setVisibility(View.GONE);
        button_cancel_ride.setVisibility(View.GONE);
        notify_arrival_complete_button.setText("COMPLETE RIDE");
        notify_arrival_complete_button.setBackground(ContextCompat.getDrawable(context, R.drawable.green_button));
        notifyAndComopleteLayout.setVisibility(View.VISIBLE);
        refreshImage.setVisibility(View.GONE);
    }

    private void CheckLocationPerm() {
        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            System.out.println("mmm if checkSelfPermission ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            GetLocation();
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int callPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        int readPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            //fragment.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            //ActivityCompat.requestPermissions(activity,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    parseActiveRiderList();
                    parseAdvancedBookings();

                    if (mapFragment != null) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap map) {
                                loadMap(map);
                            }
                        });
                    } else {
                        Toast.makeText(context, "Error - Map Fragment was null!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
                break;
        }
    }

    private void GetLocation() {
        LocationManager locationManager = (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, (android.location.LocationListener) this);
        } else {
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, (android.location.LocationListener) this);
            Toast.makeText(this.activity, "Enable Your Location", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseActiveRiderList() {

        CommonAsynTask activeRiderTask = new CommonAsynTask(activity, Constants.ACTIVE_RIDER_API, getData(Constants.zipcode, Constants.schoolId, Constants.distance),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {

            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    Singleton.getInstance().mActiveRiderArray.clear();

                    try {
                        String message = jsonObject.getString(Constants.ACTIVE_RIDE_MESSAGE);
                        String result = jsonObject.getString(Constants.ACTIVE_RIDE_RESULT);


                        if (result.equals("success")) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                ActiveRiderModel model = new ActiveRiderModel();
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                model.setDestinationLat(dataObject.getString(Constants.ACTIVE_RIDE_DESTINATION_LAT));
                                model.setDestinationLong(dataObject.getString(Constants.ACTIVE_RIDE_DESTINATION_LONG));
                                model.setPickupLat(dataObject.getString(Constants.ACTIVE_RIDE_PICKUP_LAT));
                                model.setPickupLong(dataObject.getString(Constants.ACTIVE_RIDE_DESTINATION_LONG));
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
//                                model.setDriverReqCount(String .valueOf(dataArray.length()));
                                //model.setDriverReqCount(dataObject.getString(Constants.ACTIVE_RIDE_DRIVER_REQ_COUNT));
                                model.setComments(dataObject.getString(Constants.ACTIVE_RIDE_COMMENTS));
                                //model.setDriverRequest(dataObject.getString(Constants.ACTIVE_RIDE_DRIVER_REQUEST));
                                model.setCarpoolId(dataObject.getString(Constants.ACTIVE_RIDE_CAR_POOL_ID));
                                model.setRiderImage(dataObject.getString(Constants.ACTIVE_RIDE_RIDER_IMAGE));
                                model.setPostedTime(dataObject.getString(Constants.ACTIVE_RIDE_POSTED_TIME));
                                model.setEstimatedMI(dataObject.getString(Constants.ACTIVE_RIDE_ESTIMATE_MI));
                                model.setRiderName(dataObject.getString(Constants.RIDER_NAME));
                                model.setPickupTime(dataObject.getString(Constants.PICKUP_TIME));
                                model.setRiderfirstName(dataObject.getString(Constants.RIDER_FIRST_NAME_HOME));
                                model.setRiderlastName(dataObject.getString(Constants.RIDER_LAST_NAME_HOME));
                                Singleton.getInstance().mActiveRiderArray.add(model);
                            }

                            if (Singleton.getInstance().mActiveRiderArray.size() > 0) {
                                mTotalDriversTextView.setText(String.valueOf(Singleton.getInstance().mActiveRiderArray.size()));
                            }
                        } else {
                            mTotalDriversTextView.setText(String.valueOf(Singleton.getInstance().mActiveRiderArray.size()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        activeRiderTask.execute();
    }

    private void parseAdvancedBookings() {
        CommonAsynTask advancedRiderTask = new CommonAsynTask(activity, Constants.ADVANCED_BOOKINGS_URL, getData(Constants.zipcode, Constants.schoolId, Constants.distance),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.e("jsonObject111", "" + jsonObject);
                if (jsonObject != null) {
                    Singleton.getInstance().mAdvancedRiderArray.clear();

                    try {
                        String message = jsonObject.getString(Constants.ACTIVE_RIDE_MESSAGE);
                        String result = jsonObject.getString(Constants.ACTIVE_RIDE_RESULT);
                        Log.e("jsonObject", "" + jsonObject);
                        if (result.equals(Constants.SUCCESS)) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                Log.e("jsonObject111", "" + dataArray.length());
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
                                model.setPickupTime(dataObject.getString(Constants.PICKUP_TIME));
                                model.setRiderfirstName(dataObject.getString(Constants.RIDER_FIRST_NAME_HOME));
                                model.setRiderlastName(dataObject.getString(Constants.RIDER_LAST_NAME_HOME));
                                Singleton.getInstance().mAdvancedRiderArray.add(model);

                            }
                            if (Singleton.getInstance().mAdvancedRiderArray.size() > 0) {
                                mAdvancedBookings.setText(String.valueOf(Singleton.getInstance().mAdvancedRiderArray.size()));
                            }
                        } else {
                            mAdvancedBookings.setText(String.valueOf(Singleton.getInstance().mAdvancedRiderArray.size()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        advancedRiderTask.execute();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkCallPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSmsPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //If the user has denied the permission previously your code will come to this block
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private void requestCallPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
    }

    private void requestSmsPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
    }

    private void onGoingRide() {

        CommonAsyncWithoutLoader onGoingRide = new CommonAsyncWithoutLoader(activity, Constants.ONGOING_RIDE_URL, ongoingData(), Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (Utilities.isOnline(context)) {
                    if (jsonObject != null) {
                        Log.e("jsonObject--->", "" + jsonObject);
                        try {
                            if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                                JSONArray dataArray = new JSONArray();
                                dataArray = jsonObject.getJSONArray(Constants.DATA);

                                ArrayList<Data> onGoingArray = new ArrayList<>();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    Data model = new Data();
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    model.setRideId(dataObject.getString(Constants.RIDE_ID));
                                    model.setRiderId(dataObject.getString(Constants.RIDER_ID));
                                    model.setRiderPhoneNo(dataObject.getString(Constants.RIDERPHONENO));
                                    model.setRiderName(dataObject.getString(Constants.RIDER_NAME));
                                    model.setDriverId(dataObject.getString(Constants.DRIVER_ID));
                                    model.setDriverLat(dataObject.getString(Constants.DRIVER_LAT));
                                    model.setDriverLat(dataObject.getString(Constants.DRIVER_LONG));
                                    model.setRideTime(dataObject.getString(Constants.RIDE_TIME));
                                    model.setRideType(dataObject.getString(Constants.RIDE_TYPE));
                                    model.setRideTypeId(dataObject.getString(Constants.RIDE_TYPE_ID));
                                    model.setRideDate(dataObject.getString(Constants.RIDE_DATE));
                                    model.setPickupLocation(dataObject.getString(Constants.PICKUP_LOCATION));
                                    model.setDestination(dataObject.getString(Constants.DESTINATION));
                                    model.setSeatNo(dataObject.getString(Constants.SEAT_NO));
                                    model.setTripType(dataObject.getString(Constants.TRIP_TYPE));
                                    model.setDonation(dataObject.getString(Constants.DONATION));
                                    model.setLadiesOnly(dataObject.getString(Constants.LADIES_ONLY));
                                    model.setDiscountId(dataObject.getString(Constants.DISCOUNT_ID));
                                    model.setDriverName(dataObject.getString(Constants.DRIVER_NAME));
                                    model.setDriverImage(dataObject.getString(Constants.DRIVER_IMAGE));
                                    model.setDriverRating(dataObject.getString(Constants.DRIVER_RATING));
                                    model.setCarName(dataObject.getString(Constants.CAR_NAME));
                                    model.setComments(dataObject.getString(Constants.COMMENTS));
                                    model.setPickupLat(dataObject.getString(Constants.PICK_UP_LAT));
                                    model.setPickupLong(dataObject.getString(Constants.PICKUP_LONG));
                                    model.setCarpoolId(dataObject.getString(Constants.CARPOOL_ID));
                                    model.setRiderImage(dataObject.getString(Constants.RIDER_IMAGE));
                                    model.setPostedTime(dataObject.getString(Constants.POSTED_TIME));
                                    model.setEstimatedMI(dataObject.getString(Constants.ESTIMTATED_MI));
                                    model.setIsRideStart(dataObject.getString(Constants.IS_RIDE_START));
                                    model.setIsRiderNotify(dataObject.getString(Constants.IS_RIDER_NOTIFY));
//                                    onGoingArray.add(model);
                                    Singleton.getInstance().onGoingArraySingleton.add(model);

                                    if (dataObject.getString(Constants.IS_RIDE_START).equalsIgnoreCase("yes")) {
                                        if (dataObject.getString(Constants.IS_RIDER_NOTIFY).equalsIgnoreCase("yes")) {
                                            realTimeOnComplete();
                                        } else if (dataObject.getString(Constants.IS_RIDER_NOTIFY).equalsIgnoreCase("No")) {
                                            realTimeOnApproved();
                                        } else {
                                            mtdDefault();
                                        }
                                    } else {
                                        mtdDefault();
                                    }
                                }
                            } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                                mtdDefault();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        onGoingRide.execute();
    }

    public String ongoingData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_request_ride:
                callRequestRideMethod();
                break;

            case R.id.layout_real_time_rides:
                if (Singleton.getInstance().mActiveRiderArray.size() > 0) {
                    Intent rideIntent = new Intent(context, RideLists_Activity.class);
                    startActivity(rideIntent);
                } else {
                    Toast.makeText(activity, "No Real time rides found", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.layout_advanced_booking:
                if (Singleton.getInstance().mAdvancedRiderArray.size() > 0) {
                    Intent ridebookingIntent = new Intent(context, AdvancedBooking_Activity.class);
                    startActivity(ridebookingIntent);
                } else {
                    Toast.makeText(activity, "No Advanced Booking Rides found", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.imageView_check:
                if (cancel_ride_dialog.isShowing()) {
                    cancel_ride_dialog.dismiss();
                }
                break;

            case R.id.layout_call_driver:
                if (phoneNo != null && !phoneNo.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                    startActivity(intent);
                }
                break;

            case R.id.layout_text_driver:
                if (phoneNo != null && !phoneNo.equals("")) {
                    Intent intentSms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNo));
                    intentSms.putExtra("sms_body", "Rideversity");
                    startActivity(intentSms);
                }
                break;


            case R.id.layout_source:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + LocationLat + "," + LocationLong));
                startActivity(intent);
                break;


            case R.id.layout_destination:
                Intent desintent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + DesLocationLat + "," + DesLocationLong));
                startActivity(desintent);
                break;


            case R.id.button_cancel_ride:
                cancelBeforeRideApproved();
                break;

            case R.id.cancel_Button:
                cancelAfterRideApproved();
                break;


            case R.id.notify_arrival_complete_button:
                if (notify_arrival_complete_button.getText().equals("NOTIFY ARRIVAL")) {
                    notifyArrival();
                } else if (notify_arrival_complete_button.getText().equals("COMPLETE RIDE")) {
                    completeride("0");
                }
                break;


            case R.id.refresh_btn:
                parseActiveRiderList();
                parseAdvancedBookings();
                break;

            default:
                break;
        }
    }

    private void onPendingRequest() {
        layoutRealtimerides.setVisibility(View.GONE);
        layoutAdvachedbooking.setVisibility(View.GONE);
        layoutRiderDetails.setVisibility(View.VISIBLE);
        layoutRiderTextCall.setVisibility(View.GONE);
        requestPendingBtn.setVisibility(View.VISIBLE);
        button_cancel_ride.setVisibility(View.VISIBLE);
        notifyAndComopleteLayout.setVisibility(View.GONE);
        refreshImage.setVisibility(View.GONE);
    }

    private void onNotifyArrival() {
        layoutRealtimerides.setVisibility(View.GONE);
        layoutAdvachedbooking.setVisibility(View.GONE);
        layoutRiderDetails.setVisibility(View.VISIBLE);
        layoutRiderTextCall.setVisibility(View.VISIBLE);
        requestPendingBtn.setVisibility(View.GONE);
        button_cancel_ride.setVisibility(View.GONE);
        notify_arrival_complete_button.setText("COMPLETE RIDE");
        notify_arrival_complete_button.setBackground(ContextCompat.getDrawable(context, R.drawable.green_button));
        notifyAndComopleteLayout.setVisibility(View.VISIBLE);
        refreshImage.setVisibility(View.GONE);
    }

    private void onCompleteRide() {
        layoutRealtimerides.setVisibility(View.GONE);
        layoutAdvachedbooking.setVisibility(View.GONE);
        layoutRiderDetails.setVisibility(View.VISIBLE);
        layoutRiderTextCall.setVisibility(View.VISIBLE);
        notifyAndComopleteLayout.setVisibility(View.VISIBLE);
        notify_arrival_complete_button.setText("COMPLETE RIDE");
        notify_arrival_complete_button.setBackground(ContextCompat.getDrawable(context, R.drawable.orange_button_empty));
        refreshImage.setVisibility(View.GONE);
    }

    private void cancelBeforeRideApproved() {
        //make cancel boolean true in success method
        JSONObject objData = new JSONObject();
        try {
            String rideId = sharedPref.getString("rideIdForHome");
            objData.putOpt(Constants.RIDE_ID, String.valueOf(rideId));
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString(Constants.USER_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("RideRequestCancel", objData.toString());
        CommonAsynTask rideRequestCancel = new CommonAsynTask(activity, Constants.RIDE_REQUEST_CANCEL, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (Utilities.isOnline(context)) {
                    if (jsonObject != null) {
                        try {
                            if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                                Log.e("Driver Request Cancel", jsonObject.toString());
                                handler.removeCallbacks(runnable);
                                isCanceled = true;
                                mtdDefault();
                                Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                            } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                                Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "Check Your Internet Connection and Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rideRequestCancel.execute();
        return;
    }

    private void cancelAfterRideApproved() {
        //clear the value of cancel boolean in success method
        JSONObject objData = new JSONObject();
        try {

            String rideId = sharedPref.getString("rideIdForHome");
            objData.putOpt(Constants.RIDE_ID, String.valueOf(rideId));
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString(Constants.USER_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("RideRequestCancel", objData.toString());
        CommonAsynTask driverCancelRide = new CommonAsynTask(activity, Constants.DRIVER_CANCEL_RIDE, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (Utilities.isOnline(context)) {
                    if (jsonObject != null) {
                        try {
                            if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                                Log.e("Driver Cancel Ride", jsonObject.toString());
                                Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                isCancelAfterApproved = true;
                                mtdDefault();
                                try {
                                    handler.removeCallbacks(runnable);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                            } else {
                                if (jsonObject.getString(Constants.MESSAGE).equalsIgnoreCase("Ride already cancelled")) {
                                    sharedPref.setString("status", Constants.REJECTED);
                                    onStatusChecking();
                                }
                                Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "Check Your Internet Connection and Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        driverCancelRide.execute();
    }


    public void PaymentModeDialog() {
        payment_dialog = new Dialog(getActivity());
        payment_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        payment_dialog.setContentView(R.layout.payment_mode);
        WindowManager.LayoutParams WMLP = payment_dialog.getWindow().getAttributes();
        WMLP.gravity = Gravity.TOP;
        WMLP.x = 0;
        WMLP.y = 300;
        payment_dialog.getWindow().setAttributes(WMLP);
        payment_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        btnCash = (Button) payment_dialog.findViewById(R.id.button_cash);
        btnCard = (Button) payment_dialog.findViewById(R.id.button_card);

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeride("0");
                fromComplete = false;
                payment_dialog.dismiss();
            }
        });
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeride("1");
                payment_dialog.dismiss();
                onGoingRide();
            }
        });
        payment_dialog.show();
    }

    private void callRequestRideMethod() {
        btnAcceptRide.setVisibility(View.VISIBLE);
        layoutRealtimerides.setVisibility(View.GONE);
        layoutAdvachedbooking.setVisibility(View.GONE);
        layoutSource.setVisibility(View.VISIBLE);
        layoutDestination.setVisibility(View.VISIBLE);
        layoutRiderDetails.setVisibility(View.VISIBLE);
        layoutRiderTextCall.setVisibility(View.VISIBLE);
        HomeMenu.imgRidefilter.setVisibility(View.GONE);

        Constants.RIDE_ACCPET = true;
    }

    public void completeride(final String cash) {
        JSONObject objData = new JSONObject();
        try {
            String rideId = sharedPref.getString("rideIdForHome");
            String RideId = Singleton.getInstance().onGoingArraySingleton.get(0).getRideId();
            // String rideID = sharedPref.getString("rideIdForOnStatusCheck");
//            objData.putOpt(Constants.RIDE_ID, String.valueOf(rideId));
            objData.putOpt(Constants.RIDE_ID, rideId);
            objData.putOpt(Constants.API_KEY, Constants.API_KIY);
            objData.putOpt(Constants.API_SECRET_KEY, Constants.API_SECRET_KIY);
            objData.putOpt(Constants.IS_CASH, cash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("Success", "" + objData.toString());

        CommonAsynTask loginAsyncTask = new CommonAsynTask(activity, Constants.COMPLETE_RIDE_URL, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (Utilities.isOnline(context)) {
                    if (jsonObject != null) {
                        Log.i("Test", "Coming1");
                        Log.e("CompleteRide", jsonObject.toString());
                        try {
                            if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                                try {
                                    handlerOnGoing.removeCallbacks(runnableOnGoing);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                sharedPref.setString(Constants.strCheckFromRealTimeOrAdvBook, "");
                                CompleteRideDialog();
                            } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {

                                if (!fromComplete) {
                                    Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                    fromComplete = true;
                                } else {
                                    PaymentModeDialog();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(activity, "Backend server problem", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Check the Internet Connection and Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginAsyncTask.execute();
    }

    public void onStatusCheck() {

        JSONObject objdata = new JSONObject();
        try {
            String rideID = sharedPref.getString("rideIdForHome");
            objdata.putOpt(Constants.RIDE_ID, rideID);
            objdata.putOpt(Constants.DRIVER_ID, sharedPref.getString(Constants.USER_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonAsyncWithoutLoader statusCheck = new CommonAsyncWithoutLoader(activity, Constants.REAL_TIME_RIDE_STATUS, objdata.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (Utilities.isOnline(context)) {
                    if (jsonObject != null) {
                        Log.e("JsonObject ---->", "" + jsonObject);
                        try {
                            if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {

                                JSONObject status = jsonObject.getJSONObject("data");
                                if (status.getString(Constants.RIDE_STATUS).equals(Constants.PENDING)) {
                                    sharedPref.setString("status", Constants.PENDING);

                                } else if (status.getString(Constants.RIDE_STATUS).equals(Constants.APPROVED)) {
                                    sharedPref.setString("status", Constants.APPROVED);
                                    try {
                                        handler.removeCallbacks(runnable);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    callHandlerOnGoingRide();
                                } else if (status.getString(Constants.RIDE_STATUS).equals(Constants.REJECTED)) {
                                    sharedPref.setString("status", Constants.REJECTED);
                                    sharedPref.setString(Constants.strCheckFromRealTimeOrAdvBook, "");
                                    try {
                                        handler.removeCallbacks(runnable);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                   /* Intent intentHome = new Intent(activity, HomeMenu.class);
                                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(intentHome);*/
                                }
                                onStatusChecking();
                            } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        statusCheck.execute();
    }

    public void notifyArrival() {

        JSONObject objData = new JSONObject();
        try {
            String rideId = sharedPref.getString("rideIdForHome");
            String riderId = sharedPref.getString("riderIdForHome");

            objData.putOpt(Constants.RIDE_ID, String.valueOf(rideId));
            objData.putOpt(Constants.RIDER_ID, String.valueOf(riderId));

        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonAsynTask notifyArrival = new CommonAsynTask(activity, Constants.DRIVER_ARRIVED, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (Utilities.isOnline(context)) {
                    if (jsonObject != null) {
                        try {
                            if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {

                                onNotifyArrival();
                                Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                            } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                                Toast.makeText(context, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "Check the Internet Connection and Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        notifyArrival.execute();
    }

    public void CompleteRideDialog() {
        cancel_ride_dialog = new Dialog(getActivity());
        cancel_ride_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancel_ride_dialog.setContentView(R.layout.complete_ride_dialog);
        cancel_ride_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        cancel_ride_dialog.setCanceledOnTouchOutside(false);
        imgTick = (ImageView) cancel_ride_dialog.findViewById(R.id.imageView_check);
        imgTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPref.setString("ImageTick", "onComplete");

                Intent toHomeMenu = new Intent(context, HomeMenu.class);
                startActivity(toHomeMenu);
                cancel_ride_dialog.dismiss();
            }
        });
        btnAcceptRide.setVisibility(View.GONE);
        /*btnNo=(Button) cancel_ride_dialog.findViewById(R.id.button_no);
        btnConfirm=(Button) cancel_ride_dialog.findViewById(R.id.button_confirm);
        btnNo.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);*/
        cancel_ride_dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        parseActiveRiderList();
        parseAdvancedBookings();

    }


    void loadMap(GoogleMap googleMap) {
        Log.e("loadMap", "loadMap");
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        if (map != null) {
            onLocationChanged();
        }
    }

    public void onLocationChanged() {
        double latitude = Current_Lattitude;
        double longitude = Current_landitude;
        LatLng latLng = new LatLng(latitude, longitude);
        Log.e("latitude", "" + latitude);
        Log.e("longitude", "" + longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.orange_map_pin);   // where university is the icon name that is used as a marker.
        int height = 200;
        int width = 150;
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, width, height, false);
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).position(new LatLng(latitude, longitude)).title("Colan Infotech Pvt Ltd"));
    }

    private String getData(String zipcode, String schoolId, String distance) {
        JSONObject activeRiderData = new JSONObject();
        SharedPref pref = new SharedPref(getActivity());
        String userId = pref.getString("userId");
        String femalemode = pref.getString("LadiesOnly");
        Log.i("Test", userId);
        if (!TextUtils.isEmpty(userId)) {
            try {
                activeRiderData.put("driverId", userId);
                activeRiderData.put("zipcode", zipcode);
                activeRiderData.put("schoolId", schoolId);
                activeRiderData.put("distance", distance);
                activeRiderData.put("female_mode", femalemode);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return activeRiderData.toString();
    }

    private void setUserLocation() {

        CommonAsynTask userLocationTask = new CommonAsynTask(activity, Constants.SET_USER_LOCATION, getUserData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        String message = jsonObject.getString(Constants.MESSAGE);
                        String result = jsonObject.getString(Constants.RESULT);
                        if (result.equals(Constants.SUCCESS) && message.equals(Constants.MESSAGE)) {
                            JSONObject jobject = new JSONObject();
                            jobject.put("latitude", currentLat);
                            jobject.put("longitude", currentLon);
                            jobject.put("userId", Constants.USER_ID);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        userLocationTask.execute();
    }

    private String getUserData() {
        JSONObject objData = new JSONObject();
        GPSTracker gpstracker = new GPSTracker(context, activity);
        Constants.Current_Lattitude = gpstracker.getLatitude();
        Constants.Current_landitude = gpstracker.getLongitude();
        try {
            objData.putOpt(Constants.LATITUDE, Constants.Current_Lattitude);
            objData.putOpt(Constants.LONGITUDE, Constants.Current_landitude);
            objData.putOpt(Constants.USER_ID, sharedPref.getString(Constants.USER_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objData.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Singleton.getInstance().mActiveRiderArray != null) {
            mTotalDriversTextView.setText(String.valueOf(Singleton.getInstance().mActiveRiderArray.size()));
        }

        if (Singleton.getInstance().mAdvancedRiderArray != null) {
            mAdvancedBookings.setText(String.valueOf(Singleton.getInstance().mAdvancedRiderArray.size()));
        }
    }

    public void InitView(View rootView) {
        mTotalDriversTextView = (TextView) rootView.findViewById(R.id.total_number_of_drivers);
        mAdvancedBookings = (TextView) rootView.findViewById(R.id.textView_drivers_avaliable_numbers);
        layoutRealtimerides = (RelativeLayout) rootView.findViewById(R.id.layout_real_time_rides);
        layoutAdvachedbooking = (RelativeLayout) rootView.findViewById(R.id.layout_advanced_booking);
        layoutSource = (RelativeLayout) rootView.findViewById(R.id.layout_source);
        text_Rider_Name = (TextView) rootView.findViewById(R.id.textRiderName);

        text_Location = (TextView) rootView.findViewById(R.id.text_Location);
        textDestinationLocation = (TextView) rootView.findViewById(R.id.textView_justsec);
        layoutDestination = (RelativeLayout) rootView.findViewById(R.id.layout_destination);
        layoutRiderDetails = (RelativeLayout) rootView.findViewById(R.id.layout_rider_detail);
        layoutRiderTextCall = (RelativeLayout) rootView.findViewById(R.id.layout_rider_call_text);
        requestPendingBtn = (Button) rootView.findViewById(R.id.requestPendingBtn);
        requestPendingBtn.setOnClickListener(this);
        layoutDestination.setOnClickListener(this);
        layoutSource.setOnClickListener(this);
        /*textRiderName = (TextView) rootView.findViewById(R.id.textRiderName);
        textRiderName.setOnClickListener(this);*/
        button_cancel_ride = (Button) rootView.findViewById(R.id.button_cancel_ride);
        button_cancel_ride.setOnClickListener(this);

        notify_arrival_complete_button = (Button) rootView.findViewById(R.id.notify_arrival_complete_button);
        notify_arrival_complete_button.setOnClickListener(this);

        cancelButton = (Button) rootView.findViewById(R.id.cancel_Button);
        cancelButton.setOnClickListener(this);

        refreshImage = (ImageView) rootView.findViewById(R.id.refresh_btn);
        refreshImage.setOnClickListener(this);
        imageViewDriverImage = (ImageView) rootView.findViewById(R.id.imageView_driver_image);


        HomeMenu.imgRidefilter.setVisibility(View.VISIBLE);
        layoutRealtimerides.setOnClickListener(this);
        layoutAdvachedbooking.setOnClickListener(this);
        btnAcceptRide = (Button) rootView.findViewById(R.id.button_request_ride);
        btnAcceptRide.setOnClickListener(this);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        layoutRealtimerides.setVisibility(View.VISIBLE);
        layoutAdvachedbooking.setVisibility(View.VISIBLE);
        layoutSource.setVisibility(View.GONE);
        layoutDestination.setVisibility(View.GONE);
        layoutRiderDetails.setVisibility(View.GONE);
        layoutRiderTextCall.setVisibility(View.GONE);
        btnAcceptRide.setText(getResources().getString(R.string.accept_ride));
        btnAcceptRide.setTag("accept_ride");

        layoutCalldriver = (RelativeLayout) rootView.findViewById(R.id.layout_call_driver);
        layoutTextdriver = (RelativeLayout) rootView.findViewById(R.id.layout_text_driver);
        notifyAndComopleteLayout = (LinearLayout) rootView.findViewById(R.id.notifyLinearLayout);
        layoutDriverCallText = (LinearLayout) rootView.findViewById(R.id.layout_driver_call_text);
        layoutTextdriver.setOnClickListener(this);
        layoutCalldriver.setOnClickListener(this);

        requestPendingBtn.setVisibility(View.GONE);
        button_cancel_ride.setVisibility(View.GONE);
        notifyAndComopleteLayout.setVisibility(View.GONE);
        refreshImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null)
            System.out.println("mmm My Latitude=" + location.getLatitude() + " Longitude=" + location.getLongitude());
//        setUserLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    private void callHandlerOnGoingRide() {

        runnableOnGoing = new Runnable() {
            public void run() {
                onGoingRide();
                handlerOnGoing.postDelayed(runnableOnGoing, 4000);
            }
        };
        handlerOnGoing.postDelayed(runnableOnGoing, 4000);
    }

    private void RequestPending() {
        if (!isCanceled)
            runnable = new Runnable() {
                public void run() {
                    onStatusCheck();
                    cancelAfterApprvd = sharedPref.getBoolean(Constants.CANCEL_BOOLEAN);
                    handler.postDelayed(runnable, 4000);
                }
            };
        handler.postDelayed(runnable, 4000);
    }
}