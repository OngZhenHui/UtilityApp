package com.example.user.financetracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

//firebase import
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SwipeMenuListView listview_days;
    private Button addBtn;
    public static String dayRef;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference financelist;
    private ChildEventListener childListener;

    //Arraylist & Adapter
    private ArrayList<String> list_days = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase
        database = FirebaseDatabase.getInstance();
        financelist = database.getReference();

        listview_days = (SwipeMenuListView) findViewById(R.id.dayList);
        addBtn = (Button) findViewById(R.id.addDay);

        //array adapter for reading firebase data
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_days);
        listview_days.setAdapter(arrayAdapter);
        childListener = financelist.child("Days").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getKey();
                list_days.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                arrayAdapter.remove(arrayAdapter.getItem(list_days.indexOf(dataSnapshot.getKey())));
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayRef = "";
                Intent transactions = new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(transactions);
            }
        });

        listview_days.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dayRef = list_days.get(position);
                Intent trans = new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(trans);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.RED));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listview_days.setMenuCreator(creator);

        listview_days.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                financelist.child("Days").child(list_days.get(position)).removeValue();
                //close the menu
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent settings = new Intent(MainActivity.this, SetttingsActivity.class);
        startActivity(settings);
        return super.onOptionsItemSelected(item);
    }

}
