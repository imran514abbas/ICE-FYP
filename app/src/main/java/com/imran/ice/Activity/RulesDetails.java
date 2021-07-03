package com.imran.ice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.imran.ice.R;

public class RulesDetails extends AppCompatActivity {
    String name = "";
    TextView Rules, RulesName;
    ImageView imageView;
    Button ShareRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_details);
        Rules = findViewById(R.id.rulesdetails);
        RulesName = findViewById(R.id.rulesname);
        imageView = findViewById(R.id.imageview);
        ShareRule = findViewById(R.id.sharerules);
        name = getIntent().getStringExtra("name");
        if (name.equals("Covid")) {
            RulesName.setText("Covid 19");
            Rules.setText(R.string.COVID);
            imageView.setImageResource(R.drawable.covid);
            ShareRule();

        } else if (name.equals("Traffic")) {
            RulesName.setText("Traffic Rules");

            Rules.setText(R.string.Traffic);
            imageView.setImageResource(R.drawable.traffic);
            ShareRule();
        } else if (name.equals("Fire")) {
            RulesName.setText("Fire Control");

            Rules.setText(R.string.Fire);
            imageView.setImageResource(R.drawable.fire);
            ShareRule();
        } else if (name.equals("Safity")) {
            RulesName.setText("Safity Control");

            Rules.setText(R.string.Safity);
            imageView.setImageResource(R.drawable.lock);
            ShareRule();
        } else if (name.equals("Authurization")) {
            RulesName.setText("Authurization");

            Rules.setText(R.string.Authurization);
            imageView.setImageResource(R.drawable.authorisation);
            ShareRule();
        } else if (name.equals("WorkHight")) {
            RulesName.setText("Work at Hight");

            Rules.setText(R.string.Hight);
            imageView.setImageResource(R.drawable.height);
            ShareRule();
        } else if (name.equals("Earthquake")) {
            RulesName.setText("Earthquake");
            Rules.setText(R.string.Earthquake);
            imageView.setImageResource(R.drawable.earthquakes);
            ShareRule();
        } else if (name.equals("CommonMistake")) {
            RulesName.setText("Common Mistakes");

            Rules.setText(R.string.CommonMistake);
            imageView.setImageResource(R.drawable.comman);
            ShareRule();
        }
    }

    public void ShareRule() {
        ShareRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sharetxt = Rules.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, sharetxt);
                startActivity(sendIntent);

            }
        });

    }
}
