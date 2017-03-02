package cab.com.rideversitydriver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cab.com.rideversitydriver.Activities.ChangeEmail_Activity;
import cab.com.rideversitydriver.Activities.ChangePassword_Activity;
import cab.com.rideversitydriver.Activities.DriverDoc_Activity;
import cab.com.rideversitydriver.Activities.HomeMenu;
import cab.com.rideversitydriver.Activities.MainActivity;
import cab.com.rideversitydriver.Activities.PersonalDetails_Activity;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.PolygonImageView;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Utilities;


public class Profile_Fragment extends Fragment implements View.OnClickListener {

    private Context context;
    SharedPref sharedPref;
    private Button btn_logout;
    private LinearLayout layoutPersonalDetails, layoutChangePassword, layoutChangeEmail, layoutEmergencyContacts, layoutDriverDoc;
    private PolygonImageView imgProfile;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_info_logout, container, false);
        context = this.getActivity();

        sharedPref = new SharedPref(context);
        layoutPersonalDetails = (LinearLayout) rootView.findViewById(R.id.layout_personal_details);
        layoutChangePassword = (LinearLayout) rootView.findViewById(R.id.layout_change_password);
        layoutChangeEmail = (LinearLayout) rootView.findViewById(R.id.layout_change_email);
        //layoutEmergencyContacts = (LinearLayout) rootView.findViewById(R.id.layout_emergency_contacts);
        layoutDriverDoc = (LinearLayout) rootView.findViewById(R.id.layout_driver_doc);
        imgProfile = (PolygonImageView) rootView.findViewById(R.id.imageView_profile);
        btn_logout = (Button) rootView.findViewById(R.id.button_logout);

        imgProfile.setOnClickListener(this);
        layoutPersonalDetails.setOnClickListener(this);
        layoutDriverDoc.setOnClickListener(this);
        layoutChangePassword.setOnClickListener(this);
        layoutChangeEmail.setOnClickListener(this);
        btn_logout.setOnClickListener(this);


        imgProfile.setBackgroundResource(android.R.color.transparent);
        imgProfile.setBackgroundColor(Color.TRANSPARENT);
        imgProfile.setAlpha(127);
        HomeMenu.homeRideFilter.setVisibility(View.GONE);
        HomeMenu.imgRidefilter.setVisibility(View.GONE);

        Log.e("Profile--->", "Profile");

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_personal_details:
                Constants.REG_LOAD = false;
                Intent intent = new Intent(context, PersonalDetails_Activity.class);
                startActivity(intent);
                break;
            case R.id.layout_change_password:
                Intent intentPassword = new Intent(context, ChangePassword_Activity.class);
                startActivity(intentPassword);
                break;
            case R.id.layout_change_email:
                Intent intentEmail = new Intent(context, ChangeEmail_Activity.class);
                startActivity(intentEmail);
                break;
           /* case R.id.layout_emergency_contacts:
                Intent intentEmerency = new Intent(context, EmergencyContacts_Activity.class);
                startActivity(intentEmerency);
                break;*/
            case R.id.layout_driver_doc:
                Constants.REG_LOAD = false;
                Intent intentDriver = new Intent(context, DriverDoc_Activity.class);
                startActivity(intentDriver);
                break;
            case R.id.imageView_profile:
                selectImage();
                break;
            case R.id.button_logout:
                LogOutURL();
                break;
            default:
                break;
        }
    }


    private void LogOutURL() {

        if (Utilities.isOnline(context)) {
            CommonAsynTask logoutTask = new CommonAsynTask(context, Constants.LOGOUT, getData(),
                    Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
                @Override
                public void onTaskCompleted(JSONObject jsonObject) {
                    Log.e("Logout", jsonObject.toString());
                    if (jsonObject != null) {
                        try {
                            String result = jsonObject.getString(Constants.RESULT);
                            String message = jsonObject.getString(Constants.MESSAGE);
                            if (result.equals(Constants.SUCCESS)) {

                                sharedPref.setString("userId", "");
                                Intent intentLog = new Intent(context, MainActivity.class);
                                startActivity(intentLog);
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            logoutTask.execute();
        } else {
            Toast.makeText(context, "" + Constants.NO_CONNECTION, Toast.LENGTH_LONG).show();
        }
    }


    private String getData() {
        JSONObject activeRiderData = new JSONObject();
        SharedPref pref = new SharedPref(context);
        String userId = pref.getString("userId");
        if (!TextUtils.isEmpty(userId)) {
            try {
                activeRiderData.put("driverId", userId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return activeRiderData.toString();
    }

    //dialog onclick imageview
    private void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_choose_image, null);
        Button buttonCamera = (Button) alertLayout.findViewById(R.id.camera);
        Button buttonGallery = (Button) alertLayout.findViewById(R.id.gallery);
        Button cancel = (Button) alertLayout.findViewById(R.id.cancel);
        builder.setView(alertLayout);

        final AlertDialog dialog = builder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_CAMERA);
                dialog.dismiss();

            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECT_FILE);
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                byte[] attachmentBytes = bytes.toByteArray();
                //  imageToBase64String = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgProfile.setImageBitmap(bm);


    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        byte[] attachmentBytes = bytes.toByteArray();
        //imageToBase64String = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgProfile.setImageBitmap(thumbnail);


    }

}
