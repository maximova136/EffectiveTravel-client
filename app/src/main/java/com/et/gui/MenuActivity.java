package com.et.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.et.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button petuhButton = (Button) findViewById(R.id.PetuhButton);
        petuhButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Hello pidor", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
//                startActivity(intent);
//                attemptLogin();
            }
        });
    }
}
