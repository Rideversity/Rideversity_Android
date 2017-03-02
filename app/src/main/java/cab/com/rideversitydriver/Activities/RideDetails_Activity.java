package cab.com.rideversitydriver.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Fragments.BankAccount;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ActiveRiderModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;

/**
 * Created by Kalidoss on 03/08/16.
 */
public class RideDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnRequestDriver;
    RelativeLayout imgBack;
    Context context;
    Snackbar snackbar;
    private ActiveRiderModel mActiveRiderModel;
    private TextView mPickUpLocation;
    private TextView mDestination;
    private TextView mNumberOfSeats;
    private TextView mDonation;
    private TextView mComments;
    private TextView mDriverId;
    private TextView mEstimateMI;
    private TextView mUserID;
    private TextView mUpdateClick;
    private RoundedImageView mRiderImage;
    private ImageView mBannerImageView;
    private TextView mRiderName;
    private TextView mPostedTime;
    private RelativeLayout toolbar_Ride_Details;
    SharedPref sharedPref;
    LinearLayout layoutFull;
    public static Boolean fromRealAdvacne = false;
    Activity activity;
    public static Activity activityRideDetails_Activity;

    public static boolean isFromRequestDriver = false;
    public static boolean toPayment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_details);
        context = this;
        activityRideDetails_Activity = this;
        sharedPref = new SharedPref(context);
        btnRequestDriver = (Button) findViewById(R.id.button_request_be_driver_real);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        btnRequestDriver.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        layoutFull = (LinearLayout) findViewById(R.id.layout_ride);
        mPickUpLocation = (TextView) findViewById(R.id.pick_up_location);
        mDestination = (TextView) findViewById(R.id.destination);
        mNumberOfSeats = (TextView) findViewById(R.id.number_of_seats);
        mUpdateClick = (TextView) findViewById(R.id.updateClick);
        mDonation = (TextView) findViewById(R.id.donation);
        mComments = (TextView) findViewById(R.id.comments);
        mDriverId = (TextView) findViewById(R.id.image_driverId);
        mEstimateMI = (TextView) findViewById(R.id.estimated_mi_text_view);
        mRiderImage = (RoundedImageView) findViewById(R.id.imageView_driver_pic);
        mBannerImageView = (ImageView) findViewById(R.id.banner_image_view);
        mRiderName = (TextView) findViewById(R.id.riderName);
        mPostedTime = (TextView) findViewById(R.id.posted_time_text_view);
        toolbar_Ride_Details = (RelativeLayout) findViewById(R.id.toolbar_Ride_Details);
        mUserID = (TextView) findViewById(R.id.userId);
        mUpdateClick.setOnClickListener(this);
        mUserID.setVisibility(View.VISIBLE);


        Intent intent = getIntent();
        String navFrom = intent.getStringExtra("navFrom");
        Log.e("navFrom", "" + navFrom);

        if (navFrom != null) {
            if (navFrom.equals("rewards")) {

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int position = 0;
        if (intent != null) {
            position = intent.getIntExtra("Position", 0);
            Constants.RIDE_POSITION = position;
        }

        mActiveRiderModel = Singleton.getInstance().mActiveRiderArray.get(position);
        Constants.RIDEID = mActiveRiderModel.getRideId();
        mPickUpLocation.setText(mActiveRiderModel.getPickUpLocation());

        sharedPref.setString("userLoc", mActiveRiderModel.getPickUpLocation());
        sharedPref.setString("userPickupLat", mActiveRiderModel.getPickupLat());
        sharedPref.setString("userPickupLong", mActiveRiderModel.getPickupLong());
        sharedPref.setString("userDestination", mActiveRiderModel.getDestination());
        sharedPref.setString("userDestinationLat", mActiveRiderModel.getDestinationLat());
        sharedPref.setString("userDestinationLong", mActiveRiderModel.getDestinationLong());
        sharedPref.setString("Rider_name", mActiveRiderModel.getRiderName());
        sharedPref.setString("Rider_name_Details", Singleton.getInstance().mActiveRiderArray.get(0).getRiderfirstName() + "" + Singleton.getInstance().mActiveRiderArray.get(0).getRiderlastName());
        sharedPref.setString("Rider_Image", mActiveRiderModel.getRiderImage());


        mDestination.setText(mActiveRiderModel.getDestination());
        mNumberOfSeats.setText(mActiveRiderModel.getSeatNo());
        mDonation.setText("$" + mActiveRiderModel.getDonation());
        mComments.setText(mActiveRiderModel.getComments());
        mDriverId.setText("user ID" + mActiveRiderModel.getRiderId());
        mEstimateMI.setText(mActiveRiderModel.getEstimatedMI());
        mRiderName.setText(mActiveRiderModel.getRiderfirstName() + " " + mActiveRiderModel.getRiderlastName());
        mPostedTime.setText(mActiveRiderModel.getPostedTime());
        mUserID.setText("UserID: " + mActiveRiderModel.getRiderName());
        btnRequestDriver.setText("REQUEST TO BE A DRIVER \n ETA " + mActiveRiderModel.getPickupTime());

        if (!mActiveRiderModel.getRiderImage().equals("")) {
            setImage(mRiderImage);
            setImage(mBannerImageView);
        }
    }

    private void setImage(ImageView view) {
        Picasso.with(context)
                .load(mActiveRiderModel.getRiderImage())
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_image)
                .into(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_request_be_driver_real:

                if (sharedPref.getString("DriverMode").equals("1")) {
                    CommonAsynTask acceptRide = new CommonAsynTask(context, Constants.ACCEPT_RIDE_REQUEST, getData(),
                            Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
                        @Override
                        public void onTaskCompleted(JSONObject jsonObject) {
                            if (jsonObject != null) {
                                try {
                                    String result = jsonObject.getString("result");
                                    String message = jsonObject.getString("message");
                                    if (!TextUtils.isEmpty(result)) {
                                        if (result.equals("success")) {
                                            Constants.RIDE_ACCPET = true;
                                            toPayment = false;
                                            Intent intentHome = new Intent(context, HomeMenu.class);
                                            intentHome.putExtra("message", message);
                                            sharedPref.setString(Constants.strCheckFromRealTimeOrAdvBook, Constants.yesRealTime);
                                            startActivity(intentHome);
                                            finish();

                                        } else if (jsonObject.getString(Constants.MESSAGE).equals(Constants.DRIVER_BANK_ERROR)) {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                            builder1.setTitle("Alert");
                                            builder1.setMessage(jsonObject.getString(Constants.MESSAGE));
                                            builder1.setCancelable(true);
                                            builder1.setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                            int which) {
                                                            fromRealAdvacne = true;
                                                            HomeMenu.txtEdit.setVisibility(View.VISIBLE);
                                                            mUserID.setVisibility(View.GONE);
                                                            toPayment = true;

                                                            Intent toPayment = new Intent(RideDetails_Activity.this, HomeMenu.class);
                                                            startActivity(toPayment);


                                                        }
                                                    });
                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();

                                        } else {
                                            Toast.makeText(RideDetails_Activity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    acceptRide.execute();
                } else if (sharedPref.getString("DriverMode").equals("0")) {
                    snackbar = Snackbar.make(layoutFull, "Please enable driver mode", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                }

                break;
            case R.id.imageView_backarrow:
                finish();
                break;
            default:
                break;
        }
    }

    private void start_fragment(Fragment frag, String Tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, frag, Tag);
        fragmentTransaction.commit();
    }

    private String getData() {
        JSONObject activeRiderData = new JSONObject();
        SharedPref pref = new SharedPref(this);
        sharedPref.setString("rideIdForHome", String.valueOf(mActiveRiderModel.getRideId()));
        String userId = pref.getString("userId");
        if (!TextUtils.isEmpty(userId)) {
            try {
                activeRiderData.put(Constants.ACTIVE_RIDE_RIDE_ID, mActiveRiderModel.getRideId());
                activeRiderData.put("driverId", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return activeRiderData.toString();
    }
}