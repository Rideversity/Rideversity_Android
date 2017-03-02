package cab.com.rideversitydriver.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;

import cab.com.rideversitydriver.Models.LoginModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.MyApplication;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 05/08/16.
 */
public class Login_Activity extends AppCompatActivity implements View.OnClickListener {

    static Context context;
    MyApplication application = null;
    TextView txtTermofService, textViewUsername, textViewPassword, txtErrorUsername, txtErrorPassword;
    Button buttonLogin;
    static RelativeLayout imgBack, layoutFull;
    static Activity activity;
    EditText edtUsername, edtPassword;
    static String strUsername, strPassword;
    static Snackbar snackbar;
    static String Device_ID, Device_Token = "";
    static SharedPref sharedPref;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        activity = this;
        context = MyApplication.getInstance();
        application = (MyApplication) getApplication();
        sharedPref = new SharedPref(context);
        txtErrorUsername = (TextView) findViewById(R.id.textView_error_username);
        txtErrorPassword = (TextView) findViewById(R.id.textView_error_password);
        layoutFull = (RelativeLayout) findViewById(R.id.login_layout);
        edtUsername = (EditText) findViewById(R.id.editText_username);
        edtPassword = (EditText) findViewById(R.id.editText_password);
        textViewUsername = (TextView) findViewById(R.id.textView_forgotUsername);
        textViewPassword = (TextView) findViewById(R.id.textView_forgotPassword);
        buttonLogin = (Button) findViewById(R.id.button_login);
        textViewUsername.setOnClickListener(this);
        textViewPassword.setOnClickListener(this);
        edtUsername.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        txtErrorUsername.setVisibility(View.GONE);
        txtErrorPassword.setVisibility(View.GONE);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        imgBack.setOnClickListener(this);

        Device_Token = FirebaseInstanceId.getInstance().getToken();

        Device_ID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText_username:
                edtUsername.setCursorVisible(true);
                edtUsername.setActivated(true);
                edtUsername.setPressed(true);
                break;
            case R.id.textView_forgotUsername:
                Intent intent = new Intent(activity, ForgotUsername_Activity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.textView_forgotPassword:
                Intent intentPwd = new Intent(activity, ForgetPassword_Activity.class);
                startActivity(intentPwd);
                //finish();
                break;
            case R.id.button_login:

                if (checkAndRequestPermissions()) {

                } else {
                    strUsername = edtUsername.getText().toString().trim();
                    strPassword = edtPassword.getText().toString().trim();

                    if (strUsername.isEmpty() && strPassword.isEmpty()) {
                        txtErrorUsername.setVisibility(View.VISIBLE);
                        txtErrorPassword.setVisibility(View.VISIBLE);
                        txtErrorUsername.setText("Please Enter Username");
                        txtErrorPassword.setText("Please Enter Password");
                    } else if (strUsername.isEmpty()) {
                        txtErrorUsername.setVisibility(View.VISIBLE);
                        txtErrorPassword.setVisibility(View.GONE);
                        txtErrorUsername.setText("Please Enter Username");
                    } else if (strPassword.isEmpty()) {
                        txtErrorUsername.setVisibility(View.GONE);
                        txtErrorPassword.setVisibility(View.VISIBLE);
                        txtErrorPassword.setText("Please Enter Password");
                    } else {
                        if (Utilities.isOnline(context)) {
                            txtErrorUsername.setVisibility(View.GONE);
                            txtErrorPassword.setVisibility(View.GONE);
                            Login();
                        } else {
                            Utilities.snackbarNoInternet(context, layoutFull);
                        }
                    }
                }

                if (snackbar != null) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                }

                break;
            case R.id.imageView_backarrow:
                Intent setIntent = new Intent(activity, MainActivity.class);
                startActivity(setIntent);
                finish();
                break;
            default:
                break;
        }
    }


    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA);
        int callPermission = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE);
        int readPermission = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            //fragment.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Login();
                } else {
                }
                break;
        }
    }


    public static void Login() {
        CommonAsynTask loginAsyncTask = new CommonAsynTask(activity, Constants.LOGIN_URL, getLoginData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        Singleton.getInstance().loginArrayList.clear();
                        LoginModel loginmodel = new LoginModel();
                        loginmodel.setMessage(jsonObject.getString(Constants.MESSAGE));
                        loginmodel.setResult(jsonObject.getString(Constants.RESULT));
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            JSONObject userObj = jsonObject.getJSONObject(Constants.DATA);
                            loginmodel.setPassword(strPassword);
                            loginmodel.setUserId(userObj.getString(Constants.USER_ID));
                            loginmodel.setFullName(userObj.getString(Constants.FIRST_NAME));
                            loginmodel.setUserName(userObj.getString(Constants.USERNAME));
                            loginmodel.setEmail(userObj.getString(Constants.EMAIL));
                            loginmodel.setPhoneNo(userObj.getString(Constants.PHONENO));

                            loginmodel.setGender(userObj.getString(Constants.GENDER));
                            loginmodel.setProfileImage(userObj.getString(Constants.PROFILE_IMAGE));
                            loginmodel.setUserRole(userObj.getString(Constants.USER_ROLE));
                            loginmodel.setAccountType(userObj.getString(Constants.ACCOUNT_TYPE));
                            loginmodel.setTypeOfService(userObj.getString(Constants.TYPE_OFSERVICE));
                            loginmodel.setDeviceId(userObj.getString(Constants.DEVICE_ID));
                            loginmodel.setDeviceType(userObj.getString(Constants.DEVICE_TYPE));
                            loginmodel.setDeviceToken(userObj.getString(Constants.DEVICE_TOKEN));
                            loginmodel.setLadiesOnly(userObj.getString(Constants.LADIES_ONLY));
                            loginmodel.setDriverMode(userObj.getString(Constants.DRIVER_MODE));
                            sharedPref.setString("userId", userObj.getString(Constants.USER_ID));
                            sharedPref.setString("userName", userObj.getString(Constants.FIRST_NAME));
                            sharedPref.setString("userProfileImage", userObj.getString(Constants.PROFILE_IMAGE));
                            sharedPref.setString("LadiesOnly", userObj.getString(Constants.LADIES_ONLY));
                            sharedPref.setString("gender", userObj.getString(Constants.GENDER));
                            sharedPref.setString("password", strPassword);
                            sharedPref.setString("DriverMode", userObj.getString(Constants.DRIVER_MODE));
                            sharedPref.setString("phoneNo", userObj.getString(Constants.PHONENO));
                            Intent intentHome = new Intent(activity, HomeMenu.class);
                            activity.startActivity(intentHome);
                            activity.finish();
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            snackbar = Snackbar
                                    .make(layoutFull, jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_INDEFINITE)
                                    .setAction("DONE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                        }
                                    });
                            // Changing message text color
                            snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.orange));
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }
                        Singleton.getInstance().loginArrayList.add(loginmodel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(activity, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        loginAsyncTask.execute();
    }


    private static String getLoginData() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.USERNAME, strUsername);
            objData.putOpt(Constants.PASSWORD, strPassword);
            objData.putOpt(Constants.DEVICE_ID, Device_ID);
            objData.putOpt(Constants.DEVICE_TYPE, "Android");
            if (Device_Token != null) {
                objData.putOpt(Constants.DEVICE_TOKEN, Device_Token);
            } else {
                objData.putOpt(Constants.DEVICE_TOKEN, "");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return objData.toString();
    }


    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(context, MainActivity.class);
        startActivity(setIntent);
        finish();
    }
}
