package com.example.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakingapp.BakingModel.Ingredient;
import com.example.bakingapp.BakingModel.Step;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    ArrayList<Step> stepArray;
    ArrayList<Ingredient> ingredients;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        String action_bar_name = getIntent().getStringExtra(KeyConstants.recipe);
        getSupportActionBar().setTitle(action_bar_name);

        stepArray = new ArrayList<>();
        ingredients = new ArrayList<>();

        ingredients = getIntent().getParcelableArrayListExtra(KeyConstants.ingre);
        stepArray = getIntent().getParcelableArrayListExtra(KeyConstants.step);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        Toast.makeText(this, ""+mTwoPane, Toast.LENGTH_SHORT).show();
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter
                (this, stepArray,
                        ingredients, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final ArrayList<Step> stepArrayList;
        private final ArrayList<Ingredient> ingredientArrayList;
        private final boolean mTwoPane;

        public SimpleItemRecyclerViewAdapter(ItemListActivity mParentActivity, ArrayList<Step> stepArrayList, ArrayList<Ingredient> ingredientArrayList, boolean mTwoPane) {
            this.mParentActivity = mParentActivity;
            this.stepArrayList = stepArrayList;
            this.ingredientArrayList = ingredientArrayList;
            this.mTwoPane = mTwoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_list_content,
                            viewGroup,
                            false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            if (i == 0) {
                viewHolder.mContentView.
                        setText(mParentActivity.getString(R.string.ingredients));

            } else if (i == 1) {
                viewHolder.mContentView.setText(stepArrayList.get(i - 1)
                        .getShortDescription());
            } else {
                viewHolder.mContentView.setText(
                        stepArrayList.get(i - 1).getId() + mParentActivity.getResources().getString(R.string.dot) +
                                stepArrayList.get(i - 1).getShortDescription());
            }

        }

        @Override
        public int getItemCount() {
            return stepArrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position == 0) {
                    if (mTwoPane) {

                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(KeyConstants.ingres, ingredientArrayList);
                        IngredientFragment ingredientFragment = new IngredientFragment();
                        ingredientFragment.setArguments(bundle);
                        FragmentManager fm = mParentActivity
                                .getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.exo_player_fragment,
                                ingredientFragment).commit();
                        addWidget();
                    } else {
                        Intent intent = new Intent(mParentActivity, IngredientsDetails.class);
                        intent.putParcelableArrayListExtra(KeyConstants.ingres, ingredientArrayList);

                        itemView.getContext().startActivity(intent);
                        addWidget();
                    }

                } else {
                    if (mTwoPane) {
                        Bundle b = new Bundle();
                        b.putString(mParentActivity.getResources().getString(R.string.vurl),
                                stepArrayList.get(position - 1)
                                        .getVideoURL());
                        b.putString(mParentActivity.getResources().getString(R.string.vd),
                                stepArrayList.get(position - 1)
                                        .getDescription());
                        b.putInt(mParentActivity.getResources().getString(R.string.pos),getAdapterPosition());
                        b.putParcelableArrayList(mParentActivity.getResources()
                                .getString(R.string.steplist),stepArrayList);
                        ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
                        exoPlayerFragment.setArguments(b);
                        mParentActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.exo_player_fragment, exoPlayerFragment)
                                .commit();

                        FragmentManager fragmentManager = mParentActivity
                                .getSupportFragmentManager();

                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();

                    } else {
                        Intent intent = new Intent(mParentActivity,
                                ItemDetailActivity.class);
                        intent.putExtra(mParentActivity.getResources().getString(R.string.vurl), stepArrayList.get(position - 1)
                                .getVideoURL());
                        intent.putExtra(mParentActivity.getResources().getString(R.string.vdesc), stepArrayList.get(position - 1)
                                .getDescription());
                        intent.putParcelableArrayListExtra(mParentActivity.getResources().getString(R.string.list), stepArrayList);
                        intent.putExtra(mParentActivity.getResources().getString(R.string.positionstep), position);
                        itemView.getContext()
                                .startActivity(intent);

                    }
                }

            }

            private void addWidget() {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < ingredientArrayList.size(); i++) {
                    String measure = ingredientArrayList.get(i).getMeasure();
                    String quantity = ingredientArrayList.get(i).getQuantity();
                    String ing = ingredientArrayList.get(i).getIngredient();
                    sb.append(ing + "\n" + quantity + "\t" + measure + "\n");
                }
                SharedPreferences sp = mParentActivity
                        .getSharedPreferences(KeyConstants.filename,
                                MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(mParentActivity.getResources().getString(R.string.q), sb.toString());
                editor.commit();


                Intent intent1 = new Intent(mParentActivity, BakingAppWidget.class);
                intent1.setAction(mParentActivity.getResources().getString(R.string.updatewidget));
                int ids[] = AppWidgetManager.
                        getInstance(mParentActivity.getApplication())
                        .getAppWidgetIds(new ComponentName(mParentActivity, BakingAppWidget.class));
                intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                mParentActivity.sendBroadcast(intent1);
            }
        }
    }
}
