package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Fragments.ActiveRide_Fragment;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ActiveRiderModel;
import cab.com.rideversitydriver.Models.Data;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;


/**
 * Created by KEERTHINI on 8/1/2016.
 */

public class ActiveRideDetail_Activity extends AppCompatActivity implements View.OnClickListener {

    private ActiveRiderModel mActiveRiderModel;
    private Data mData;
    private Context mContext;
    private RelativeLayout driverProfile, backArrow, layout_active_ride;
    private TextView rideTitle, userId, riderName, mPickupdate, mPickupTime, mpickupLocation, mDestination, msuggestedDonation, mnumberOfSeats, mcomments;
    private RoundedImageView imageViewDriverPic;
    private ImageView adbannerImageView;
    private Button requestPendingOrCancel, btnStartRide;
    private static SharedPref sharedPref;
    private static int Position = 0;
    public static boolean advanceBookingValue = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_booking);

        mContext = this;
        sharedPref = new SharedPref(mContext);
        driverProfile = (RelativeLayout) findViewById(R.id.layout_active_ride);
        userId = (TextView) findViewById(R.id.userId);
        riderName = (TextView) findViewById(R.id.riderName);
        mPickupdate = (TextView) findViewById(R.id.pickupdate);
        requestPendingOrCancel = (Button) findViewById(R.id.requestPending_or_cancel);
        btnStartRide = (Button) findViewById(R.id.btn_start_ride);
        mpickupLocation = (TextView) findViewById(R.id.pickupLocation);
        mPickupTime = (TextView) findViewById(R.id.pickupTime);
        mDestination = (TextView) findViewById(R.id.pickupDestination);
        msuggestedDonation = (TextView) findViewById(R.id.suggestedDonation);
        imageViewDriverPic = (RoundedImageView) findViewById(R.id.imageView_driver_pic_pending);
        adbannerImageView = (ImageView) findViewById(R.id.ad_banner_image_view_pending);
        mnumberOfSeats = (TextView) findViewById(R.id.numberOfSeats);
        mcomments = (TextView) findViewById(R.id.comments);
        rideTitle = (TextView) findViewById(R.id.ride_title);
        backArrow = (RelativeLayout) findViewById(R.id.imageView_backarrow);


        driverProfile.setVisibility(View.VISIBLE);
        userId.setVisibility(View.VISIBLE);
        backArrow.setOnClickListener(this);
        requestPendingOrCancel.setOnClickListener(this);
        btnStartRide.setOnClickListener(this);


        Intent mIntent = getIntent();
        if (mIntent != null) {
            Position = mIntent.getIntExtra("POSITION", 0);
        }

        if (Singleton.getInstance().activeRideArray.size() > 0) {
            if (Singleton.getInstance().activeRideArray.get(0).getActiverideLists().size() > 0) {

                userId.setText("User ID: " + Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRiderName());

                riderName.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getriderfName() + " " +
                        Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getriderlName());

                mPickupdate.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRideDate());
                mPickupTime.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRideTime());
                mpickupLocation.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getPickupLocation());
                mDestination.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getDestination());

                //to store PickUp&Destination For if driver clicks on start ride have to use in Main fragment

                sharedPref.setString("PickUpForHome", Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getPickupLocation());
                sharedPref.setString("PickUpLatLongForHome", Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getPickUpLat() + "~" +
                        Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getPickUpLong());

                sharedPref.setString("DestinationForHome", Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getDestination());
                sharedPref.setString("DestinationLatLongForHome", Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getDestinationLat() + "~" +
                        Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getDestinationLong());


                msuggestedDonation.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getDonation());
                mnumberOfSeats.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getSeatNo());


                if ((Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getComments().equals(""))) {
                    mcomments.setText("-");

                } else {
                    mcomments.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getComments());
                }

                if (Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRideTypeId().equals("1")) {
                    rideTitle.setText(R.string.realtimeridetext);
                } else if (Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRideTypeId().equals("2")) {
                    rideTitle.setText(R.string.adavanceBookingText);
                }

                mcomments.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getComments());

                if (Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRiderImage().length() == 0 || Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRiderImage().equals("")) {
//                    adbannerImageView.setImageResource(R.drawable.pic_upload_icon);
                    imageViewDriverPic.setImageResource(R.drawable.pic_upload_icon);
                } else {
                    setImage(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRiderImage());
                }

                if (Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getDriverId().equals("0")) {
                    requestPendingOrCancel.setVisibility(View.VISIBLE);
                    requestPendingOrCancel.setText("REQUEST PENDING");
                    requestPendingOrCancel.setAlpha((float) 0.5);
                    requestPendingOrCancel.setEnabled(false);
                    btnStartRide.setVisibility(View.GONE);
                } else {
                    btnStartRide.setVisibility(View.VISIBLE);
                    requestPendingOrCancel.setEnabled(true);
                    btnStartRide.setEnabled(true);
                    requestPendingOrCancel.setClickable(true);
                    btnStartRide.setClickable(true);
                    requestPendingOrCancel.setText("CANCEL RIDE");
                    btnStartRide.setText("START RIDE");
                    btnStartRide.setBackgroundResource((R.drawable.green_button));
                }
            }
        }
    }

    private void setImage(String URL) {
        Picasso.with(mContext)
                .load(URL)
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_image)
                .into(adbannerImageView);
        Picasso.with(mContext)
                .load(URL)
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_image)
                .into(imageViewDriverPic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.requestPending_or_cancel:
                cancelAfterRideApproved();
                break;

            case R.id.btn_start_ride:

                startRideAPI();
                break;
            default:
                break;
        }
    }


    public void startRideAPI() {
        CommonAsynTask advanceRideStart = new CommonAsynTask(mContext, Constants.ADVANCE_BOOKING_RIDE_START, dataStartRide(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        String result = jsonObject.getString(Constants.RESULT);

                        if (result.equals("success")) {
                            Log.e("Advance booking data", "" + jsonObject);
                            advanceBookingValue = true;
                            sharedPref.setString("onOpeningScreen", "toNotify");
                            Intent advanceIntent = new Intent(ActiveRideDetail_Activity.this, HomeMenu.class);
                            startActivity(advanceIntent);
                            ActiveRide_Fragment.activity.finish();
                            finish();

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            Toast.makeText(mContext, String.valueOf(jsonObject.getString(Constants.MESSAGE).equals(Constants.ERROR)), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        advanceRideStart.execute();
    }

    public static String dataStartRide() {
        JSONObject objdata = new JSONObject();
        try {

            String strRideId = Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRideId();
            String strRiderId = Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRiderId();
            String strDriverID = sharedPref.getString(Constants.USER_ID);
            objdata.putOpt(Constants.DRIVER_ID, strDriverID);
            objdata.putOpt(Constants.RIDE_ID, String.valueOf(strRideId));


            sharedPref.setString("rideIdForHome", strRideId);
            sharedPref.setString("riderIdForHome", strRiderId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(objdata);
    }


    private void cancelAfterRideApproved() {
        //clear the value of cancel boolean in success method
        JSONObject objData = new JSONObject();
        try {
            String strRideId = Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRideId();
            String strRiderId = Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(Position).getRiderId();
            objData.putOpt(Constants.RIDE_ID, String.valueOf(strRideId));
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString(Constants.USER_ID));

            sharedPref.setString("rideIdForHome", strRideId);
            sharedPref.setString("riderIdForHome", strRiderId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("RideRequestCancel", objData.toString());
        CommonAsynTask driverCancelRide = new CommonAsynTask(mContext, Constants.DRIVER_CANCEL_RIDE, objData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (Utilities.isOnline(mContext)) {
                    if (jsonObject != null) {
                        try {
                            if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {

                                Toast.makeText(mContext, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent toHomeMenu = new Intent(ActiveRideDetail_Activity.this, HomeMenu.class);
                                startActivity(toHomeMenu);
                                ActiveRide_Fragment.activity.finish();
                                finish();

                            } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                                Toast.makeText(mContext, jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Check Your Internet Connection and Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        driverCancelRide.execute();
    }


}