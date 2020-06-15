package com.masb1ue.app.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
        TextView _dataTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        _dataTV = findViewById(R.id.dataTV);
        Intent data = getIntent();
        if (data != null)
            _dataTV.setText(data.getStringExtra("data"));

    }
}
