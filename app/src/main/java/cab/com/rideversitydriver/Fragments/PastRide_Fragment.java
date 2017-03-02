package cab.com.rideversitydriver.Fragments;

/**
 * Created by KEERTHINI on 8/4/2016.
 */

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cab.com.rideversitydriver.Activities.PastRideDetail_Activity;
import cab.com.rideversitydriver.Adapters.PastRide_RecyclerAdapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.PastRideCarImageModel;
import cab.com.rideversitydriver.Models.PastRideListModel;
import cab.com.rideversitydriver.Models.PastRideModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;

public class PastRide_Fragment extends Fragment {

    private Context context;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<String> data;
    public static View.OnClickListener pastRideClickListener;
    private boolean isViewShown = false;
    static Activity activity;
    static SharedPref sharedPref;
    Snackbar snackbar;
    LinearLayout layoutFull;
    View view;


    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart------", "onStart");
        //sharedPref = new SharedPref(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = inflater.inflate(R.layout.fragment_active_ride, container, false);
        context = this.getActivity().getApplicationContext();
        activity = this.getActivity();
        sharedPref = new SharedPref(context);
        pastRideClickListener = new PastRideOnClick(getActivity());
        layoutFull = (LinearLayout) view.findViewById(R.id.layout_activeride);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_active_ride);
        recyclerView.setHasFixedSize(true);
        Constants.HISTORY_FROM = "";
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.e("onCreateView------", "onCreateView");
        if (isViewShown == true) {
            GetPastRide();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("Visible------", "Visible");
            isViewShown = true;
            ActiveRide_Fragment.firstload = true;
            GetPastRide();
        } else {
            Log.e("Nooo", "Nooo");
            isViewShown = false;
        }
    }

    private class PastRideOnClick implements View.OnClickListener {

        private final Context context;

        private PastRideOnClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            moveToPastRide(v);
        }

        private void moveToPastRide(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            Intent intent = new Intent(getActivity(), PastRideDetail_Activity.class);
            intent.putExtra("POSITION", selectedItemPosition);
            startActivity(intent);
        }
    }

    public void GetPastRide() {
        CommonAsynTask getactiverideAsyncTask = new CommonAsynTask(activity, Constants.PAST_RIDE_URL, getPastRide(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.e("jsonObject--->12121", "" + jsonObject);

                if (jsonObject != null) {
                    Singleton.getInstance().pastRideArray.clear();
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            PastRideModel activeridemodel = new PastRideModel();
                            activeridemodel.setMessage(jsonObject.getString(Constants.MESSAGE));
                            activeridemodel.setResult(jsonObject.getString(Constants.RESULT));
                            JSONArray dataArray = jsonObject.getJSONArray(Constants.DATA);
                            Log.e("first--->", "first");
                            ArrayList<PastRideListModel> activerideListArray = new ArrayList<>();
                            if (dataArray != null && dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);
                                    PastRideListModel activeridelistmodel = new PastRideListModel();
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
                                    activeridelistmodel.setDriverName(dataObj.getString(Constants.DRIVER_NAME));
                                    activeridelistmodel.setDriverImage(dataObj.getString(Constants.DRIVER_IMAGE));
                                    activeridelistmodel.setDriverRating(dataObj.getString(Constants.DRIVER_RATING));
                                    activeridelistmodel.setComments(dataObj.getString(Constants.COMMENTS));
                                    activeridelistmodel.setCarName(dataObj.getString(Constants.CAR_NAME));
                                    activeridelistmodel.setPickUpLat(dataObj.getString(Constants.PICKUP_LAT));
                                    activeridelistmodel.setPickUpLong(dataObj.getString(Constants.PICKUP_LONG));
                                    activeridelistmodel.setDestinationLat(dataObj.getString(Constants.DESTINATION_LAT));
                                    activeridelistmodel.setDestinationLong(dataObj.getString(Constants.DESTINATION_LONG));
                                    activeridelistmodel.setRiderName(dataObj.getString(Constants.RIDER_NAME));
                                    activeridelistmodel.setRiderfName(dataObj.getString(Constants.RIDER_FIRST_NAME));
                                    activeridelistmodel.setRiderlName(dataObj.getString(Constants.RIDER_LAST_NAME));
                                    activeridelistmodel.setRiderImage(dataObj.getString(Constants.RIDER_IMAGE));

                                    ArrayList<PastRideCarImageModel> coverPhotoArray = new ArrayList<>();
                                    JSONArray CoverOptions = dataObj.getJSONArray(Constants.CAR_IMAGE);
                                    if (CoverOptions != null && CoverOptions.length() > 0) {
                                        for (int k = 0; k < CoverOptions.length(); k++) {
                                            JSONObject choiseobj = CoverOptions.getJSONObject(k);
                                            PastRideCarImageModel covermodel = new PastRideCarImageModel();
                                            covermodel.setCarImage(choiseobj.getString(Constants.COVER));
                                            Log.e("three--->", "second");
                                            coverPhotoArray.add(covermodel);
                                        }
                                    }
                                    activeridelistmodel.setCarImages(coverPhotoArray);
                                    activerideListArray.add(activeridelistmodel);

                                }
                                activeridemodel.setPastRideListModels(activerideListArray);
                                Singleton.getInstance().pastRideArray.add(activeridemodel);
                            }
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
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

                    Log.e("rewardsArray--->", "" + Singleton.getInstance().pastRideArray.size());

                    if (Singleton.getInstance().pastRideArray.size() > 0) {
                        if (Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().size() > 0) {
                            adapter = new PastRide_RecyclerAdapter(context);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } else {
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        getactiverideAsyncTask.execute();
    }

    private static String getPastRide() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("PastRide", objData.toString());
        return objData.toString();
    }
}

