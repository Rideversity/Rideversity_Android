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
import android.text.TextUtils;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.DriverInfoModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.PolygonImageView;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.ExifUtil;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

import static cab.com.rideversitydriver.R.id.image_driver;

/**
 * Created by KEERTHINI on 8/4/2016.
 */

public class DriverDoc_Activity extends AppCompatActivity implements View.OnClickListener {
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
    public static Bitmap bitmapDriver, bitmapCarproof, bitmapVehicle, bitmapRegproof;
    boolean driver = false, carinsurance = false, vehicletag = false, regproof = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.become_a_driver);
        context = this;
        next = (Button) findViewById(R.id.button_next);
        imgDriver = (PolygonImageView) findViewById(image_driver);
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


        if (Constants.PERSONAL_VEHICLE_CAMERA_RECALL == true) {
            Intent intent = new Intent(context, VehicleDoc_Activity.class);
            startActivity(intent);
        }

        if (Constants.REG_LOAD == false) {
            parseDriverInfo();
        } else {

        }


    }

    public void parseDriverInfo() {
        CommonAsynTask driverInfoTask = new CommonAsynTask(this, Constants.DRIVER_INFO, getData(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                Log.i("DriverInfo", jsonObject.toString());
                Singleton.getInstance().mDriverInfoModels.clear();
                if (jsonObject != null) {
                    try {
                        String result = jsonObject.getString(Constants.RESULT);
                        if (result.equals(Constants.SUCCESS)) {
                            JSONObject dataObject = jsonObject.getJSONObject(Constants.DATA);
                            DriverInfoModel driverInfoModel = new DriverInfoModel();
                            driverInfoModel.setDriverId(dataObject.getString(Constants.DRIVER_ID));
                            driverInfoModel.setDriverLicProof(dataObject.getString(Constants.DRIVER_LIC_PROOF));
                            Log.i("DriverSize", dataObject.getString(Constants.DRIVER_LIC_PROOF));
                            driverInfoModel.setDriverLicNumber(dataObject.getString(Constants.DRIVER_LIC_NUMBER));
                            driverInfoModel.setDriverImg(dataObject.getString(Constants.DRIVER_IMG));
                            driverInfoModel.setDriverLicExpirayDate(dataObject.getString(Constants.DRIVER_LIC_EXPIRAY_DATE));
                            driverInfoModel.setCarInsNumber(dataObject.getString(Constants.CAR_INS_NUMBER));
                            driverInfoModel.setCarInsProof(dataObject.getString(Constants.CAR_INS_PROOF));
                            driverInfoModel.setVehicleTagProof(dataObject.getString(Constants.VEHICAL_TAG_PROOF));
                            driverInfoModel.setVehicleTagNumber(dataObject.getString(Constants.VEHICAL_TAG_NUMBER));
                            driverInfoModel.setRegisterProof(dataObject.getString(Constants.REGISTER_PROOF));
                            driverInfoModel.setVehicleFrontPicture(dataObject.getString(Constants.VEHICAL_FRONT_PICTURE));
                            driverInfoModel.setVehicleRearPicture(dataObject.getString(Constants.VEHICAL_REAR_PICTURE));
                            driverInfoModel.setVehicleSidePicture(dataObject.getString(Constants.VEHICAL_SIDE_PICTURE));
                            driverInfoModel.setPersonalEmail(dataObject.getString(Constants.PERSONAL_EMAIL));
                            driverInfoModel.setMailingAddress(dataObject.getString(Constants.MAILING_ADDRESS));
                            driverInfoModel.setCarYear(dataObject.getString(Constants.CAR_YEAR));
                            driverInfoModel.setCarModel(dataObject.getString(Constants.CAR_MODEL));
                            driverInfoModel.setCarName(dataObject.getString(Constants.CAR_NAME));
                            driverInfoModel.setCarInsExpirayDate(dataObject.getString(Constants.CAR_INS_EXPIRAY_DATE));
                            driverInfoModel.setSeatAvailable(dataObject.getString(Constants.SEAT_AVALIABLE));
                            Singleton.getInstance().mDriverInfoModels.add(driverInfoModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                if (Singleton.getInstance().mDriverInfoModels.size() > 0) {
                    DriverInfoModel model = Singleton.getInstance().mDriverInfoModels.get(0);
                    if (model != null) {
                        if (!model.getDriverImg().equals("")) {
                            setImage(imgDriver, model.getDriverImg());

                        }

                        edtDriverLicenseNumber.setText(model.getDriverLicNumber());
                        txtDriverLicenseExpirationDate.setText(model.getDriverLicExpirayDate());
                        if (!model.getCarInsProof().equals("")) {
                            setImage(imgCarProof, model.getCarInsProof());

                        }
                        txtInsuranceExpirationDate.setText(model.getCarInsExpirayDate());
                        edtInsuranceNumber.setText(model.getCarInsNumber());
                        if (!model.getVehicleTagProof().equals("")) {
                            setImage(imgVehicle, model.getVehicleTagProof());

                        }
                        edtTagNumber.setText(model.getVehicleTagNumber());
                        if (!model.getRegisterProof().equals("")) {
                            setImage(imgRegProof, model.getRegisterProof());

                        }

                    }
                }
            }
        });
        driverInfoTask.execute();
    }

    private void setImage(PolygonImageView view, String imageUrl) {

        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_image)
                .into(view);


    }

    private String getData() {
        JSONObject activeRiderData = new JSONObject();
        SharedPref pref = new SharedPref(this);
        String userId = pref.getString("userId");

        if (!TextUtils.isEmpty(userId)) {
            try {
                activeRiderData.put("driverId", userId);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return activeRiderData.toString();
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
                dpd.setMinDate(now);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.setAccentColor(Color.parseColor("#278455"));
                dpd.setTitle("DatePicker Title");
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;

            case R.id.textview_insurance_exp_date:
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpds = DatePickerDialog.newInstance(
                        new DatesListeners(),
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                );
                dpds.setMinDate(c);
                dpds.vibrate(true);
                dpds.dismissOnPause(true);
                dpds.setAccentColor(Color.parseColor("#278455"));
                dpds.setTitle("DatePicker Title");
                dpds.show(getFragmentManager(), "Datepickerdialog");
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

                Constants.thumbnailDriverLicence = ((BitmapDrawable) imgDriver.getDrawable()).getBitmap();
                Constants.bitmapCarproof = ((BitmapDrawable) imgCarProof.getDrawable()).getBitmap();
                Constants.bitmapVehicle = ((BitmapDrawable) imgVehicle.getDrawable()).getBitmap();
                Constants.bitmapRegproof = ((BitmapDrawable) imgRegProof.getDrawable()).getBitmap();

                driver = Utilities.compareBitmap(bitmapEmpty, bitmapDriver);
                carinsurance = Utilities.compareBitmap(bitmapEmpty, bitmapCarproof);
                vehicletag = Utilities.compareBitmap(bitmapEmpty, bitmapVehicle);
                regproof = Utilities.compareBitmap(bitmapEmpty, bitmapRegproof);

                if (Constants.strDriverLicenseNo.equals("Tap Here") || Constants.strLicenseExpDate.equals("Tap Here") || Constants.strInsuranceExpDate.isEmpty() || Constants.strTagno.isEmpty() || driver == true || carinsurance == true || vehicletag == true || regproof == true) {
                    snackbar = Snackbar.make(layoutMain, "Please enter all text values and images", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                    snackbar.show();
                } else {
                    Intent intents = new Intent(context, VehicleDoc_Activity.class);
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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public class DateListeners implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
            //Toast.makeText(getActivity(),date, Toast.LENGTH_SHORT).show();
            txtDriverLicenseExpirationDate.setText(date);
        }
    }

    public class DatesListeners implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
            //Toast.makeText(getActivity(),date, Toast.LENGTH_SHORT).show();
            txtInsuranceExpirationDate.setText(date);
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
        Constants.REG_LOAD = true;

        Constants.thumbnailDriverLicence = ((BitmapDrawable) imgDriver.getDrawable()).getBitmap();
        Constants.bitmapCarproof = ((BitmapDrawable) imgCarProof.getDrawable()).getBitmap();
        Constants.bitmapVehicle = ((BitmapDrawable) imgVehicle.getDrawable()).getBitmap();
        Constants.bitmapRegproof = ((BitmapDrawable) imgRegProof.getDrawable()).getBitmap();


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
                startActivityForResult(takePicture, REQUEST_CAMERA);
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.CAMERA_RECALL = true;
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(pickPhoto, SELECT_FILE);
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


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

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
