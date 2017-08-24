package com.siukit.raspconn;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity{

    private Handler handler = new Handler();

    private static String jsonString;
    private static StringBuilder jsonResult;

    private static JSONObject jsonResponse;
    private static JSONArray jsonMainNode;
    private static JSONObject burglarObject;
    private static JSONObject sirenObject;

    private static TextView tvSiren;
    private static TextView tvConnection;

    //check if asynctask is completed or not
    private boolean isBusy = false;
    private boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button bViewStream = (Button) findViewById(R.id.bViewStream);
        final Button bViewLogs = (Button) findViewById(R.id.bViewLogs);
        final Button bViewIntruders = (Button) findViewById(R.id.bViewIntruders);
        final Button bTrainFace = (Button) findViewById(R.id.bTrainFace);
        final ToggleButton tbBurglarMode = (ToggleButton) findViewById(R.id.tbBurglarMode);
        tvSiren = (TextView) findViewById(R.id.tvSiren);
        tvConnection = (TextView) findViewById(R.id.tvConnection);

        startHandler();

//        tvSiren.setTextColor(Color.RED);
//        tvConnection.setTextColor(Color.RED);

        tbBurglarMode.setTextOff("TURN ON ANTI-BURGLAR MODE");
        tbBurglarMode.setTextOn("TURN OFF ANTI-BURGLAR MODE");
        tbBurglarMode.setChecked(false);

        tbBurglarMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Response.Listener<String> rl = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject jr = null;
                            try {
                                jr = new JSONObject(response);
                                boolean success = jr.getBoolean("success");

                                //if switched mode successfully
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Anti-burglar mode is ON!")
                                            .setNegativeButton("Ok", null)
                                            .create()
                                            .show();
                                    // if fail to switch mode
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Switch mode failed, server is not responding!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //send the String 'on' to 'is_on' column on system_mode table
                    SwitchModeRequest switchModeRequest = new SwitchModeRequest("on", "burglar", rl);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(switchModeRequest);
                } else {
                    // The toggle is disabled
                    Response.Listener<String> rl = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject jr = null;
                            try {
                                jr = new JSONObject(response);
                                boolean success = jr.getBoolean("success");

                                //if switched mode successfully
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Anti-burglar mode is OFF!")
                                            .setNegativeButton("Ok", null)
                                            .create()
                                            .show();
                                    // if fail to switch mode
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Switch mode failed, server is not responding!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //send the String 'on' to 'is_on' column on system_mode table
                    SwitchModeRequest switchModeRequest = new SwitchModeRequest("off", "burglar", rl);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(switchModeRequest);
                }
            }
        });


        bViewStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent streamIntent = new Intent(MainActivity.this, ViewStreamActivity.class);
                startActivity(streamIntent);
            }
        });

        bViewLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent streamIntent = new Intent(MainActivity.this, ViewLogsActivity.class);
                startActivity(streamIntent);
            }
        });

        bViewIntruders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent streamIntent = new Intent(MainActivity.this, ViewIntrudersActivity.class);
                startActivity(streamIntent);
            }
        });

        bTrainFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent streamIntent = new Intent(MainActivity.this, TrainFaceActivity.class);
                startActivity(streamIntent);
            }
        });
    }



    class GetModesJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            jsonResult = new StringBuilder();
            try {
                //connect to getUsers php file
//                URL url = new URL("http://" + SERVER_IP + "/get_modes_status.php");
                URL url = new URL("http://194.81.104.22/~14412104/dis/get_modes_status.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(conn.getInputStream());
//                    String data = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                System.out.println("jsonString = " + jsonString);
                while ((jsonString = reader.readLine()) != null) {
                    System.out.println("jsonString = " + jsonString);
                    jsonResult.append(jsonString);
                }
//                System.out.println(jsonString);
                reader.close();
                in.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {

            displayModesStatus();
        }

    }

    //run AysncTask every three second (after previous one has finished)
    private void startHandler() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isBusy) startAysncTask();
                if (!stop) startHandler();

            }
        }, 3000);
    }

    private void startAysncTask () {
        new MainActivity.GetModesJSON().execute();
    }

    private void displayModesStatus(){
        try {
            jsonResponse = new JSONObject(jsonResult.toString());
            jsonMainNode = jsonResponse.optJSONArray("system_mode");

            burglarObject = jsonMainNode.getJSONObject(0);
            if(burglarObject.optString("is_on").equals("off")){
                tvConnection.setTextColor(Color.RED);
                tvConnection.setText("Mode: Face recognition");
            }else{
                tvConnection.setTextColor(Color.RED);
                tvConnection.setText("Mode: Anti-burglar");
            }

            sirenObject = jsonMainNode.getJSONObject(1);
            if(sirenObject.optString("is_on").equals("on")){
                tvSiren.setTextColor(Color.RED);
                tvSiren.setText("Siren: ON");
            }else{
                tvSiren.setTextColor(Color.RED);
                tvSiren.setText("Siren: OFF");
            }

        } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
//                    Toast.LENGTH_SHORT).show();
//            System.out.println("ERROR:     " + e);
        }
    }

}
