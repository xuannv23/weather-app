package com.example.myappweather.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myappweather.Adapter.FutureAdapter;
import com.example.myappweather.Adapter.HourlyAdapter;
import com.example.myappweather.Model.Future;
import com.example.myappweather.Model.Hourly;
import com.example.myappweather.Model.Weather;
import com.example.myappweather.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FutureActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterDay;
    private RecyclerView recyclerView;
    String link;
    ImageView pic1;
    TextView city1, temp1, main1, cloudy1, windy1, humidity1, location;

    public static ArrayList<Weather> weatherArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        setViews();
        link = getlink();
        getLocation();
        getForeCastWeatherData(link);
    }

    public static void click(Future abc){
//        temp1.setText(abc.get);
//        main1.setText();
//        cloudy1.setText();
//        windy1.setText();
//        humidity1.setText();
    }
    private void setViews() {
        pic1 = (ImageView) findViewById(R.id.pic1);
        city1 = (TextView) findViewById(R.id.city1);
        temp1 = (TextView) findViewById(R.id.temp1);
        main1 = (TextView) findViewById(R.id.main1);
        cloudy1 = (TextView) findViewById(R.id.cloudy1);
        windy1 = (TextView) findViewById(R.id.windy1);
        humidity1 = (TextView) findViewById(R.id.humidity1);
        recyclerView = findViewById(R.id.RVDay);
        location = findViewById(R.id.location1);
    }

    private void getForeCastWeatherData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(FutureActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("ket qua: ", response);////////////////////////
                        weatherArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                            JSONObject jsonObjectHour;
                            jsonObjectHour = jsonArrayList.getJSONObject(0);

                            // gan vao cai to nhat
                            Picasso.get().load("https://openweathermap.org/img/wn/"+jsonObjectHour.getJSONArray("weather").getJSONObject(0).getString("icon")+"@4x.png").into(pic1);
//                            city1.setText(jsonObject.getJSONObject("city").getString("name")+"tommorow");
                            Double t = Double.valueOf(jsonObjectHour.getJSONObject("main").getString("temp"));
                            String tempm = String.valueOf(t.intValue());
                            if(MainActivity.checkTemp == true){
                                temp1.setText(tempm+"°C");
                            }else{
                                int k = (int) (t.intValue() * 1.8 + 32);
                                temp1.setText((String.valueOf(k)+"°F"));
                            }
                            main1.setText(jsonObjectHour.getJSONArray("weather").getJSONObject(0).getString("main"));
                            cloudy1.setText(jsonObjectHour.getJSONObject("clouds").getString("all") + "%");
                            windy1.setText(jsonObjectHour.getJSONObject("wind").getString("speed") + " m/s");
                            humidity1.setText(jsonObjectHour.getJSONObject("main").getString("humidity") + "%");

                            ArrayList<Future> items = new ArrayList<>();
                            for(int i=0;i<39;i+=8){
                                jsonObjectHour = jsonArrayList.getJSONObject(i);
                                String day = toDayOfWeek(jsonObjectHour.getString("dt"));

                                Double h = Double.valueOf(jsonObjectHour.getJSONObject("main").getString("temp_max"));
                                Double l = Double.valueOf(jsonObjectHour.getJSONObject("main").getString("temp_min"));
                                int templ, temph;
                                if(MainActivity.checkTemp == true){
                                    temph = h.intValue();
                                    templ = l.intValue();
                                }else {
                                    temph = (int) (h.intValue() * 1.8 + 32);
                                    templ = (int) (l.intValue()* 1.8 + 32);
                                }

                                //add tt chi tiet weather
                                String cloudy = jsonObjectHour.getJSONObject("clouds").getString("all");
                                String winspeed = jsonObjectHour.getJSONObject("wind").getString("speed");
                                String degg = jsonObjectHour.getJSONObject("wind").getString("deg");
                                String hum = jsonObjectHour.getJSONObject("main").getString("humidity");
                                String tem = jsonObjectHour.getJSONObject("main").getString("temp");
                                String as = jsonObjectHour.getJSONObject("main").getString("pressure");


                                String icon = jsonObjectHour.getJSONArray("weather").getJSONObject(0).getString("icon");
                                String link = "https://openweathermap.org/img/wn/"+icon+"@4x.png";
                                String status = jsonObjectHour.getJSONArray("weather").getJSONObject(0).getString("main");
                                items.add(new Future(day, link, status, temph, templ));
                                weatherArrayList.add(new Weather(status,cloudy,winspeed,hum,tem,as,degg));
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(FutureActivity.this,LinearLayoutManager.VERTICAL,false));
                            adapterDay = new FutureAdapter(items, FutureActivity.this);
                            recyclerView.setAdapter(adapterDay);
                            adapterDay.notifyDataSetChanged();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FutureActivity.this, "kiem tra lai noi dung ban nhap", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }
    private String getlink() {
        Intent intent = getIntent();
        return intent.getStringExtra("link");

    }

    private void getLocation(){
        Intent intent = getIntent();
        location.setText(intent.getStringExtra("location"));
    }


    static boolean isEmpty(EditText txt) {
        String str = txt.getText().toString();
        return TextUtils.isEmpty(str);
    }
    private String toDayOfWeek(String day) {
        long m = Long.valueOf(day);
        Date date =  new Date(m * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        String datem = simpleDateFormat.format(date);
        return datem;
    }
}