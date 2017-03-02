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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cab.com.rideversitydriver.Activities.CarPoolCreate_Activity;
import cab.com.rideversitydriver.Activities.CarPoolDetail_Activity;
import cab.com.rideversitydriver.Activities.HomeMenu;
import cab.com.rideversitydriver.Adapters.CarPool_RecyclerAdapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.CarpoolListModel;
import cab.com.rideversitydriver.Models.CarpoolModel;
import cab.com.rideversitydriver.Models.CoverPhotoModel;
import cab.com.rideversitydriver.Models.InvitedRiderModel;
import cab.com.rideversitydriver.Models.JoinedRiderModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 03/08/16.
 */


public class Carpool_Fragment extends Fragment {

    private Context context;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static View.OnClickListener carPoolClickListener;
    Snackbar snackbar;
    LinearLayout layoutFull;
    private static SharedPref sharedPref;
    public static boolean isCarpoolAdded = false;
    static Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.carpool, container, false);
        context = this.getActivity();
        activity = this.getActivity();
        sharedPref = new SharedPref(context);
        carPoolClickListener = new Carpool_Fragment.CarPoolOnClick(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_active_ride);
        recyclerView.setHasFixedSize(true);
        layoutFull = (LinearLayout) view.findViewById(R.id.carpool_layout);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (Utilities.isOnline(context)) {
            GetCarpools();
        } else {
            snackbar = Snackbar.make(layoutFull, Constants.NO_CONNECTION, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            snackbar.show();
        }

        HomeMenu.imgCreateCarPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCarpoolAdded = false;
                Intent intent = new Intent(activity, CarPoolCreate_Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private class CarPoolOnClick implements View.OnClickListener {

        private final Context context;

        private CarPoolOnClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            moveToCarPoolRide(v);
        }

        private void moveToCarPoolRide(View v) {
            Intent intent = new Intent(getActivity(), CarPoolDetail_Activity.class);
            startActivity(intent);

        }
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();
        if (isCarpoolAdded == true) {
            if (Utilities.isOnline(context)) {
                GetCarpools();
            }
        }
    }

    public void GetCarpools() {
        JSONObject jsonReqData = new JSONObject();
        try {
            jsonReqData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonAsynTask getcarpoolsAsyncTask = new CommonAsynTask(context, Constants.CARPOOL_URL, jsonReqData.toString(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.e("jsonObject--->", "" + jsonObject);

                if (jsonObject != null) {
                    Singleton.getInstance().carpoolArray.clear();
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            CarpoolModel model = new CarpoolModel();
                            model.setMessage(jsonObject.getString(Constants.MESSAGE));
                            model.setResult(jsonObject.getString(Constants.RESULT));
                            JSONArray dataArray = jsonObject.getJSONArray(Constants.DATA);
                            Log.e("first--->", "first");
                            ArrayList<CarpoolListModel> carpoolListArray = new ArrayList<CarpoolListModel>();
                            if (dataArray != null && dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);

                                    CarpoolListModel carpoollistmodel = new CarpoolListModel();
                                    carpoollistmodel.setPosition(i);
                                    carpoollistmodel.setCarpoolId(dataObj.getString(Constants.CARPOOL_ID));
                                    carpoollistmodel.setDriverId(dataObj.getString(Constants.DRIVER_ID));
                                    carpoollistmodel.setRideTime(dataObj.getString(Constants.RIDE_TIME));
                                    carpoollistmodel.setRideDate(dataObj.getString(Constants.RIDE_DATE));
                                    carpoollistmodel.setPickupLocation(dataObj.getString(Constants.PICKUP_LOCATION));
                                    carpoollistmodel.setDestination(dataObj.getString(Constants.DESTINATION));
                                    carpoollistmodel.setSeatNo(dataObj.getString(Constants.SEAT_NO));
                                    carpoollistmodel.setSeatAvailable(dataObj.getString(Constants.SEAT_AVALIABLE));
                                    carpoollistmodel.setTripType(dataObj.getString(Constants.TRIP_TYPE));
                                    carpoollistmodel.setDonation(dataObj.getString(Constants.DONATION));
                                    carpoollistmodel.setLadiesOnly(dataObj.getString(Constants.LADIES_ONLY));
                                    carpoollistmodel.setDiscountId(dataObj.getString(Constants.DISCOUNT_ID));
                                    carpoollistmodel.setDriverFirstName(dataObj.getString(Constants.DRIVER_FIRSTNAME));
                                    carpoollistmodel.setDriverImage(dataObj.getString(Constants.DRIVER_IMAGE));
                                    carpoollistmodel.setDriverRating(dataObj.getString(Constants.DRIVER_RATING));
                                    carpoollistmodel.setCarName(dataObj.getString(Constants.CAR_NAME));
                                    carpoollistmodel.setComments(dataObj.getString(Constants.COMMENTS));
                                    carpoollistmodel.setPickupLat(dataObj.getString(Constants.PICKUP_LAT));
                                    carpoollistmodel.setPickupLong(dataObj.getString(Constants.PICKUP_LONG));
                                    carpoollistmodel.setDestinationLat(dataObj.getString(Constants.DESTINATION_LAT));
                                    carpoollistmodel.setDestinationLong(dataObj.getString(Constants.DESTINATION_LONG));

                                    ArrayList<CoverPhotoModel> coverPhotoArray = new ArrayList<CoverPhotoModel>();
                                    JSONArray CoverOptions = dataObj.getJSONArray(Constants.CAR_IMAGE);
                                    if (CoverOptions != null && CoverOptions.length() > 0) {
                                        for (int k = 0; k < CoverOptions.length(); k++) {
                                            JSONObject choiseobj = CoverOptions.getJSONObject(k);
                                            CoverPhotoModel covermodel = new CoverPhotoModel();
                                            covermodel.setCover(choiseobj.getString(Constants.COVER));
                                            Log.e("three--->", "second");
                                            coverPhotoArray.add(covermodel);
                                        }
                                    }
                                    carpoollistmodel.setCoverPhotoLists(coverPhotoArray);

                                    ArrayList<InvitedRiderModel> invitedriderArray = new ArrayList<InvitedRiderModel>();
                                    JSONArray invitedrider = dataObj.getJSONArray(Constants.INVITED_RIDER);
                                    if (invitedrider != null && invitedrider.length() > 0) {
                                        for (int l = 0; l < invitedrider.length(); l++) {
                                            JSONObject invitedobj = invitedrider.getJSONObject(l);
                                            InvitedRiderModel ridermodel = new InvitedRiderModel();
                                            ridermodel.setName(invitedobj.getString(Constants.NAME));
                                            ridermodel.setUserId(invitedobj.getString(Constants.USER_ID));
                                            Log.e("three--->", "second");
                                            invitedriderArray.add(ridermodel);
                                        }
                                    }
                                    carpoollistmodel.setInvitedriderLists(invitedriderArray);

                                    ArrayList<JoinedRiderModel> joinedriderArray = new ArrayList<JoinedRiderModel>();
                                    JSONArray joinedrider = dataObj.getJSONArray(Constants.JOINED_RIDER);
                                    if (joinedrider != null && joinedrider.length() > 0) {
                                        for (int l = 0; l < joinedrider.length(); l++) {
                                            JSONObject joinedobj = joinedrider.getJSONObject(l);
                                            JoinedRiderModel joinedridermodel = new JoinedRiderModel();
                                            joinedridermodel.setName(joinedobj.getString(Constants.NAME));
                                            joinedridermodel.setUserId(joinedobj.getString(Constants.USER_ID));
                                            joinedridermodel.setRiderImage(joinedobj.getString(Constants.RIDER_IMAGE));
                                            //Log.e("three--->", "second");
                                            joinedriderArray.add(joinedridermodel);
                                        }
                                    }
                                    carpoollistmodel.setJoinedriderLists(joinedriderArray);
                                    carpoolListArray.add(carpoollistmodel);
                                }
                                model.setCarpoolLists(carpoolListArray);
                            }
                            Singleton.getInstance().carpoolArray.add(model);

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

                    if (Singleton.getInstance().carpoolArray.size() > 0) {
                        if (Singleton.getInstance().carpoolArray.get(0).getCarpoolLists().size() > 0) {
                            adapter = new CarPool_RecyclerAdapter(context, Singleton.getInstance().carpoolArray.get(0).getCarpoolLists());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } else {
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        getcarpoolsAsyncTask.execute();
    }
}

