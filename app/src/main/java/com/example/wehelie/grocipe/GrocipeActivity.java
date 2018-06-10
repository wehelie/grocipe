package com.example.wehelie.grocipe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
//import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;

public class GrocipeActivity extends AppCompatActivity {

    EditText nDish;
    EditText gRecipe;
    Button closeAdd;
    Button delete;
    boolean isItemNew = false;
    Spinner menu;
    public ArrayList<String> menuList = new ArrayList<>(Arrays.asList(Constants.meals));
    AppDatabase gDatabase;
    Grocipe updateGrocery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // find views
        menu = findViewById(R.id.menu);
        nDish = findViewById(R.id.dName);
        closeAdd = findViewById(R.id.closeAdd);
        gRecipe = findViewById(R.id.grecipe);
        delete = findViewById(R.id.delete);

        // drop down menu for mealtypes
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, menuList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(adapter);

        // Creates a RoomDatabase.Builder for a persistent database.
        gDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME).build();

        // setup id
        int grocipe_id = getIntent().getIntExtra(Constants.GROCIPE_ID, -100);

        // check if id is defaultValue
        if (grocipe_id  == -100) {
            isItemNew = true;
        }
        // if it is not a new item, show the delete button
        if (!isItemNew) {
            fetchById(grocipe_id );
            delete.setVisibility(View.VISIBLE);
        }


        closeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check to see if the fields are empgty
                if (nDish.getText().toString().matches("") || gRecipe.getText().toString().matches("")) {
                    Toasty.warning(getApplicationContext(), "Please fill out all required fields!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (isItemNew) {
                    Grocipe grocipe = new Grocipe();
                    grocipe.dishName = nDish.getText().toString();
                    grocipe.recipe = gRecipe.getText().toString();
                    grocipe.mealtype = menu.getSelectedItem().toString();

                    insertRow(grocipe);
                } else {

                    updateGrocery.dishName = nDish.getText().toString();
                    updateGrocery.recipe = gRecipe.getText().toString();
                    updateGrocery.mealtype = menu.getSelectedItem().toString();

                    updateRow(updateGrocery);
                }
            }
        });

        // delete an item from the grocipe list
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow(updateGrocery);
            }
        });
    }


    // method to insert row into database
    @SuppressLint("StaticFieldLeak")
    private void insertRow(Grocipe grocipe) {
        new AsyncTask<Grocipe, Void, Long>() {
            @Override
            protected Long doInBackground(Grocipe... lists) {
                return gDatabase.GrocipeDao().insertGroceryItem(lists[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra(Constants.ISNEW, true).putExtra(Constants.GROCIPE_ID, id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(grocipe);

    }

    // get an item by id
    @SuppressLint("StaticFieldLeak")
    private void fetchById(final int grocipe_id) {
        new AsyncTask<Integer, Void, Grocipe>() {
            @Override
            protected Grocipe doInBackground(Integer... lists) {

                return gDatabase.GrocipeDao().fetchGrocipeListById(lists[0]);

            }

            @Override
            protected void onPostExecute(Grocipe grocipe) {
                super.onPostExecute(grocipe);
                nDish.setText(grocipe.dishName);
                gRecipe.setText(grocipe.recipe);
                menu.setSelection(menuList.indexOf(grocipe.mealtype));

                updateGrocery = grocipe;
            }
        }.execute(grocipe_id);

    }

    // deletes a row in database
    @SuppressLint("StaticFieldLeak")
    private void deleteRow(Grocipe grocipe) {
        new AsyncTask<Grocipe, Void, Integer>() {
            @Override
            protected Integer doInBackground(Grocipe... lists) {
                return gDatabase.GrocipeDao().deleteGrocipe(lists[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra(Constants.ISDELETED, true).putExtra(Constants.NUMS, number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(grocipe);

    }


    // updates row in database
    @SuppressLint("StaticFieldLeak")
    private void updateRow(Grocipe grocipe) {
        new AsyncTask<Grocipe, Void, Integer>() {
            @Override
            protected Integer doInBackground(Grocipe... lists) {
                return gDatabase.GrocipeDao().updateGrocipe(lists[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra(Constants.ISNEW, false).putExtra(Constants.NUMS, number);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }.execute(grocipe);

    }

}