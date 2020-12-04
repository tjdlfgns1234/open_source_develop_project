package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.vipulasri.timelineview.R;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton1Clicked(View v){
        Toast.makeText(this,"집 위치 정보가 입력되었습니다.",Toast.LENGTH_LONG).show();
    }
}