package cab.com.rideversitydriver.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cab.com.rideversitydriver.Models.InviteRiderModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.Singleton;

/**
 * Created by Kalidoss on 17/10/16.
 */
public class InviteRider_RecyclerAdapter extends RecyclerView.Adapter<InviteRider_RecyclerAdapter.InviteDriverViewHolder> {

    private ArrayList<InviteRiderModel> dataSet;
    private List<InviteRiderModel> searchList = null;
    Context context;

    public static class InviteDriverViewHolder extends RecyclerView.ViewHolder {

        TextView txtViewDriverName;
        TextView txtViewAddress;
        RoundedImageView imgProfilePic;
        RelativeLayout driverLayout;

        public InviteDriverViewHolder(View itemView) {
            super(itemView);
            this.txtViewDriverName = (TextView) itemView.findViewById(R.id.textView_invite_driver_name);
            this.txtViewAddress = (TextView) itemView.findViewById(R.id.textView_invite_driver_address);
            this.imgProfilePic = (RoundedImageView) itemView.findViewById(R.id.imageView_invite_driver_images);
            this.driverLayout = (RelativeLayout) itemView.findViewById(R.id.layout_invite_driver);

        }
    }

    public InviteRider_RecyclerAdapter(Context context, ArrayList<InviteRiderModel> data) {
        this.context = context;
        this.dataSet = data;
        this.searchList = data;
    }

    @Override
    public InviteDriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invite_rider_list, parent, false);

        InviteDriverViewHolder rideViewHolder = new InviteDriverViewHolder(view);
        return rideViewHolder;
    }

    @Override
    public void onBindViewHolder(final InviteDriverViewHolder holder, final int listPosition) {

        TextView textViewName = holder.txtViewDriverName;
        TextView textViewPhoneAddress = holder.txtViewAddress;
        RoundedImageView imgProfilePic = holder.imgProfilePic;
        RelativeLayout driverLayout = holder.driverLayout;

        textViewName.setText(searchList.get(listPosition).getFirstName());
        textViewPhoneAddress.setText(searchList.get(listPosition).getAddress());

        if (!searchList.get(listPosition).getProfileImage().isEmpty()) {
            Picasso.with(context).load(searchList.get(listPosition).getProfileImage()).fit().centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.imgProfilePic);
        } else {
            holder.imgProfilePic.setImageResource(R.drawable.no_image);
        }


        if (searchList.get(listPosition).isSelected() == true) {
            holder.driverLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_grey));
        } else {
            holder.driverLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        driverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constants.INVITE_DRIVER_FROM.equals("PickupNow")) {
                    if (searchList.get(listPosition).isSelected() == false) {
                        Singleton.getInstance().inviteRiderArray.get(listPosition).setSelected(true);
                        holder.driverLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_grey));
                    } else {
                        Singleton.getInstance().inviteRiderArray.get(listPosition).setSelected(false);
                        holder.driverLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    }
                } else {
                    if (searchList.get(listPosition).isSelected() == false) {
                        Singleton.getInstance().inviteRiderArray.get(listPosition).setSelected(true);
                        holder.driverLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_grey));
                    } else {
                        Singleton.getInstance().inviteRiderArray.get(listPosition).setSelected(false);
                        holder.driverLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchList = new ArrayList<>();
        searchList.clear();
        if (charText.length() == 0) {
            searchList.addAll(dataSet);
        } else {
            for (InviteRiderModel wp : dataSet) {
                if (wp.getFirstName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filters(final String text) {
        searchList = new ArrayList<InviteRiderModel>();
        searchList.clear();

        if (TextUtils.isEmpty(text)) {
            searchList.addAll(dataSet);
        } else {
            for (InviteRiderModel item : dataSet) {
                if (item.getFirstName().toLowerCase(Locale.getDefault()).contains(text)) {
                    searchList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}