package com.weatherapp.weatherupdate;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sandipbhattacharya.weatherupdate.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity;
    TextView tvResult;
    TextView tvResult_Cityname, tvResultTem_1, tvResult_Description;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "047010def8e6aed6b7e93763a966c342";
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);
        tvResultTem_1 = findViewById(R.id.tvResultTem_1);
        tvResult_Description = findViewById(R.id.tvResult_Description);
        tvResult_Cityname = findViewById(R.id.tvResult_Cityname);


    }


    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();

        if (city.equals("")) {
            tvResult.setText("City field can not be empty!");
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + appid;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("response", response);


                try {
                    String output = "";
                    String temperature = "";
                    String Weather_decscription = "";

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");

                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");

                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    String feelsLike = String.valueOf(jsonObjectMain.getDouble("feels_like") - 273.15);
                    float pressure = jsonObjectMain.getInt("pressure");
                    String humidity = String.valueOf(jsonObjectMain.getInt("humidity"));

                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");

                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");

                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");

                    tvResult.setTextColor(Color.rgb(248, 249, 249));
                    tvResult.setBackgroundResource(R.drawable.gradient_background_banner);

                    double feelsLikeDouble = Double.parseDouble(feelsLike);
                    String formattedFeelsLike = df.format(feelsLikeDouble);

                    output += "Current weather" +
                            "of " + cityName + " (" + countryName + ")"
                            + "\n Temp: " + df.format(temp) + " °C"
                            + "\n Feels Like: " + formattedFeelsLike + " °C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Description: " + description
                            + "\n Wind Speed: " + wind + "m/s (meters per second)"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa";


                    temperature += df.format(temp);
                    tvResultTem_1.setText(temperature + " °C");


                    Weather_decscription += description;
                    tvResult_Description.setText(Weather_decscription);

                    tvResult_Cityname.setText(cityName);

                    tvResult.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
}
