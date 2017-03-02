package cab.com.rideversitydriver.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cab.com.rideversitydriver.Adapters.SponsorSpinnerAdapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.RewardsListModel;
import cab.com.rideversitydriver.Models.RewardsModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.DatePicker;
import cab.com.rideversitydriver.Utils.MySpinner;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;

public class RewardsFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mFromDateTextView;
    private TextView mToDateTextView;
    private Button mApplyFilterButton;
    private MySpinner mCategorySpinner;
    private boolean spinnerTouch = false;
    private LayoutInflater layoutInflater;
    private ArrayList<String> categoryList;
    private RelativeLayout mBackArrowLayout;
    private SharedPref mSharedPref;
    Snackbar snackbar;
    Context context;
    RelativeLayout layoutFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_filter);
        context=this;
        mFromDateTextView = (TextView) findViewById(R.id.fromDate);
        mToDateTextView = (TextView) findViewById(R.id.toDate);
        mApplyFilterButton = (Button) findViewById(R.id.button_filter);
        mCategorySpinner = (MySpinner) findViewById(R.id.spinner_category);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBackArrowLayout = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        categoryList = new ArrayList<>();
        mSharedPref = new SharedPref(this);
        layoutFull=(RelativeLayout) findViewById(R.id.content_rewards_filter);
        mFromDateTextView.setOnClickListener(this);
        mToDateTextView.setOnClickListener(this);
        mApplyFilterButton.setOnClickListener(this);
        mBackArrowLayout.setOnClickListener(this);

        mCategorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(spinnerTouch==false)
                    {
                        spinnerTouch=true;
                        mCategorySpinner.setBackgroundResource(R.drawable.spinner_up);
                    }
                    else
                    {
                        spinnerTouch=false;
                        mCategorySpinner.setBackgroundResource(R.drawable.spinner_down);
                    }
                }
                return false;
            }
        });
        parseSponserCategory();
    }

    private void parseSponserCategory() {
        final CommonAsynTask categoryTask = new CommonAsynTask(this, Constants.SPONSOR_CATEGORY, getData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {

            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        String result = jsonObject.getString(Constants.RESULT);
                        if (result.equals(Constants.SUCCESS)) {
                            JSONArray dataArray = jsonObject.getJSONArray(Constants.DATA);
                            if (dataArray.length() > 0) {
                                for (int i= 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    categoryList.add(dataObject.getString(Constants.CATEGORY));
                                }
                                if (categoryList.size() > 0) {
                                     SponsorSpinnerAdapter adapter = new SponsorSpinnerAdapter(layoutInflater, categoryList);
                                    mCategorySpinner.setAdapter(adapter);
                                    mCategorySpinner.setSelection(0);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        categoryTask.execute();
    }

    private void getRewards(StringBuilder builder) {
        CommonAsynTask getrewardsAsyncTask = new CommonAsynTask(this, Constants.REWARDS_URL, getRewardListData(builder),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    Singleton.getInstance().rewardsArray.clear();
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS))
                        {
                            RewardsModel rewardsmodel = new RewardsModel();
                            rewardsmodel.setMessage(jsonObject.getString(Constants.MESSAGE));
                            rewardsmodel.setResult(jsonObject.getString(Constants.RESULT));
                            JSONArray dataArray = jsonObject.getJSONArray(Constants.DATA);
                            ArrayList<RewardsListModel> rewardsListArray = new ArrayList<RewardsListModel>();
                            if (dataArray != null && dataArray.length() > 0)
                            {
                                for (int i = 0; i < dataArray.length(); i++)
                                {
                                    JSONObject dataObj = dataArray.getJSONObject(i);
                                    RewardsListModel rewardslistmodel=new RewardsListModel();
                                    rewardslistmodel.setPosition(i);
                                    rewardslistmodel.setDiscountId(dataObj.getString(Constants.DISCOUNT_ID));
                                    rewardslistmodel.setSponsorId(dataObj.getString(Constants.SPONSOR_ID));
                                    rewardslistmodel.setCouponCode(dataObj.getString(Constants.COUPON_CODE));
                                    rewardslistmodel.setTitle(dataObj.getString(Constants.TITLE));
                                    rewardslistmodel.setStartDate(dataObj.getString(Constants.START_DATE));
                                    rewardslistmodel.setEndDate(dataObj.getString(Constants.END_DATE));
                                    rewardslistmodel.setDescription(dataObj.getString(Constants.DESCRIPTION));
                                    rewardslistmodel.setDiscountImage(dataObj.getString(Constants.DISCOUNT_IMAGE));
                                    rewardslistmodel.setSponsorName(dataObj.getString(Constants.SPONSOR_NAME));
                                    rewardslistmodel.setEta(dataObj.getString(Constants.ETA));

                                    rewardsListArray.add(rewardslistmodel);
                                }
                                rewardsmodel.setRewardsLists(rewardsListArray);
                            }
                            Singleton.getInstance().rewardsArray.add(rewardsmodel);
                            setResult(100);
                            finish();
                        }
                        else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR))
                        {
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        getrewardsAsyncTask.execute();
    }

    private String getData() {
        JSONObject sponsorData = new JSONObject();
        try {
            sponsorData.putOpt(Constants.USER_ID , mSharedPref.getString("driverId"));
            sponsorData.putOpt(Constants.CATEGORY, Constants.ALL);
            sponsorData.putOpt("expiryDate", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sponsorData.toString();
    }

    private String getRewardListData(StringBuilder builder) {
        JSONObject sponsorData = new JSONObject();
        try {
            sponsorData.putOpt(Constants.CATEGORY, mCategorySpinner.getSelectedItem().toString());
            sponsorData.putOpt("expiryDate", builder.toString());
            sponsorData.putOpt(Constants.USER_ID , mSharedPref.getString("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sponsorData.toString();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fromDate:
                SelectDate(mFromDateTextView);
                break;

            case R.id.toDate:
                Calendar now2 = Calendar.getInstance();
                DatePickerDialog dpd2 = DatePickerDialog.newInstance(new DateListenersSecond(),
                        now2.get(Calendar.YEAR),
                        now2.get(Calendar.MONTH),
                        now2.get(Calendar.DAY_OF_MONTH)
                );
                SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
                Date d2 = new Date();
                dpd2.setMinDate(now2);
                dpd2.vibrate(true);
                dpd2.dismissOnPause(true);
                dpd2.setAccentColor(Color.parseColor("#278455"));
                dpd2.setTitle(sdf2.format(d2));
                dpd2.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.button_filter:
                StringBuilder builder = new StringBuilder();
                if (mFromDateTextView != null && (!mFromDateTextView.getText().toString().equals("Tap to select")))
                {
                    if (mToDateTextView != null && (!mToDateTextView.getText().toString().equals("Tap to select")))
                    {
                        builder.append(mFromDateTextView.getText().toString());
                        builder.append("-");
                        builder.append(mToDateTextView.getText().toString());
                        getRewards(builder);

                    } else {
                        snackbar = Snackbar.make(layoutFull, "Please select date values", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                        snackbar.show();
                    }

                } else {
                    snackbar = Snackbar.make(layoutFull, "Please select date values", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                }
                mCategorySpinner.getSelectedItem().toString();
                break;
            case R.id.imageView_backarrow:
                mSharedPref.setString(Constants.CATEGORY, "success");
                finish();
                break;
        }
    }

    private void SelectDate(TextView textView){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(new DateListeners(),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dpd.setMinDate(now);
        dpd.vibrate(true);
        dpd.dismissOnPause(true);
        dpd.setAccentColor(Color.parseColor("#278455"));
        dpd.setTitle(sdf.format(d));
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public class  DateListeners implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (monthOfYear +1 )+"/"+ (dayOfMonth)+"/"+year;
            mFromDateTextView.setText(date);

            if(dayOfMonth<10)
            {
                if(String.valueOf(dayOfMonth).length()==1)
                {
                    mFromDateTextView.setText(((monthOfYear) + 1)+"/0"+dayOfMonth + "/" + year);
                }
                else
                {
                    mFromDateTextView.setText(((monthOfYear) + 1)+"/"+dayOfMonth + "/" + year);
                }
            }
            else
            {
                mFromDateTextView.setText(((monthOfYear) + 1)+"/"+dayOfMonth + "/" + year);
            }
        }
    }

    public class  DateListenersSecond implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (monthOfYear  +1)+"/"+ (dayOfMonth)+"/"+year;
            mToDateTextView.setText(date);

            if(dayOfMonth<10)
            {
                if(String.valueOf(dayOfMonth).length()==1)
                {
                    mToDateTextView.setText(((monthOfYear) + 1)+"/0"+dayOfMonth + "/" + year);
                }
                else
                {
                    mToDateTextView.setText(((monthOfYear) + 1)+"/"+dayOfMonth + "/" + year);
                }
            }
            else
            {
                mToDateTextView.setText(((monthOfYear) + 1)+"/"+dayOfMonth + "/" + year);
            }
        }
    }


    private void callDatePicker(TextView view) {
        DatePicker dialog = new DatePicker(view);
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "DatePicker");
    }
}
