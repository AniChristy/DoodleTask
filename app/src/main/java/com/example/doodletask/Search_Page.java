package com.example.doodletask;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.app.SearchManager;

import android.widget.SearchView.OnQueryTextListener;

public class Search_Page extends AppCompatActivity  {

    ProgressBar pro;
    View v;
    SearchRecyclerview recyclerViewadapter;
    SearchRecyclerview adapter;
    RecyclerView recyclerView;//
    int RecyclerViewItemPosition ;//
    ArrayList<String> ArrayListProductID2;
    List<SearchAdapter> ListOfdataAdapter1;
    List<SearchAdapter> filterList;
    ArrayList<String> coins = new ArrayList<>();

    public static final String JSON_STRING = "https://api.coincap.io/v2/assets";

    ArrayList<String>name=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__page);

        pro = findViewById(R.id.pro);
        ArrayListProductID2  = new ArrayList<>();//
        ListOfdataAdapter1 = new ArrayList<>();//
        filterList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager =  new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);//
        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(Search_Page.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });


            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                v = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(v != null && gestureDetector.onTouchEvent(motionEvent)) {

                    Intent intent=new Intent(getApplicationContext(),Description.class);
                    startActivity(intent);

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        JSON_HTTP_CALL();
    }
    JsonObjectRequest RequestOfJSonArray;//
    RequestQueue requestQueue;//
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


                        name.clear();
                        ListOfdataAdapter1.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Log.e("---------------", "--------entering for loop---------" );


                            SearchAdapter GetDataAdapter2 = new SearchAdapter();

                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                GetDataAdapter2.setPName(jsonObject.getString("name"));

                                name.add(jsonObject.getString("name"));

                                coins.add(jsonObject.getString("name").toString());

                                String name = jsonObject.getString("name");
                                coins.add(name);


                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            ListOfdataAdapter1.add(GetDataAdapter2);

                        }


                        recyclerViewadapter = new SearchRecyclerview(ListOfdataAdapter1, Search_Page.this);

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

            requestQueue = Volley.newRequestQueue(Search_Page.this);

            requestQueue.add(RequestOfJSonArray);

        } catch (Exception e) {
            Log.e("---------Http Recyler Exception-----", "" + e);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.recycler_menu,menu);
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                List<SearchAdapter>myList=new ArrayList<>();
                for(SearchAdapter search:ListOfdataAdapter1){
                    String name=search.getPName().toLowerCase();
                    if(name.contains(newText)){
                        myList.add(search);
                    }
                }
                recyclerViewadapter.setSearchOperation(myList);
                return false;
            }
        });
        return true;
    }
}





