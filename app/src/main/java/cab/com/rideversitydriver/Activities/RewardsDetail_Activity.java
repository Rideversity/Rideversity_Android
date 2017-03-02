package cab.com.rideversitydriver.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;


/**
 * Created by KEERTHINI on 8/1/2016.
 */

public class RewardsDetail_Activity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    TextView sponsor, place, desc, placeDesc, txtClose;
    Dialog select_ride_dialog;
    Button btnPickupnow, btnPickuplater;
    ImageView imgClose;
    static int Position;
    ImageView imgRewards;
    RelativeLayout layout;
    double LocationLat, LocationLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.rewards_detail);
        context = getApplicationContext();
        RelativeLayout backArrow = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        sponsor = (TextView) findViewById(R.id.textView_sponsorName);
        place = (TextView) findViewById(R.id.textView_locationName);
        desc = (TextView) findViewById(R.id.textView_desc_header);
        placeDesc = (TextView) findViewById(R.id.textView_rewards_desc);
        Button takemeToLocation = (Button) findViewById(R.id.button_location);
        imgRewards = (ImageView) findViewById(R.id.imageview_rewards);
        layout = (RelativeLayout) findViewById(R.id.layout_rewards_details);


        Intent mIntent = getIntent();
        if (mIntent != null) {
            Position = mIntent.getIntExtra("POSITION", 0);
            sponsor.setText(getResources().getString(R.string.reward_sponsor));
            place.setText(Singleton.getInstance().rewardsArray.get(0).getRewardsLists().get(Position).getSponsorName());
            desc.setText(getResources().getString(R.string.desc));
            placeDesc.setText(Singleton.getInstance().rewardsArray.get(0).getRewardsLists().get(Position).getDescription());

            if (Singleton.getInstance().rewardsArray.get(0).getRewardsLists().get(Position).getDiscountImage().isEmpty()) {
                imgRewards.setImageDrawable(ActivityCompat.getDrawable(context,
                        R.drawable.no_image));
            } else {
                Picasso.with(context).load(Singleton.getInstance().rewardsArray.get(0).getRewardsLists().get(Position).getDiscountImage()).fit()
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .centerInside()
                        .into(imgRewards);
            }

            LocationLat = Double.parseDouble(Singleton.getInstance().rewardsArray.get(0).getRewardsLists().get(Position).getLocation_lat());
            LocationLong = Double.parseDouble(Singleton.getInstance().rewardsArray.get(0).getRewardsLists().get(Position).getLocation_long());
        }

        takemeToLocation.setOnClickListener(this);
        backArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.button_location:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + LocationLat + "," + LocationLong));
                startActivity(intent);
                // SelectRideTypeDialog();
                break;
            case R.id.button_pickup_now:
                Intent rideIntent = new Intent(context, RideDetails_Activity.class);
                rideIntent.putExtra("navFrom", "rewards");
                startActivity(rideIntent);
                if (select_ride_dialog.isShowing()) {
                    select_ride_dialog.dismiss();
                }

                break;
            case R.id.button_pickup_later:
                Intent ridelaterIntent = new Intent(context, AdvancedBooking_Activity.class);
                ridelaterIntent.putExtra("navFrom", "rewards");
                startActivity(ridelaterIntent);

                if (select_ride_dialog.isShowing()) {
                    select_ride_dialog.dismiss();
                }

                break;
            case R.id.imageView_close:
                if (select_ride_dialog.isShowing()) {
                    select_ride_dialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    public void SelectRideTypeDialog() {
        select_ride_dialog = new Dialog(RewardsDetail_Activity.this);
        select_ride_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        select_ride_dialog.setContentView(R.layout.select_ride_type_dialog);


        WindowManager.LayoutParams WMLP = select_ride_dialog.getWindow().getAttributes();
        WMLP.gravity = Gravity.TOP;
        WMLP.x = 0;
        WMLP.y = 300;
        select_ride_dialog.getWindow().setAttributes(WMLP);
        select_ride_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        imgClose = (ImageView) select_ride_dialog.findViewById(R.id.imageView_close);
        btnPickupnow = (Button) select_ride_dialog.findViewById(R.id.button_pickup_now);
        btnPickuplater = (Button) select_ride_dialog.findViewById(R.id.button_pickup_later);
        btnPickupnow.setOnClickListener(this);
        btnPickuplater.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        select_ride_dialog.show();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        InputMethodManager imm = (InputMethodManager) RewardsDetail_Activity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            Utilities.hideKeyboard(RewardsDetail_Activity.this);
        }
        //SoftkeyBoard Handles
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = layout.getRootView().getHeight() - layout.getHeight();

                if (heightDiff > 100) {

                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    InputMethodManager inputManager = (InputMethodManager) layout
                            .getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    IBinder binder = layout.getWindowToken();
                    inputManager.hideSoftInputFromWindow(binder,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (heightDiff < 100) {
                }
            }
        });
    }
}
