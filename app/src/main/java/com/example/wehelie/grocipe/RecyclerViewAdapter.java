package com.example.wehelie.grocipe;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Grocipe> grocipeList;
    private RecyclerViewAdapter.ClickListener clickListener;

    public RecyclerViewAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        grocipeList = new ArrayList<>();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Grocipe todo = grocipeList.get(position);
        holder.dishName.setText(todo.dishName);
        holder.recipeNumber.setText("Recipe #" + String.valueOf(todo.groceryItem_id));
        holder.recipe.setText(todo.recipe);
        holder.meals.setText(todo.mealtype);

    }

    @Override
    public int getItemCount() {
        return grocipeList.size();
    }


    public void updateGrocipeList(List<Grocipe> data) {
        grocipeList.clear();
        grocipeList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Grocipe data) {
        grocipeList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dishName;
        public TextView recipeNumber;
        public TextView recipe;
        public TextView meals;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            recipeNumber = view.findViewById(R.id.recipeNumber);
            dishName = view.findViewById(R.id.dishName);
            recipe = view.findViewById(R.id.recipe);
            meals = view.findViewById(R.id.meals);
            cardView = view.findViewById(R.id.card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.launchIntent(grocipeList.get(getAdapterPosition()).groceryItem_id);
                }
            });
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}