package com.danny.cryptkurs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout BuyBTN =(RelativeLayout)findViewById(R.id.buyBTN);

        BuyBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String url = "https://www.binance.com/en/register?ref=AVSNP990";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        content();

    }

    private void content()
    {
        getDogePrice("https://api.binance.com/api/v3/ticker/price?symbol=DOGEEUR");
        getDogePrice("https://api.binance.com/api/v3/ticker/price?symbol=DOGEBUSD");
        getDogePrice("https://api.binance.com/api/v3/ticker/price?symbol=DOGEBTC");
        refresh(15000);
    }

    private void refresh(int milliseconds)
    {
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                content();
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }

    private void getDogePrice(String url)
    {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NetworkError) {
                    setErrorIntent();
                } else {
                    Toast.makeText(getApplicationContext(), "can't get data...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }

    private void setErrorIntent()
    {
        Intent offlineIntent = new Intent(this, OfflineActivity.class);
        startActivity(offlineIntent);
    }

    public void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            String currencyPrice = object.getString("price");
            String currencySymbol = object.getString("symbol");

            switch (currencySymbol) {
                case "DOGEEUR":
                    TextView DogeEuroTextView = (TextView) findViewById(R.id.euroPrice);
                    DogeEuroTextView.setText(currencyPrice);
                    break;
                case "DOGEBTC":
                    TextView DogeBTCTextView = (TextView) findViewById(R.id.btcPrice);
                    DogeBTCTextView.setText(currencyPrice);
                    break;
                case "DOGEBUSD":
                    TextView DogeBUSDTextView = (TextView) findViewById(R.id.usdPrice);
                    DogeBUSDTextView.setText(currencyPrice);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doge_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, InfoActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}