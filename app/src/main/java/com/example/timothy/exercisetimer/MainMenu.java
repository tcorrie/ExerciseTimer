package com.example.timothy.exercisetimer;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
// Simply a button and some text (defined in XML file) on the main page
        Button button_exercises = (Button) findViewById(R.id.button_exercises);
        button_exercises.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ExercisesPage.class);
                startActivity(intent);
            }
        });
    }
}
