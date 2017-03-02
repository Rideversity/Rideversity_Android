package cab.com.rideversitydriver.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cab.com.rideversitydriver.Fragments.ActiveRide_Fragment;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.Singleton;


/**
 * Created by KEERTHINI on 7/28/2016.
 */
public class ActiveRide_RecyclerAdapter extends RecyclerView.Adapter<ActiveRide_RecyclerAdapter.ActiveRideViewHolder> {
    Context context;

    public static class ActiveRideViewHolder extends RecyclerView.ViewHolder {

        TextView txtPickupLocation, txtPickupLocationUpcoming, txtDestination, txtDestinationUpcoming, txtDateTime, txtDateTimeUPcoming, txtDriverCount;
        RoundedImageView imgDriver, imgDriverUpcoming;
        LinearLayout layoutActive, layoutUpcoming;

        public ActiveRideViewHolder(View itemView) {
            super(itemView);
            this.txtDriverCount = (TextView) itemView.findViewById(R.id.driver_count);
            this.txtPickupLocation = (TextView) itemView.findViewById(R.id.addressLine1);
            this.txtPickupLocationUpcoming = (TextView) itemView.findViewById(R.id.addressLine1_upcom);
            this.txtDestination = (TextView) itemView.findViewById(R.id.addressLine2);
            this.txtDestinationUpcoming = (TextView) itemView.findViewById(R.id.addressLine2_upcom);
            this.txtDateTime = (TextView) itemView.findViewById(R.id.dateTime);
            this.txtDateTimeUPcoming = (TextView) itemView.findViewById(R.id.time_upcom);
            this.layoutActive = (LinearLayout) itemView.findViewById(R.id.layout_pickup_now);
            this.layoutUpcoming = (LinearLayout) itemView.findViewById(R.id.layout_pickup_later);
            this.imgDriver = (RoundedImageView) itemView.findViewById(R.id.imageView_driver);
            this.imgDriverUpcoming = (RoundedImageView) itemView.findViewById(R.id.imageView_driver);

        }
    }

    public ActiveRide_RecyclerAdapter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public ActiveRideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_active_ride, parent, false);
        view.setOnClickListener(ActiveRide_Fragment.activeRideClickListener);
        ActiveRideViewHolder rideViewHolder = new ActiveRideViewHolder(view);
        return rideViewHolder;
    }

    @Override
    public void onBindViewHolder(final ActiveRideViewHolder holder, final int listPosition) {
        holder.layoutActive.setVisibility(View.GONE);
        holder.layoutUpcoming.setVisibility(View.GONE);

        if (Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getDriverId().equals("0")) {
            holder.layoutActive.setVisibility(View.VISIBLE);
            holder.layoutUpcoming.setVisibility(View.GONE);

            holder.txtPickupLocation.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getPickupLocation());
            holder.txtDestination.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getDestination());
            holder.txtDateTime.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getRideDate() + " " + Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getRideTime());

            if (!Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getRiderImage().isEmpty()) {
                Picasso.with(context).load(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getRiderImage()).fit().centerCrop()
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(holder.imgDriver);
            } else {
                holder.imgDriver.setImageResource(R.drawable.no_image);
            }

        } else {

            holder.layoutActive.setVisibility(View.GONE);
            holder.layoutUpcoming.setVisibility(View.VISIBLE);

            holder.txtPickupLocationUpcoming.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getPickupLocation());
            holder.txtDestinationUpcoming.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getDestination());
            holder.txtDateTimeUPcoming.setText(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getRideDate() + " " + Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getRideTime());

            if (!Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getRiderImage().isEmpty()) {
                Picasso.with(context).load(Singleton.getInstance().activeRideArray.get(0).getActiverideLists().get(listPosition).getRiderImage()).fit().centerCrop()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.no_image)
                        .into(holder.imgDriverUpcoming);
            } else {
                holder.imgDriverUpcoming.setImageResource(R.drawable.no_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return Singleton.getInstance().activeRideArray.get(0).getActiverideLists().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}