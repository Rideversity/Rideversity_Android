package cab.com.rideversitydriver.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ReportModel;
import cab.com.rideversitydriver.Models.ReportReasonModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.RoundedImageView;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.MySpinner;
import cab.com.rideversitydriver.Utils.PathJSONParser;
import cab.com.rideversitydriver.Utils.ServiceHelper;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

import static cab.com.rideversitydriver.Activities.RewardsDetail_Activity.Position;


/**
 * Created by KEERTHINI on 8/1/2016.
 */

public class PastRideDetail_Activity extends FragmentActivity implements View.OnClickListener {
    private TextView mDateTimeTextView;
    private TextView mPickUpLocationTextView;
    private TextView mDestinationTextView;
    private TextView mNoSeatsTextView;
    private TextView mRideTypeTextView;
    private TextView mDonationTextView;
    private TextView mCommentsTextView;
    private TextView mReportRider;
    private ImageView mRiderImage;
    private TextView mRiderName;
    private TextView txtCommentsError;
    static TextView edtComments;
    Dialog report_dialog;
    private RelativeLayout layoutReport;
    private RoundedImageView roundedImageView;
    private ImageView imgClose;
    private Button submit_report;
    MySpinner spinnerReason;
    ArrayAdapter<ReportReasonModel> adapter;
    static String strReportReson;
    private boolean spinnerTouch = false;
    private int iCurrentSelection;
    static SharedPref sharedPref;
    static String Message;
    Snackbar snackbar;
    LinearLayout layoutFull;


    private SupportMapFragment mMapFragment;
    private LatLng mPickUpLatLng;
    private LatLng mDestinationLatLng;
    private GoogleMap mGoogleMap;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_ride_detail);
        RelativeLayout backArrow = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        backArrow.setOnClickListener(this);
        context = this;

        mDateTimeTextView = (TextView) findViewById(R.id.date_time);
        mPickUpLocationTextView = (TextView) findViewById(R.id.past_pick_up_location);
        mDestinationTextView = (TextView) findViewById(R.id.past_destination);
        mNoSeatsTextView = (TextView) findViewById(R.id.past_no_seats);
        mRideTypeTextView = (TextView) findViewById(R.id.past_ride_type);
        mDonationTextView = (TextView) findViewById(R.id.past_suggested_donation);
        mCommentsTextView = (TextView) findViewById(R.id.past_comments);
        mReportRider = (TextView) findViewById(R.id.report_Rider);
        mRiderName = (TextView) findViewById(R.id.textView_RriderName);
        mRiderImage = (ImageView) findViewById(R.id.imageView_Rider);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_past_ride);
        mReportRider.setOnClickListener(this);
        mReportRider.setVisibility(View.VISIBLE);
        txtCommentsError = (TextView) findViewById(R.id.textview_error);
        edtComments = (TextView) findViewById(R.id.edittext_comments);
        layoutReport = (RelativeLayout) findViewById(R.id.layout_report_driver);


        getReportLists();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            int position = intent.getIntExtra("POSITION", -1);
            if (position != -1) {
                if (Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().size() > 0) {
                    mDateTimeTextView.setText(
                            Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getRideDate() + "/" +
                                    Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getRideTime());
                    mPickUpLocationTextView.setText(
                            Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getPickupLocation());
                    mDestinationTextView.setText(
                            Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getDestination());
                    mNoSeatsTextView.setText(
                            Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getSeatNo());
                    mRideTypeTextView.setText(
                            Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getRideType());
                    mDonationTextView.setText("$ " +
                            Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getDonation());
                    mCommentsTextView.setText(
                            Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getComments());
                    mRiderName.setText(
                            Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getRiderfName() + " " +
                                    Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getRiderlName());


                    if (Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getRiderImage() != null) {
                        String strURL = Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getRiderImage();

                        if (strURL.equals("")) {
                            mRiderImage.setImageResource(R.drawable.no_image);
                        } else
                            Picasso.with(context)
                                    .load(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(position).getRiderImage())
                                    .error(getResources().getDrawable(R.drawable.no_image))
                                    .into(mRiderImage);
                    } else {
                        mRiderImage.setImageResource(R.drawable.no_image);
                    }


                    if (mMapFragment != null) {
                        mMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                if (Utilities.isOnline(PastRideDetail_Activity.this)) {
                                    loadMap(googleMap);
                                }
                            }
                        });
                    }

                }
            }
        }
    }

    private void loadMap(GoogleMap googleMap) {

        mPickUpLatLng = new LatLng(Double.parseDouble(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getPickUpLat()), Double.parseDouble(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getPickUpLong()));
        mDestinationLatLng = new LatLng(Double.parseDouble(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getDestinationLat()), Double.parseDouble(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getDestinationLong()));

        MarkerOptions options = new MarkerOptions();
        options.position(mPickUpLatLng);
        options.position(mDestinationLatLng);
        mGoogleMap = googleMap;
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
        addMarkers();
    }

    private String getMapsApiDirectionsUrl() {

        String waypoints = "waypoints=optimize:true|"
                + mPickUpLatLng.latitude + "," + mPickUpLatLng.longitude
                + "|" + "|" + mDestinationLatLng.latitude + ","
                + mDestinationLatLng.longitude;
        String OriDest = "origin=" + mPickUpLatLng.latitude + "," + mPickUpLatLng.longitude + "&destination=" + mDestinationLatLng.latitude + "," + mDestinationLatLng.longitude;

        String sensor = "sensor=false";
        String params = OriDest + "&%20" + waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = new ServiceHelper(PastRideDetail_Activity.this).readUrl(url[0]);
            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            if (routes != null) {
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(5);
                    polyLineOptions.color(Color.BLUE);
                }

                if (polyLineOptions != null) {
                    mGoogleMap.addPolyline(polyLineOptions);
                }
                // googleMap.addPolyline(polyLineOptions);
            }

        }
    }

    private void addMarkers() {
        if (mGoogleMap != null) {

            Bitmap destination_icon = BitmapFactory.decodeResource(getResources(), R.drawable.orange_map_pin);
            Bitmap pickup_icon = BitmapFactory.decodeResource(getResources(), R.drawable.green_map_pin);
            int height = 70;
            int width = 60;
            Bitmap PickupMarker = Bitmap.createScaledBitmap(pickup_icon, width, height, false);
            Bitmap DestinationMarker = Bitmap.createScaledBitmap(destination_icon, width, height, false);
            // googleMap.clear();
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPickUpLatLng, 16));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(PickupMarker)).position(mPickUpLatLng)
                    .title("Source"));
            mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(DestinationMarker)).position(mDestinationLatLng)
                    .title("Destination"));

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_Rider:
                ReportDriver_Dialog();
               /* ReportDriver_Dialog dialog = new ReportDriver_Dialog();
                dialog.setContext(PastRideDetail_Activity.this);
                dialog.show(getSupportFragmentManager(), "reportfrag");*/
                break;
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.submit_report:

                if (!edtComments.getText().toString().isEmpty()) {
                    txtCommentsError.setVisibility(View.GONE);
                    submitReport();
                } else {
                    txtCommentsError.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.close:
                if (report_dialog.isShowing()) {
                    report_dialog.dismiss();
                }
                break;
            default:
                break;
        }

    }

    public void ReportDriver_Dialog() {
        report_dialog = new Dialog(PastRideDetail_Activity.this);
        report_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        report_dialog.setContentView(R.layout.dialog_report_rider);
        layoutReport = (RelativeLayout) report_dialog.findViewById(R.id.layout_report_driver);

        txtCommentsError = (TextView) report_dialog.findViewById(R.id.textview_error);
        TextView txtDriver = (TextView) report_dialog.findViewById(R.id.textview_report_driver_name);
        imgClose = (ImageView) report_dialog.findViewById(R.id.close);
        RoundedImageView imgReportDriver = (RoundedImageView) report_dialog.findViewById(R.id.imageView_report_driver);
        submit_report = (Button) report_dialog.findViewById(R.id.submit_report);
        spinnerReason = (MySpinner) report_dialog.findViewById(R.id.reason_spinner);
        edtComments = (EditText) report_dialog.findViewById(R.id.edittext_comments);
        submit_report.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        txtCommentsError.setVisibility(View.GONE);

        if (!Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getDriverName().isEmpty()) {
            txtDriver.setText("Rider: " + Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getRiderfName() + " " + Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getRiderlName());
        } else {
            txtDriver.setText("-");
        }

        if (!Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getRiderImage().isEmpty()) {
            String strImage = Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getRiderImage();
            if (strImage.equals("")) {
                imgReportDriver.setImageResource(R.drawable.no_image);
            } else {
                Picasso.with(context).load(Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getRiderImage()).fit().centerCrop()
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(imgReportDriver);
            }

        } else {
            imgReportDriver.setImageResource(R.drawable.no_image);
        }

        //Log.e("getReportResonArray",""+Singleton.getInstance().reportReasonArray.get(0).getReportResonArray().size());

        if (Singleton.getInstance().reportReasonArray.get(0).getReportResonArray().size() > 0) {

            adapter = new ArrayAdapter<ReportReasonModel>(context, R.layout.spinner_row, Singleton.getInstance().reportReasonArray.get(0).getReportResonArray()) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    ((TextView) v).setTextColor(ContextCompat.getColor(context, R.color.orange));
                    return v;
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    return v;
                }
            };
            adapter.setDropDownViewResource(R.layout.spinner_orange_textview);
            spinnerReason.setAdapter(adapter);
            strReportReson = Singleton.getInstance().reportReasonArray.get(0).getReportResonArray().get(0).getReportReason();
        }


        spinnerReason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (spinnerTouch == false) {
                        spinnerTouch = true;
                        spinnerReason.setBackgroundResource(R.drawable.spinner_up_orange);
                    } else {
                        spinnerTouch = false;
                        spinnerReason.setBackgroundResource(R.drawable.spinner_dropdown_orange);
                    }
                }
                return false;
            }
        });

        iCurrentSelection = 0;
        spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (iCurrentSelection != position) {
                    spinnerTouch = false;
                    spinnerReason.setBackgroundResource(R.drawable.spinner_dropdown_orange);
                    strReportReson = Singleton.getInstance().reportReasonArray.get(0).getReportResonArray().get(position).getReportReason();
                } else {
                    spinnerTouch = false;
                    spinnerReason.setBackgroundResource(R.drawable.spinner_dropdown_orange);
                    //Log.e("Same Position","Same Position");
                }
                iCurrentSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerReason.setBackgroundResource(R.drawable.spinner_up_orange);
                spinnerTouch = false;
            }
        });
        report_dialog.show();
    }

    private void getReportLists() {

        CommonAsynTask reportReasonAssynTask = new CommonAsynTask(PastRideDetail_Activity.this, Constants.REPORT_REASON_LIST, "",
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        if (jsonObject.has(Constants.RESULT)) {
                            if (jsonObject.optString(Constants.RESULT).equals(Constants.SUCCESS)) {
                                ReportModel reportModel = new ReportModel();
                                reportModel.setMessage(jsonObject.optString(Constants.MESSAGE));
                                reportModel.setResult(jsonObject.optString(Constants.RESULT));

                                JSONArray dataArray = jsonObject.getJSONArray(Constants.DATA);
                                ArrayList<ReportReasonModel> reasonsArray = new ArrayList<ReportReasonModel>();
                                if (dataArray != null && dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        ReportReasonModel model = new ReportReasonModel();
                                        JSONObject dataObj = dataArray.getJSONObject(i);
                                        model.setReportReason(dataObj.optString(Constants.REPORT_REASON));
                                        reasonsArray.add(model);
                                    }
                                }
                                reportModel.setReportResonArray(reasonsArray);
                                Singleton.getInstance().reportReasonArray.clear();
                                Singleton.getInstance().reportReasonArray.add(reportModel);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        reportReasonAssynTask.execute();
    }

    private void submitReport() {
        CommonAsynTask sunmitreportAssynTask = new CommonAsynTask(PastRideDetail_Activity.this, Constants.REPORT_RIDER, getReportDetails(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        if (jsonObject.has(Constants.RESULT)) {
                            if (jsonObject.optString(Constants.RESULT).equals(Constants.SUCCESS)) {
                                Message = jsonObject.optString(Constants.MESSAGE);
                                if (report_dialog.isShowing()) {
                                    report_dialog.dismiss();
                                }
                                snackbar = Snackbar.make(layoutFull, Message, Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                                snackbar.show();
                            } else if (jsonObject.optString(Constants.RESULT).equals(Constants.ERROR)) {
                                snackbar = Snackbar.make(layoutReport, "" + jsonObject.optString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                                snackbar.show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sunmitreportAssynTask.execute();
    }

    private static String getReportDetails() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.RIDE_ID, Singleton.getInstance().pastRideArray.get(0).getPastRideListModels().get(Position).getRideId());
            objData.putOpt(Constants.REPORT_CAT, strReportReson);
            objData.putOpt(Constants.REPORT_REPORT, edtComments.getText().toString().trim());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return objData.toString();
    }


}