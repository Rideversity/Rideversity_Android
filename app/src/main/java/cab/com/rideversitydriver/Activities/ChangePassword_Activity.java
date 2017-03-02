package cab.com.rideversitydriver.Activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ForgetModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.MyApplication;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;


/**
 * Created by Kalidoss on 01/08/16.
 */
public class ChangePassword_Activity extends AppCompatActivity implements View.OnClickListener {

    MyApplication application = null;
    TextView txtUpdate, txtForgetpassword;
    RelativeLayout imgBack;
    static EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    static Context context;
    static Snackbar snackbar;
    static SharedPref sharedPref;
    static LinearLayout layoutFull;
    static Dialog forgetDialog;
    TextView txtEmailError;
    EditText email;
    static String strForgetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        context = this;
        sharedPref = new SharedPref(context);
        application = (MyApplication) getApplication();
        layoutFull = (LinearLayout) findViewById(R.id.layout_change_password);
        edtCurrentPassword = (EditText) findViewById(R.id.editText_current_password);
        edtConfirmPassword = (EditText) findViewById(R.id.editText_confirm_password);
        edtNewPassword = (EditText) findViewById(R.id.editText_new_password);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        txtUpdate = (TextView) findViewById(R.id.textView_update);
        txtForgetpassword = (TextView) findViewById(R.id.textview_forget_password);
        imgBack.setOnClickListener(this);
        txtUpdate.setOnClickListener(this);
        edtCurrentPassword.setOnClickListener(this);
        txtForgetpassword.setOnClickListener(this);

        edtCurrentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText_current_password:
                edtCurrentPassword.setCursorVisible(true);
                edtCurrentPassword.setActivated(true);
                edtCurrentPassword.setPressed(true);
                break;
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.textview_forget_password:
                ForgetDialog();
                break;
            case R.id.textView_update:

                String strCurrentPass = edtCurrentPassword.getText().toString();
                String strNewPass = edtNewPassword.getText().toString();
                String strConfirmPass = edtConfirmPassword.getText().toString();

                //Hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (strCurrentPass.isEmpty() || strNewPass.isEmpty() || strConfirmPass.isEmpty()) {
                    snackbar = Snackbar.make(layoutFull, "Please enter all passwords", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setMaxLines(15);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (!strNewPass.equals(strConfirmPass)) {
                    snackbar = Snackbar.make(layoutFull, "New password and confirm password should be same", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setMaxLines(15);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else {
                    if (Utilities.isOnline(context)) {
                        UpdatePassword();
                    } else {
                        Utilities.snackbarNoInternetTwo(context, layoutFull);
                    }
                }


                break;
            default:
                break;
        }
    }

    public void ForgetDialog() {
        forgetDialog = new Dialog(ChangePassword_Activity.this);
        forgetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgetDialog.setContentView(R.layout.forget_password_dialog);

        Button btnOk = (Button) forgetDialog.findViewById(R.id.button_ok_forget);
        txtEmailError = (TextView) forgetDialog.findViewById(R.id.textView_email_error);
        email = (EditText) forgetDialog.findViewById(R.id.editText_email);
        txtEmailError.setVisibility(View.GONE);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strForgetEmail = email.getText().toString().trim();

                if (strForgetEmail.isEmpty()) {
                    txtEmailError.setVisibility(View.VISIBLE);
                    txtEmailError.setText("Please enter your Email Id");
                } else if (!Utilities.isValidMail(strForgetEmail)) {
                    txtEmailError.setVisibility(View.VISIBLE);
                    txtEmailError.setText("Invalid email");
                } else {
                    if (Utilities.isOnline(context)) {
                        txtEmailError.setVisibility(View.GONE);
                        ForgetPassword();
                    } else {
                        Utilities.snackbarNoInternetTwo(context, layoutFull);
                    }
                }
            }
        });

        forgetDialog.show();
    }

    public static void ForgetPassword() {
        CommonAsynTask forgetpasswordAsyncTask = new CommonAsynTask(context, Constants.FORGET_PASSWORD_URL, getForgetData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        Singleton.getInstance().forgetPasswordArray.clear();
                        ForgetModel forgetmodel = new ForgetModel();
                        forgetmodel.setMessage(jsonObject.getString(Constants.MESSAGE));
                        forgetmodel.setResult(jsonObject.getString(Constants.RESULT));
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            forgetDialog.dismiss();
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            forgetDialog.dismiss();
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }

                        Singleton.getInstance().forgetPasswordArray.add(forgetmodel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
        forgetpasswordAsyncTask.execute();
    }

    private static String getForgetData() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.EMAIL, strForgetEmail);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return objData.toString();
    }

    public static void UpdatePassword() {
        CommonAsynTask loginAsyncTask = new CommonAsynTask(context, Constants.CHANGE_PASSWORD_URL, getData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setMaxLines(15);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setMaxLines(15);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        loginAsyncTask.execute();
    }


    private static String getData() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.USER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.CURRENT_PASSWORD, edtCurrentPassword.getText().toString().trim());
            objData.putOpt(Constants.NEW_PASSWORD, edtNewPassword.getText().toString().trim());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return objData.toString();
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
