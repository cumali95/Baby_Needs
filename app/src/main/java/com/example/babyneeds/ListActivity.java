package com.example.babyneeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

       recyclerView=findViewById(R.id.recyclerview);
       fab=findViewById(R.id.fab);

      databaseHandler=new DatabaseHandler(this);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));

      itemList=new ArrayList<>();

      itemList=databaseHandler.getAllItems();
      for (Item item:itemList)
      {
          Log.d("activityList", "onCreate: "+item.getItemName());

      }

      recyclerViewAdapter=new RecyclerViewAdapter(this,itemList);
      recyclerView.setAdapter(recyclerViewAdapter);
      recyclerViewAdapter.notifyDataSetChanged();

      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              createPopDialog();
          }
      });


    }

    private void createPopDialog() {
        builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);


        babyItem=view.findViewById(R.id.babyItem);
        itemQuantity=view.findViewById(R.id.itemQuantity);
        itemColor=view.findViewById(R.id.itemColor);
        itemSize=view.findViewById(R.id.itemSize);
        saveButton=view.findViewById(R.id.saveButton);




        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!babyItem.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty())
                {

                    saveItem(v);

                }else {
                    Snackbar.make(v,"empty fields not allowed ",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void saveItem(View v) {
        Item item = new Item();

        String newItem = babyItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int size = Integer.parseInt(itemSize.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(quantity);
        item.setItemSize(size);

        databaseHandler.addItem(item);

        Snackbar.make(v, "Item Saved",Snackbar.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to be run
                alertDialog.dismiss();

                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        },1200);


    }
}
