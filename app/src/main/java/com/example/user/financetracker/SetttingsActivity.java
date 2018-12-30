package com.example.user.financetracker;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class SetttingsActivity extends AppCompatActivity {

    private ConstraintLayout settings;
    private RadioGroup theme, lang;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setttings);

        preferences = getSharedPreferences("value", MODE_PRIVATE);

        settings = (ConstraintLayout) findViewById(R.id.settings);
        theme = (RadioGroup) findViewById(R.id.theme);
        theme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton tempColor = (RadioButton) findViewById(checkedId);
                String checkColor = tempColor.getText().toString();
                Toast.makeText(SetttingsActivity.this, checkColor, Toast.LENGTH_SHORT).show();
                if(checkColor.equals("Blue")) {
                    settings.setBackgroundColor(getResources().getColor(R.color.blue));
                    preferences.edit()
                            .putString("color", "blue")
                            .apply();
                }
                else if(checkColor.equals("Green")){
                    settings.setBackgroundColor(getResources().getColor(R.color.green));
                    preferences.edit()
                            .putString("color", "green")
                            .apply();
                }
                else if(checkColor.equals("Red")){
                    settings.setBackgroundColor(getResources().getColor(R.color.red));
                    preferences.edit()
                            .putString("color", "red")
                            .apply();
                }
                else {
                    settings.setBackgroundColor(getResources().getColor(R.color.defwhite));
                    preferences.edit()
                            .putString("color", "white")
                            .apply();
                }
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

                if(checkLang.equals("Japanese")){
                    preferences.edit()
                            .putString("lang", "ja")
                            .apply();
                    Locale locale = new Locale("ja");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                }
                else if(checkLang.equals("English")){
                    preferences.edit()
                            .putString("lang", "en")
                            .apply();
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String color = preferences.getString("color", "white");

        if(color.equals("red")){
            settings.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if(color.equals("green")){
            settings.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if (color.equals("blue")){
            settings.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        else {
            settings.setBackgroundColor(getResources().getColor(R.color.defwhite));
        }

        String lang = preferences.getString("lang", "en");

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
