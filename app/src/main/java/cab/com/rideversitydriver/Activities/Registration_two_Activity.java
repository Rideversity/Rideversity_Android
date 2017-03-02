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
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.PolygonImageView;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.DatePicker;
import cab.com.rideversitydriver.Utils.ExifUtil;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by Kalidoss on 27/10/16.
 */
public class Registration_two_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button next;
    public static PolygonImageView imgDriver, imgCarProof, imgVehicle, imgRegProof;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 2;
    public static int imageclick = 10;
    static Activity activity;
    Context context;
    private TextView upload1, upload2, upload3, upload4;
    EditText edtDriverLicenseNumber, getEdtDriverLicenseExpirationDate, edtInsuranceNumber, edtInsuranceExpirationDate, edtTagNumber;
    Snackbar snackbar;
    LinearLayout layoutMain;
    TextView txtDriverLicenseExpirationDate, txtInsuranceExpirationDate;
    public static byte[] byteDriverLic = null, byteCarInsurance = null, byteVehicaltag = null, byteRegistration = null;
    Uri imageUri1;
    private Uri mImageFileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Rideversity";
    private ArrayList<String> capturedImageUris;
    public static Bitmap bitmapDriver = null, bitmapCarproof = null, bitmapVehicle = null, bitmapRegproof = null;
    boolean driver = false, carinsurance = false, vehicletag = false, regproof = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.become_a_driver);
        context = this;
        next = (Button) findViewById(R.id.button_next);
        imgDriver = (PolygonImageView) findViewById(R.id.image_driver);
        imgCarProof = (PolygonImageView) findViewById(R.id.imageView_carproof);
        imgVehicle = (PolygonImageView) findViewById(R.id.imageView_vehicle);
        imgRegProof = (PolygonImageView) findViewById(R.id.imageView_regproof);
        upload1 = (TextView) findViewById(R.id.upload1);
        upload2 = (TextView) findViewById(R.id.upload2);
        upload3 = (TextView) findViewById(R.id.upload3);
        upload4 = (TextView) findViewById(R.id.upload4);
        layoutMain = (LinearLayout) findViewById(R.id.layout_becomedriver_one);
        edtDriverLicenseNumber = (EditText) findViewById(R.id.Edittext_driver_license_number);
        edtInsuranceNumber = (EditText) findViewById(R.id.edittext_insurance_number);
        edtTagNumber = (EditText) findViewById(R.id.Edittext_tag_number);
        txtDriverLicenseExpirationDate = (TextView) findViewById(R.id.textview_driver_license_exp_date);
        txtInsuranceExpirationDate = (TextView) findViewById(R.id.textview_insurance_exp_date);
        txtDriverLicenseExpirationDate.setOnClickListener(this);
        txtInsuranceExpirationDate.setOnClickListener(this);
        imgDriver.setOnClickListener(this);
        imgVehicle.setOnClickListener(this);
        imgCarProof.setOnClickListener(this);
        imgRegProof.setOnClickListener(this);
        upload1.setOnClickListener(this);
        upload2.setOnClickListener(this);
        upload3.setOnClickListener(this);
        upload4.setOnClickListener(this);
        next.setOnClickListener(this);

        RelativeLayout backArrow = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        backArrow.setOnClickListener(this);

        String device_name = Build.MANUFACTURER;

        if (Constants.CAMERA_RECALL == true) {
            if (Constants.thumbnailDriverLicence != null) {
                imgDriver.setImageBitmap(Constants.thumbnailDriverLicence);
            }
            if (Constants.bitmapCarproof != null) {
                imgCarProof.setImageBitmap(Constants.bitmapCarproof);
            }
            if (Constants.bitmapVehicle != null) {
                imgVehicle.setImageBitmap(Constants.bitmapVehicle);
            }
            if (Constants.bitmapRegproof != null) {
                imgRegProof.setImageBitmap(Constants.bitmapRegproof);
            }
            edtDriverLicenseNumber.setText(Constants.strDriverLicenseNumber);
            txtDriverLicenseExpirationDate.setText(Constants.strDriverLicenceExpDate);
            txtInsuranceExpirationDate.setText(Constants.strInsuranceExpdate);
            edtInsuranceNumber.setText(Constants.strinsuranceNumber);
            edtTagNumber.setText(Constants.strTagNumber);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_driver_license_exp_date:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DateListeners(),
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                dpd.setMinDate(now);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.setAccentColor(Color.parseColor("#278455"));
                dpd.setTitle(sdf.format(d));
                dpd.show(getFragmentManager(), "Datepickerdialog");
                //callDatePicker(txtDriverLicenseExpirationDate);
                break;

            case R.id.textview_insurance_exp_date:
                Calendar now2 = Calendar.getInstance();
                DatePickerDialog dpd2 = DatePickerDialog.newInstance(
                        new DatesListenersSecond(),
                        now2.get(Calendar.YEAR),
                        now2.get(Calendar.MONTH),
                        now2.get(Calendar.DAY_OF_MONTH)
                );
                SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
                Date d2 = new Date();
                dpd2.setMinDate(now2);
                dpd2.vibrate(true);
                dpd2.dismissOnPause(true);
                dpd2.setAccentColor(Color.parseColor("#278455"));
                dpd2.setTitle(sdf2.format(d2));
                dpd2.show(getFragmentManager(), "Datepickerdialog");
                //callDatePicker(txtInsuranceExpirationDate);
                break;


            case R.id.button_next:

                Constants.strDriverLicenseNo = edtDriverLicenseNumber.getText().toString();
                Constants.strLicenseExpDate = txtDriverLicenseExpirationDate.getText().toString();
                Constants.strInsuranceExpDate = txtInsuranceExpirationDate.getText().toString();
                Constants.strInsuranceNo = edtInsuranceNumber.getText().toString();
                Constants.strTagno = edtTagNumber.getText().toString();


                Bitmap bitmapEmpty = BitmapFactory.decodeResource(context.getResources(), R.drawable.pic_upload_icon);
                bitmapDriver = ((BitmapDrawable) imgDriver.getDrawable()).getBitmap();
                bitmapCarproof = ((BitmapDrawable) imgCarProof.getDrawable()).getBitmap();
                bitmapVehicle = ((BitmapDrawable) imgVehicle.getDrawable()).getBitmap();
                bitmapRegproof = ((BitmapDrawable) imgRegProof.getDrawable()).getBitmap();

                driver = Utilities.compareBitmap(bitmapEmpty, bitmapDriver);
                carinsurance = Utilities.compareBitmap(bitmapEmpty, bitmapCarproof);
                vehicletag = Utilities.compareBitmap(bitmapEmpty, bitmapVehicle);
                regproof = Utilities.compareBitmap(bitmapEmpty, bitmapRegproof);

                if (driver == true) {
                    snackbar = Snackbar.make(layoutMain, "Please Upload License Image", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strDriverLicenseNo.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter License Number", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strLicenseExpDate.equals("Tap Here")) {
                    snackbar = Snackbar.make(layoutMain, "Please Select License Expiration Date", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (carinsurance == true) {
                    snackbar = Snackbar.make(layoutMain, "Please Upload Car Insurance Image", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();

                } else if (Constants.strInsuranceExpDate.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Select Insurance Expiration Date", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strInsuranceNo.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Insurance Number", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (vehicletag == true) {
                    snackbar = Snackbar.make(layoutMain, "Please Upload Vehicle Tag Image", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (Constants.strTagno.isEmpty()) {
                    snackbar = Snackbar.make(layoutMain, "Please Enter Tag Number", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else if (regproof == true) {
                    snackbar = Snackbar.make(layoutMain, "Please Upload Proof Of Registration Image", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else {
                    Intent intents = new Intent(context, VehicleDocReg_Activity.class);
                    startActivity(intents);
                }

                break;
            case R.id.upload1:
                imageclick = 11;
                Constants.CAMERA_ONE = 11;
                selectImage();
                break;
            case R.id.upload2:
                imageclick = 12;
                Constants.CAMERA_ONE = 12;
                selectImage();
                break;
            case R.id.upload3:
                imageclick = 13;
                Constants.CAMERA_ONE = 13;
                selectImage();
                break;
            case R.id.upload4:
                imageclick = 14;
                Constants.CAMERA_ONE = 14;
                selectImage();
                break;
            case R.id.imageView_backarrow:
                Constants.PERSONAL_VEHICLE_CAMERA_RECALL = false;
                finish();
                break;
            default:
                break;
        }
    }

    private void callDatePicker(TextView view) {
        DatePicker dialog = new DatePicker(view);
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        dialog.show(fragmentTransaction, "DatePicker");
    }

    public class DateListeners implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (monthOfYear) + 1 + "/" + dayOfMonth + "/" + year;
            txtDriverLicenseExpirationDate.setText(date);
            if (dayOfMonth < 10) {
                if (String.valueOf(dayOfMonth).length() == 1) {
                    txtDriverLicenseExpirationDate.setText(((monthOfYear) + 1) + "/0" + dayOfMonth + "/" + year);
                } else {
                    txtDriverLicenseExpirationDate.setText(((monthOfYear) + 1) + "/" + dayOfMonth + "/" + year);
                }
            } else {
                txtDriverLicenseExpirationDate.setText(((monthOfYear) + 1) + "/" + dayOfMonth + "/" + year);
            }
        }
    }

    public class DatesListenersSecond implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
            txtInsuranceExpirationDate.setText(date);

            if (dayOfMonth < 10) {
                if (String.valueOf(dayOfMonth).length() == 1) {
                    txtInsuranceExpirationDate.setText(((monthOfYear) + 1) + "/0" + dayOfMonth + "/" + year);
                } else {
                    txtInsuranceExpirationDate.setText(((monthOfYear) + 1) + "/" + dayOfMonth + "/" + year);
                }
            } else {
                txtInsuranceExpirationDate.setText(((monthOfYear) + 1) + "/" + dayOfMonth + "/" + year);
            }
        }
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

        Constants.strDriverLicenseNumber = edtDriverLicenseNumber.getText().toString();
        Constants.strDriverLicenceExpDate = txtDriverLicenseExpirationDate.getText().toString();
        Constants.strInsuranceExpdate = txtInsuranceExpirationDate.getText().toString();
        Constants.strinsuranceNumber = edtInsuranceNumber.getText().toString();
        Constants.strTagNumber = edtTagNumber.getText().toString();


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
                dialog.dismiss();
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //BecomeDriver_Fragment.activity.startActivityForResult(takePicture, REQUEST_CAMERA);
                startActivityForResult(takePicture, REQUEST_CAMERA);
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.CAMERA_RECALL = true;
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECT_FILE);
                dialog.dismiss();

            }
        });
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            if (requestCode == SELECT_FILE) {
                if (Constants.CAMERA_ONE == 11) {
                    Uri imageUri = data.getData();
                    String imagePath = getRealPathFromURI(imageUri);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, myBitmap);
                    Constants.thumbnailDriverLicence = orientedBitmap;
                    imgDriver.setImageBitmap(orientedBitmap);
                } else if (Constants.CAMERA_ONE == 12) {
                    Uri imageUri = data.getData();
                    String imagePath = getRealPathFromURI(imageUri);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, myBitmap);
                    Constants.bitmapCarproof = orientedBitmap;
                    imgCarProof.setImageBitmap(orientedBitmap);
                } else if (Constants.CAMERA_ONE == 13) {
                    Uri imageUri = data.getData();
                    String imagePath = getRealPathFromURI(imageUri);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, myBitmap);
                    Constants.bitmapVehicle = orientedBitmap;
                    imgVehicle.setImageBitmap(orientedBitmap);
                } else if (Constants.CAMERA_ONE == 14) {
                    Uri imageUri = data.getData();
                    String imagePath = getRealPathFromURI(imageUri);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, myBitmap);
                    Constants.bitmapRegproof = orientedBitmap;
                    imgRegProof.setImageBitmap(orientedBitmap);
                }
            } else if (requestCode == REQUEST_CAMERA) {

                if (Constants.CAMERA_ONE == 11) {
                    //imgDriver.setImageBitmap(imgBitmap);
                    Constants.uriDriverLicence = data.getData();
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Constants.thumbnailDriverLicence = thumbnail;
                    imgDriver.setImageBitmap(thumbnail);
                    //strDriverLicense=getEncoded64ImageStringFromBitmap(onSelectFromGalleryResult(data));
                } else if (Constants.CAMERA_ONE == 12) {
                    Constants.uriDriverLicence = data.getData();
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Constants.bitmapCarproof = thumbnail;
                    imgCarProof.setImageBitmap(thumbnail);
                } else if (Constants.CAMERA_ONE == 13) {
                    Constants.uriDriverLicence = data.getData();
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Constants.bitmapVehicle = thumbnail;
                    imgVehicle.setImageBitmap(thumbnail);
                } else if (Constants.CAMERA_ONE == 14) {
                    Constants.uriDriverLicence = data.getData();
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Constants.bitmapRegproof = thumbnail;
                    imgRegProof.setImageBitmap(thumbnail);
                }
            }
        }
    }


    private Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                byte[] attachmentBytes = bytes.toByteArray();
                //  imageToBase64String = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);


                if (imageclick == 11) {
                    // bmDriverLicense=bm;
                    byteDriverLic = attachmentBytes;
                    // imageUri1==getIntent().getData();
                } else if (imageclick == 12) {
                    //bmCarInsurance=bm;
                    byteCarInsurance = attachmentBytes;
                } else if (imageclick == 13) {
                    //bmVehicaltag=bm;
                    byteVehicaltag = attachmentBytes;
                } else if (imageclick == 14) {
                    //bmRegistration=bm;
                    byteRegistration = attachmentBytes;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    private Bitmap onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        byte[] attachmentBytes = bytes.toByteArray();
        //imageToBase64String = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        //File myFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), fileName);
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

        if (imageclick == 11) {
            //bmDriverLicense=thumbnail;
            byteDriverLic = attachmentBytes;
        } else if (imageclick == 12) {
            // bmCarInsurance=thumbnail;
            byteCarInsurance = attachmentBytes;
        } else if (imageclick == 13) {
            // bmVehicaltag=thumbnail;
            byteVehicaltag = attachmentBytes;
        } else if (imageclick == 14) {
            //bmRegistration=thumbnail;
            byteRegistration = attachmentBytes;
        }
        return thumbnail;
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (imageUri1 != null) {
            outState.putString("cameraImageUri", Constants.uriDriverLicence.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void previewCapturedImage() {
        try {

            //hideSelectPictureContainer();

            addImages(mImageFileUri.getPath());


            // bitmap factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 3;

// Bitmap bitmap = BitmapFactory.decodeFile(mCapturedImageUris.get(position));

            final Bitmap bitmap = BitmapFactory.decodeFile(capturedImageUris.get(0),
                    options);
            for (String uris : capturedImageUris) {
            }
            //Uri uri = Uri.parse(mCapturedImageUris.get(position));
            String device_name = Build.MANUFACTURER;

            Bitmap afterRotation = null;

            if (device_name.equals("samsung")) {
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(capturedImageUris.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        afterRotation = rotateImage(bitmap, 90);
                        imgDriver.setImageBitmap(afterRotation);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        afterRotation = rotateImage(bitmap, 180);
                        imgDriver.setImageBitmap(afterRotation);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        afterRotation = rotateImage(bitmap, 270);
                        imgDriver.setImageBitmap(afterRotation);
                        break;
                    default:
                        imgDriver.setImageBitmap(bitmap);
                }

            } else {
                imgDriver.setImageBitmap(bitmap);
            }

            imgDriver.setAdjustViewBounds(true);
            imgDriver.setScaleType(ImageView.ScaleType.FIT_CENTER);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return retVal;
    }


    private void addImages(String path) {
        capturedImageUris.add(path);

    }
}