package cab.com.rideversitydriver.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
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
import java.util.regex.Pattern;

import cab.com.rideversitydriver.Adapters.GenderSpinnerAdapter;
import cab.com.rideversitydriver.Adapters.SchoolSpinnerAdapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.SchoolListModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.PolygonImageView;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.ExifUtil;
import cab.com.rideversitydriver.Utils.MySpinner;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.UsPhoneNumberFormatter;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 05/08/16.
 */
public class Registration_Activity extends AppCompatActivity implements View.OnClickListener {

    static Activity activity;
    Context context;
    Button btnNext;
    TextView textView_dateofbirth;
    EditText edtConfirmPassword, edtPassword, edtFirstname, edtLastname, edtUsername, edtPhonenumber, edtSchoolemail, edtMailindaddress, edtStreetdaddress, edtLocality, edtRegion, edtPostalCode, edtSocialsecurity, edtStudentid;
    MySpinner spnGender, spnSchoolname;
    //public static String strFirstname,strLastname,strUsername,strPhonenumber,strSchoolemail,strMailindaddress,strSocialsecurity,strStudentid;
    Snackbar snackbar;
    LinearLayout layoutMain;
    PolygonImageView imgProfile;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 2;
    public static byte[] byteProfilePic;
    boolean spinnerTouch = false;
    int iCurrentSelection;
    private String gender = "", schoolName = "";
    private LayoutInflater layoutInflater;
    private CheckBox checkBoxAgree, checkBoxLicense;
    public static Bitmap bitmapDriverImage = null;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.driver_information);
        activity = this;
        context = this;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutMain = (LinearLayout) findViewById(R.id.layout_driver_information);
        edtFirstname = (EditText) findViewById(R.id.editText_driver_information_firstname);
        edtLastname = (EditText) findViewById(R.id.editText_driver_information_lastname);
        edtUsername = (EditText) findViewById(R.id.editText_driver_information_username);
        edtPhonenumber = (EditText) findViewById(R.id.Edittext_driver_information_phonenumber);

        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(edtPhonenumber));
        edtPhonenumber.addTextChangedListener(addLineNumberFormatter);

        edtSchoolemail = (EditText) findViewById(R.id.Edittext_driver_information_school_email);
        edtStreetdaddress = (EditText) findViewById(R.id.Edittext_street_address);
        edtLocality = (EditText) findViewById(R.id.edittext_Locality);
        edtRegion = (EditText) findViewById(R.id.edittext_Region);
        edtPostalCode = (EditText) findViewById(R.id.edittext_postal_code);
        edtSocialsecurity = (EditText) findViewById(R.id.Edittext_driver_information_social_security_number);
        edtStudentid = (EditText) findViewById(R.id.Edittext_driver_information_student_id);
        spnGender = (MySpinner) findViewById(R.id.spinner_driver_information_gender);
        spnSchoolname = (MySpinner) findViewById(R.id.spinner_school);
        checkBoxLicense = (CheckBox) findViewById(R.id.checkbox_license);
        checkBoxAgree = (CheckBox) findViewById(R.id.checkbox_agree);
        imgProfile = (PolygonImageView) findViewById(R.id.imageView_driver_information);
        imgProfile.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.button_next);
        btnNext.setOnClickListener(this);

        edtPassword = (EditText) findViewById(R.id.Edittext_driver_information_password);
        edtConfirmPassword = (EditText) findViewById(R.id.Edittext_driver_information_confirm_password);

        RelativeLayout backArrow = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        backArrow.setOnClickListener(this);

        textView_dateofbirth = (TextView) findViewById(R.id.textview_dateofbirth);
        textView_dateofbirth.setOnClickListener(this);

        checkAndRequestPermissions();

        TextView txtAgree = (TextView) findViewById(R.id.textView_agree);
        txtAgree.setTextColor(ContextCompat.getColor(context, R.color.black));
        //txtAgree.setLinkTextColor(Color.RED);
        SpannableString ss = new SpannableString("I AGREE to the Terms & Conditions");
        ss.setSpan(new URLSpan("http://www.android-examples.com/"), 0, 33,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtAgree.setText(ss);
        //txtAgree.setTextColor(ContextCompat.getResources().getColor(R.color.black));

        txtAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTerms = new Intent(context, TermsandCondition_Activity.class);
                startActivity(intentTerms);
            }
        });

        if (Constants.REG_LOAD == false) {
            if (Utilities.isOnline(context)) {
                schoolListAPI();
            } else {
                Utilities.snackbarNoInternetTwo(context, layoutMain);
            }
        } else {
            imgProfile.setImageBitmap(Constants.bitmapRegProfile);
        }


        Singleton.getInstance().genderArray.clear();
        SchoolListModel schoolModelDefaults = new SchoolListModel();
        schoolModelDefaults.schoolId = "1";
        schoolModelDefaults.schoolName = "Male";
        Singleton.getInstance().genderArray.add(schoolModelDefaults);

        SchoolListModel schoolModelDefaultss = new SchoolListModel();
        schoolModelDefaultss.schoolId = "2";
        schoolModelDefaultss.schoolName = "Female";
        Singleton.getInstance().genderArray.add(schoolModelDefaultss);

        SchoolListModel schoolModelDefault = new SchoolListModel();
        schoolModelDefault.schoolId = "0";
        schoolModelDefault.schoolName = "Tap Here";
        Singleton.getInstance().genderArray.add(schoolModelDefault);

        GenderSpinnerAdapter adaptergender = new GenderSpinnerAdapter(layoutInflater, Singleton.getInstance().genderArray);
        spnGender.setAdapter(adaptergender);
        spnGender.setSelection(adaptergender.getCount());

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
                    gender = "Male";
                    Constants.GENDERS = "Male";
                }
                if (position == 1) {
                    gender = "Female";
                    Constants.GENDERS = "Female";
                }


                if (iCurrentSelection != position) {
                    spnGender.setBackgroundResource(R.drawable.spinner_down);
                }
                iCurrentSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spnGender.setBackgroundResource(R.drawable.spinner_up);
            }
        });

        spnSchoolname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (spinnerTouch == false) {
                        spinnerTouch = true;
                        spnSchoolname.setBackgroundResource(R.drawable.spinner_up);
                    } else {
                        spinnerTouch = false;
                        spnSchoolname.setBackgroundResource(R.drawable.spinner_down);
                    }
                }
                return false;
            }
        });
        iCurrentSelection = spnSchoolname.getSelectedItemPosition();
        spnSchoolname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTouch = false;
                Constants.SCHOOL_ID = Singleton.getInstance().schoolArray.get(position).schoolId;
                if (iCurrentSelection != position) {
                    spnSchoolname.setBackgroundResource(R.drawable.spinner_down);
                }
                iCurrentSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spnSchoolname.setBackgroundResource(R.drawable.spinner_up);
            }
        });

        if (Singleton.getInstance().schoolArray.size() > 0) {
            SchoolSpinnerAdapter adapterSchool = new SchoolSpinnerAdapter(layoutInflater, Singleton.getInstance().schoolArray);
            spnSchoolname.setAdapter(adapterSchool);
            //spnSchoolname.setSelection(0);
            spnSchoolname.setSelection(adapterSchool.getCount());
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imageView_backarrow:
                //Constants.PERSONAL_VEHICLE_CAMERA_RECALL=false;
                finish();
                break;
            case R.id.imageView_driver_information:
                Constants.REGISTRATION_ONE = 11;
                selectImage();
                break;

            case R.id.textview_dateofbirth:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DateListeners(),
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                //dpd.setMinDate(now);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.setAccentColor(Color.parseColor("#278455"));
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;

            case R.id.button_next:
                Constants.strFirstname = edtFirstname.getText().toString().trim();
                Constants.strLastname = edtLastname.getText().toString().trim();
                Constants.strUsername = edtUsername.getText().toString().trim();
                Constants.strPhonenumber = edtPhonenumber.getText().toString().trim();
                Constants.strSchoolemail = edtSchoolemail.getText().toString().trim();
                Constants.strStreetAddress = edtStreetdaddress.getText().toString().trim();
                Constants.strLocality = edtLocality.getText().toString().trim();
                Constants.strRegion = edtRegion.getText().toString().trim();
                Constants.strPostalCode = edtPostalCode.getText().toString().trim();
                Constants.strSocialsecurity = edtSocialsecurity.getText().toString().trim();
                Constants.strStudentid = edtStudentid.getText().toString().trim();
                Constants.strPassword = edtPassword.getText().toString().trim();
                Constants.strConfirmPassword = edtConfirmPassword.getText().toString().trim();
                Constants.strDateOfBirth = textView_dateofbirth.getText().toString().trim();

                bitmapDriverImage = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();

                if (Constants.strFirstname.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Your First Name", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strFirstname.length() < 2 || Constants.strFirstname.length() > 15) {
                    snackbar = Snackbar.make(layoutMain, "First Name should be at least 2 characters and maximum 15 characters", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strLastname.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Your Last Name", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strLastname.length() < 2 || Constants.strLastname.length() > 15) {
                    snackbar = Snackbar.make(layoutMain, "Last Name should be at least 2 characters and maximum 15 characters", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strUsername.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Your User Name", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strUsername.length() < 3 || Constants.strUsername.length() > 15) {
                    snackbar = Snackbar.make(layoutMain, "Username should be at least 3 characters and maximum 15 characters", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strPhonenumber.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Your Phone Number", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strSchoolemail.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter School Email Id", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (!Utilities.isValidMail(Constants.strSchoolemail)) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Valid School Email Id", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strStreetAddress.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Street Address", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strLocality.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter City Name", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strRegion.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter State Name", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strPostalCode.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter ZIP Code", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strDateOfBirth.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Date of birth", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.GENDERS.equals("Tap Here")) {
                    snackbar = Snackbar.make(layoutMain, "Please Select Gender", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.SCHOOL_ID.equals("Tap Here")) {
                    snackbar = Snackbar.make(layoutMain, "Please Select School Name", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strSocialsecurity.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter SSN Number", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strSocialsecurity.length() > 9 || Constants.strSocialsecurity.length() < 9) {
                    snackbar = Snackbar.make(layoutMain, "Your SSN number must be 9 digits", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strStudentid.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Student ID", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strPassword.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Password", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strPassword.length() < 6 || Constants.strPassword.length() > 10) {
                    snackbar = Snackbar.make(layoutMain, "Password should be at least 6 characters and maximum 10 characters", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (isLegalPassword(Constants.strPassword) == false) {
//                   snackbar = Snackbar.make(layoutMain, "Password must include any three combination (i.e) uppercase letters(A-Z), lowercase letters(a-z), numbers(0-9), symbols(such as !, #, $, %).", Snackbar.LENGTH_LONG);
                    snackbar = Snackbar.make(layoutMain, "Password Must Be 6-10 Characters & Include The Following: Uppercase Letter Lowercase Letter Number", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setMaxLines(15);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (!Constants.strPassword.equals(Constants.strConfirmPassword)) {
                    snackbar = Snackbar.make(layoutMain, "Password and Confirm password should be same", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (!checkBoxLicense.isChecked()) {
                    snackbar = Snackbar.make(layoutMain, "Agree Consent to run a license & Background", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (!checkBoxAgree.isChecked()) {
                    snackbar = Snackbar.make(layoutMain, "Agree Terms And Condition", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else {
                    Intent intent = new Intent(context, Registration_two_Activity.class);
                    startActivity(intent);
                }

                break;
            default:
                break;
        }
    }


    private static final Pattern[] passwordRegexes = new Pattern[4];

    {
        passwordRegexes[0] = Pattern.compile(".*[A-Z].*");
        passwordRegexes[1] = Pattern.compile(".*[a-z].*");
        passwordRegexes[2] = Pattern.compile(".*\\d.*");
        passwordRegexes[3] = Pattern.compile(".*[~!].*");
    }

    public boolean isLegalPassword(String pass) {

        if (passwordRegexes[0].matcher(pass).matches() &&
                passwordRegexes[1].matcher(pass).matches()
                && passwordRegexes[2].matcher(pass).matches()) {
            return true;
        } else if (passwordRegexes[1].matcher(pass).matches()
                && passwordRegexes[2].matcher(pass).matches()
                && passwordRegexes[3].matcher(pass).matches()) {
            return true;
        } else if (passwordRegexes[2].matcher(pass).matches()
                && passwordRegexes[3].matcher(pass).matches()
                && passwordRegexes[0].matcher(pass).matches()) {
            return true;
        }

        return false;
    }

    private void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_choose_image, null);
        Button buttonCamera = (Button) alertLayout.findViewById(R.id.camera);
        Button buttonGallery = (Button) alertLayout.findViewById(R.id.gallery);
        Button cancel = (Button) alertLayout.findViewById(R.id.cancel);
        builder.setView(alertLayout);

        final boolean result = checkCameraPermission() && checkSDCardPermission();


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
                Constants.CAMERA_RECALL = true;
                Constants.REG_LOAD = true;
                dialog.dismiss();
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //BecomeDriver_Fragment.activity.startActivityForResult(takePicture, REQUEST_CAMERA);
                startActivityForResult(takePicture, REQUEST_CAMERA);
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Constants.CAMERA_RECALL = true;
                    Constants.REG_LOAD = true;
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(pickPhoto, SELECT_FILE);
                    startActivityForResult(pickPhoto, SELECT_FILE);
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();


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
                Uri imageUris = data.getData();
                String imagePath = getRealPathFromURI(imageUris);
                Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, myBitmap);
                Constants.bitmapRegProfile = orientedBitmap;
                imgProfile.setImageBitmap(Constants.bitmapRegProfile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //imgProfile.setImageBitmap(bm);
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        Constants.bitmapRegProfile = thumbnail;

        byte[] attachmentBytes = bytes.toByteArray();
        byteProfilePic = attachmentBytes;

        //imageToBase64String = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);

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
        imgProfile.setImageBitmap(Constants.bitmapRegProfile);
    }

    private boolean checkCameraPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSDCardPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

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
                                spnSchoolname.setAdapter(adapterSchool);
                                // spnSchoolname.setSelection(0);
                                spnSchoolname.setSelection(adapterSchool.getCount());
                            }
                        }
                    }

                }
            });
            schoolListAsync.execute();

        }
    }

    private boolean checkAndRequestPermissions() {

        int cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int readPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    public class DateListeners implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear - 1) + "/" + dayOfMonth + "/" + year;
            textView_dateofbirth.setText(date);

            if (dayOfMonth < 10) {
                if (String.valueOf(dayOfMonth).length() == 1) {
                    textView_dateofbirth.setText((++monthOfYear - 1) + "/0" + dayOfMonth + "/" + year);
                } else {
                    textView_dateofbirth.setText((++monthOfYear - 1) + "/" + dayOfMonth + "/" + year);
                }
            } else {
                textView_dateofbirth.setText((++monthOfYear - 1) + "/" + dayOfMonth + "/" + year);
            }
        }
    }
}
