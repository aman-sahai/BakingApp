package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bakingapp.BakingModel.Recipe;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen_Adapter extends RecyclerView.Adapter<HomeScreen_Adapter.MyHomeViewHolder>
{
    Context ct;
    List<Recipe> recipeList;

    public HomeScreen_Adapter(Context ct, List<Recipe> recipeList) {
        this.ct = ct;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public MyHomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v= LayoutInflater
                .from(ct)
                .inflate(R.layout.home_screen_adapter_layout, viewGroup,false);
        return new MyHomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHomeViewHolder myHomeViewHolder, int i)
    {
        Glide.with(ct)
                .load(recipeList.get(i).getImage())
                .placeholder(R.drawable.food_placeholder)
                .into(myHomeViewHolder.iv);
        myHomeViewHolder.tv.setText(recipeList.get(i).getName());
    }

    @Override
    public int getItemCount()
    {
        return recipeList.size();
    }

    public class MyHomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        TextView tv;
        public MyHomeViewHolder(@NonNull View itemView)
        {
            super(itemView);
            iv=itemView.findViewById(R.id.recipe_image);
            tv=itemView.findViewById(R.id.recipe_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            Intent intent=new Intent(ct,ItemListActivity.class);
            intent.putExtra(KeyConstants.recipe,
                    recipeList.get(pos).getName());
            intent.putParcelableArrayListExtra(KeyConstants.step,
                    (ArrayList<? extends Parcelable>) recipeList.get(pos).getSteps());
            intent.putParcelableArrayListExtra(KeyConstants.ingre,
                    (ArrayList<? extends Parcelable>) recipeList.get(pos).getIngredients());
            ct.startActivity(intent);


        }
    }
}
