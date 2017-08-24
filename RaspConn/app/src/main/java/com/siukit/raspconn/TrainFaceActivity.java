package com.siukit.raspconn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.ceylonlabs.imageviewpopup.ImagePopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TrainFaceActivity extends AppCompatActivity {

//    private final Context context;
    private static Uri selectedImage1;
    private static Uri selectedImage2;
    private static Uri selectedImage3;
    private static Bitmap bitmap1;
    private static Bitmap bitmap2;
    private static Bitmap bitmap3;

    private static ImageView frontalPrev;
    private static ImageView smilePrev;
    private static ImageView glassesPrev;
    private static ImagePopup imagePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_face);
        imagePopup = new ImagePopup(this);
        //decalre all the buttons/edittext/textview
        final Button bFrontalPic = (Button) findViewById(R.id.bFrontalPic);
        final Button bSmilePic = (Button) findViewById(R.id.bSmilePic);
        final Button bGlassesOn = (Button) findViewById(R.id.bGlassesOn);
        final Button bTrainFaces = (Button) findViewById(R.id.bTrainFaces);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final TextView tvWarning = (TextView) findViewById(R.id.tvWarning);

        frontalPrev = (ImageView) findViewById(R.id.ivFrontal);
        smilePrev = (ImageView) findViewById(R.id.ivSmile);
        glassesPrev = (ImageView) findViewById(R.id.ivGlasses);

        //when user press the 'TRAIN NEW FACE' button
        bTrainFaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if glasses image was not taken
                if(!etName.getText().toString().isEmpty() && !getImageString(bitmap1).isEmpty() && !getImageString(bitmap2).isEmpty() && getImageString(bitmap3).isEmpty()) {
                    final String name = etName.getText().toString();
                    final String frontal_pic = getImageString(bitmap1);
                    final String smile_pic = getImageString(bitmap2);
                    //put the default string to the variable
                    final String glasses_pic = "no_glasses";
                    DateFormat today = new SimpleDateFormat("yyyy/MM/dd");
                    final String reg_time = today.format(Calendar.getInstance().getTime());

                    //response listener for register
                    Response.Listener<String> faceTrainListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Boolean success = jsonResponse.getBoolean("success");

                                //if the register was successful, goes to LoginActivity
                                if (success) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(TrainFaceActivity.this);
                                    builder.setMessage("Successfully uploaded face images, faces will be trained within a minute")
                                            .setNegativeButton("OK", null)
                                            .create()
                                            .show();
//                                    Intent homeIntent = new Intent(TrainFaceActivity.this, MainActivity.class);
//                                    startActivity(homeIntent);

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TrainFaceActivity.this);
                                    builder.setMessage("Fail to connect to database!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    //send data to server php file
                    TrainFaceRequest regReq = new TrainFaceRequest(name, frontal_pic, smile_pic, glasses_pic, reg_time, faceTrainListener);
                    RequestQueue queue = Volley.newRequestQueue(TrainFaceActivity.this);
                    queue.add(regReq);
                    //if all the face pictures were taken
                }else if (!etName.getText().toString().isEmpty() && !getImageString(bitmap1).isEmpty() && !getImageString(bitmap2).isEmpty() && !getImageString(bitmap3).isEmpty()){
                    final String name = etName.getText().toString();
                    final String frontal_pic = getImageString(bitmap1);
                    final String smile_pic = getImageString(bitmap2);
                    final String glasses_pic = getImageString(bitmap3);
                    DateFormat today = new SimpleDateFormat("yyyy/MM/dd");
                    final String reg_time = today.format(Calendar.getInstance().getTime());

                    //response listener for register
                    Response.Listener<String> faceTrainListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Boolean success = jsonResponse.getBoolean("success");

                                //if the register was successful, goes to LoginActivity
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TrainFaceActivity.this);
                                    builder.setMessage("Successfully uploaded face images, faces will be trained within a minute")
                                            .setNegativeButton("OK", null)
                                            .create()
                                            .show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TrainFaceActivity.this);
                                    builder.setMessage("Fail to connect to database!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    //send data to server php file
                    TrainFaceRequest regReq = new TrainFaceRequest(name, frontal_pic, smile_pic, glasses_pic, reg_time, faceTrainListener);
                    RequestQueue queue = Volley.newRequestQueue(TrainFaceActivity.this);
                    queue.add(regReq);
                    //if name/pictures were missing
                }else{
                    tvWarning.setTextColor(Color.RED);
                    tvWarning.setText("Please make sure the name, frontal pic and smile pic exist!!");
                }

            }
        });

        //open camera when button being clicked for taking face image
        bFrontalPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeFrontalPic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takeFrontalPic, 0);
            }
        });
        //open camera when button being clicked for taking face image
        bSmilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeSmilePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takeSmilePic, 1);
            }
        });
        //open camera when button being clicked for taking face image
        bGlassesOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicWithGlasses = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicWithGlasses, 2);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    //get the image data from imageReturnedIntent
                    selectedImage1 = imageReturnedIntent.getData();
//                    String result = selectedImage.getPath();
                    try {
                        //set the image to the image view so user can view the chosen picture before registering
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage1);
//                        RotateBitmap(bitmap1, 270);
                        frontalPrev.setImageBitmap(bitmap1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    frontalPrev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagePopup.setBackgroundColor(Color.BLACK);
                            imagePopup.setWindowWidth(1200);
                            imagePopup.setWindowHeight(1200);
                            imagePopup.setHideCloseIcon(false);
                            imagePopup.initiatePopup(frontalPrev.getDrawable());
                        }
                    });
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    //get the image data from imageReturnedIntent
                    selectedImage2 = imageReturnedIntent.getData();
                    try {
                        //set the image to the image view so user can view the chosen picture before registering
                        bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage2);
//                        RotateBitmap(bitmap2, 270);

                        smilePrev.setImageBitmap(bitmap2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    smilePrev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagePopup.setBackgroundColor(Color.BLACK);
                            imagePopup.setWindowWidth(1200);
                            imagePopup.setWindowHeight(1200);
                            imagePopup.setHideCloseIcon(false);
                            imagePopup.initiatePopup(smilePrev.getDrawable());
                        }
                    });
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    //get the image data from imageReturnedIntent
                    selectedImage3 = imageReturnedIntent.getData();
                    try {
                        //set the image to the image view so user can view the chosen picture before registering
                        bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage3);
//                        RotateBitmap(bitmap3, 270);
                        glassesPrev.setImageBitmap(bitmap3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    glassesPrev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagePopup.setBackgroundColor(Color.BLACK);
                            imagePopup.setWindowWidth(1200);
                            imagePopup.setWindowHeight(1200);
                            imagePopup.setHideCloseIcon(false);
                            imagePopup.initiatePopup(glassesPrev.getDrawable());
                        }
                    });
                }
                break;
        }
    }

    //encod the bitmap image to base64 string
    public String getImageString(Bitmap bmp) {
        //declare byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //crop the image to 300x300 resolution
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 900, 500, true);
        //compress as JPEG format
//        RotateBitmap(scaled, 270);
        scaled.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        scaled.recycle();
        byte[] imageBytes = baos.toByteArray();
        String encodedImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImg;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
