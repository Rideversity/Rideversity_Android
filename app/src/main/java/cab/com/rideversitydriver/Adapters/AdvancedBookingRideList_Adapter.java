package cab.com.rideversitydriver.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cab.com.rideversitydriver.Activities.AdvancedBooking_Activity;
import cab.com.rideversitydriver.Models.ActiveRiderModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.Singleton;

import static cab.com.rideversitydriver.R.id.imageView_driver;

/**
 * Created by Kalidoss on 25/08/16.
 */
public class AdvancedBookingRideList_Adapter extends RecyclerView.Adapter<AdvancedBookingRideList_Adapter.MyViewHolder> {

    Context mContext;
    ArrayList<ActiveRiderModel> rideListArray;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDollar, txtDateTime, txtMin, txtSourceAddress, txtDestinationAddress;
        private RoundedImageView mImageView;

        public MyViewHolder(View view) {
            super(view);
            this.txtDollar = (TextView) itemView.findViewById(R.id.textview_dollar);
            this.txtDateTime = (TextView) itemView.findViewById(R.id.textview_date_time);
            this.txtMin = (TextView) itemView.findViewById(R.id.textview_mins);
            this.txtSourceAddress = (TextView) itemView.findViewById(R.id.addressLine1);
            this.txtDestinationAddress = (TextView) itemView.findViewById(R.id.addressLine2);
            this.mImageView = (RoundedImageView) itemView.findViewById(imageView_driver);
        }
    }

    public AdvancedBookingRideList_Adapter(Context context) {
        this.mContext = context;
        if (Singleton.getInstance().mAdvancedRiderArray.size() > 0
                && Singleton.getInstance().mAdvancedRiderArray != null) {
            this.rideListArray = Singleton.getInstance().mAdvancedRiderArray;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_list_row, parent, false);
        // itemView.setOnClickListener(DriversAvaliable_Activity.driversClickListener);
        itemView.setOnClickListener(AdvancedBooking_Activity.activeRideClickListener);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ActiveRiderModel ridelist = rideListArray.get(position);
        holder.txtSourceAddress.setText(ridelist.getPickUpLocation());
        holder.txtDestinationAddress.setText(ridelist.getDestination());
        holder.txtDollar.setText("$" + ridelist.getDonation());
        holder.txtDateTime.setText(ridelist.getRideTime() + " " + ridelist.getRideDate());
        holder.txtMin.setText(ridelist.getPickupTime() + " away");

        if (!ridelist.getRiderImage().equals("")) {
            Picasso.with(mContext).load(ridelist.getRiderImage()).fit().centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image).
                    into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;

        if (rideListArray != null) {
            size = rideListArray.size();
        }
        return size;
    }
}

