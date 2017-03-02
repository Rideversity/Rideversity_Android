package cab.com.rideversitydriver.Adapters;

/**
 * Created by KEERTHINI on 8/4/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cab.com.rideversitydriver.Fragments.PastRide_Fragment;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.Singleton;

public class PastRide_RecyclerAdapter extends RecyclerView.Adapter<PastRide_RecyclerAdapter.PastRideViewHolder> {

    Context context;

    public static class PastRideViewHolder extends RecyclerView.ViewHolder {

        TextView txtPickuplocation, txtDestination, txtDate, txtTime;
        RoundedImageView imgDriver;

        public PastRideViewHolder(View itemView) {
            super(itemView);
            this.txtPickuplocation = (TextView) itemView.findViewById(R.id.addressLine1);
            this.txtDestination = (TextView) itemView.findViewById(R.id.addressLine2);
            this.txtDate = (TextView) itemView.findViewById(R.id.date);
            this.txtTime = (TextView) itemView.findViewById(R.id.time);
            this.imgDriver = (RoundedImageView) itemView.findViewById(R.id.imageView_driver);
        }
    }

    public PastRide_RecyclerAdapter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public PastRideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_past_ride, parent, false);


        view.setOnClickListener(PastRide_Fragment.pastRideClickListener);

        PastRideViewHolder rideViewHolder = new PastRideViewHolder(view);
        return rideViewHolder;
    }

    @Override
    public void onBindViewHolder(final PastRideViewHolder holder, final int listPosition) {
        holder.txtPickuplocation.setText(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(listPosition).getPickupLocation());
        holder.txtDestination.setText(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(listPosition).getDestination());
        holder.txtDate.setText(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(listPosition).getRideDate());
        holder.txtTime.setText(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(listPosition).getRideTime());

        if (!Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(listPosition).getDriverImage().isEmpty()) {
            Picasso.with(context).load(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(listPosition).getDriverImage()).fit().centerCrop()
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.imgDriver);
        } else {
            holder.imgDriver.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public int getItemCount() {
        return Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().size();
    }

}