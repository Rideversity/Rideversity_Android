package cab.com.rideversitydriver.Activities;

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
 * Created by Kalidoss on 25/08/16.
 */
public class AdvancedBookingRideDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnRequestDriver;
    RelativeLayout imgBack;
    Context context;

    private ActiveRiderModel mActiveRiderModel;
    private TextView mPickUpLocation;
    private TextView mDestination;
    private TextView mNumberOfSeats;
    private TextView mDonation;
    private TextView mTrip;
    private TextView mComments;
    private TextView mDate;
    private TextView mTime;
    private TextView mDriverId;
    private TextView mEstimatedMI;
    private RoundedImageView mImageView;
    private ImageView mBannerImageView;
    private TextView mRiderName;
    private TextView mUserId;
    SharedPref sharedPref;
    LinearLayout layoutFull;
    Snackbar snackbar;
    AlertDialog.Builder alertDialog;
    public static Boolean fromRealAdvacne = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_booking_ride_details);
        context = this;
        sharedPref = new SharedPref(context);
        btnRequestDriver = (Button) findViewById(R.id.button_request_be_driver);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        btnRequestDriver.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        layoutFull = (LinearLayout) findViewById(R.id.layout_advanced);
        mPickUpLocation = (TextView) findViewById(R.id.pick_up_location);
        mDestination = (TextView) findViewById(R.id.destination);
        mNumberOfSeats = (TextView) findViewById(R.id.number_of_seats);
        mDonation = (TextView) findViewById(R.id.donation);
        mTrip = (TextView) findViewById(R.id.trip);
        mComments = (TextView) findViewById(R.id.comments);
        mDate = (TextView) findViewById(R.id.dateTextView);
        mTime = (TextView) findViewById(R.id.timeTextView);
        mDriverId = (TextView) findViewById(R.id.image_driverId);
        mEstimatedMI = (TextView) findViewById(R.id.advanced_estimated_mi_text_view);
        mImageView = (RoundedImageView) findViewById(R.id.imageView_driver_pic);
        mBannerImageView = (ImageView) findViewById(R.id.ad_banner_image_view);
        mRiderName = (TextView) findViewById(R.id.image_driverName);
        mUserId = (TextView) findViewById(R.id.userId);
        mUserId.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        int position = 0;
        if (intent != null) {
            position = intent.getIntExtra("Position", 0);
        }

        mActiveRiderModel = Singleton.getInstance().mAdvancedRiderArray.get(position);
        mDate.setText(mActiveRiderModel.getRideDate());
        mTime.setText(mActiveRiderModel.getRideTime());
        mPickUpLocation.setText(mActiveRiderModel.getPickUpLocation());
        mDestination.setText(mActiveRiderModel.getDestination());

        sharedPref.setString("userLoc", mActiveRiderModel.getPickUpLocation());
        sharedPref.setString("userPickupLat", mActiveRiderModel.getPickupLat());
        sharedPref.setString("userPickupLong", mActiveRiderModel.getPickupLong());
        sharedPref.setString("userDestination", mActiveRiderModel.getDestination());
        sharedPref.setString("userDestinationLat", mActiveRiderModel.getDestinationLat());
        sharedPref.setString("userDestinationLong", mActiveRiderModel.getDestinationLong());
        sharedPref.setString("Rider_name", mActiveRiderModel.getRiderName());
        sharedPref.setString("Rider_name_details", mActiveRiderModel.getRiderfirstName() + "" + mActiveRiderModel.getRiderlastName());
        sharedPref.setString("Rider_Image", mActiveRiderModel.getRiderImage());

        mNumberOfSeats.setText(mActiveRiderModel.getSeatNo());
        mDonation.setText("$" + mActiveRiderModel.getDonation());
        //mTrip.setText(mActiveRiderModel.getTripType());   //Hided
        mComments.setText(mActiveRiderModel.getComments());
        mDriverId.setText("User Id: " + mActiveRiderModel.getRideId());//Visibility GONE IN UI
        mEstimatedMI.setText(mActiveRiderModel.getEstimatedMI());
        mRiderName.setText(mActiveRiderModel.getRiderfirstName() + " " + mActiveRiderModel.getRiderlastName());
        btnRequestDriver.setText("REQUEST TO BE DRIVER \n ETA " + "" + mActiveRiderModel.getPickupTime());
        mUserId.setText("UserID: " + mActiveRiderModel.getRiderName());

        if (!mActiveRiderModel.getRiderImage().equals("")) {
            setImage(mImageView);
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
            case R.id.button_request_be_driver:
                callService();
                break;
            case R.id.imageView_backarrow:
                finish();
                break;
            default:
                break;
        }
    }

    private String getData() {
        JSONObject activeRiderData = new JSONObject();
        SharedPref pref = new SharedPref(context);
        String userId = pref.getString("userId");
        Log.i("Test", userId);
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

    void showDialog(Context mContext) {

        alertDialog = new AlertDialog.Builder(AdvancedBookingRideDetails_Activity.this);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure?");
        // Setting Icon to Dialog
//        alertDialog.setIcon(R.drawable.save);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callService();
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.setCancelable(false);
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void callService() {

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
                                    Constants.RIDEID = mActiveRiderModel.getRideId();
                                    Intent intentHome = new Intent(context, HomeMenu.class);
                                    //intentHome.putExtra("message", message);
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
                                                    mUserId.setVisibility(View.GONE);
                                                    HomeMenu.txtEdit.setVisibility(View.VISIBLE);
                                                    BankAccount bankAccount = new BankAccount();
                                                    start_fragment(bankAccount, "bankaccount");

                                                }
                                            });
                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();

                                } else {
                                    Toast.makeText(AdvancedBookingRideDetails_Activity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                            /**/
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
    }

    private void start_fragment(Fragment frag, String Tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, frag, Tag);
        fragmentTransaction.commit();
    }
}