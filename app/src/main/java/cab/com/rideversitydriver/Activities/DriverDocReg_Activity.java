package cab.com.rideversitydriver.Activities;

/**
 * Created by KEERTHINI on 8/25/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Shapes.PolygonImageView;


/**
 * Created by KEERTHINI on 8/4/2016.
 */

public class DriverDocReg_Activity extends AppCompatActivity implements View.OnClickListener {

    private PolygonImageView imgDriver, imgCarProof, imgVehicle, imgRegProof;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 2;
    private int imageclick = 10;
    private TextView upload1, upload2, upload3, upload4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.become_a_driver_reg);

        Button next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(this);
        RelativeLayout backArrow = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        backArrow.setOnClickListener(this);
        imgDriver = (PolygonImageView) findViewById(R.id.image_upload1);
        imgCarProof = (PolygonImageView) findViewById(R.id.image_upload2);
        imgVehicle = (PolygonImageView) findViewById(R.id.image_upload3);
        imgRegProof = (PolygonImageView) findViewById(R.id.image_upload4);
        upload1 = (TextView) findViewById(R.id.upload1);
        upload2 = (TextView) findViewById(R.id.upload2);
        upload3 = (TextView) findViewById(R.id.upload3);
        upload4 = (TextView) findViewById(R.id.upload4);

        imgDriver.setOnClickListener(this);
        imgVehicle.setOnClickListener(this);
        imgCarProof.setOnClickListener(this);
        imgRegProof.setOnClickListener(this);
        upload1.setOnClickListener(this);
        upload2.setOnClickListener(this);
        upload3.setOnClickListener(this);
        upload4.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next:
                Intent intent = new Intent(DriverDocReg_Activity.this, VehicleDocReg_Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imageView_backarrow:
                finish();
                break;
            case R.id.upload1:
            case R.id.image_upload1:
                imageclick = 11;
                selectImage();
                break;
            case R.id.upload2:
            case R.id.image_upload2:
                imageclick = 12;
                selectImage();
                break;
            case R.id.upload3:
            case R.id.image_upload3:
                imageclick = 13;
                selectImage();
                break;
            case R.id.upload4:
            case R.id.image_upload4:
                imageclick = 14;
                selectImage();
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //dialog onclick imageview
    private void selectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DriverDocReg_Activity.this);
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

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_CAMERA);
                dialog.dismiss();

            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if (imageclick == 11) {
                    imgDriver.setImageBitmap(onSelectFromGalleryResult(data));
                }
                if (imageclick == 12) {
                    imgCarProof.setImageBitmap(onSelectFromGalleryResult(data));
                }
                if (imageclick == 13) {
                    imgVehicle.setImageBitmap(onSelectFromGalleryResult(data));
                }
                if (imageclick == 14) {
                    imgRegProof.setImageBitmap(onSelectFromGalleryResult(data));
                }
            } else if (requestCode == REQUEST_CAMERA) {
                if (imageclick == 11) {
                    imgDriver.setImageBitmap(onCaptureImageResult(data));
                }
                if (imageclick == 12) {
                    imgCarProof.setImageBitmap(onCaptureImageResult(data));
                }
                if (imageclick == 13) {
                    imgVehicle.setImageBitmap(onCaptureImageResult(data));
                }
                if (imageclick == 14) {
                    imgRegProof.setImageBitmap(onCaptureImageResult(data));
                }
            }
        }
    }

    private Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(DriverDocReg_Activity.this.getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                byte[] attachmentBytes = bytes.toByteArray();
                //imageToBase64String = Base64.encodeToString(attachmentBytes, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    private Bitmap onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

        byte[] attachmentBytes = bytes.toByteArray();
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
        return thumbnail;
    }

}
