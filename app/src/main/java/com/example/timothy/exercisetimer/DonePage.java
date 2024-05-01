package com.example.timothy.exercisetimer;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
public class DonePage extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//We are here for 5 seconds, then returned to the main menu. That's all.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_page);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(DonePage.this, MainMenu.class);
                startActivity(intent);
            }
        };
        handler.postDelayed(runnable,5000);
    }
}
