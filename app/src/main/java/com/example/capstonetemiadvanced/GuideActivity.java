package com.example.capstonetemiadvanced;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


public class GuideActivity extends AppCompatActivity {

    private WebServer server;
    public String goserver = "http://192.168.43.244:8080";
    public int portNumber = 8080;
    public String levelNo = "3"; //TEMI current level
    public String level; // Level from the req URL
    public String shelfNo; // Shelf No from the req URL
    public String bookId; // Bookid from the req URL
    public String bookName; // BookName from the req URL
    public ImageButton back;
    public boolean free;
    private String currentphotopath;
    public Bitmap imageReceived;

    public TextView booknametxt;
    public TextView bookidtxt;
    public TextView taskfinishtxt;
    public Boolean answer = true;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        server = new WebServer();
        try {
            server.start();
        } catch (IOException ioe) {
            Log.w("Httpd", "The server could not start.");
        }
        Log.w("Httpd", "Web server initialized.");
        // ATTENTION: This was auto-generated to handle app links.
        handleIntent();

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent();
    }

    private void handleIntent(){
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();

        // This code is ran through clicking of url + same level
        if(appLinkData != null){

            // "http://temibot.com/level/level=3&shelfno=1&bookname=Michelle%20Obama's%20Life%20%26%20Experience&bookid=E909%2E%20O24%20O12%20PBK"
            String rawdata = appLinkData.getLastPathSegment();
            String[] data = rawdata.split("&",4 );

            for (int i =0; i < 4; i++) {
                String[] dataPair = data[i].split("=", 2);
                String key = dataPair[0];
                if (key.equals("level")) {
                    level = dataPair[1];
                }
                else if (key.equals("shelfno")) {
                    shelfNo = dataPair[1];
                }
                else if (key.equals("bookid")) {
                    bookId = dataPair[1];
                }
                else if (key.equals("bookname")) {
                    bookName = dataPair[1].replace("~", "&");
                }
            }

            if(level.equals(levelNo)){

                booknametxt = findViewById(R.id.book_name);
                bookidtxt = findViewById(R.id.book_id);
                taskfinishtxt = findViewById(R.id.taskFinishTxt);

                booknametxt.setText(bookName);
                bookidtxt.setText(bookId);
                taskfinishtxt.setText("We've reached shelf " + shelfNo + "! Your book should be nearby :)");


                String requestUrl = "https://capstonetemi-3ec7.restdb.io/rest/book-history";
                JSONObject postData = new JSONObject();
                try {
                    postData.put("level", level);
                    postData.put("shelfno", shelfNo);
                    postData.put("bookid", bookId);
                    postData.put("bookname", bookName);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestUrl, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("content-type", "application/json");
                        params.put("x-apikey", "2f9040149a55d3c3e6bfa3f356b6dec655137");
                        params.put("cache-control","no-cache");

                        return params;
                    }
                };

                RequestQueue namerequestQueue = Volley.newRequestQueue(GuideActivity.this);
                namerequestQueue.add(jsonObjectRequest);

                }
            // For different Level TEMIs
            else{
                /*
                Log.v("suck", "bruh");
                ActivityResultLauncher<Intent> imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (result.getResultCode() == Activity.RESULT_OK) {
                                    imageReceived = BitmapFactory.decodeFile(currentphotopath);


                                    // inflate the layout of the popup window

                                    LayoutInflater inflater = LayoutInflater.from(GuideActivity.this);
                                    View popupView = inflater.inflate(R.layout.popup3, null);

                                    // create the popup window
                                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                    boolean focusable = true; // lets taps outside the popup also dismiss it
                                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                                    CountDownTimer waitTimer;
                                    waitTimer = new CountDownTimer(3000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            Log.v("suck", "bruh");
                                            if (imageReceived != null) {
                                                // Send the image in json
                                                String requestUrl = goserver + "/receiveimage";
                                                JSONObject postData = new JSONObject();

                                                // Encode the bitmap
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                imageReceived.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                                byte[] imageBytes = baos.toByteArray();
                                                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                                                try {
                                                    postData.put("image", encodedImage);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestUrl, postData, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.v("jy", "ugu");
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        error.printStackTrace();
                                                    }
                                                });
                                                RequestQueue nameRequestQueue = Volley.newRequestQueue(GuideActivity.this);
                                                nameRequestQueue.add(jsonObjectRequest);
                                                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                                deleteTempFiles(storageDirectory);
                                            }

                                        }

                                        public void onFinish() {



                                            // show the popup window
                                            // which view you pass in doesn't matter, it is only used for the window tolken
                                            popupWindow.showAtLocation(GuideActivity.this.findViewById(R.id.main), Gravity.CENTER, 0, 0);
                                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent event) {
                                                    popupWindow.dismiss();
                                                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    return true;
                                                }
                                            });



                                        }
                                    }.start();


                                }
                            }

                        });

                        // inflate the layout of the popup window
                        LayoutInflater inflater = LayoutInflater.from(GuideActivity.this);
                        View popupView = inflater.inflate(R.layout.popup2, null);

                        // create the popup window
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window tolken
                        Log.v("suck", "bro");


                        CountDownTimer waitTimer;
                        waitTimer = new CountDownTimer(3000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                Log.v("suck", "bruh");

                            }

                            public void onFinish() {
                                popupWindow.showAtLocation(GuideActivity.this.findViewById(R.id.main), Gravity.CENTER, 0, 0);
                                popupView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        popupWindow.dismiss();
                                        String fileName = "photo";
                                        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                                        try{
                                            File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                                            currentphotopath = imageFile.getAbsolutePath();
                                            Uri imageUri = FileProvider.getUriForFile(GuideActivity.this, "com.example.capstone_temi.fileprovider", imageFile);
                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                            imageActivityResultLauncher.launch(intent);
                                        }
                                        catch (IOException e){
                                            e.printStackTrace();
                                        }
                                        return true;
                                    }
                                });


                            }
                        }.start();

                 */

                }
            } else{
            bookId = appLinkIntent.getStringExtra("bookId");
            level = appLinkIntent.getStringExtra("level");
            shelfNo = appLinkIntent.getStringExtra("shelfNo");
            bookName = appLinkIntent.getStringExtra("bookName");

            if(level.equals(levelNo)) {

                /*

                boolean difflevel = appLinkIntent.getBooleanExtra("difflevel", false);
                if (difflevel) {
                    robot.goTo("waitingarea");

                    robot.addOnGoToLocationStatusChangedListener(new OnGoToLocationStatusChangedListener() {
                        @Override
                        public void onGoToLocationStatusChanged(@NonNull String location, @NonNull String status, int id, @NonNull String desc) {
                            // If the TEMI is not returned to the home base yet
                            if (!location.equals("home base")) {
                                if (status.equals("complete")) {
                                    Intent intent = new Intent(GuideActivity.this, FaceVerificationActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }

                 */
            }
        }
    }

/*
    public void popup() {

        // inflate the layout of the popup window
        LayoutInflater inflater = LayoutInflater.from(this.getApplicationContext());
        View popupView = inflater.inflate(R.layout.popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM, 0, 0);

        Button yes = popupView.findViewById(R.id.yes);
        Button no = popupView.findViewById(R.id.no);
        answer = true;


        CountDownTimer waitTimer;
        TextView countdown = popupView.findViewById(R.id.timer);
        waitTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                int time =  Integer.parseInt(countdown.getText().toString()) - 1;
                countdown.setText(String.valueOf(time));

            }

            public void onFinish() {

                if(answer == true){
                    answer = false;
                    Intent launchIntent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(launchIntent);
                    popupWindow.dismiss();
                    robot.goTo("home base");
                }

            }
        }.start();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer = false;
                Intent launchIntent = new Intent(GuideActivity.this, MainActivity.class);
                if (launchIntent != null) {

                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer = false;
                Intent launchIntent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(launchIntent);
                popupWindow.dismiss();
                robot.goTo("home base");

            }
        });

    }

 */

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean deleteTempFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteTempFiles(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
        return file.delete();
    }




    // DON'T FORGET to stop the server
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (server != null)
            server.stop();
    }

    private class WebServer extends NanoHTTPD {

        public WebServer()
        {
            super(portNumber);
        }

        @Override
        public Response serve(IHTTPSession session) {

            if (session.getMethod() == Method.POST) {
                try {
                    final HashMap<String, String> map = new HashMap<String, String>();
                    session.parseBody(map);
                    String data = map.get("postData");
                    Context ctx=getApplicationContext();


                    Intent intent = new Intent(ctx, GuideActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
                    // the activity from a service
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(intent);




                    JSONObject json = new JSONObject(data);
                    bookId = json.getString("bookid");
                    level = json.getString("level");
                    shelfNo = json.getString("shelfno");
                    bookName = json.getString("bookname");

                    Log.w("lol", bookId);

                    booknametxt.setText(bookName);
                    bookidtxt.setText(bookId);

                    //appLinkIntent = null;
/*
                    robot.goTo("shelf"+shelfNo);
                    robot.addOnGoToLocationStatusChangedListener(new OnGoToLocationStatusChangedListener() {
                        @Override
                        public void onGoToLocationStatusChanged(@NonNull String location, @NonNull String status, int id, @NonNull String desc) {
                            // If the TEMI is not returned to the home base yet
                            if(!location.equals("home base")){
                                if(status.equals("complete")){
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                    r.play();
                                    popup();
                                }
                            }
                        }
                    });

 */

                    return newFixedLengthResponse(data);
                } catch (IOException | ResponseException | JSONException e) {
                    // handle
                    e.printStackTrace();
                }
            }

            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                    "The requested resource does not exist");

        }


    }

}
