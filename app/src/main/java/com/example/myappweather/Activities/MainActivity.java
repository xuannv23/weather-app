package com.example.myappweather.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
import com.example.myappweather.BroadcastReceiver.NetWorkBRC;
import com.example.myappweather.Model.Hourly;
import com.example.myappweather.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar toolbar;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    public static final String CHANNEL_ID = "push_notification_id";
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;
    LocationManager locationManager;
    TextView cityDay, cityNameTxt, mainWeather, hightLow, temp, cloud, windyspeed, humidity;
    ImageView imvIcon;

    private String cityName;
    private Boolean check = true, checkCity = true;
    public static Boolean checkTemp = true;
    private SwipeRefreshLayout swipeRefreshLayout;

    String x;

    int C, Ch, Cl;
    ArrayList<Hourly> tempsF = new ArrayList<>();
    ArrayList<Hourly> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegisterNetWorkBrc();
        setView();
        ktraQuyen();
        setRefresh();
    }

    public void setRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void RegisterNetWorkBrc() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetWorkBRC(MainActivity.this), intentFilter);
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
                InputMethodManager mrg = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mrg.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                searchView.setQuery("", false);
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
//        checkTemp = true;
        if (isEmpty(city)) {
            Toast.makeText(MainActivity.this, "Cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            String link = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";
            String urlc = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";

            x = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=15d04f0b2d4468620d7ad3467eef82f9&units=metric";


            getCurrentWeatherData(link);
            getForeCastWeatherData(urlc);
//            if(checkCity == true){
//                cityNameTxt.setText(city);
//            }
            check = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.next5day) {
            Intent intent = new Intent(MainActivity.this, FutureActivity.class);
            intent.putExtra("link", x);
            intent.putExtra("location", cityNameTxt.getText().toString());
            startActivity(intent);
        }
        if (item.getItemId() == R.id.CtoC) {
            checkTemp = true;
            temp.setText(CC(C) + "°C");
            hightLow.setText("H: " + CC(Ch) + "°C L: " + CC(Cl) + "°C");
            fillDataC();
        }
        if (item.getItemId() == R.id.FtoF) {
            checkTemp = false;
            temp.setText(FF(C) + "°F");
            hightLow.setText("H: " + FF(Ch) + "°F L: " + FF(Cl) + "°F");
            fillDataF();
            Log.d("111111111111111111111111111111111", checkTemp.toString());
        }
        if (item.getItemId() == R.id.info) {
            Intent info = new Intent(MainActivity.this, InforActivity.class);
            startActivity(info);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setView() {
        cityDay = (TextView) findViewById(R.id.city_day);
        mainWeather = (TextView) findViewById(R.id.main_weather);
        imvIcon = (ImageView) findViewById(R.id.image_view_icon);
        hightLow = (TextView) findViewById(R.id.hight_low);
        temp = (TextView) findViewById(R.id.txttemp);
        cloud = (TextView) findViewById(R.id.txtCloud);
        windyspeed = (TextView) findViewById(R.id.txtWindySpeed);
        humidity = (TextView) findViewById(R.id.txtHumidity);
        cityNameTxt = (TextView) findViewById(R.id.nameLocation);
        recyclerView = (RecyclerView) findViewById(R.id.RvHour);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.Refesh);

    }

    public void ktraQuyen() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //nếu chưa có quyền, yêu cầu quyền truy cập vị trí
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Nếu đã có quyền, lấy vị trí hiện tại
            getLocation();
        }
    }

    private String getCityName(Double lon, Double lat) {
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(lat, lon, 10);
            for (Address add : addresses) {
                String city = add.getLocality();
                if (city != null && !city.equals("")) {
                    cityName = city;
                } else {
                    Log.d("TAG", "City not found");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cityName;
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
        Log.d("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",url);
    }
    private void getCurrentWeatherData(String url) {

        // thực thi request mà mình gửi đi
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //location and date
                            cityNameTxt.setText(jsonObject.getString("name"));
                            String day = jsonObject.getString("dt");
                            long m = Long.valueOf(day);
                            Date date =  new Date(m * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                            String datem = simpleDateFormat.format(date);
                            cityDay.setText(datem);

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
                            C = t.intValue();
                            Ch = h.intValue();
                            Cl = l.intValue();
                            hightLow.setText("H: "+hightm+"°C L: "+lowm+"°C");
                            temp.setText(tempm+"°C");

                            //cloud,humidity and windyspeed
                            windyspeed.setText(jsonObject.getJSONObject("wind").getString("speed")+" m/s");
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
                        tempsF.clear();
                        items.clear();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                            JSONObject jsonObjectHour;


                            for(int i=0;i<8;i++){
                                jsonObjectHour = jsonArrayList.getJSONObject(i);
                                String time = toTime(jsonObjectHour.getString("dt"));
                                Double h = Double.valueOf(jsonObjectHour.getJSONObject("main").getString("temp"));
                                int temp = h.intValue();
                                String icon = jsonObjectHour.getJSONArray("weather").getJSONObject(0).getString("icon");
                                String link = "https://openweathermap.org/img/wn/"+icon+"@4x.png";
                                tempsF.add(new Hourly(time,(int) (temp*1.8 + 32), link));
                                items.add(new Hourly(time,temp, link));
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                            fillDataC();


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        checkCity = false;
                        Toast.makeText(MainActivity.this, "Error...", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onRefresh() {
        checkTemp= true;
        if(check == false){
            String x= cityNameTxt.getText().toString();

            getLocation();
            if(!cityName.equals(x)){
                swipeRefreshLayout.setRefreshing(false);
            }
            check = true;
        }else{
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public int CC(int x){
        return x;
    }

    public int FF(int x){
        return (int) (x * 1.8 + 32);
    }

    public void fillDataC(){
        adapterHourly = new HourlyAdapter(items, MainActivity.this);
        recyclerView.setAdapter(adapterHourly);
        adapterHourly.notifyDataSetChanged();
    }
    public void fillDataF(){
        adapterHourly = new HourlyAdapter(tempsF, MainActivity.this);
        recyclerView.setAdapter(adapterHourly);
        adapterHourly.notifyDataSetChanged();
    }
}
