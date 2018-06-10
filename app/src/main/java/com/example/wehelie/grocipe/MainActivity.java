package com.example.wehelie.grocipe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ClickListener, AdapterView.OnItemSelectedListener {
    Spinner menu;
    AppDatabase gDatabase;
    RecyclerView recyclerView;
    private final int  SPLASH_DISPLAY_LENGTH = 3000;
    RecyclerViewAdapter recyclerViewAdapter;
    FloatingActionButton Addbutton;
    ArrayList<String> menuList = new ArrayList<>(Arrays.asList(Constants.meals));
    ArrayList<Grocipe> grocipeArrayList = new ArrayList<>();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.spinner);
        Addbutton = findViewById(R.id.floatingButton);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, menuList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(adapter);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        gDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME).fallbackToDestructiveMigration().build();
        checkFirstRun(savedInstanceState);

        menu.setOnItemSelectedListener(this);
        menu.setSelection(0);

        Addbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, GrocipeActivity.class), Constants.NEW_REQUEST_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            menu.setSelection(0);

            if (requestCode == Constants.NEW_REQUEST_CODE) {
                long id = data.getLongExtra("id", -1);
                Toasty.success(getApplicationContext(), "New Grocery Recipe Has ben created", Toast.LENGTH_SHORT).show();
                fetchGrocipeByIdInsert((int) id);

            } else if (requestCode == Constants.UPDATE_REQUEST_CODE) {
                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toasty.info(getApplicationContext(), number + " Deleted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.success(getApplicationContext(), number +" Updated!", Toast.LENGTH_SHORT).show();
                }

                loadAllGrocipes();
            }


        } else {
            Toasty.info(getApplicationContext(), "No Changes Made To Your Groccery List", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkFirstRun(Bundle savedInstanceState) {
     Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);

     if (isFirstRun && null == savedInstanceState) {
         Toasty.success(getApplicationContext(), "Click on the + button to start", Toast.LENGTH_SHORT).show();
         Grocipe grocipe = new Grocipe();
         grocipe.dishName = "Pizza";
         grocipe.mealtype = "Lunch";
         grocipe.recipe = "Blue Cheese, Walnut, and Pear Pizza";

         grocipeArrayList.add(grocipe);

         insertGrocipeList(grocipeArrayList);

     }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertGrocipeList(List<Grocipe> grocipeList) {
        new AsyncTask<List<Grocipe>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Grocipe>... lists) {
                gDatabase.GrocipeDao().insertGrocipeList(lists[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
            }
        }.execute(grocipeList);
    }

    @Override
    public void launchIntent(int id) {
        // get result from the other activity.
        startActivityForResult(new Intent(MainActivity.this, GrocipeActivity.class).putExtra("id", id), Constants.UPDATE_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if (position == 0) {
            loadAllGrocipes();
        } else {
            String string = arg0.getItemAtPosition(position).toString();
            filteredGrocipeList(string);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toasty.warning(MainActivity.this, "Nothing is selected", Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("StaticFieldLeak")
    private void filteredGrocipeList(String category) {
        new AsyncTask<String, Void, List<Grocipe>>() {
            @Override
            protected List<Grocipe> doInBackground(String... lists) {
                return gDatabase.GrocipeDao().fetchGrocipeByCategory(lists[0]);
            }

            @Override
            protected void onPostExecute(List<Grocipe> grocipeList) {
                recyclerViewAdapter.updateGrocipeList(grocipeList);
            }
        }.execute(category);

    }


    @SuppressLint("StaticFieldLeak")
    private void fetchGrocipeByIdInsert(int id) {
        new AsyncTask<Integer, Void, Grocipe>() {
            @Override
            protected Grocipe doInBackground(Integer... lists) {
                return gDatabase.GrocipeDao().fetchGrocipeListById(lists[0]);

            }

            @Override
            protected void onPostExecute(Grocipe grocipe) {
                recyclerViewAdapter.addRow(grocipe);
            }
        }.execute(id);

    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllGrocipes() {
        new AsyncTask<String, Void, List<Grocipe>>() {
            @Override
            protected List<Grocipe> doInBackground(String... lists) {

                return gDatabase.GrocipeDao().fetchAllGrocipe();
            }

            @Override
            protected void onPostExecute(List<Grocipe> grocipeList) {

                recyclerViewAdapter.updateGrocipeList(grocipeList);
            }
        }.execute();
    }


}