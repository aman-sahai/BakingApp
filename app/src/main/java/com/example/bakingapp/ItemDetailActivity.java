package com.example.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.bakingapp.BakingModel.Step;

import java.util.ArrayList;
public class ItemDetailActivity extends AppCompatActivity {

    private ArrayList<Step> stepArrayList;
    private String video_url,desc;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        stepArrayList=new ArrayList<>();


        if(savedInstanceState == null) {

            position=getIntent().getIntExtra(getResources().getString(R.string.positionstep),0);
            video_url=getIntent().getStringExtra(getResources().getString(R.string.vurl));
            desc=getIntent().getStringExtra(getResources().getString(R.string.vdesc));
            stepArrayList=getIntent().getParcelableArrayListExtra(getResources().getString(R.string.list));

            Bundle bundle=new Bundle();
            bundle.putParcelableArrayList(getResources().getString(R.string.steplist),  stepArrayList);
            bundle.putString(getResources().getString(R.string.vurl),video_url);
            bundle.putString(getResources().getString(R.string.vd),desc);
            bundle.putInt(getResources().getString(R.string.vd),position);

            FragmentManager f_m = getSupportFragmentManager();
            FragmentTransaction f_t = f_m.beginTransaction();
            ExoPlayerFragment epf = new ExoPlayerFragment();
            epf.setArguments(bundle);
            f_t.replace(R.id.exoplayer1, epf).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent=new Intent(this,ItemListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
