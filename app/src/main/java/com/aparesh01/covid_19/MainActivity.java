package com.aparesh01.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//    Button btn1 = findViewById(R.id.btn1);
//    Button btn2 = findViewById(R.id.btn2);

    public void signup(View view){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    public void onlogIn(View view) {
        startActivity(new Intent(MainActivity.this,LogInActivity.class));
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
