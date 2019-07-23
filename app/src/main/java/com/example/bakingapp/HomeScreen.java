package com.example.bakingapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bakingapp.BakingModel.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private static final String BAKING_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    RecyclerView rv;
    List<Recipe> recipeList = new ArrayList<>();
    Recipe recipes[];
    HomeScreen_Adapter hsa;

    boolean b=false;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        pd=new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.load));
        pd.show();
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            pd.cancel();
            getData();
        }
        else
        {
            AlertDialog.Builder ab=new AlertDialog.Builder(this);
            ab.setTitle(getResources().getString(R.string.checknet));
            ab.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            ab.show();
        }
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(BAKING_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                recipes = gson.fromJson(response, Recipe[].class);
                recipeList.addAll(Arrays.asList(recipes));
                hsa = new HomeScreen_Adapter(HomeScreen.this, recipeList);
                rv.setAdapter(hsa);
                rv.setLayoutManager(new LinearLayoutManager(HomeScreen.this));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}