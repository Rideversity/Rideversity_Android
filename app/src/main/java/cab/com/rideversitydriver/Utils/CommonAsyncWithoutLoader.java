package cab.com.rideversitydriver.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;

/**
 * Created by CIPL0372 on 12/21/2016.
 */

public class CommonAsyncWithoutLoader extends AsyncTask<String, Void, JSONObject> {
    Activity activity;
    private ActivityIndicator pDialog;
    //private Progressbar progressDialog;
    String url, data, methodtype;
    private AsyncTaskInterface taskCompliteListener;
    private boolean isToShowIndicator;

    public CommonAsyncWithoutLoader(Context context, String url, String data, String methodtype, AsyncTaskInterface taskCompliteListener) {
        this.activity = (Activity) context;
        this.url = url;
        this.data = data;
        this.methodtype = methodtype;
        this.taskCompliteListener = taskCompliteListener;
        //pDialog = null;
        //progressDialog=null;
        isToShowIndicator = true;
    }

    public void showIndicator(boolean isToShowIndicator) {
        this.isToShowIndicator = isToShowIndicator;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (activity == null || activity.isFinishing()) {
            return;
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            return new ServiceHelper(activity).doHttpUrlConnectionAction(data, url, methodtype);
        } catch (Exception e) {
            try {
                return new JSONObject("Error:" + e.getMessage());
            } catch (JSONException e1) {
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (!activity.isFinishing()) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
        if (result != null) {
            if (!TextUtils.isEmpty(result.toString()) && result.toString().contains("Error:")) {
                Toast.makeText(activity, result.toString(), Toast.LENGTH_SHORT).show();
            }
            taskCompliteListener.onTaskCompleted(result);
        }
    }
}