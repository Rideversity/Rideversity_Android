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
 * Created by kalidoss on 26/07/16.
 */

public class CommonAsynTask extends AsyncTask<String, Void, JSONObject> {
    Activity activity;
    private ActivityIndicator pDialog;
    //private Progressbar progressDialog;
    String url, data, methodtype;
    private AsyncTaskInterface taskCompliteListener;
    private boolean isToShowIndicator;

    public CommonAsynTask(Context context, String url, String data, String methodtype, AsyncTaskInterface taskCompliteListener) {
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
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        } else if (pDialog != null)
            pDialog = null;
        if (isToShowIndicator && pDialog == null) {
            pDialog = new ActivityIndicator(activity);
            pDialog.setCancelable(false);
            pDialog.setLoadingText("Loading....");
            pDialog.show();
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            JSONObject jsonObject = new ServiceHelper(activity).doHttpUrlConnectionAction(data, url, methodtype);
            return jsonObject;
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
        try {
            if (!activity.isFinishing()) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (result != null) {
            if (!TextUtils.isEmpty(result.toString()) && result.toString().contains("Error:")) {
                Toast.makeText(activity, result.toString(), Toast.LENGTH_SHORT).show();
            }
            taskCompliteListener.onTaskCompleted(result);
        }
    }

	/*@Override
    protected void onPostExecute(MyResult result) {
		try {
			if ((this.mDialog != null) && this.mDialog.isShowing()) {
				this.mDialog.dismiss();
			}
		} catch (final IllegalArgumentException e) {
			// Handle or log or ignore
		} catch (final Exception e) {
			// Handle or log or ignore
		} finally {
			this.mDialog = null;
		}
	}*/
}
