package cab.com.rideversitydriver.Adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cab.com.rideversitydriver.Activities.NotificationSetting_Activity;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;


/**
 * Created by Kalidoss on 20/09/16.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    Snackbar snackbar;
    ArrayList<String> arrayListTitle;
    HashMap<String, ArrayList> hashMapData;

    NotificationListAdapter notificationListAdapter;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNotification, txtNotificationHeader;
        private TextView txtHeader;
        SwitchCompat switchbutton;
        RelativeLayout layoutFull;
        ListView listViewNotification;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtNotificationHeader = (TextView) itemView.findViewById(R.id.notification_Header);

            this.listViewNotification = (ListView) itemView.findViewById(R.id.lst_view_notification);
        }
    }

    public NotificationAdapter(Context context, ArrayList<String> title, HashMap hashMapData) {

        this.context = context;
        this.arrayListTitle = title;
        this.hashMapData = hashMapData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list, parent, false);

        view.setOnClickListener(NotificationSetting_Activity.notificationClickListener);
        ViewHolder rewardViewHolder = new ViewHolder(view);
        return rewardViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        holder.txtNotificationHeader.setText(arrayListTitle.get(listPosition).replace("_", " "));
        notificationListAdapter = new NotificationListAdapter(context, hashMapData.get(arrayListTitle.get(listPosition)));

        try {
            holder.listViewNotification.setAdapter(notificationListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayListTitle.size();
    }


}