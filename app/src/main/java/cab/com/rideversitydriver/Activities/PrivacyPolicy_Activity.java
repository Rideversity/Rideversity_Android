package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
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
import cab.com.rideversitydriver.Utils.Utilities;


/**
 * Created by Kalidoss on 01/08/16.
 */
public class PrivacyPolicy_Activity extends AppCompatActivity implements View.OnClickListener {

    MyApplication application = null;
    TextView txtTermofService;
    static TextView txtPrivacyPolicy;
    RelativeLayout imgBack;
    static Context context;
    static RelativeLayout layoutFull;
    static Snackbar snackbar;
    private WebView webViewPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        context = this;
        application = (MyApplication) getApplication();
        layoutFull = (RelativeLayout) findViewById(R.id.layout_privacy_policy);
        txtPrivacyPolicy = (TextView) findViewById(R.id.textView_privacy_policy);
        webViewPrivacyPolicy = (WebView) findViewById(R.id.webView);
        webViewPrivacyPolicy.setOnClickListener(this);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        imgBack.setOnClickListener(this);

        if (Utilities.isOnline(context)) {
            PrivacyPolicy();
        } else {
            Utilities.snackbarNoInternet(context, layoutFull);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            default:
                break;
        }
    }

    //changed from Static to non static
    public void PrivacyPolicy() {
        CommonAsynTask privacyAsyncTask = new CommonAsynTask(context, Constants.PRIVACY_POLICY_URL, "",
                Constants.REQUEST_TYPE_GET, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            JSONObject userObj = jsonObject.getJSONObject(Constants.DATA);
                            webViewPrivacyPolicy.loadData(userObj.getString(Constants.DESCRIPTION), "text/html ; charset=utf-8", "UTF-8");
                            //txtPrivacyPolicy.setText(userObj.getString(Constants.DESCRIPTION));
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
        privacyAsyncTask.execute();
    }
}