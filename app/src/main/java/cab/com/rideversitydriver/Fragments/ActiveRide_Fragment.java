package cab.com.rideversitydriver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cab.com.rideversitydriver.Activities.ActiveRideDetail_Activity;
import cab.com.rideversitydriver.Adapters.ActiveRide_RecyclerAdapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ActiveRideListModel;
import cab.com.rideversitydriver.Models.ActiveRideModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;


/**
 * Created by KEERTHINI on 7/28/2016.
 */

public class ActiveRide_Fragment extends Fragment {

    private Context context;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<String> data;
    public static View.OnClickListener activeRideClickListener;
    static SharedPref sharedPref;
    Snackbar snackbar;
    public static Activity activity;
    LinearLayout layoutFull;
    View view;
    private boolean isViewShown = false;
    public static boolean firstload = false;

    private String mDriverId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = inflater.inflate(R.layout.fragment_active_ride, container, false);
        context = this.getActivity().getApplicationContext();
        activity = this.getActivity();
        sharedPref = new SharedPref(context);
        layoutFull = (LinearLayout) view.findViewById(R.id.layout_activeride);
        activeRideClickListener = new ActiverideOnClick(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_active_ride);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (firstload == false) {
            GetActiveRide();
        }
        return view;
    }

    private class ActiverideOnClick implements View.OnClickListener {

        private final Context context;

        private ActiverideOnClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            moveToActiveRide(v);
        }

        private void moveToActiveRide(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            Intent intent = new Intent(getActivity(), ActiveRideDetail_Activity.class);
            intent.putExtra("POSITION", selectedItemPosition);
            startActivity(intent);
        }
    }


    @Override
    public void onResume() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onResume();
        GetActiveRide();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (firstload == true) {
                GetActiveRide();
            }
            isViewShown = true;
        } else {
            isViewShown = false;
        }
    }

    public void GetActiveRide() {
        CommonAsynTask getactiverideAsyncTask = new CommonAsynTask(activity, Constants.ACTIVE_RIDE_URL, getActiveRide(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    Singleton.getInstance().activeRideArray.clear();
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            ActiveRideModel activeridemodel = new ActiveRideModel();
                            activeridemodel.setMessage(jsonObject.getString(Constants.MESSAGE));
                            activeridemodel.setResult(jsonObject.getString(Constants.RESULT));
                            JSONArray dataArray = jsonObject.getJSONArray(Constants.DATA);
                            ArrayList<ActiveRideListModel> activerideListArray = new ArrayList<ActiveRideListModel>();
                            if (dataArray != null && dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);
                                    ActiveRideListModel activeridelistmodel = new ActiveRideListModel();

                                    activeridelistmodel.setRiderName(dataObj.getString(Constants.RIDER_FULL_NAME));

                                    activeridelistmodel.setriderfName(dataObj.getString("riderfName"));
                                    activeridelistmodel.setriderlName(dataObj.getString("riderlName"));
                                    activeridelistmodel.setRiderName(dataObj.getString(Constants.RIDER_FULL_NAME));
//                                    activeridelistmodel.setriderfName(dataObj.getString("riderfName"));
//                                    activeridelistmodel.setriderfName(dataObj.getString(Constants.RIDER_FIRST_NAME));
//                                    activeridelistmodel.setriderlName(dataObj.getString(Constants.RIDER_LAST_NAME));
                                    activeridelistmodel.setRideId(dataObj.getString(Constants.RIDE_ID));
                                    activeridelistmodel.setRiderId(dataObj.getString(Constants.RIDER_ID));
                                    activeridelistmodel.setDriverId(dataObj.getString(Constants.DRIVER_ID));
                                    activeridelistmodel.setRideTime(dataObj.getString(Constants.RIDE_TIME));
                                    activeridelistmodel.setRideType(dataObj.getString(Constants.RIDE_TYPE));
                                    activeridelistmodel.setRideTypeId(dataObj.getString(Constants.RIDE_TYPE_ID));
                                    activeridelistmodel.setRideDate(dataObj.getString(Constants.RIDE_DATE));
                                    activeridelistmodel.setPickupLocation(dataObj.getString(Constants.PICKUP_LOCATION));
                                    activeridelistmodel.setDestination(dataObj.getString(Constants.DESTINATION));
                                    activeridelistmodel.setSeatNo(dataObj.getString(Constants.SEAT_NO));
                                    activeridelistmodel.setTripType(dataObj.getString(Constants.TRIP_TYPE));
                                    activeridelistmodel.setDonation(dataObj.getString(Constants.DONATION));
                                    activeridelistmodel.setLadiesOnly(dataObj.getString(Constants.LADIES_ONLY));
                                    activeridelistmodel.setDiscountId(dataObj.getString(Constants.DISCOUNT_ID));
                                    activeridelistmodel.setRiderName(dataObj.getString(Constants.RIDER_NAME));
                                    activeridelistmodel.setRiderImage(dataObj.getString(Constants.RIDER_IMAGE));
                                    activeridelistmodel.setComments(dataObj.getString(Constants.COMMENTS));
                                    activeridelistmodel.setPickUpLat(dataObj.getString(Constants.PICK_UP_LAT));
                                    activeridelistmodel.setPickUpLong(dataObj.getString(Constants.PICK_UP_LONG));
                                    activeridelistmodel.setDestinationLat(dataObj.getString(Constants.DESTINATION_LAT));
                                    activeridelistmodel.setDestinationLong(dataObj.getString(Constants.DESTINATION_LONG));
                                    activeridelistmodel.setCarpoolId(dataObj.getString(Constants.CARPOOL_ID));

                                    activerideListArray.add(activeridelistmodel);
                                }
                                activeridemodel.setActiverideLists(activerideListArray);
                            }


                            Singleton.getInstance().activeRideArray.add(activeridemodel);
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            if (snackbar != null) {
                                snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                                snackbar.show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Singleton.getInstance().activeRideArray.size() > 0) {
                        if (Singleton.getInstance().activeRideArray.get(0).getActiverideLists().size() > 0) {
                            adapter = new ActiveRide_RecyclerAdapter(context);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } else {
                }
            }
        });
        getactiverideAsyncTask.execute();

    }

    private String getActiveRide() {
        JSONObject objData = new JSONObject();
        SharedPref pref = new SharedPref(activity);
        String userId = pref.getString("userId");
        try {
            objData.putOpt(Constants.DRIVER_ID, userId);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objData.toString();
    }
}
