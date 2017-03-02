package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.MyApplication;

import static cab.com.rideversitydriver.R.id.textView_terms_of_service;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    MyApplication application= null;
    TextView txtTermofService;
    RelativeLayout layoutRegisteremail,layoutLogin;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context=this;
        application = (MyApplication) getApplication();
        txtTermofService=(TextView)findViewById(textView_terms_of_service);
        layoutRegisteremail=(RelativeLayout)findViewById(R.id.layout_register);
        layoutLogin=(RelativeLayout)findViewById(R.id.layout_login);
        layoutRegisteremail.setOnClickListener(this);
        layoutLogin.setOnClickListener(this);
        txtTermofService.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_register:
                Constants.REG_LOAD=false;
                Constants.PERSONAL_VEHICLE_CAMERA_RECALL=false;
                Constants.CAMERA_RECALL=false;
                ClearAllBitmaps();
                CLearValues();
                Intent registerIntent=new Intent(context,Registration_Activity.class);
                startActivity(registerIntent);
                break;
            case R.id.layout_login:
                Intent loginIntent=new Intent(context,Login_Activity.class);
                startActivity(loginIntent);
                //finish();
                break;
            case R.id.textView_terms_of_service:
                Intent intentTerms = new Intent(context, TermsandCondition_Activity.class);
                startActivity(intentTerms);
                break;
            default:
                break;
        }
    }
    public void CLearValues()
    {
        Constants.strDriverLicenseNumber="";
        Constants.strDriverLicenceExpDate="";
        Constants.strInsuranceExpdate="";
        Constants.strinsuranceNumber="";
        Constants.strTagNumber="";
    }

    public void ClearAllBitmaps()
    {
        if (Constants.bitmapRegProfile != null) {
            Constants.bitmapRegProfile.recycle();
            Constants.bitmapRegProfile = null;
        }
        if (Constants.thumbnailDriverLicence != null) {
            Constants.thumbnailDriverLicence.recycle();
            Constants.thumbnailDriverLicence = null;
        }
        if (Constants.bitmapCarproof != null) {
            Constants.bitmapCarproof.recycle();
            Constants.bitmapCarproof = null;
        }
        if (Constants.bitmapVehicle != null) {
            Constants.bitmapVehicle.recycle();
            Constants.bitmapVehicle = null;
        }
        if (Constants.bitmapRegproof != null) {
            Constants.bitmapRegproof.recycle();
            Constants.bitmapRegproof = null;
        }


        if (VehicleDocReg_Activity.bitmapFront != null) {
            VehicleDocReg_Activity.bitmapFront.recycle();
            VehicleDocReg_Activity.bitmapFront = null;
        }
        if (VehicleDocReg_Activity.bitmapRear != null) {
            VehicleDocReg_Activity.bitmapRear.recycle();
            VehicleDocReg_Activity.bitmapRear = null;
        }
        if (VehicleDocReg_Activity.bitmapSide != null) {
            VehicleDocReg_Activity.bitmapSide.recycle();
            VehicleDocReg_Activity.bitmapSide = null;
        }


        //Registration_two_Activity.bitmapDriver.recycle();
        if (Registration_two_Activity.bitmapDriver != null) {
            Registration_two_Activity.bitmapDriver.recycle();
            Registration_two_Activity.bitmapDriver = null;
        }
        if (Registration_two_Activity.bitmapCarproof != null) {
            Registration_two_Activity.bitmapCarproof.recycle();
            Registration_two_Activity.bitmapCarproof = null;
        }
        if (Registration_two_Activity.bitmapVehicle != null) {
            Registration_two_Activity.bitmapVehicle.recycle();
            Registration_two_Activity.bitmapVehicle = null;
        }
        if (Registration_two_Activity.bitmapRegproof != null) {
            Registration_two_Activity.bitmapRegproof.recycle();
            Registration_two_Activity.bitmapRegproof = null;
        }

        if (VehicleDocReg_Activity.bitmapFront != null) {
            VehicleDocReg_Activity.bitmapFront.recycle();
            VehicleDocReg_Activity.bitmapFront = null;
        }
        if (VehicleDocReg_Activity.bitmapRear != null) {
            VehicleDocReg_Activity.bitmapRear.recycle();
            VehicleDocReg_Activity.bitmapRear = null;
        }
        if (VehicleDocReg_Activity.bitmapSide != null) {
            VehicleDocReg_Activity.bitmapSide.recycle();
            VehicleDocReg_Activity.bitmapSide = null;
        }
    }
    @Override
    public void onBackPressed() {
        // finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

