package com.example.capstonetemiadvanced;

/*
import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

public class MainActivity extends AppCompatActivity {

    private WebServer server;
    public String goserver = "http://192.168.43.244:8080";
    public int portNumber = 8080;

    public ImageView imageSending;
    public Bitmap imageReceived;


    public ImageView photo;
    public TextView book_name;
    public Button wrongLevelButton;

    public String encodedImage;

    public ActivityResultLauncher<Intent> imageActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photo = findViewById(R.id.photo);
        book_name = findViewById(R.id.book_name);
        wrongLevelButton = findViewById(R.id.wronglevelbutton);

        server = new WebServer();
        try {
            server.start();
        } catch (IOException ioe) {
            Log.w("Httpd", "The server could not start.");
        }
        Log.w("Httpd", "Web server initialized.");


        imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageReceived = (Bitmap) data.getExtras().get("data");

                            CountDownTimer waitTimer;
                            waitTimer = new CountDownTimer(3000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    if (imageReceived != null) {
                                        // Send the image in json
                                        String requestUrl = goserver + "/receiveimage";
                                        JSONObject postData = new JSONObject();

                                        // Encode the bitmap
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        imageReceived.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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

                                        RequestQueue nameRequestQueue = Volley.newRequestQueue(MainActivity.this);
                                        nameRequestQueue.add(jsonObjectRequest);

                                    }
                                }
                                public void onFinish() {
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(intent);

                                }
                            }.start();

                        }
                    }

                });

        wrongLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String requestUrl = goserver + "/wronglevel";
                JSONObject postData = new JSONObject();
                try {
                    postData.put("level", "3");
                    postData.put("shelfno", "3");
                    postData.put("bookid", "EB201213");
                    postData.put("bookname", "The Excellence of Play");

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestUrl, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("lol", "got response");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.w("lol", error.toString());

                    }
                });


                RequestQueue namerequestQueue = Volley.newRequestQueue(MainActivity.this);
                namerequestQueue.add(jsonObjectRequest);
                Log.w("lol", "dwad");

            }
        });

    }

    private class WebServer extends NanoHTTPD {

        public WebServer()
        {
            super(portNumber);
        }


        @Override
        public Response serve(IHTTPSession session) {
            if (session.getMethod() == Method.GET) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                imageActivityResultLauncher.launch(intent);
                return newFixedLengthResponse("Sending...");

            }

            if (session.getMethod() == Method.POST) {
                try {
                    final HashMap<String, String> map = new HashMap<String, String>();
                    session.parseBody(map);
                    String data = map.get("postData");
                    Context ctx=getApplicationContext();
                    JSONObject json = new JSONObject(data);
                    book_name.setText(json.getString("bookname"));
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

}*/



import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;


public class MainActivity extends AppCompatActivity {


    private WebView webView = null;
    private MainActivity.WebServer server;
    public String url = "https://chen-han-np.github.io/Capstone-TEMI-Website-Demo/";
    public boolean free;

    public TextView name;
    public ImageButton reload;
    public ImageButton back;
    public Button home;
    public int portNumber = 8080;
    // Temi current level: 2


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server = new MainActivity.WebServer();
        try {
            server.start();
        } catch (IOException ioe) {
            Log.w("Httpd", "The server could not start.");
        }
        Log.w("Httpd", "Web server initialized.");

        setContentView(R.layout.activity_main);
        reload = (ImageButton) findViewById(R.id.refresh);
        back = (ImageButton) findViewById(R.id.back);

        this.webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //---- For Website hosting with INTERNET-----
        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.goBack();
            }
        });
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
                    Log.w("Httpd", data);
                    JSONObject json = new JSONObject(data);

                    Context ctx=getApplicationContext();

                    Intent intent = new Intent(ctx, GuideActivity.class);
                    intent.putExtra("bookId", json.getString("bookid"));
                    intent.putExtra("level", json.getString("level"));
                    intent.putExtra("shelfNo", json.getString("shelfno"));
                    intent.putExtra("bookName", json.getString("bookname"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
                    // the activity from a service
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.putExtra("free", false);
                    intent.putExtra("difflevel", true);
                    startActivity(intent);

                    return newFixedLengthResponse("Request succeeded.");
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