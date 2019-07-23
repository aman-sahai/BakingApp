package com.example.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.BakingModel.Ingredient;

import java.util.ArrayList;

public class IngredientsDetails extends AppCompatActivity {

    ArrayList<Ingredient> list;
    RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_details);
        list = getIntent().getParcelableArrayListExtra(KeyConstants.ingres);
        rv = findViewById(R.id.ingredient_details_rv);
        MyIngredientAdapter my = new MyIngredientAdapter(list,this);
        rv.setAdapter(my);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    public class MyIngredientAdapter extends RecyclerView.Adapter<MyIngredientAdapter.MyViewHolder> {
        ArrayList<Ingredient> ingredientArrayList;
        Context context;

        public MyIngredientAdapter(ArrayList<Ingredient> ingredientArrayList, Context context) {
            this.ingredientArrayList = ingredientArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public MyIngredientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.ingredient_adapter,
                            viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyIngredientAdapter.MyViewHolder myViewHolder, int i) {
            myViewHolder
                    .textView_ingredients
                    .setText(getResources().getString(R.string.in) + ingredientArrayList.get(i).getIngredient()
                            + "\n" + getResources().getString(R.string.quant) + ingredientArrayList.get(i).getQuantity()
                            + "\n" + getResources().getString(R.string.measure) + ingredientArrayList.get(i).getMeasure());

        }

        @Override
        public int getItemCount() {
            return ingredientArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView_ingredients;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView_ingredients =itemView.findViewById(R.id.ing);
            }
        }
    }
}
