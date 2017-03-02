package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cab.com.rideversitydriver.Dialogs.CustomDialog;
import cab.com.rideversitydriver.Interfaces.DialogDismisser;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.Singleton;

/**
 * Created by KEERTHINI on 8/1/2016.
 */

public class CarPoolDetail_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView txtDate, txtTime, txtPickupLocation, txtDestination, txtDonation, txtTripType, txtComments, txtSeats;
    int Position;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carpool_details);
        context = this;
        RelativeLayout backArrow = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        Button joincarPool = (Button) findViewById(R.id.button_start_carpool);
        txtDate = (TextView) findViewById(R.id.textview_carpool_date);
        txtTime = (TextView) findViewById(R.id.textview_carpool_time);
        txtPickupLocation = (TextView) findViewById(R.id.textview_carpool_pickuplocation);
        txtDestination = (TextView) findViewById(R.id.textview_carpool_destination);
        txtDonation = (TextView) findViewById(R.id.textview_carpool_donation);
        txtTripType = (TextView) findViewById(R.id.textview_carpool_triptype);
        txtComments = (TextView) findViewById(R.id.textview_carpool_comments);
        txtSeats = (TextView) findViewById(R.id.textview_carpool_seats);

        joincarPool.setOnClickListener(this);
        backArrow.setOnClickListener(this);

        Intent mIntent = getIntent();
        if (mIntent != null) {
            Position = mIntent.getIntExtra("POSITION", 0);

            if (!Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getRideDate().isEmpty()) {
                txtDate.setText(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getRideDate());
            } else {
                txtDate.setText("-");
            }

            if (!Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getRideTime().isEmpty()) {
                txtTime.setText(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getRideTime());
            } else {
                txtTime.setText("-");
            }

            if (!Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getPickupLocation().isEmpty()) {
                txtPickupLocation.setText(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getPickupLocation());
            } else {
                txtPickupLocation.setText("-");
            }

            if (!Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getDestination().isEmpty()) {
                txtDestination.setText(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getDestination());
            } else {
                txtDestination.setText("-");
            }

            if (!Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getDonation().isEmpty()) {
                txtDonation.setText("$" + Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getDonation());
            } else {
                txtDonation.setText("-");
            }

            if (!Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getTripType().isEmpty()) {
                txtTripType.setText(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getTripType());
            } else {
                txtTripType.setText("-");
            }

            if (!Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getComments().isEmpty()) {
                txtComments.setText(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getComments());
            } else {
                txtComments.setText("-");
            }

            if (!Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getSeatNo().isEmpty()) {
                txtSeats.setText(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getSeatAvailable() + "/" + Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getSeatNo());
            } else {
                txtSeats.setText("-");
            }


            LinearLayout layoutJoinedRider = (LinearLayout) findViewById(R.id.layout_rider_list);
            for (int i = 0; i < Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getJoinedriderLists().size(); i++) {
                View child = getLayoutInflater().inflate(R.layout.layout_rider_row, null);
                TextView txtRiderName = (TextView) child.findViewById(R.id.textView_rider_name);
                RoundedImageView imgRider = (RoundedImageView) child.findViewById(R.id.imageView_joined_rider);
                Picasso.with(context).load(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getJoinedriderLists().get(i).getRiderImage()).fit().centerCrop()
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(imgRider);

                txtRiderName.setText(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getJoinedriderLists().get(i).getName());
                layoutJoinedRider.addView(child);
            }
            int seatNo = Integer.parseInt(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getSeatNo());
            int seatsAvaliable = Integer.parseInt(Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().get(Position).getSeatAvailable());

            if (seatsAvaliable != 0) {
                if (seatsAvaliable > 0) {
                    for (int j = 0; j < seatsAvaliable; j++) {
                        View Emptychild = getLayoutInflater().inflate(R.layout.layout_empty_rider_row, null);
                        layoutJoinedRider.addView(Emptychild);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.button_start_carpool:
                CustomDialog customDialog = new CustomDialog();
                customDialog.setDialogTitle("Carpool Started", new DialogDismisser() {
                    @Override
                    public void dismissDialog(int dismiss) {
                        Intent intent = new Intent(CarPoolDetail_Activity.this, HomeMenu.class);
                        startActivity(intent);
                    }
                });
                customDialog.setDialogDescription("");
                customDialog.show(getSupportFragmentManager(), "carpool");
                break;
            default:
                break;
        }
    }
}