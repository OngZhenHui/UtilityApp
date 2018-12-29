package com.example.user.financetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SetttingsActivity extends AppCompatActivity {

    private RadioGroup theme, lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setttings);

        theme = (RadioGroup) findViewById(R.id.theme);
        theme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton tempColor = (RadioButton) findViewById(checkedId);
                String checkColor = tempColor.getText().toString();
                Toast.makeText(SetttingsActivity.this, checkColor, Toast.LENGTH_SHORT).show();
            }
        });

        lang = (RadioGroup) findViewById(R.id.language);
        lang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //checkedID is RadioButton Selected
                RadioButton tempLang = (RadioButton) findViewById(checkedId);
                String checkLang = tempLang.getText().toString();
                Toast.makeText(SetttingsActivity.this, checkLang, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
