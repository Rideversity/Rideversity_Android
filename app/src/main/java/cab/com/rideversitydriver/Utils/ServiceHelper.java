package cab.com.rideversitydriver.Utils;

import android.content.Context;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kalidoss on 26/07/16.
 */

public class ServiceHelper {
	Context context;
	JSONObject jsonResultText;
	private String TAG = "RequestDataRideversity";

	public ServiceHelper(Context context) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		this.context = context;

	}

	public JSONObject doHttpUrlConnectionAction(String requestData, String requestURL, String requestType) throws Exception {
 		URL url = null;
		BufferedReader reader = null;
		StringBuilder stringBuilder;
		Log.e(TAG, requestData + Constants.EMPTY_STRING + requestURL);
		try {
			// create the HttpURLConnection
			url = new URL(requestURL.replace(" ", "%20"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			if (requestType.equalsIgnoreCase(Constants.REQUEST_TYPE_POST)) {
				connection.setDoOutput(true);
			} else {
				connection.setDoOutput(false);
			}
			connection.setRequestMethod(requestType);
			connection.setReadTimeout(500 * 1000);
			connection.setRequestProperty(Constants.KEY_CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
			connection.setRequestProperty(Constants.KEY_ACCEPT, Constants.VALUE_CONTENT_TYPE);
			connection.connect();
			if (requestType.equalsIgnoreCase(Constants.REQUEST_TYPE_POST)) {
				OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
				streamWriter.write(requestData);
				streamWriter.close();
			}
			// read the output from the server
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			stringBuilder = new StringBuilder("");

			String line = null;
			while ((line = reader.readLine()) != null) {
				//Toast.makeText(context, "line " + line, Toast.LENGTH_SHORT).show();
				stringBuilder.append(line + "\n");

			}

			//Log.i("asdf", stringBuilder.toString());
			try {
				if(!TextUtils.isEmpty(stringBuilder.toString())){
					return new JSONObject(stringBuilder.toString());
				}
			}catch (Exception e){
				e.printStackTrace();
				Log.d("Error Msg" , "The Above JSON Object is causing error");
			}
			//connection.
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// close the reader; this can throw an exception too, so
			// wrap it in another try/catch block.
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

//for google map async task
	public String readUrl(String mapsApiDirectionsUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(mapsApiDirectionsUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();
		} catch (Exception e) {
			Log.d("Exception url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

}
