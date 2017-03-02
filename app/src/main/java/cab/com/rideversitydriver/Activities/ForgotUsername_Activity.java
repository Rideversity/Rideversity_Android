package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ForgetModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.MyApplication;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 05/08/16.
 */
public class ForgotUsername_Activity extends AppCompatActivity implements View.OnClickListener {

    MyApplication application = null;
    TextView txtTermofService, txtErrorEmail;
    static RelativeLayout imgBack, layoutSubmit, layoutFull;
    static Context context;
    static Snackbar snackbar;
    EditText edtEmail;
    static String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_username);
        context = this;
        application = (MyApplication) getApplication();
        txtErrorEmail = (TextView) findViewById(R.id.textView_forgetusername_error_email);
        layoutSubmit = (RelativeLayout) findViewById(R.id.layout_submit_username);
        layoutFull = (RelativeLayout) findViewById(R.id.layout_forgetusername_full);
        edtEmail = (EditText) findViewById(R.id.editText_email_forgetusername);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        imgBack.setOnClickListener(this);
        layoutSubmit.setOnClickListener(this);
        edtEmail.setOnClickListener(this);
        txtErrorEmail.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText_email_forgetusername:

                edtEmail.setCursorVisible(true);
                edtEmail.setActivated(true);
                edtEmail.setPressed(true);
                break;
            case R.id.imageView_backarrow:
                Intent intent = new Intent(ForgotUsername_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.layout_submit_username:
                if (snackbar != null) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                }
                strEmail = edtEmail.getText().toString().trim();
                if (strEmail.isEmpty()) {
                    txtErrorEmail.setVisibility(View.VISIBLE);
                    txtErrorEmail.setText("Please enter your Email Id");
                } else if (!Utilities.isValidMail(strEmail)) {
                    txtErrorEmail.setVisibility(View.VISIBLE);
                    txtErrorEmail.setText("Invalid email");
                } else {
                    if (Utilities.isOnline(context)) {
                        txtErrorEmail.setVisibility(View.GONE);
                        ForgetUsername();
                    } else {
                        Utilities.snackbarNoInternet(context, layoutFull);
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void ForgetUsername() {
        CommonAsynTask forgetUsernameAsyncTask = new CommonAsynTask(context, Constants.FORGET_USERNAME_URL, getEmailData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        Singleton.getInstance().forgetUsernameArray.clear();
                        ForgetModel forgetmodel = new ForgetModel();
                        forgetmodel.setMessage(jsonObject.getString(Constants.MESSAGE));
                        forgetmodel.setResult(jsonObject.getString(Constants.RESULT));
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();

                            JSONObject userObj = jsonObject.getJSONObject(Constants.DATA);
                            forgetmodel.setUserId(userObj.getString(Constants.USER_ID));
                            forgetmodel.setUserName(userObj.getString(Constants.USERNAME));
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }

                        Singleton.getInstance().forgetUsernameArray.add(forgetmodel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (Singleton.getInstance().forgetUsernameArray.get(0).getResult().equals(Constants.SUCCESS)) {
                            snackbar = Snackbar
                                    .make(layoutFull, "" + Singleton.getInstance().forgetUsernameArray.get(0).getMessage(), Snackbar.LENGTH_INDEFINITE)
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
        forgetUsernameAsyncTask.execute();
    }


    private static String getEmailData() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.EMAIL, strEmail);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return objData.toString();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotUsername_Activity.this, Login_Activity.class);
        startActivity(intent);
        finish();
    }
}
