package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String url1 = "https://api.openweathermap.org/data/2.5/weather?q=";
    String API = BuildConfig.API_KEY;
    String CITY;
    private EditText location, country, temperature, humidity, pressure;
    private TextView time_update;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time_update = (TextView) findViewById(R.id.waktu);

        location = (EditText) findViewById(R.id.lokasi);
        country = (EditText) findViewById(R.id.negara);
        temperature = (EditText) findViewById(R.id.suhu);
        humidity = (EditText) findViewById(R.id.kelembaban);
        pressure = (EditText) findViewById(R.id.tekanan);

        search = (Button) findViewById(R.id.cari);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CITY = location.getText().toString();
                new weatherTask().execute();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "JSON data is downloading..", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... args) {
            String urlFinal = url1 + CITY + "&units=metric&appid=" + API;
            HTTPHandler sh = new HTTPHandler();
            String JSONStr = sh.makeServiceCall(urlFinal);
            Log.e("Main", "Response from URL: " + urlFinal);

            return JSONStr;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject main = jsonObj.getJSONObject("main");
                    JSONObject sys = jsonObj.getJSONObject("sys");

                    String countrytext = sys.getString("country");
                    String temperaturetext = main.getString("temp");
                    String humiditytext = main.getString("humidity");
                    String pressuretext = main.getString("pressure");
                    Long updatedAt = jsonObj.getLong("dt");
                    String updatedAtText = "Last updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));

                    time_update.setText(updatedAtText);
                    country.setText(countrytext);
                    temperature.setText(temperaturetext + "°C");
                    humidity.setText(humiditytext + "%");
                    pressure.setText(pressuretext + " hPa");
                }

                catch (final JSONException e) {
                    Log.e("Main", "JSON parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            else {
                Log.e("Main", "Couldn't get JSON from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get JSON from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}