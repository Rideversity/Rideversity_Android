package cab.com.rideversitydriver.Adapters;

/**
 * Created by KEERTHINI on 8/3/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cab.com.rideversitydriver.Activities.CarPoolDetail_Activity;
import cab.com.rideversitydriver.Fragments.Carpool_Fragment;
import cab.com.rideversitydriver.Models.CarpoolListModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;


/**
 * Created by KEERTHINI on 8/3/2016.
 */


public class CarPool_RecyclerAdapter extends RecyclerView.Adapter<CarPool_RecyclerAdapter.CarPoolViewHolder> {

    Context context;
    private ArrayList<CarpoolListModel> dataSet;
    public static List<CarpoolListModel> searchListRewards = null;

    public static class CarPoolViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAppName;
        TextView textViewPickupAddress, txtDestinationAddress, txtDate, txtTime;
        RoundedImageView imgDriver;
        LinearLayout layout;

        public CarPoolViewHolder(View itemView) {
            super(itemView);
            this.textViewAppName = (TextView) itemView.findViewById(R.id.addressLine1);
            this.textViewPickupAddress = (TextView) itemView.findViewById(R.id.addressLine1);
            this.txtDestinationAddress = (TextView) itemView.findViewById(R.id.addressLine2);
            this.txtDate = (TextView) itemView.findViewById(R.id.date);
            this.txtTime = (TextView) itemView.findViewById(R.id.time);
            this.imgDriver = (RoundedImageView) itemView.findViewById(R.id.imageView_driver);
            this.layout = (LinearLayout) itemView.findViewById(R.id.layout_pickup_now);


        }
    }

    public CarPool_RecyclerAdapter(Context ctx, ArrayList<CarpoolListModel> data) {
        this.context = ctx;
        this.dataSet = data;
        this.searchListRewards = data;
    }

    @Override
    public CarPoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carpool_lists, parent, false);


        view.setOnClickListener(Carpool_Fragment.carPoolClickListener);

        CarPoolViewHolder rideViewHolder = new CarPoolViewHolder(view);
        return rideViewHolder;
    }

    @Override
    public void onBindViewHolder(final CarPoolViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewAppName;

        holder.textViewPickupAddress.setText(searchListRewards.get(listPosition).getPickupLocation());
        holder.txtDestinationAddress.setText(searchListRewards.get(listPosition).getDestination());
        holder.txtDate.setText(searchListRewards.get(listPosition).getRideDate());
        holder.txtTime.setText("Departure: " + searchListRewards.get(listPosition).getRideTime());

        if (!searchListRewards.get(listPosition).getDriverImage().isEmpty()) {
            Picasso.with(context).load(searchListRewards.get(listPosition).getDriverImage()).fit().centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.imgDriver);
        } else {
            holder.imgDriver.setImageResource(R.drawable.no_image);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CarPoolDetail_Activity.class);
                Log.e("selectedItemPosition", "" + searchListRewards.get(listPosition).getPosition());
                intent.putExtra("POSITION", searchListRewards.get(listPosition).getPosition());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}