package cab.com.rideversitydriver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cab.com.rideversitydriver.Activities.HomeMenu;
import cab.com.rideversitydriver.Activities.RewardsFilterActivity;
import cab.com.rideversitydriver.Adapters.Rewards_RecyclerAdapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.RewardsListModel;
import cab.com.rideversitydriver.Models.RewardsModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 03/08/16.
 */
public class Rewards_Fragment extends Fragment {

    private Context context;
    static Activity activity;
    // private static RecyclerView.Adapter adapter;
    Rewards_RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<String> data;
    public static View.OnClickListener rewardClickListener;
    String strMessage, strResult;
    Snackbar snackbar;
    LinearLayout layoutMain;
    View rootView;
    EditText edtSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.rewards, container, false);
        context = this.getActivity();
        activity = this.getActivity();
        rewardClickListener = new RewardsOnClick(getActivity());
        edtSearch = (EditText) rootView.findViewById(R.id.search_rewards);
        layoutMain = (LinearLayout) rootView.findViewById(R.id.rewards_mainLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_reward);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (Utilities.isOnline(context)) {

            GetRewards();

        } else {
            Toast.makeText(context, Constants.NO_CONNECTION, Toast.LENGTH_SHORT).show();
        }


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filters(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        HomeMenu.imgRidefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rewardsFilterIntent = new Intent(getActivity(), RewardsFilterActivity.class);
                startActivityForResult(rewardsFilterIntent, 100);
            }
        });


        return rootView;
    }

    private class RewardsOnClick implements View.OnClickListener {

        private final Context context;

        private RewardsOnClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

        }
    }


    @Override
    public void onResume() {
        Log.e("DEBUG--->", "onResume of HomeFragment");
        super.onResume();

    }

    public void GetRewards() {
        CommonAsynTask getrewardsAsyncTask = new CommonAsynTask(context, Constants.REWARDS_URL, getData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.e("jsonObject--->", "" + jsonObject);

                if (jsonObject != null) {
                    Singleton.getInstance().rewardsArray.clear();
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            RewardsModel rewardsmodel = new RewardsModel();
                            rewardsmodel.setMessage(jsonObject.getString(Constants.MESSAGE));
                            rewardsmodel.setResult(jsonObject.getString(Constants.RESULT));
                            JSONArray dataArray = jsonObject.getJSONArray(Constants.DATA);
                            Log.e("first--->", "first");
                            ArrayList<RewardsListModel> rewardsListArray = new ArrayList<RewardsListModel>();
                            if (dataArray != null && dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);
                                    RewardsListModel rewardslistmodel = new RewardsListModel();
                                    rewardslistmodel.setPosition(i);
                                    rewardslistmodel.setDiscountId(dataObj.getString(Constants.DISCOUNT_ID));
                                    rewardslistmodel.setSponsorId(dataObj.getString(Constants.SPONSOR_ID));
                                    rewardslistmodel.setCouponCode(dataObj.getString(Constants.COUPON_CODE));
                                    rewardslistmodel.setTitle(dataObj.getString(Constants.TITLE));
                                    rewardslistmodel.setEta(dataObj.getString(Constants.ETA));
                                    rewardslistmodel.setStartDate(dataObj.getString(Constants.START_DATE));
                                    rewardslistmodel.setEndDate(dataObj.getString(Constants.END_DATE));
                                    rewardslistmodel.setLocation(dataObj.getString(Constants.LOCATION));
                                    rewardslistmodel.setLocation_lat(dataObj.getString(Constants.LOCATION_LAT));
                                    rewardslistmodel.setLocation_long(dataObj.getString(Constants.LOCATION_LONG));
                                    rewardslistmodel.setDescription(dataObj.getString(Constants.DESCRIPTION));
                                    rewardslistmodel.setDiscountImage(dataObj.getString(Constants.DISCOUNT_IMAGE));
                                    rewardslistmodel.setSponsorName(dataObj.getString(Constants.SPONSOR_NAME));
                                    rewardsListArray.add(rewardslistmodel);
                                }
                                rewardsmodel.setRewardsLists(rewardsListArray);
                            }
                            Singleton.getInstance().rewardsArray.add(rewardsmodel);

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            snackbar = Snackbar.make(layoutMain, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("rewardsArray--->", "" + Singleton.getInstance().rewardsArray.size());

                    if (Singleton.getInstance().rewardsArray.size() > 0) {
                        if (Singleton.getInstance().rewardsArray.get(0).getRewardsLists().size() > 0) {
                            adapter = new Rewards_RecyclerAdapter(context, Singleton.getInstance().rewardsArray.get(0).getRewardsLists());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } else {
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        getrewardsAsyncTask.execute();
    }

    private String getData() {
        JSONObject sponsorData = new JSONObject();
        try {
            SharedPref sharedPref = new SharedPref(context);
            sponsorData.putOpt(Constants.USER_ID, sharedPref.getString(Constants.USER_ID));
            sponsorData.putOpt(Constants.CATEGORY, Constants.ALL);
            sponsorData.putOpt("expiryDate", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sponsorData.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Singleton.getInstance().rewardsArray.size() > 0) {
            if (Singleton.getInstance().rewardsArray.get(0).getRewardsLists() != null) {

                if (Singleton.getInstance().rewardsArray.get(0).getRewardsLists().size() > 0) {
                    adapter = new Rewards_RecyclerAdapter(context, Singleton.getInstance().rewardsArray.get(0).getRewardsLists());
                    recyclerView.setAdapter(adapter);
                }
            }
        }
    }
}