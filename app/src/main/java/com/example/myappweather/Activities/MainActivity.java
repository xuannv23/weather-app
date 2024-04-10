package com.example.myappweather.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myappweather.Adapter.HourlyAdapter;
import com.example.myappweather.Model.Hourly;
import com.example.myappweather.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;
    LocationManager locationManager;
    TextView cityDay, mainWeather, hightLow, temp, cloud, windyspeed, humidity;
    ImageView imvIcon;



    String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        ktraQuyen();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi nhấn nút tìm kiếm hoặc nhấn phím Enter
                timkiem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void timkiem(String city) {
        if(isEmpty(city)){
            Toast.makeText(MainActivity.this,"Cần nhập đủ thông tin",Toast.LENGTH_SHORT).show();
        }
        else{
            String link = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
            String urlc = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
            x = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
            getCurrentWeatherData(link);
            getForeCastWeatherData(urlc);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.next5day){
            Intent intent = new Intent(MainActivity.this, FutureActivity.class);
            intent.putExtra("link",x);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setView() {
        cityDay = (TextView) findViewById(R.id.city_day);
        mainWeather = (TextView) findViewById(R.id.main_weather);
        imvIcon = (ImageView) findViewById(R.id.image_view_icon);
        hightLow = (TextView) findViewById(R.id.hight_low);
        temp = (TextView) findViewById(R.id.txttemp);
        cloud = (TextView) findViewById(R.id.txtCloud);
        windyspeed = (TextView) findViewById(R.id.txtWindySpeed);
        humidity = (TextView) findViewById(R.id.txtHumidity);

        recyclerView = findViewById(R.id.RvHour);
    }

    private void ktraQuyen() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //nếu chưa có quyền, yêu cầu quyền truy cập vị trí
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Nếu đã có quyền, lấy vị trí hiện tại
            getLocation();
        }
    }

    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
        String urlfc = "https://api.openweathermap.org/data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
        x = "https://api.openweathermap.org/data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
        // Xử lý vị trí hiện tại (latitude và longitude)
        getCurrentWeatherData(url);
        getForeCastWeatherData(urlfc);
    }
    private void getCurrentWeatherData(String url) {
        // thực thi request mà mình gửi đi
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("ket qua: ", response);////////////////////////
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //location and date
                            String day = jsonObject.getString("dt");
                            String city = jsonObject.getString("name");
                            long m = Long.valueOf(day);
                            Date date =  new Date(m * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                            String datem = simpleDateFormat.format(date);
                            cityDay.setText(city+"__"+datem);

                            //weather and icon
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);//lấy cái cụm thứ 0 tương ứng với cụm weather
                            mainWeather.setText(jsonObjectWeather.getString("main"));
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.get().load("https://openweathermap.org/img/wn/"+icon+"@4x.png").into(imvIcon);

                            //main
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            Double h = Double.valueOf(jsonObjectMain.getString("temp_max"));

                            String hightm = String.valueOf(h.intValue());
                            Double l = Double.valueOf(jsonObjectMain.getString("temp_min"));
                            String lowm = String.valueOf(l.intValue());
                            Double t = Double.valueOf(jsonObjectMain.getString("temp"));
                            String tempm = String.valueOf(t.intValue());
                            hightLow.setText("H:"+hightm+"° L:"+lowm+"°");
                            temp.setText(tempm+"°C");

                            //cloud,humidity and windyspeed
                            windyspeed.setText(jsonObject.getJSONObject("wind").getString("speed")+"m/s");
                            humidity.setText(jsonObjectMain.getString("humidity")+"%");
                            cloud.setText(jsonObject.getJSONObject("clouds").getString("all")+"%");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

//private void getLocation() {
//    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        return;
//    }
//    //yêu cầu cập nhật vị trí
//    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            latitude = String.valueOf(location.getLatitude());
//            longitude = String.valueOf(location.getLongitude());
//            String url = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
//            String urlfc = "https://api.openweathermap.org/data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
//            x = "https://api.openweathermap.org/data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
//            // Xử lý vị trí hiện tại (latitude và longitude)
//            getCurrentWeatherData(url);
//            getForeCastWeatherData(urlfc);
//            // Ngưng lắng nghe vị trí sau khi lấy được vị trí hiện tại
//            locationManager.removeUpdates(this);
//        }
//
//        @Override
//        public void onProviderDisabled(@NonNull String provider) {
//        }
//
//        @Override
//        public void onProviderEnabled(@NonNull String provider) {
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//    });
//}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu người dùng chấp nhận quyền, lấy vị trí hiện tại
                getLocation();
            } else {
                // Nếu người dùng từ chối quyền, thông báo cho họ biết rằng vị trí không thể lấy được
                Toast.makeText(this, "Không thể lấy được vị trí!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getForeCastWeatherData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("ket qua: ", response);////////////////////////
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                            JSONObject jsonObjectHour;

                            ArrayList<Hourly> items = new ArrayList<>();
                            for(int i=0;i<8;i++){
                                jsonObjectHour = jsonArrayList.getJSONObject(i);
                                String time = toTime(jsonObjectHour.getString("dt"));
                                Double h = Double.valueOf(jsonObjectHour.getJSONObject("main").getString("temp"));
                                int temp = h.intValue();

                                String icon = jsonObjectHour.getJSONArray("weather").getJSONObject(0).getString("icon");
                                String link = "https://openweathermap.org/img/wn/"+icon+"@4x.png";

                                items.add(new Hourly(time,temp, link));
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                            adapterHourly = new HourlyAdapter(items, MainActivity.this);
                            recyclerView.setAdapter(adapterHourly);
                            adapterHourly.notifyDataSetChanged();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "kiem tra lai noi dung ban nhap", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    private String toTime(String day) {
        long m = Long.valueOf(day);
        Date date =  new Date(m * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String datem = simpleDateFormat.format(date);
        return datem;
    }
    static boolean isEmpty(String txt) {
        return TextUtils.isEmpty(txt);
    }
}