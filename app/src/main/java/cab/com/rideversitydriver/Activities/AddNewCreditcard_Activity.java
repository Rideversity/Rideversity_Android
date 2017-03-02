package cab.com.rideversitydriver.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cab.com.rideversitydriver.Fragments.CreditCard_Fragment;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.DatePicker;
import cab.com.rideversitydriver.Utils.MyApplication;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 04/08/16.
 */
public class AddNewCreditcard_Activity extends AppCompatActivity implements View.OnClickListener {

    MyApplication application = null;
    TextView txtClose, txtFullnameError, txtCardNumberError, txtExpDateError, txtCVCcodeError, txtExpDate;
    Button btnAddcreditCard;
    Context context;
    EditText edtFullName, edtCardNumber, edtExpDate, edtCVCcode;
    public static String strFullName, strCardNumber, strExpDate, strCVVcode, strCardmessage, result, cardId;
    private static SharedPref sharedPref;
    public static Activity activity = null;
    static LinearLayout layoutFull;
    static String strTYPE = "", strCardId, strHoderName, strCardNo, strExpdate, strCVV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_credit_card);
        activity = this;
        context = getApplicationContext();
        sharedPref = new SharedPref(context);
        application = (MyApplication) getApplication();

        layoutFull = (LinearLayout) findViewById(R.id.layout_addpayment_full);
        txtClose = (TextView) findViewById(R.id.textView_backarrow);
        btnAddcreditCard = (Button) findViewById(R.id.button_add_card);
        btnAddcreditCard.setOnClickListener(this);
        txtClose.setOnClickListener(this);
        edtFullName = (EditText) findViewById(R.id.editText_full_name);
        edtCardNumber = (EditText) findViewById(R.id.editText_card_number);
        // edtExpDate=(EditText)findViewById(R.id.editText_expiration_date);
        edtCVCcode = (EditText) findViewById(R.id.editText_cvc);

        edtFullName.setOnClickListener(this);

        txtFullnameError = (TextView) findViewById(R.id.textView_full_name_error);
        txtCardNumberError = (TextView) findViewById(R.id.textView_card_number_error);
        txtExpDateError = (TextView) findViewById(R.id.textView_exp_date_error);
        txtCVCcodeError = (TextView) findViewById(R.id.textView_cvc_error);
        txtExpDate = (TextView) findViewById(R.id.textView_expiration_date);
        txtExpDate.setOnClickListener(this);

        txtFullnameError.setVisibility(View.GONE);
        txtCardNumberError.setVisibility(View.GONE);
        txtExpDateError.setVisibility(View.GONE);
        txtCVCcodeError.setVisibility(View.GONE);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                strTYPE = null;
            } else {
                strTYPE = extras.getString("TYPE");
                if (strTYPE.equals("Update")) {
                    strCardId = extras.getString("CARD_ID");
                    strHoderName = extras.getString("HOLDER_NAME");
                    strCardNo = extras.getString("CARD_NUMBER");
                    strExpdate = extras.getString("EXPIRATION_DATE");
                    strCVV = extras.getString("CARD_CVV");
                    Log.e("strHoderName", "" + strHoderName);
                    edtFullName.setText(strHoderName);
                    edtCardNumber.setText(strCardNo);
                    txtExpDate.setText(strExpdate);
                    edtCVCcode.setText(strCVV);
                    btnAddcreditCard.setText("UPDATE CARD");
                    btnAddcreditCard.setTag("update");
                } else {
                    btnAddcreditCard.setText("ADD CARD");
                    btnAddcreditCard.setTag("add");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.textView_expiration_date:
                DatePicker dialog = new DatePicker(txtExpDate);
                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                dialog.show(fragmentTransaction, "DatePicker");
                break;
            case R.id.editText_full_name:
                edtFullName.setCursorVisible(true);
                edtFullName.setActivated(true);
                edtFullName.setPressed(true);
                break;
            case R.id.button_add_card:

                strFullName = edtFullName.getText().toString();
                strCardNumber = edtCardNumber.getText().toString();
                strExpDate = txtExpDate.getText().toString();
                strCVVcode = edtCVCcode.getText().toString();
                //Four
                if (strFullName.isEmpty() && strCardNumber.isEmpty() && strExpDate.isEmpty() && strCVVcode.isEmpty()) {
                    txtFullnameError.setVisibility(View.VISIBLE);
                    txtCardNumberError.setVisibility(View.VISIBLE);
                    txtExpDateError.setVisibility(View.VISIBLE);
                    txtCVCcodeError.setVisibility(View.VISIBLE);
                }

                //Three
                else if (strFullName.isEmpty() && strCardNumber.isEmpty() && strExpDate.isEmpty()) {
                    txtFullnameError.setVisibility(View.VISIBLE);
                    txtCardNumberError.setVisibility(View.VISIBLE);
                    txtExpDateError.setVisibility(View.VISIBLE);
                    txtCVCcodeError.setVisibility(View.GONE);
                } else if (strFullName.isEmpty() && strCardNumber.isEmpty() && strCVVcode.isEmpty()) {
                    txtFullnameError.setVisibility(View.VISIBLE);
                    txtCardNumberError.setVisibility(View.VISIBLE);
                    txtExpDateError.setVisibility(View.GONE);
                    txtCVCcodeError.setVisibility(View.VISIBLE);
                } else if (strFullName.isEmpty() && strExpDate.isEmpty() && strCVVcode.isEmpty()) {
                    txtFullnameError.setVisibility(View.VISIBLE);
                    txtCardNumberError.setVisibility(View.GONE);
                    txtExpDateError.setVisibility(View.VISIBLE);
                    txtCVCcodeError.setVisibility(View.VISIBLE);
                } else if (strCardNumber.isEmpty() && strExpDate.isEmpty() && strCVVcode.isEmpty()) {
                    txtFullnameError.setVisibility(View.GONE);
                    txtCardNumberError.setVisibility(View.VISIBLE);
                    txtExpDateError.setVisibility(View.VISIBLE);
                    txtCVCcodeError.setVisibility(View.VISIBLE);
                }

                //Two
                else if (strFullName.isEmpty() && strCardNumber.isEmpty()) {
                    txtFullnameError.setVisibility(View.VISIBLE);
                    txtCardNumberError.setVisibility(View.VISIBLE);
                    txtExpDateError.setVisibility(View.GONE);
                    txtCVCcodeError.setVisibility(View.GONE);
                } else if (strExpDate.isEmpty() && strCVVcode.isEmpty()) {
                    txtFullnameError.setVisibility(View.GONE);
                    txtCardNumberError.setVisibility(View.GONE);
                    txtExpDateError.setVisibility(View.VISIBLE);
                    txtCVCcodeError.setVisibility(View.VISIBLE);
                } else if (strFullName.isEmpty() && strCVVcode.isEmpty()) {
                    txtFullnameError.setVisibility(View.VISIBLE);
                    txtCardNumberError.setVisibility(View.GONE);
                    txtExpDateError.setVisibility(View.GONE);
                    txtCVCcodeError.setVisibility(View.VISIBLE);
                } else if (strCardNumber.isEmpty() && strExpDate.isEmpty()) {
                    txtFullnameError.setVisibility(View.GONE);
                    txtCardNumberError.setVisibility(View.VISIBLE);
                    txtExpDateError.setVisibility(View.VISIBLE);
                    txtCVCcodeError.setVisibility(View.GONE);
                } else if (strCardNumber.isEmpty() && strCVVcode.isEmpty()) {
                    txtFullnameError.setVisibility(View.GONE);
                    txtCardNumberError.setVisibility(View.VISIBLE);
                    txtExpDateError.setVisibility(View.GONE);
                    txtCVCcodeError.setVisibility(View.VISIBLE);
                } else if (strFullName.isEmpty() && strExpDate.isEmpty()) {
                    txtFullnameError.setVisibility(View.VISIBLE);
                    txtCardNumberError.setVisibility(View.GONE);
                    txtExpDateError.setVisibility(View.VISIBLE);
                    txtCVCcodeError.setVisibility(View.GONE);
                }

                //Single
                else if (strFullName.isEmpty()) {
                    txtFullnameError.setVisibility(View.VISIBLE);
                    txtCardNumberError.setVisibility(View.GONE);
                    txtExpDateError.setVisibility(View.GONE);
                    txtCVCcodeError.setVisibility(View.GONE);
                } else if (strCardNumber.isEmpty()) {
                    txtFullnameError.setVisibility(View.GONE);
                    txtCardNumberError.setVisibility(View.VISIBLE);
                    txtExpDateError.setVisibility(View.GONE);
                    txtCVCcodeError.setVisibility(View.GONE);
                } else if (strExpDate.isEmpty()) {
                    txtFullnameError.setVisibility(View.GONE);
                    txtCardNumberError.setVisibility(View.GONE);
                    txtExpDateError.setVisibility(View.VISIBLE);
                    txtCVCcodeError.setVisibility(View.GONE);
                } else if (strCVVcode.isEmpty()) {
                    txtFullnameError.setVisibility(View.GONE);
                    txtCardNumberError.setVisibility(View.GONE);
                    txtExpDateError.setVisibility(View.GONE);
                    txtCVCcodeError.setVisibility(View.VISIBLE);
                } else {
                    if (Utilities.isOnline(context)) {
                        txtFullnameError.setVisibility(View.GONE);
                        txtCardNumberError.setVisibility(View.GONE);
                        txtExpDateError.setVisibility(View.GONE);
                        txtCVCcodeError.setVisibility(View.GONE);

                        if (btnAddcreditCard.getTag().equals("add")) {
                            AddCard();
                        } else {
                            UpdateCard();
                        }


                    } else {
                        Utilities.snackbarNoInternetTwo(context, layoutFull);
                    }
                }
                break;
            case R.id.textView_backarrow:
                CreditCard_Fragment.isCardAdded = false;
                CreditCard_Fragment.isCardAdded = false;
                Constants.BACK_PRESS = true;
                finish();
                break;
            default:
                break;
        }
    }

    public void AddCard() {
        CommonAsynTask addcardsyncTask = new CommonAsynTask(AddNewCreditcard_Activity.this, Constants.ADD_CARD_URL, getCardDetails(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            CreditCard_Fragment.isCardAdded = true;
                            strCardmessage = jsonObject.getString(Constants.MESSAGE);
                            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            im.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getApplicationWindowToken(), 0);
                            Constants.BACK_PRESS = true;
                            Snackbar snackbar = Snackbar
                                    .make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                            finish();

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            Snackbar snackbar = Snackbar
                                    .make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
        addcardsyncTask.execute();
    }

    private static String getCardDetails() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.HOLDER_NAME, strFullName);
            objData.putOpt(Constants.CARD_NUMBER, strCardNumber);
            objData.putOpt(Constants.EXPIRATION_DATE, strExpDate);
            objData.putOpt(Constants.CARD_CVV, strCVVcode);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objData.toString();
    }

    public void UpdateCard() {
        CommonAsynTask updatecardsyncTask = new CommonAsynTask(AddNewCreditcard_Activity.this, Constants.UPDATE_CARD_URL, getUpdateCardDetails(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            CreditCard_Fragment.isCardAdded = true;
                            strCardmessage = jsonObject.getString(Constants.MESSAGE);
                            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            im.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getApplicationWindowToken(), 0);
                            Constants.BACK_PRESS = true;
                            Snackbar snackbar = Snackbar
                                    .make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                            finish();

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            Snackbar snackbar = Snackbar
                                    .make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(activity, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        updatecardsyncTask.execute();
    }

    private static String getUpdateCardDetails() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.CARD_ID, strCardId);
            objData.putOpt(Constants.RIDER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.HOLDER_NAME, strFullName);
            objData.putOpt(Constants.CARD_NUMBER, strCardNumber);
            objData.putOpt(Constants.EXPIRATION_DATE, strExpDate);
            objData.putOpt(Constants.CARD_CVV, strCVVcode);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objData.toString();
    }
}