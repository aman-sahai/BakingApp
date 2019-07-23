package com.example.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakingapp.BakingModel.Ingredient;

import java.util.ArrayList;

public class IngredientFragment extends Fragment {

    RecyclerView rv;

    public IngredientFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ingredient, container, false);
        rv = v.findViewById(R.id.rv_ingredients);

        ArrayList<Ingredient> ingredientArrayList = getArguments().
                getParcelableArrayList(KeyConstants.ingres);

        Toast.makeText(getContext(), ""+ingredientArrayList.size(), Toast.LENGTH_SHORT).show();
        IngredientsAdapter ingredientsAdapter = new
                IngredientsAdapter(ingredientArrayList, getContext());

        rv.setAdapter(ingredientsAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {
        ArrayList<Ingredient> ingredientArrayList;
        Context context;

        public IngredientsAdapter(ArrayList<Ingredient> ingredientArrayList, Context context) {
            this.ingredientArrayList = ingredientArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public IngredientsAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.ingredient_adapter, viewGroup, false);
            IngredientViewHolder ivh = new IngredientViewHolder(view);
            return ivh;
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientViewHolder ingredientViewHolder, int i) {
            ingredientViewHolder
                    .ing_tv
                    .setText(getResources().getString(R.string.in) + ingredientArrayList.get(i).getIngredient()
                            + "\n" + getResources().getString(R.string.quant) + ingredientArrayList.get(i).getQuantity()
                            + "\n" + getResources().getString(R.string.measure) + ingredientArrayList.get(i).getMeasure());

        }

        @Override
        public int getItemCount() {
            return ingredientArrayList!=null?ingredientArrayList.size():0;
        }

        public class IngredientViewHolder extends RecyclerView.ViewHolder {
            TextView ing_tv;

            public IngredientViewHolder(@NonNull View itemView) {
                super(itemView);
                ing_tv = itemView.findViewById(R.id.ing);
            }
        }
    }
}
