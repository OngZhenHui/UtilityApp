package com.example.user.financetracker;

import java.text.NumberFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//firebase import
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button add, save, cancel;
    private TextView date, remain_amt;
    private EditText startAmt;
    private Boolean check;
    private DatePickerDialog.OnDateSetListener mPickDate;

    private int chg_count;
    private int item_count;
    private Boolean check_trans;
    private String dateRef;
    private Double amt_left;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference translist;
    private ChildEventListener childListener;
    private ChildEventListener secondListener;
    private ListView trans_view;

    //Arraylist & Adapter
    private ArrayList<String> mTransactions =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transation);

        //Declaration
        add = (Button) findViewById(R.id.add_trans);
        save = (Button) findViewById(R.id.saveBtn);
        cancel = (Button) findViewById(R.id.cancelBtn);
        add.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        date = (TextView) findViewById(R.id.date);
        date.setOnClickListener(this);
        startAmt = (EditText) findViewById(R.id.start_amt);
        remain_amt = (TextView) findViewById(R.id.remain_amt);

        final NumberFormat format = NumberFormat.getCurrencyInstance();

        dateRef = MainActivity.dayRef;
        check_trans = false;

        //firebase
        database = FirebaseDatabase.getInstance();
        translist = database.getReference();
        trans_view = (ListView) findViewById(R.id.transList);


        if(!dateRef.isEmpty() || !dateRef.equals("")){
            date.setText(dateRef);
            translist.child("Days").child(dateRef).child("Start").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String startValue = dataSnapshot.getValue().toString();
                    startAmt.setText(startValue);
                    amt_left = Double.parseDouble(startValue);
                    remain_amt.setText(amt_left.toString());
                    check_trans = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        //if end
        }
        if(check_trans=true){
            //array adapter for reading firebase data
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTransactions);
            trans_view.setAdapter(arrayAdapter);
            childListener = translist.child("Days").child(dateRef).child("Transactions").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Double number = Double.parseDouble(dataSnapshot.getValue().toString());
                    String value = dataSnapshot.getKey() + ":\n\n" + format.format(number);
                    mTransactions.add(value);
                    amt_left = amt_left + number;
                    String amt = format.format(amt_left);
                    remain_amt.setText(amt);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    arrayAdapter.remove(arrayAdapter.getItem(mTransactions.indexOf(dataSnapshot.getKey())));
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())){
            //add amount
            case R.id.add_trans:
                check = checkfield();
                if(check != false) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(TransactionActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.add_dialog, null);
                    final EditText mTitle = (EditText) mView.findViewById(R.id.add_title);
                    final EditText mAmt = (EditText) mView.findViewById(R.id.add_amt);
                    Button mConfirm = (Button) mView.findViewById(R.id.add_confirm);
                    Button mCancel = (Button) mView.findViewById(R.id.add_cancel);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    //if confirm
                    mConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mTitle.getText().toString().isEmpty()) {
                                Toast.makeText(TransactionActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                            } else if (mAmt.getText().toString().isEmpty()) {
                                Toast.makeText(TransactionActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TransactionActivity.this, "Amount Added", Toast.LENGTH_SHORT).show();
                                check_trans = true;
                                dateRef = date.getText().toString();
                                translist.child("Days").child(dateRef).child("Transactions").child(mTitle.getText().toString()).setValue(mAmt.getText().toString());
                                translist.child("Days").child(dateRef).child("Start").setValue(startAmt.getText().toString());
                                dialog.dismiss();
                            }
                        }
                    });


                    //if cancel
                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(TransactionActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.saveBtn:
                Intent save = new Intent(TransactionActivity.this, MainActivity.class);
                startActivity(save);
                break;
            case R.id.cancelBtn:
                Intent cancel = new Intent(TransactionActivity.this, MainActivity.class);
                startActivity(cancel);
                break;
            case R.id.date:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(
                        TransactionActivity.this,
                        android.R.style.Theme_DeviceDefault,
                        mPickDate,
                        year,month,day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dateDialog.show();
                break;
        }

        mPickDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String mDate = month + "-" + day + "-" + year;
                date.setText(mDate);
            }
        };
    }

    private Boolean checkfield() {
        if(!date.getText().toString().equals("Select Date") && !startAmt.getText().toString().isEmpty()){
            return true;
        }
        else{
            Toast.makeText(TransactionActivity.this, "Please enter both the date and start amount to continue",Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
