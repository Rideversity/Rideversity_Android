package cab.com.rideversitydriver.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cab.com.rideversitydriver.Adapters.SchoolSpinnerAdapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.ForgetModel;
import cab.com.rideversitydriver.Models.LoginModel;
import cab.com.rideversitydriver.Models.SchoolListModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.PolygonImageView;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.ExifUtil;
import cab.com.rideversitydriver.Utils.MyApplication;
import cab.com.rideversitydriver.Utils.MySpinner;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.UsPhoneNumberFormatter;
import cab.com.rideversitydriver.Utils.Utilities;


/**
 * Created by Kalidoss on 01/08/16.
 */
public class PersonalDetails_Activity extends AppCompatActivity implements View.OnClickListener {

    MyApplication application = null;
    TextView txtDOB, txtUpdate, txtSpinnerHeader;
    RelativeLayout imgBack;
    public static PolygonImageView imgProfile;
    MySpinner spnSchool, spnGender, spnClassification;
    boolean spinnerTouch = false;

    int iCurrentSelection;
    public static LinearLayout layoutFull;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 2;
    public static Context context;
    static SharedPref sharedPref;
    public static Snackbar snackbar;
    LinearLayout layoutMain;
    EditText edtUserName, edtFullName, editTextFirstname, editTextLastname, edtPhoneNumber, edtEmail, edtStreetdaddress, edtLocality, edtRegion, edtPostalCode, edtSocialsecurity, edtStudentid;
    LayoutInflater layoutInflater;
    public static String strUsername, strFirstname, strLastname, strFullname, strPhoneNo, strEmail, strSchool = "0", strClassification, strGender = "0", strProfilePic,
            strDOB, strMailingAddress, strStreetAddress, strLocality, strRegion, strPostalCode, strSocialSecurityNumber, strStudentId;
    public static byte[] byteProfilePic;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;
    public static String imageToBase64String, strForgetEmail;
    Dialog dialog;
    EditText edtPas, email;
    public static TextView txtError, txtForget, txtEmailError;
    public static Dialog forgetDialog;
    public static String TYPE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details);
        context = this;
        sharedPref = new SharedPref(context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application = (MyApplication) getApplication();
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        imgProfile = (PolygonImageView) findViewById(R.id.imageView_profileDetail);
        layoutFull = (LinearLayout) findViewById(R.id.layout_personal_profile);
        imgProfile.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        //imgProfile=(PolygonImageView)findViewById(R.id.imageView_profileDetail);
        edtUserName = (EditText) findViewById(R.id.editText_username);
        // edtFullName=(EditText)findViewById(R.id.Edittext_fullname);
        editTextFirstname = (EditText) findViewById(R.id.editText_firstname);
        editTextLastname = (EditText) findViewById(R.id.editText_lastname);
        edtPhoneNumber = (EditText) findViewById(R.id.Edittext_phonenumber);
        edtStreetdaddress = (EditText) findViewById(R.id.Edittext_street_address);
        edtLocality = (EditText) findViewById(R.id.edittext_Locality);
        edtRegion = (EditText) findViewById(R.id.edittext_Region);
        edtPostalCode = (EditText) findViewById(R.id.edittext_postal_code);
        txtDOB = (TextView) findViewById(R.id.textview_dateofbirth);
        edtSocialsecurity = (EditText) findViewById(R.id.Edittext_driver_information_social_security_number);
        edtStudentid = (EditText) findViewById(R.id.Edittext_driver_information_student_id);

        //String formatedNumber = PhoneNumberUtils.formatNumber(edtPhoneNumber.getText().toString());
        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<>(edtPhoneNumber));
        edtPhoneNumber.addTextChangedListener(addLineNumberFormatter);


        edtEmail = (EditText) findViewById(R.id.Edittext_email);

        spnSchool = (MySpinner) findViewById(R.id.spinner_school);
        spnGender = (MySpinner) findViewById(R.id.spinner_personal_gender);

        txtUpdate = (TextView) findViewById(R.id.textView_update);
        txtUpdate.setOnClickListener(this);


        edtUserName.setFocusable(false);
        edtUserName.setEnabled(false);
        edtUserName.setCursorVisible(false);

        imgProfile.setBackgroundResource(android.R.color.transparent);
        imgProfile.setBackgroundColor(Color.TRANSPARENT);
        imgProfile.setAlpha(127);

        checkAndRequestPermissions();

        Log.e("PersonalDetails_Act->", "Personal");

        if (Constants.REG_LOAD == false) {
            if (Utilities.isOnline(context)) {
                schoolListAPI();
                GetPersonalInfo();
            } else {
                Utilities.snackbarNoInternetTwo(context, layoutFull);
            }
        } else {
            imgProfile.setImageBitmap(Constants.bitmapPersonalImage);

            if (Singleton.getInstance().schoolArray.size() > 0) {
                SchoolSpinnerAdapter adapterSchool = new SchoolSpinnerAdapter(layoutInflater, Singleton.getInstance().schoolArray);
                spnSchool.setAdapter(adapterSchool);
                spnSchool.setSelection(adapterSchool.getCount());
            }
            //strSchool
            if (Singleton.getInstance().profileArrayList.size() > 0) {
                if (!Singleton.getInstance().profileArrayList.get(0).getSchoolId().isEmpty()) {
                    for (int i = 0; i < Singleton.getInstance().schoolArray.size(); i++) {
                        if (Singleton.getInstance().profileArrayList.get(0).getSchoolId().equals(Singleton.getInstance().schoolArray.get(i).schoolId)) {
                            strSchool = Singleton.getInstance().schoolArray.get(i).schoolId;
                            spnSchool.setSelection(i);
                        }
                    }
                }
            }
        }

        //spnSchool
        spnSchool.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (spinnerTouch == false) {
                        spinnerTouch = true;
                        spnSchool.setBackgroundResource(R.drawable.spinner_up);
                    } else {
                        spinnerTouch = false;
                        spnSchool.setBackgroundResource(R.drawable.spinner_down);
                    }
                }
                return false;
            }
        });

        iCurrentSelection = spnSchool.getSelectedItemPosition();
        spnSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTouch = false;
                if (iCurrentSelection != position) {
                    spnSchool.setBackgroundResource(R.drawable.spinner_down);
                    strSchool = Singleton.getInstance().schoolArray.get(position).schoolId;
                } else {
                    spnSchool.setBackgroundResource(R.drawable.spinner_down);
                }
                iCurrentSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spnSchool.setBackgroundResource(R.drawable.spinner_up);
            }
        });

        //spnGender
        spnGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (spinnerTouch == false) {
                        spinnerTouch = true;
                        spnGender.setBackgroundResource(R.drawable.spinner_up);
                    } else {
                        spinnerTouch = false;
                        spnGender.setBackgroundResource(R.drawable.spinner_down);
                    }
                }
                return false;
            }
        });
        iCurrentSelection = spnGender.getSelectedItemPosition();
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTouch = false;

                if (position == 0) {
                    strGender = "";
                }
                if (position == 1) {
                    strGender = "Male";
                }
                if (position == 2) {
                    strGender = "Female";
                }

                if (iCurrentSelection != position) {
                    spnGender.setBackgroundResource(R.drawable.spinner_down);
                } else {
                    spnSchool.setBackgroundResource(R.drawable.spinner_down);
                }
                iCurrentSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spnGender.setBackgroundResource(R.drawable.spinner_up);
            }
        });

        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(
                this, R.array.gender_array, R.layout.spinner_textview);
        adapter_gender.setDropDownViewResource(R.layout.spinner_textview);
        spnGender.setAdapter(adapter_gender);
        spnGender.setEnabled(false);


        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DateListeners(),
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                SimpleDateFormat sdf = new SimpleDateFormat();
                //dpd.setMinDate(now);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                // dpd.setTitle(sdf.format(now));
                dpd.setAccentColor(Color.parseColor("#278455"));
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.imageView_profileDetail:
                selectImage();
                break;
            case R.id.textView_update:
                // strFullname=edtFullName.getText().toString();
                strFirstname = editTextFirstname.getText().toString();
                strLastname = editTextLastname.getText().toString();
                strUsername = edtUserName.getText().toString();
                strPhoneNo = edtPhoneNumber.getText().toString();
                strEmail = edtEmail.getText().toString();
                strStreetAddress = edtStreetdaddress.getText().toString();
                strLocality = edtLocality.getText().toString();
                strRegion = edtRegion.getText().toString();
                strPostalCode = edtPostalCode.getText().toString();
                strDOB = txtDOB.getText().toString();
                strSocialSecurityNumber = edtSocialsecurity.getText().toString();
                strStudentId = edtStudentid.getText().toString();
                //Hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (strFirstname.isEmpty() || strLastname.isEmpty() || strUsername.isEmpty() || strPhoneNo.isEmpty() || strEmail.isEmpty()
                        || strStreetAddress.isEmpty() || strLocality.isEmpty() || strRegion.isEmpty() || strPostalCode.isEmpty()
                        || strStudentId.isEmpty() || strSocialSecurityNumber.isEmpty()) {
                    snackbar = Snackbar.make(layoutFull, "Please enter all values", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strStreetAddress.toString().isEmpty()) {
                    snackbar = Snackbar.make(layoutFull, "Please Enter Street Address", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strLocality.toString().isEmpty()) {
                    snackbar = Snackbar.make(layoutFull, "Please Enter Locality", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strRegion.toString().isEmpty()) {
                    snackbar = Snackbar.make(layoutFull, "Please Enter Region", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strPostalCode.toString().isEmpty()) {
                    snackbar = Snackbar.make(layoutFull, "Please Enter Postal Code", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strSocialSecurityNumber.toString().isEmpty()) {
                    snackbar = Snackbar.make(layoutFull, "Please Enter Security number", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strSocialSecurityNumber.toString().length() > 9 || strSocialSecurityNumber.toString().length() < 9) {
                    snackbar = Snackbar.make(layoutFull, "Your SSN number must be 9 digits", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strStudentId.toString().isEmpty()) {
                    snackbar = Snackbar.make(layoutFull, "Please Enter Student ID", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strSchool.equals("0")) {
                    snackbar = Snackbar.make(layoutFull, "Please Select School name", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (strGender.equals("0")) {
                    snackbar = Snackbar.make(layoutFull, "Please Select Gender", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else {
                    if (Utilities.isOnline(context)) {
                        dialog = new Dialog(PersonalDetails_Activity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.password_update);
                        Button btnOk = (Button) dialog.findViewById(R.id.button_ok);
                        txtError = (TextView) dialog.findViewById(R.id.textView_error);
                        txtForget = (TextView) dialog.findViewById(R.id.textView_forget);
                        edtPas = (EditText) dialog.findViewById(R.id.editText_current_password);
                        txtError.setVisibility(View.GONE);

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String strPassword = edtPas.getText().toString().trim();

                                if (strPassword.isEmpty()) {
                                    txtError.setVisibility(View.VISIBLE);
                                    txtError.setText("Please enter your password");
                                } else if (strPassword.equals(sharedPref.getString("password"))) {
                                    dialog.dismiss();
                                    txtError.setVisibility(View.GONE);
                                    Update();
                                } else {
                                    txtError.setVisibility(View.VISIBLE);
                                    txtError.setText("Incorrect password");
                                }
                            }
                        });

                        txtForget.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                // ForgetDialog();
                            }
                        });
                        dialog.show();
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
        forgetDialog = new Dialog(PersonalDetails_Activity.this);
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
                        //ForgetPassword();
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
                            txtEmailError.setVisibility(View.VISIBLE);
                            txtEmailError.setText(jsonObject.getString(Constants.RESULT));
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


    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(PersonalDetails_Activity.this, Manifest.permission.CAMERA);
        // int permissionExternalStoarge = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            //fragment.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            // requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            ActivityCompat.requestPermissions(PersonalDetails_Activity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void Update() {
        CommonAsynTask updateAsyncTask = new CommonAsynTask(context, Constants.EDIT_USER_INFO_URL, getUpdateData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {

                if (jsonObject != null) {
                    try {

                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            JSONObject userObj = jsonObject.getJSONObject(Constants.DATA);
                            // userObj.getString(Constants.PROFILE_IMAGE);
                            sharedPref.setString("userProfileImage", userObj.getString(Constants.PROFILE_IMAGE));

                            if (!sharedPref.getString("userProfileImage").isEmpty()) {
                                Picasso.with(context).load(sharedPref.getString("userProfileImage")).fit().centerCrop()
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.no_image).
                                        into(HomeMenu.imgProfile);
                            } else {
                                imgProfile.setImageResource(R.drawable.no_image);
                            }
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
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
                    Toast.makeText(context, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        updateAsyncTask.execute();
    }

    private static String getUpdateData() {
        if (byteProfilePic != null) {
            strProfilePic = Base64.encodeToString(byteProfilePic, Base64.NO_WRAP);
        } else {
            Bitmap bitmap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            strProfilePic = Base64.encodeToString(b, Base64.DEFAULT);
        }


        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.USER_ID, sharedPref.getString("userId"));
            objData.putOpt(Constants.FIRST_NAME, strFirstname);
            objData.putOpt(Constants.LAST_NAME, strLastname);
            objData.putOpt(Constants.USERNAME, strUsername);
            objData.putOpt(Constants.EMAIL, strEmail);
            objData.putOpt(Constants.PHONENO, strPhoneNo);
            objData.putOpt(Constants.GENDER, strGender);
            objData.putOpt(Constants.SCHOOLID, strSchool);
            objData.putOpt(Constants.PROFILE_IMAGE, strProfilePic);
            objData.putOpt(Constants.SOCIAL_SECURITY_NO, strSocialSecurityNumber);
            objData.putOpt(Constants.DATE_OF_BIRTH, strDOB);
            objData.putOpt(Constants.MAILING_ADDRESS, strMailingAddress);
            objData.putOpt(Constants.STREET_ADDRESS, strStreetAddress);
            objData.putOpt(Constants.LOCALITY, strLocality);
            objData.putOpt(Constants.REGION, strRegion);
            objData.putOpt(Constants.POSTAL_CODE, strPostalCode);
            objData.putOpt(Constants.STUDENT_ID, strStudentId);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objData.toString();
    }


    public void GetPersonalInfo() {
        CommonAsynTask getpersonalAsyncTask = new CommonAsynTask(context, Constants.DRIVER_INFO_URL, getData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        Singleton.getInstance().profileArrayList.clear();
                        LoginModel loginmodel = new LoginModel();
                        loginmodel.setMessage(jsonObject.getString(Constants.MESSAGE));
                        loginmodel.setResult(jsonObject.getString(Constants.RESULT));

                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            JSONObject userObj = jsonObject.getJSONObject(Constants.DATA);
                            loginmodel.setUserId(userObj.getString(Constants.USER_ID));
                            // loginmodel.setFullName(userObj.getString(Constants.FULLNAME));
                            loginmodel.setFirstName(userObj.getString(Constants.FIRST_NAME));
                            loginmodel.setLastName(userObj.getString(Constants.LAST_NAME));
                            loginmodel.setUserName(userObj.getString(Constants.USERNAME));
                            loginmodel.setEmail(userObj.getString(Constants.EMAIL));
                            loginmodel.setPhoneNo(userObj.getString(Constants.PHONENO));
                            loginmodel.setStreetAddress(userObj.getString(Constants.STREET_ADDRESS));
                            loginmodel.setLocality(userObj.getString(Constants.LOCALITY));
                            loginmodel.setRegion(userObj.getString(Constants.REGION));
                            loginmodel.setPostalCode(userObj.getString(Constants.POSTAL_CODE));
                            loginmodel.setDateofBirth(userObj.getString(Constants.DATE_OF_BIRTH));
                            loginmodel.setSocialSecurityNumber(userObj.getString(Constants.SOCIAL_SECURITY_NO));
                            loginmodel.setStudentID(userObj.getString(Constants.STUDENT_ID));
                            loginmodel.setGender(userObj.getString(Constants.GENDER));
                            loginmodel.setProfileImage(userObj.getString(Constants.PROFILE_IMAGE));
                            loginmodel.setUserRole(userObj.getString(Constants.USER_ROLE));
                            loginmodel.setAccountType(userObj.getString(Constants.ACCOUNT_TYPE));
                            loginmodel.setSchoolId(userObj.getString(Constants.SCHOOLID));
                            loginmodel.setClassId(userObj.getString(Constants.CLASS_ID));
                            loginmodel.setPositionId(userObj.getString(Constants.POSITION_ID));

                            loginmodel.setTypeOfService(userObj.getString(Constants.TYPE_OFSERVICE));
                            loginmodel.setDeviceId(userObj.getString(Constants.DEVICE_ID));
                            loginmodel.setDeviceType(userObj.getString(Constants.DEVICE_TYPE));
                            loginmodel.setDeviceToken(userObj.getString(Constants.DEVICE_TOKEN));
                            loginmodel.setAccountTypeId(userObj.getString(Constants.ACCOUNT_TYPE_ID));

                            sharedPref.setString("userId", userObj.getString(Constants.USER_ID));
                            sharedPref.setString("userName", userObj.getString(Constants.USERNAME));
                            sharedPref.setString("userProfileImage", userObj.getString(Constants.PROFILE_IMAGE));
                            sharedPref.setString("phoneNo", userObj.getString(Constants.PHONENO));

                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            snackbar = Snackbar.make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }
                        Singleton.getInstance().profileArrayList.add(loginmodel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (Singleton.getInstance().profileArrayList.size() > 0) {
                        if (!Singleton.getInstance().profileArrayList.get(0).getProfileImage().isEmpty()) {
                            Picasso.with(context).load(Singleton.getInstance().profileArrayList.get(0).getProfileImage()).fit().centerCrop()
                                    .placeholder(R.drawable.no_image)
                                    .error(R.drawable.no_image).
                                    into(imgProfile);
                        } else {
                            imgProfile.setImageResource(R.drawable.no_image);
                        }
                        edtUserName.setText(Singleton.getInstance().profileArrayList.get(0).getUserName());
                        // edtFullName.setText(Singleton.getInstance().profileArrayList.get(0).getFullName());
                        editTextFirstname.setText(Singleton.getInstance().profileArrayList.get(0).getFirstName());
                        editTextLastname.setText(Singleton.getInstance().profileArrayList.get(0).getLastName());
                        try {
                            edtPhoneNumber.setText(String.valueOf(Singleton.getInstance().profileArrayList.get(0).getPhoneNo()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        edtEmail.setText(Singleton.getInstance().profileArrayList.get(0).getEmail());
                        edtStreetdaddress.setText(Singleton.getInstance().profileArrayList.get(0).getStreetAddress());
                        edtLocality.setText(Singleton.getInstance().profileArrayList.get(0).getLocality());
                        edtRegion.setText(Singleton.getInstance().profileArrayList.get(0).getRegion());
                        edtPostalCode.setText(Singleton.getInstance().profileArrayList.get(0).getPostalCode());
                        txtDOB.setText(Singleton.getInstance().profileArrayList.get(0).getDateofBirth());
                        edtSocialsecurity.setText(Singleton.getInstance().profileArrayList.get(0).getSocialSecurityNumber());
                        edtStudentid.setText(Singleton.getInstance().profileArrayList.get(0).getStudentID());

                        //strSchool
                        if (Singleton.getInstance().profileArrayList.size() > 0) {
                            if (!Singleton.getInstance().profileArrayList.get(0).getSchoolId().isEmpty()) {
                                for (int i = 0; i < Singleton.getInstance().schoolArray.size(); i++) {
                                    if (Singleton.getInstance().profileArrayList.get(0).getSchoolId().equals(Singleton.getInstance().schoolArray.get(i).schoolId)) {
                                        strSchool = Singleton.getInstance().schoolArray.get(i).schoolId;
                                        spnSchool.setSelection(i);
                                    }
                                }
                            }
                        }


                        for (int i = 0; i < Singleton.getInstance().studentArray.size(); i++) {
                            if (Singleton.getInstance().profileArrayList.get(0).getPositionId().equals(Singleton.getInstance().studentArray.get(i).schoolId)) {
                                strClassification = Singleton.getInstance().studentArray.get(i).schoolId;
                                spnClassification.setSelection(i);
                            }
                        }

                        //spnClassification
                        if (Singleton.getInstance().profileArrayList.size() > 0) {
                            if (!Singleton.getInstance().profileArrayList.get(0).getGender().isEmpty()) {
                                if (Singleton.getInstance().profileArrayList.get(0).getGender().equals("Male")) {
                                    strGender = "Male";
                                    spnGender.setSelection(1);
                                } else if (Singleton.getInstance().profileArrayList.get(0).getGender().equals("Female")) {
                                    strGender = "Female";
                                    spnGender.setSelection(2);
                                } else {
                                    strGender = "";
                                    spnGender.setSelection(0);
                                }
                            }
                        }
                    }
                } else {
                }
            }
        });
        getpersonalAsyncTask.execute();
    }

    //data for school spinner
    private void schoolListAPI() {
        if (Utilities.isOnline(context)) {
            CommonAsynTask schoolListAsync = new CommonAsynTask(context, Constants.SCHOOLLIST_URL, "", Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
                @Override
                public void onTaskCompleted(JSONObject jsonObject) {

                    if (jsonObject != null) {
                        if (jsonObject.has(Constants.MESSAGE)) {
                            String message = jsonObject.optString(Constants.MESSAGE);
                            if (message.equals("message success")) {
                                JSONArray jsonArray = jsonObject.optJSONArray(Constants.DATA);
                                Singleton.getInstance().schoolArray.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    SchoolListModel schoolListModel = new SchoolListModel();
                                    JSONObject dataObject = jsonArray.optJSONObject(i);
                                    schoolListModel.schoolId = dataObject.optString(Constants.SCHOOLID);
                                    schoolListModel.schoolName = dataObject.optString(Constants.SCHOOLNAME);
                                    Singleton.getInstance().schoolArray.add(schoolListModel);

                                }
                                SchoolListModel schoolModelDef = new SchoolListModel();
                                schoolModelDef.schoolId = "0";
                                schoolModelDef.schoolName = "Tap Here";
                                Singleton.getInstance().schoolArray.add(schoolModelDef);
                                SchoolSpinnerAdapter adapterSchool = new SchoolSpinnerAdapter(layoutInflater, Singleton.getInstance().schoolArray);
                                spnSchool.setAdapter(adapterSchool);
                                //spnSchool.setSelection(0);
                                spnSchool.setSelection(adapterSchool.getCount());
                            }
                        }
                    }

                }
            });
            schoolListAsync.execute();

        }
    }

    private void facultyListAPI() {
        if (Utilities.isOnline(context)) {
            CommonAsynTask schoolListAsync = new CommonAsynTask(context, Constants.FACULTY_POSITION_URL, "", Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
                @Override
                public void onTaskCompleted(JSONObject jsonObject) {

                    if (jsonObject != null) {
                        if (jsonObject.has(Constants.MESSAGE)) {
                            String message = jsonObject.optString(Constants.MESSAGE);
                            if (message.equals("message success")) {
                                Singleton.getInstance().studentArray.clear();
                                SchoolListModel schoolModelDefault = new SchoolListModel();
                                schoolModelDefault.schoolId = "0";
                                schoolModelDefault.schoolName = "Tap Here";
                                Singleton.getInstance().studentArray.add(schoolModelDefault);
                                JSONArray jsonArray = jsonObject.optJSONArray(Constants.DATA);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SchoolListModel schoolListModel = new SchoolListModel();
                                    JSONObject dataObject = jsonArray.optJSONObject(i);
                                    schoolListModel.schoolId = dataObject.optString(Constants.SCHOOL_FACULTY_POSITION_ID);
                                    schoolListModel.schoolName = dataObject.optString(Constants.POSITION);
                                    Singleton.getInstance().studentArray.add(schoolListModel);
                                }
                                SchoolSpinnerAdapter adapterFaculty = new SchoolSpinnerAdapter(layoutInflater, Singleton.getInstance().studentArray);
                                spnClassification.setAdapter(adapterFaculty);
                                spnClassification.setSelection(0);
                            }
                        }
                    }

                }
            });
            schoolListAsync.execute();
        }
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

    private static String getData() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return objData.toString();
    }

    //dialog onclick imageview
    private void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PersonalDetails_Activity.this, R.style.alertDialog));

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_choose_image, null);
        Button buttonCamera = (Button) alertLayout.findViewById(R.id.camera);
        Button buttonGallery = (Button) alertLayout.findViewById(R.id.gallery);
        Button cancel = (Button) alertLayout.findViewById(R.id.cancel);
        builder.setView(alertLayout);


        final AlertDialog dialog = builder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.PERSONAL_CAMERA_RECALL = true;
                Constants.REG_LOAD = true;
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_CAMERA);
                dialog.dismiss();
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.REG_LOAD = true;
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECT_FILE);
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Hide keyboard
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }


    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);


                byte[] attachmentBytes = bytes.toByteArray();
                byteProfilePic = attachmentBytes;
                //  imageToBase64String = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);

                Uri imageUri = data.getData();
                String imagePath = getRealPathFromURI(imageUri);
                Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, myBitmap);
                Constants.bitmapPersonalImage = orientedBitmap;
                imgProfile.setImageBitmap(orientedBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //imgProfile.setImageBitmap(bm);
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        byte[] attachmentBytes = bytes.toByteArray();
        byteProfilePic = attachmentBytes;

        imageToBase64String = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constants.bitmapPersonalImage = thumbnail;
        imgProfile.setImageBitmap(thumbnail);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putString("photopath", imageToBase64String);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("photopath")) {
                imageToBase64String = savedInstanceState.getString("photopath");
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    public class DateListeners implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear - 1) + "/" + dayOfMonth + "/" + year;
            txtDOB.setText(date);


            if (dayOfMonth < 10) {
                if (String.valueOf(dayOfMonth).length() == 1) {

                    txtDOB.setText((++monthOfYear - 1) + "/0" + dayOfMonth + "/" + year);
                } else {
                    txtDOB.setText((++monthOfYear - 1) + "/" + dayOfMonth + "/" + year);
                }
            } else {
                txtDOB.setText((++monthOfYear - 1) + "/" + dayOfMonth + "/" + year);
            }
        }
    }
}