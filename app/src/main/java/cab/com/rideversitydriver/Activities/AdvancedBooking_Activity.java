package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cab.com.rideversitydriver.Adapters.AdvancedBookingRideList_Adapter;
import cab.com.rideversitydriver.Models.RideListModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.Singleton;

/**
 * Created by Kalidoss on 25/08/16.
 */
public class AdvancedBooking_Activity extends AppCompatActivity implements View.OnClickListener {

    Button btnAcceptRide;
    RelativeLayout imgBack;
    Context context;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ArrayList<String> data;
    public static View.OnClickListener activeRideClickListener;
    RideListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_list);
        context = this;
        //btnAcceptRide=(Button)findViewById(R.id.button_details_ride_accept);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        //btnAcceptRide.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        activeRideClickListener = new RideListOnClick(context);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_ride_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        addValues();
        adapter = new AdvancedBookingRideList_Adapter(context);
        recyclerView.setAdapter(adapter);
    }

    private class RideListOnClick implements View.OnClickListener {

        private final Context context;

        private RideListOnClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            Intent detailsIntent = new Intent(context, AdvancedBookingRideDetails_Activity.class);
            detailsIntent.putExtra("Position", selectedItemPosition);
            startActivity(detailsIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            default:
                break;
        }
    }

    public void addValues() {

        Singleton.getInstance().RideListsArray.clear();
        model = new RideListModel();
        model.setSourceAddress("308 NE 1ND AVE");
        model.setDestinationAddress("133 NE 2ND AVE");
        model.setDatetime("10/10/16 5.40 PM");
        model.setDollar("$10,00");
        model.setMin("3 min");
        Singleton.getInstance().RideListsArray.add(model);

        model = new RideListModel();
        model.setSourceAddress("308 NE 1ND AVE");
        model.setDestinationAddress("133 NE 2ND AVE");
        model.setDatetime("10/10/16 4.40 PM");
        model.setDollar("$50,00");
        model.setMin("3 min");
        Singleton.getInstance().RideListsArray.add(model);

        model = new RideListModel();
        model.setSourceAddress("308 NE 1ND AVE");
        model.setDestinationAddress("133 NE 2ND AVE");
        model.setDatetime("12/10/16 5.30 PM");
        model.setDollar("$30,00");
        model.setMin("3 min");
        Singleton.getInstance().RideListsArray.add(model);
    }
}