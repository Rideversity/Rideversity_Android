package cab.com.rideversitydriver.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cab.com.rideversitydriver.Activities.RewardsDetail_Activity;
import cab.com.rideversitydriver.Fragments.Rewards_Fragment;
import cab.com.rideversitydriver.Models.RewardsListModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.PorterShapeImageView;

/**
 * Created by KEERTHINI on 7/28/2016.
 */

public class Rewards_RecyclerAdapter extends RecyclerView.Adapter<Rewards_RecyclerAdapter.RewardsViewHolder> {

    Context context;
    private ArrayList<RewardsListModel> dataSet;
    public static List<RewardsListModel> searchListRewards = null;

    public static class RewardsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewReward, textViewMinutesText;
        private ImageView imageArrow;
        PorterShapeImageView imageReward;
        RelativeLayout layout;


        public RewardsViewHolder(View itemView) {
            super(itemView);
            this.textViewReward = (TextView) itemView.findViewById(R.id.textView_reward);
            this.textViewMinutesText = (TextView) itemView.findViewById(R.id.minutesText);
            this.imageReward = (PorterShapeImageView) itemView.findViewById(R.id.imageView_reward);
            this.imageArrow = (ImageView) itemView.findViewById(R.id.imageView_right_arrow);
            this.layout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_rewardrow);
        }
    }

    public Rewards_RecyclerAdapter(Context context, ArrayList<RewardsListModel> data) {
        this.context = context;
        this.dataSet = data;
        this.searchListRewards = data;
    }

    @Override
    public RewardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rewards_list, parent, false);


        view.setOnClickListener(Rewards_Fragment.rewardClickListener);

        RewardsViewHolder rewardViewHolder = new RewardsViewHolder(view);
        return rewardViewHolder;
    }

    @Override
    public void onBindViewHolder(final RewardsViewHolder holder, final int listPosition) {

        TextView textViewReward = holder.textViewReward;
        TextView textViewMinutes = holder.textViewMinutesText;

        textViewReward.setText(searchListRewards.get(listPosition).getSponsorName());
        textViewMinutes.setText(searchListRewards.get(listPosition).getEta());

        if (searchListRewards.get(listPosition).getDiscountImage().isEmpty()) {
            holder.imageReward.setImageDrawable(ActivityCompat.getDrawable(context,
                    R.drawable.no_image));
        } else {
            Picasso.with(context).load(searchListRewards.get(listPosition).getDiscountImage()).fit().centerCrop()
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.imageReward);
        }


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RewardsDetail_Activity.class);
                Log.e("selectedItemPosition", "" + searchListRewards.get(listPosition).getPosition());
                intent.putExtra("POSITION", searchListRewards.get(listPosition).getPosition());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchListRewards.size();
    }

    public void filters(final String text) {
        searchListRewards = new ArrayList<RewardsListModel>();
        searchListRewards.clear();

        if (TextUtils.isEmpty(text)) {
            searchListRewards.addAll(dataSet);
        } else {
            for (RewardsListModel item : dataSet) {
                if (item.getSponsorName().toLowerCase(Locale.getDefault()).contains(text)) {
                    searchListRewards.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}