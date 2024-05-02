package com.example.timothy.exercisetimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
// Simply a button and some text (defined in XML file) on the main page
        Button button_exercises = findViewById(R.id.button_exercises);
        button_exercises.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, ExercisesPage.class);
            startActivity(intent);
        });
    }
}
