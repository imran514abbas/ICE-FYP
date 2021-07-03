package com.imran.ice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.imran.ice.R;

public class LifeSavingRules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_saving_rules);
    }

    public void Covid(View view) {
        Intent intent = new Intent(getApplicationContext(), RulesDetails.class);
        intent.putExtra("name", "Covid");
        startActivity(intent);
    }

    public void Traffic(View view) {
        Intent intent = new Intent(getApplicationContext(), RulesDetails.class);
        intent.putExtra("name", "Traffic");
        startActivity(intent);
    }

    public void Fire(View view) {
        Intent intent = new Intent(getApplicationContext(), RulesDetails.class);
        intent.putExtra("name", "Fire");
        startActivity(intent);
    }

    public void Safity(View view) {
        Intent intent = new Intent(getApplicationContext(), RulesDetails.class);
        intent.putExtra("name", "Safity");
        startActivity(intent);
    }


    public void Authorization(View view) {
        Intent intent = new Intent(getApplicationContext(), RulesDetails.class);
        intent.putExtra("name", "Authurization");
        startActivity(intent);
    }


    public void WorkonHight(View view) {
        Intent intent = new Intent(getApplicationContext(), RulesDetails.class);
        intent.putExtra("name", "WorkHight");
        startActivity(intent);
    }

    public void Earthquake(View view) {
        Intent intent = new Intent(getApplicationContext(), RulesDetails.class);
        intent.putExtra("name", "Earthquake");
        startActivity(intent);
    }

    public void CommonMistake(View view) {
        Intent intent = new Intent(getApplicationContext(), RulesDetails.class);
        intent.putExtra("name", "CommonMistake");
        startActivity(intent);
    }

}
