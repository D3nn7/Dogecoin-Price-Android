package com.danny.cryptkurs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.danny.cryptkurs.updater.Updater;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences = null;
    String preferences_firstrun = "firstrun";
    String storedDoge = "1";
    TextView DogeAmount = null;
    Handler updateHandler = new Handler();

    boolean isAppOffline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Updater updater = new Updater();
        updater.checkForUpdates(BuildConfig.VERSION_NAME, this);

        DogeAmount = (TextView)findViewById(R.id.doge);
        LinearLayout changeDogeAmount =(LinearLayout)findViewById(R.id.doge_container);

        setPreferences();

        changeDogeAmount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeDoge();
            }
        });

        updateHandler.postDelayed(runPriceUpdater, 5000);

        content();

    }

    Runnable runPriceUpdater = new Runnable()
    {
        @Override
        public void run()
        {
            content();
            updateHandler.postDelayed(this, 5000);
        }
    };

    private void setPreferences()
    {
        preferences = getSharedPreferences("com.danny.cryptkurs", 0);
        if (preferences.getBoolean(preferences_firstrun, true)) {

            SharedPreferences.Editor preferencesEditor = preferences.edit();
            preferencesEditor.putString("VERSION", BuildConfig.VERSION_NAME);
            preferencesEditor.putString("AMOUNT", "1");
            preferencesEditor.apply();

            preferences.edit().putBoolean(preferences_firstrun, false).apply();
        } else {
            preferences = getSharedPreferences("com.danny.cryptkurs", 0);
            storedDoge = preferences.getString("AMOUNT", "1");
        }


        DogeAmount.setText(storedDoge);
    }

    private void changeDoge()
    {
        preferences = getSharedPreferences("com.danny.cryptkurs", 0);
        String getAmount = preferences.getString("AMOUNT", "1");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.set_doge_message);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);

        input.setText(getAmount);
        builder.setView(input);
        builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog,int id){
                String newDogeAmount = input.getText().toString();
                long newDogeAmountlong = Long.parseLong(input.getText().toString());

                if (newDogeAmountlong > Integer.MAX_VALUE) {
                    newDogeAmount = "1999999999";
                }

                SharedPreferences.Editor preferencesEditor = preferences.edit();
                preferencesEditor.putString("AMOUNT", newDogeAmount);
                preferencesEditor.apply();

                String getNewAmount = preferences.getString("AMOUNT", "1");
                DogeAmount = (TextView)findViewById(R.id.doge);
                DogeAmount.setText(getNewAmount);

                dialog.dismiss();
                content();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id){
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void content()
    {
        getDogePrice("https://api.binance.com/api/v3/ticker/price?symbol=DOGEEUR");
        getDogePrice("https://api.binance.com/api/v3/ticker/price?symbol=DOGEBUSD");
        getDogePrice("https://api.binance.com/api/v3/ticker/price?symbol=DOGEBTC");
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
                    if (!isAppOffline) {
                        isAppOffline = true;
                        setErrorIntent();
                    }

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
        finish();
    }

    public void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            String currencyPrice = object.getString("price");
            String currencySymbol = object.getString("symbol");

            preferences = getSharedPreferences("com.danny.cryptkurs", 0);
            storedDoge = preferences.getString("AMOUNT", "1");

            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            df.setMaximumFractionDigits(340);
            String calculateCurrencyPrice = df.format(Double.parseDouble(currencyPrice) * Long.parseLong(storedDoge));
            String calculatedPrice = null;

            switch (currencySymbol) {
                case "DOGEEUR":
                    calculatedPrice = round(calculateCurrencyPrice, false);
                    TextView DogeEuroTextView = (TextView) findViewById(R.id.euroPrice);
                    DogeEuroTextView.setText(calculatedPrice);
                    break;
                case "DOGEBTC":
                    calculatedPrice = round(calculateCurrencyPrice, true);
                    TextView DogeBTCTextView = (TextView) findViewById(R.id.btcPrice);
                    DogeBTCTextView.setText(calculatedPrice);
                    break;
                case "DOGEBUSD":
                    calculatedPrice = round(calculateCurrencyPrice, false);
                    TextView DogeBUSDTextView = (TextView) findViewById(R.id.usdPrice);
                    DogeBUSDTextView.setText(calculatedPrice);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        private String round(String stringValue, boolean isBTC) {
            double value = Double.parseDouble(stringValue);


            if (isBTC) {
                value = value * (double) 10000000;
                value = (int) value;
                value = (double) value / (double) 10000000;
            } else {
                value = value * (double) 1000;
                value = (int) value;
                value = (double) value / (double) 1000;
            }

            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            df.setMaximumFractionDigits(340);
            String result = df.format(value);

            return result;
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