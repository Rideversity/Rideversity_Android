package cab.com.rideversitydriver.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cab.com.rideversitydriver.R;

/**
 * Created by CIPL0293 on 11/3/2016.
 */

public class SponsorSpinnerAdapter extends BaseAdapter {

    private ArrayList<String> mCategoryList;
    private LayoutInflater mLayoutInflater;

    public SponsorSpinnerAdapter(LayoutInflater layoutInflater, ArrayList<String> categoryList) {
        mLayoutInflater = layoutInflater;
        mCategoryList = categoryList;
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCategoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        SponsorSpinnerAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.spinner_textview, viewGroup, false);
            holder = new ViewHolder();
            holder.txtSpinner = (TextView) convertView.findViewById(R.id.textSpinner);
            convertView.setTag(holder);
        } else
            holder = (SponsorSpinnerAdapter.ViewHolder) convertView.getTag();

        holder.txtSpinner.setText(mCategoryList.get(i));
        return convertView;
    }

    private class ViewHolder {
        TextView txtSpinner;
    }
}
