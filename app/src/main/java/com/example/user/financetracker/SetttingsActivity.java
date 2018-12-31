package com.example.user.financetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class SetttingsActivity extends AppCompatActivity {

    private ConstraintLayout settings;
    private RadioGroup theme, lang;
    private Button confirm;
    private SharedPreferences preferences;
    private RadioButton whiteBg,blueBg,greenBg,redBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setttings);

        preferences = getSharedPreferences("value", MODE_PRIVATE);

        whiteBg = (RadioButton) findViewById(R.id.whiteBg);
        greenBg = (RadioButton) findViewById(R.id.greenBg);
        blueBg = (RadioButton) findViewById(R.id.blueBg);
        redBg = (RadioButton) findViewById(R.id.redBg);
        confirm = (Button) findViewById(R.id.confirm_set);
        settings = (ConstraintLayout) findViewById(R.id.settings);
        theme = (RadioGroup) findViewById(R.id.theme);
        theme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton tempColor = (RadioButton) findViewById(checkedId);
                int checkColor = tempColor.getId();
                if(checkColor==blueBg.getId()){
                    settings.setBackgroundColor(getResources().getColor(R.color.blue));
                    preferences.edit()
                            .putString("color", "blue")
                            .apply();
                }
                else if((checkColor==greenBg.getId())){
                    settings.setBackgroundColor(getResources().getColor(R.color.green));
                    preferences.edit()
                            .putString("color", "green")
                            .apply();
                }
                else if((checkColor==redBg.getId())){
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
                    Locale locale = new Locale("ja");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                    preferences.edit()
                            .putString("lang", "ja")
                            .apply();
                }
                else if(checkLang.equals("English")){
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

                    preferences.edit()
                            .putString("lang", "en")
                            .apply();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirm = new Intent(SetttingsActivity.this, MainActivity.class);
                startActivity(confirm);
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
