package cab.com.rideversitydriver.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cab.com.rideversitydriver.Models.SchoolListModel;
import cab.com.rideversitydriver.R;

/**
 * Created by Kalidoss on 10/11/16.
 */
public class GenderSpinnerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<SchoolListModel> schoolListArray;

    public GenderSpinnerAdapter(LayoutInflater inflater, ArrayList<SchoolListModel> schoolListArray) {
        this.inflater = inflater;
        this.schoolListArray = schoolListArray;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return schoolListArray.size();

        int count = schoolListArray.size();
        return count > 0 ? count - 1 : count;
    }

    @Override
    public SchoolListModel getItem(int position) {
        // TODO Auto-generated method stub
        return schoolListArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public String getSchoolId(int position) {
        return getItem(position).schoolId;
    }

    public String getSchoolName(int position) {
        return getItem(position).schoolName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_textview, viewGroup, false);
            holder = new ViewHolder();
            holder.txtSpinner = (TextView) convertView.findViewById(R.id.textSpinner);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtSpinner.setText(getSchoolName(position));

        return convertView;
    }

    private class ViewHolder {
        TextView txtSpinner;
    }

}


