package cab.com.rideversitydriver.Adapters;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import cab.com.rideversitydriver.Activities.NotificationSetting_Activity;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by CIPL0372 on 1/5/2017.
 */

public class NotificationListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> stringsData;

    NotificationListAdapter(Context context, ArrayList<String> stringsArrayList) {
        this.mContext = context;
        this.stringsData = stringsArrayList;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderData viewHolder;

        if (convertView == null) {
            viewHolder = new HolderData();
            viewHolder = new HolderData();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.notification_inner_list, parent, false);

            viewHolder.textname = (TextView) convertView.findViewById(R.id.textview_notification);
            viewHolder.switchCompat = (SwitchCompat) convertView.findViewById(R.id.toggle_notification);
        } else {
            viewHolder = (HolderData) convertView.getTag();
        }

        ArrayList<String> name = new ArrayList();

        return convertView;
    }

    class HolderData {
        TextView textname;
        SwitchCompat switchCompat;

    }
}
