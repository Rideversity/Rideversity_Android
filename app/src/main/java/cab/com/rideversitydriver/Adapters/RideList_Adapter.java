package cab.com.rideversitydriver.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cab.com.rideversitydriver.Activities.RideLists_Activity;
import cab.com.rideversitydriver.Models.ActiveRiderModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.Singleton;

/**
 * Created by Kalidoss on 04/08/16.
 */

public class RideList_Adapter extends RecyclerView.Adapter<RideList_Adapter.MyViewHolder> {

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
            mImageView = (RoundedImageView) itemView.findViewById(R.id.imageView_driver);
        }
    }

    public RideList_Adapter(Context context) {
        this.mContext = context;
        this.rideListArray = Singleton.getInstance().mActiveRiderArray;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_list_row, parent, false);
        itemView.setOnClickListener(RideLists_Activity.activeRideClickListener);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ActiveRiderModel riderModel = rideListArray.get(position);
        holder.txtSourceAddress.setText(riderModel.getPickUpLocation());
        holder.txtDestinationAddress.setText(riderModel.getDestination());
        holder.txtMin.setText(riderModel.getPickupTime() + " away");
        holder.txtDollar.setText("$" + riderModel.getDonation());
        holder.txtDateTime.setText(riderModel.getPostedTime());

        if (!riderModel.getRiderImage().equals("")) {
            Picasso.with(mContext).load(riderModel.getRiderImage()).fit().centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image).
                    into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return rideListArray.size();
    }
}

