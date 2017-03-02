package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.MyApplication;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Utilities;


/**
 * Created by Kalidoss on 01/08/16.
 */
public class ChangeEmail_Activity extends AppCompatActivity implements View.OnClickListener {

    MyApplication application = null;
    TextView txtUpdate;
    RelativeLayout imgBack;
    static EditText edtCurrentEmail, edtNewEmail;
    static Context context;
    static Snackbar snackbar;
    static SharedPref sharedPref;
    static LinearLayout layoutFull;
    String strCurrentEmail, strNewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);
        context = this;
        sharedPref = new SharedPref(context);
        application = (MyApplication) getApplication();
        layoutFull = (LinearLayout) findViewById(R.id.layout_change_email);
        edtCurrentEmail = (EditText) findViewById(R.id.editText_current_email);
        edtNewEmail = (EditText) findViewById(R.id.editText_new_email);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        txtUpdate = (TextView) findViewById(R.id.textView_update);
        imgBack.setOnClickListener(this);
        txtUpdate.setOnClickListener(this);
        edtCurrentEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText_current_email:
                edtCurrentEmail.setCursorVisible(true);
                edtCurrentEmail.setActivated(true);
                edtCurrentEmail.setPressed(true);
                break;
            case R.id.imageView_backarrow:
                finish();
                break;

            case R.id.textView_update:

                strCurrentEmail = edtCurrentEmail.getText().toString().trim();
                strNewEmail = edtCurrentEmail.getText().toString().trim();

                if (Utilities.isOnline(context)) {
                    UpdateEmail();
                } else {
                    Utilities.snackbarNoInternetTwo(context, layoutFull);
                }
                break;

            default:
                break;
        }
    }

    public static void UpdateEmail() {
        CommonAsynTask loginAsyncTask = new CommonAsynTask(context, Constants.CHANGE_EMAIL_URL, getData(),
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
            objData.putOpt(Constants.CURRENT_EMAIL, edtCurrentEmail.getText().toString().trim());
            objData.putOpt(Constants.NEW_EMAIL, edtNewEmail.getText().toString().trim());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objData.toString();
    }
}