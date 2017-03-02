package cab.com.rideversitydriver.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Activities.ActiveRideDetail_Activity;
import cab.com.rideversitydriver.Activities.HomeMenu;
import cab.com.rideversitydriver.Activities.RideDetails_Activity;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 04/08/16.
 */
public class BankAccount extends Fragment {

    private EditText mBankEditText;
    private EditText mAccountNumberEditText;
    private EditText mAccountHolderEditText;
    private EditText mRoutingNumberEditText;

    private static SharedPref sharedPref;
    private Context mContext;
    private String BankEditText, AccountNumberEditText, AccountHolderEditText, RoutingNumberEditText;
    private TextView bankError, accountNumberError, accountHolderNameError, routingNumberError;
    public static boolean fromRealAdvacne;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bank_account, container, false);
        HomeMenu.txtEdit.setVisibility(View.VISIBLE);

        sharedPref = new SharedPref(getActivity());
        mBankEditText = (EditText) rootView.findViewById(R.id.bank);
        mAccountNumberEditText = (EditText) rootView.findViewById(R.id.account_number);
        mAccountHolderEditText = (EditText) rootView.findViewById(R.id.account_holder);
        mRoutingNumberEditText = (EditText) rootView.findViewById(R.id.routing_Number);
        bankError = (TextView) rootView.findViewById(R.id.bank_error);
        accountNumberError = (TextView) rootView.findViewById(R.id.account_number_error);
        accountHolderNameError = (TextView) rootView.findViewById(R.id.account_holder_name_error);
        routingNumberError = (TextView) rootView.findViewById(R.id.routing_number_error);

        bankError.setVisibility(View.GONE);
        accountNumberError.setVisibility(View.GONE);
        accountHolderNameError.setVisibility(View.GONE);
        routingNumberError.setVisibility(View.GONE);

        BankEditText = mBankEditText.getText().toString();
        AccountNumberEditText = mAccountNumberEditText.getText().toString();
        AccountHolderEditText = mAccountHolderEditText.getText().toString();
        RoutingNumberEditText = mRoutingNumberEditText.getText().toString();

        mContext = getActivity();
        parseBankInfo();

        if (RideDetails_Activity.fromRealAdvacne) {
            HomeMenu.txtEdit.setVisibility(View.VISIBLE);
        }

        HomeMenu.txtEdit.setVisibility(View.VISIBLE);
        HomeMenu.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdate();
            }
        });
        return rootView;
    }

    private void parseBankInfo() {
        CommonAsynTask bankInfoTask = new CommonAsynTask(getActivity(), Constants.BANK_INFO, getInfoParam(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.i("BankInfo", jsonObject.toString());
                if (jsonObject != null) {
                    try {
                        String result = jsonObject.getString(Constants.RESULT);
                        if (result.equals(Constants.SUCCESS)) {
                            JSONObject dataObject = jsonObject.getJSONObject(Constants.DATA);
                            if (dataObject != null) {
                                sharedPref.setString(Constants.BANK_ID, dataObject.getString(Constants.BANK_ID));
                                mBankEditText.setText(dataObject.getString(Constants.BANK_NAME));
                                mAccountNumberEditText.setText(dataObject.getString(Constants.ACCOUNT_NUMBER));
                                mAccountHolderEditText.setText(dataObject.getString(Constants.ACCOUNT_HOLDER));
                                mRoutingNumberEditText.setText(dataObject.getString(Constants.ROUTING_NUMBER));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bankInfoTask.execute();

    }

    private void parseUpdateBankInfo() {
        CommonAsynTask updateBankInfoTask = new CommonAsynTask(getActivity(), Constants.UPDATE_BANK_INFO, getUpdateBankInfoParam(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.i("BankInfoUpdate", jsonObject.toString());
                if (jsonObject != null) {
                    try {
                        String result = jsonObject.getString(Constants.RESULT);
                        if (result.equals(Constants.SUCCESS)) {
                            JSONObject dataObject = jsonObject.getJSONObject(Constants.DATA);
                            if (dataObject != null) {
                                sharedPref.setString(Constants.BANK_ID, dataObject.getString(Constants.BANK_ID));
                                //sharedPref.getString();
                                mBankEditText.setText(dataObject.getString(Constants.BANK_NAME));
                                mAccountNumberEditText.setText("****" + getLastFour(dataObject.getString(Constants.ACCOUNT_NUMBER)));
                                mAccountHolderEditText.setText((dataObject.getString(Constants.ACCOUNT_HOLDER)));
                                mRoutingNumberEditText.setText("****" + getLastFour(dataObject.getString(Constants.ROUTING_NUMBER)));
                                Toast.makeText(mContext, "" + jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Toast.makeText(mContext, "" + jsonObject.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        updateBankInfoTask.execute();
    }

    private String getUpdateBankInfoParam() {
        JSONObject objData = new JSONObject();
        String bankId = sharedPref.getString(Constants.BANK_ID);

        try {
            if (bankId != null && (!bankId.equals(""))) {
                objData.putOpt(Constants.BANK_ID, bankId);
            } else {
                objData.putOpt(Constants.BANK_ID, "");
            }

            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));


            if (!mBankEditText.getText().toString().equals("")) {
                objData.putOpt(Constants.BANK_NAME, mBankEditText.getText().toString());
            } else {
                //Toast.makeText(mContext, "Bank name field shouldn't be empty", Toast.LENGTH_SHORT).show();
                objData.putOpt(Constants.BANK_NAME, "");
            }

            if (!mAccountNumberEditText.getText().toString().equals("")) {
                objData.putOpt(Constants.ACCOUNT_NUMBER, mAccountNumberEditText.getText().toString());
            } else {
                objData.putOpt(Constants.ACCOUNT_NUMBER, "");
            }

            if (!mAccountHolderEditText.getText().toString().equals("")) {
                objData.putOpt(Constants.ACCOUNT_HOLDER, mAccountHolderEditText.getText().toString());
            } else {
                objData.putOpt(Constants.ACCOUNT_HOLDER, "");
            }

            if (!mRoutingNumberEditText.getText().toString().equals("")) {
                objData.putOpt(Constants.ROUTING_NUMBER, mRoutingNumberEditText.getText().toString());
            } else {
                objData.putOpt(Constants.ROUTING_NUMBER, "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objData.toString();
    }

    private String getInfoParam() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objData.toString();
    }

    public String getLastFour(String myString) {
        if (myString.length() > 3)
            return myString.substring(myString.length() - 4);
        else
            return myString;
    }


    public void onClickUpdate() {
        {
            BankEditText = mBankEditText.getText().toString();
            AccountNumberEditText = mAccountNumberEditText.getText().toString();
            AccountHolderEditText = mAccountHolderEditText.getText().toString();
            RoutingNumberEditText = mRoutingNumberEditText.getText().toString();

            bankError.setVisibility(View.INVISIBLE);
            accountNumberError.setVisibility(View.INVISIBLE);
            accountHolderNameError.setVisibility(View.INVISIBLE);
            routingNumberError.setVisibility(View.INVISIBLE);


            //four
            if (BankEditText.isEmpty() && AccountNumberEditText.isEmpty() && AccountHolderEditText.isEmpty() && RoutingNumberEditText.isEmpty()) {
                bankError.setVisibility(View.VISIBLE);
                accountNumberError.setVisibility(View.VISIBLE);
                accountHolderNameError.setVisibility(View.VISIBLE);
                routingNumberError.setVisibility(View.VISIBLE);
            }
            //three
            else if (AccountNumberEditText.isEmpty() && AccountHolderEditText.isEmpty() && RoutingNumberEditText.isEmpty()) {
                bankError.setVisibility(View.GONE);
                accountNumberError.setVisibility(View.VISIBLE);
                accountHolderNameError.setVisibility(View.VISIBLE);
                routingNumberError.setVisibility(View.VISIBLE);
            } else if (BankEditText.isEmpty() && AccountHolderEditText.isEmpty() && RoutingNumberEditText.isEmpty()) {
                bankError.setVisibility(View.VISIBLE);
                accountNumberError.setVisibility(View.GONE);
                accountHolderNameError.setVisibility(View.VISIBLE);
                routingNumberError.setVisibility(View.VISIBLE);
            } else if (BankEditText.isEmpty() && AccountNumberEditText.isEmpty() && RoutingNumberEditText.isEmpty()) {
                bankError.setVisibility(View.VISIBLE);
                accountNumberError.setVisibility(View.VISIBLE);
                accountHolderNameError.setVisibility(View.GONE);
                routingNumberError.setVisibility(View.VISIBLE);
            } else if (BankEditText.isEmpty() && AccountNumberEditText.isEmpty() && AccountHolderEditText.isEmpty()) {
                bankError.setVisibility(View.VISIBLE);
                accountNumberError.setVisibility(View.VISIBLE);
                accountHolderNameError.setVisibility(View.VISIBLE);
                routingNumberError.setVisibility(View.GONE);
            }

            //two
            else if (BankEditText.isEmpty() && AccountNumberEditText.isEmpty()) {
                bankError.setVisibility(View.VISIBLE);
                accountNumberError.setVisibility(View.VISIBLE);
                accountHolderNameError.setVisibility(View.GONE);
                routingNumberError.setVisibility(View.GONE);

            } else if (AccountHolderEditText.isEmpty() && RoutingNumberEditText.isEmpty()) {
                bankError.setVisibility(View.GONE);
                accountNumberError.setVisibility(View.GONE);
                accountHolderNameError.setVisibility(View.VISIBLE);
                routingNumberError.setVisibility(View.VISIBLE);
            } else if (AccountNumberEditText.isEmpty() && RoutingNumberEditText.isEmpty()) {
                bankError.setVisibility(View.GONE);
                accountNumberError.setVisibility(View.VISIBLE);
                accountHolderNameError.setVisibility(View.GONE);
                routingNumberError.setVisibility(View.VISIBLE);

            } else if (BankEditText.isEmpty() && RoutingNumberEditText.isEmpty()) {
                bankError.setVisibility(View.VISIBLE);
                accountNumberError.setVisibility(View.GONE);
                accountHolderNameError.setVisibility(View.GONE);
                routingNumberError.setVisibility(View.VISIBLE);

            } else if (AccountNumberEditText.isEmpty() && AccountHolderEditText.isEmpty()) {
                bankError.setVisibility(View.GONE);
                accountNumberError.setVisibility(View.VISIBLE);
                accountHolderNameError.setVisibility(View.VISIBLE);
                routingNumberError.setVisibility(View.GONE);
            }
            //Single
            else if (BankEditText.isEmpty()) {
                bankError.setVisibility(View.VISIBLE);
                accountNumberError.setVisibility(View.GONE);
                accountHolderNameError.setVisibility(View.GONE);
                routingNumberError.setVisibility(View.GONE);
            } else if (AccountNumberEditText.isEmpty()) {
                bankError.setVisibility(View.GONE);
                accountNumberError.setVisibility(View.VISIBLE);
                accountHolderNameError.setVisibility(View.GONE);
                routingNumberError.setVisibility(View.GONE);
            } else if (AccountHolderEditText.isEmpty()) {
                bankError.setVisibility(View.GONE);
                accountNumberError.setVisibility(View.GONE);
                accountHolderNameError.setVisibility(View.VISIBLE);
                routingNumberError.setVisibility(View.GONE);
            } else if (RoutingNumberEditText.isEmpty()) {
                bankError.setVisibility(View.GONE);
                accountNumberError.setVisibility(View.GONE);
                accountHolderNameError.setVisibility(View.GONE);
                routingNumberError.setVisibility(View.VISIBLE);
            } else {
                if (Utilities.isOnline(mContext)) {
                    bankError.setVisibility(View.GONE);
                    accountNumberError.setVisibility(View.GONE);
                    accountHolderNameError.setVisibility(View.GONE);
                    routingNumberError.setVisibility(View.GONE);
                    try {
                        parseUpdateBankInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean validateRoutingNumber(String s) {

        int checksum = 0, len = 0, sum = 0, mod = 0;
        len = s.length();

        if (len != 9) {
            return false;
        } else {
            String newString = s.substring(s.length() - 1);
            checksum = Integer.parseInt(newString);

            sum = (3 * (Integer.parseInt("" + s.charAt(0)) + Integer.parseInt("" + s.charAt(3)) + Integer.parseInt("" + s.charAt(6)))) +
                    (7 * (Integer.parseInt("" + s.charAt(1)) + Integer.parseInt("" + s.charAt(4)) + Integer.parseInt("" + s.charAt(7)))) +
                    (1 * (Integer.parseInt("" + s.charAt(2)) + Integer.parseInt("" + s.charAt(5))));


            mod = sum % 10;

            if (mod == checksum)
                return true;
            else
                return false;

        }
    }
}