package com.example.doodletask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                implements SwipeRefreshLayout.OnRefreshListener{

    public static final String JSON_STRING = "https://api.coincap.io/v2/assets";

    private SwipeRefreshLayout refresh;
    LinearLayout menubar;
    LinearLayout linear_bottom;
    TextView txt_visibility;
    List<MyListDataAdapter> data = new ArrayList<MyListDataAdapter>();
    RecyclerView.Adapter recyclerViewadapter;//
    RecyclerView recyclerView;//
    LinearLayout lin_seacrh;
    String price,change;

    ArrayList<String>rank=new ArrayList<>();
    ArrayList<String>symbol=new ArrayList<>();
    ArrayList<String>name=new ArrayList<>();
    ArrayList<String>priceUsd=new ArrayList<>();
    ArrayList<String>changePercent24Hr=new ArrayList<>();


    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> coins = new ArrayList<>();

    JSONObject json_name;
    TextView txthead,txtpt,txtName,txtPrice,txtChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.White));
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txt_visibility = findViewById(R.id.txt_visiblity);
        linear_bottom = findViewById(R.id.linear_bottom);
        menubar = findViewById(R.id.menu_bar);
        txthead=findViewById(R.id.txt_head);
        txtpt=findViewById(R.id.txt_pt);
        txtName=findViewById(R.id.txtName);
        txtChange=findViewById(R.id.txtChange);
        txtPrice=findViewById(R.id.txtPrice);

        lin_seacrh=findViewById(R.id.lin_seacrh);
        lin_seacrh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Search_Page.class);
                startActivity(intent);

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager =  new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);//
        recyclerView.setLayoutManager(mLayoutManager);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        menubar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = txt_visibility.getText().toString();

                if (a.equals("open")) {
                    linear_bottom.setVisibility(View.VISIBLE);
                    txt_visibility.setText("close");
                }
                if (a.equals("close")) {
                    linear_bottom.setVisibility(View.GONE);
                    txt_visibility.setText("open");
                }
            }
        });


        refresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setOnRefreshListener(MainActivity.this);

        JSON_HTTP_CALL();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);

    }

    JsonObjectRequest RequestOfJSonArray;
    RequestQueue requestQueue;
    JSONObject jobCat;

    public void JSON_HTTP_CALL() {

        try {
            String URL = JSON_STRING;
            Log.e("---------------", "--------URL----------" + URL);

            RequestOfJSonArray = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("---------------", "--------response----------" + response);

                    try {
                        jobCat = new JSONObject(String.valueOf(response));

                        Log.e("---------------", "--------jobCat response----------" + jobCat);


                        JSONArray jsonArray = jobCat.getJSONArray("data");

                        Log.e("---------------", "--------jsonArray data----------" + jsonArray);

                        rank.clear();
                        symbol.clear();
                        name.clear();
                        priceUsd.clear();
                        changePercent24Hr.clear();
                        data.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                Log.e("---------------", "--------entering for loop---------" );


                                MyListDataAdapter GetDataAdapter2 = new MyListDataAdapter();

                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    try {
                                        String a = jsonObject.getString("priceUsd");
                                        String b = jsonObject.getString("changePercent24Hr");
                                        Log.e("---n---", "---string value---" + a);
                                        double n = Double.parseDouble(a);
                                        double m = Double.parseDouble(b);
                                        Log.e("---n---", "---double value---" + n);
                                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                                        DecimalFormat twoDForm1 = new DecimalFormat("#.##");
                                        double f = Double.valueOf(twoDForm.format(n));
                                        double g = Double.valueOf(twoDForm1.format(m));
                                        Log.e("---f---", "---final double value---" + f);
                                        price=String.valueOf(f);
                                        change=String.valueOf(g);
                                        Log.e("---f---", "---double to string---" + price);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        Log.e("------errorrr---","---error in formating---");
                                    }


                                    GetDataAdapter2.setRank(jsonObject.getString("rank"));
                                    GetDataAdapter2.setSymbol(jsonObject.getString("symbol"));
                                    GetDataAdapter2.setName(jsonObject.getString("name"));
                                    GetDataAdapter2.setPriceUsd(price);
                                    GetDataAdapter2.setChangePercent24Hr(change);

                                    rank.add(jsonObject.getString("rank"));
                                    symbol.add(jsonObject.getString("symbol"));
                                    name.add(jsonObject.getString("name"));
                                    priceUsd.add(jsonObject.getString("priceUsd"));
                                    changePercent24Hr.add(jsonObject.getString("changePercent24Hr"));

                                   coins.add(jsonObject.getString("name").toString());

                                    String name = jsonObject.getString("name");
                                    coins.add(name);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                data.add(GetDataAdapter2);

                            }


                        try {
                            String names[] = coins.toArray(new String[0]);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (MainActivity.this, android.R.layout.simple_list_item_1, names);
                            AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.at_search);
                            actv.setThreshold(1);
                            actv.setAdapter(adapter);
                            actv.setTextColor(Color.BLUE);
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("--------error in search----","");
                        }


                        refresh.setRefreshing(false);

                        recyclerViewadapter = new MyListRecyclerview(data, MainActivity.this);

                        recyclerView.setAdapter(recyclerViewadapter);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.e("---------------JSON RESPONCE------------", "---------" + jobCat);

                }
            },
                    new Response.ErrorListener( ) {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("---------------JSON RESPONCE------------", "-----ERRORRRRRRRR----"+error);

                        }
                    });

            requestQueue = Volley.newRequestQueue(MainActivity.this);

            requestQueue.add(RequestOfJSonArray);

        } catch (Exception e) {
            Log.e("---------Http Recyler Exception-----", "" + e);
        }


    }

    @Override
    public void onRefresh() {

        refresh.setRefreshing(true);
        JSON_HTTP_CALL();


    }

}