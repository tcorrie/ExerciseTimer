package com.example.timothy.exercisetimer;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
public class ExercisesPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText namefield,timeon,timeoff,rest,side,reps,sets;
    Button buttonBack, buttonAdd, buttonPerform, buttonDelete, buttonUpdate;
    DataBaseHelper dataBaseHelper;
    Spinner exerciseListNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_page);
        // Get all the variables needed to create the exercise
        namefield = findViewById(R.id.editabletext_namefield);
        timeon = findViewById(R.id.editablenumber_timeon);
        timeoff = findViewById(R.id.editablenumber_timeoff);
        rest = findViewById(R.id.editablenumber_rest);
        side = findViewById(R.id.editablenumber_side);
        reps = findViewById(R.id.editablenumber_reps);
        sets = findViewById(R.id.editablenumber_sets);
        dataBaseHelper = new DataBaseHelper(ExercisesPage.this);
        exerciseListNames = findViewById(R.id.spinner_exercisenames);
        exerciseListNames.setOnItemSelectedListener(this);
        List<String> namesList = dataBaseHelper.getAllNames();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,namesList){
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.comingsoon);
                ((TextView) v).setTypeface(typeface);
                return v;
            }
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.comingsoon);
                ((TextView) v).setTypeface(typeface);
                return v;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseListNames.setAdapter(arrayAdapter);
        exerciseListNames.setSelection(0);
////////////////////////////// Back button
        buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ExercisesPage.this, MainMenu.class);
                startActivity(intent);
            }
        });
///////////////////////// Add button
        buttonAdd = findViewById(R.id.button_addexercise);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (namefield.getText().toString().trim().length()>0) {
                    ExerciseList exerciseList;
                    try {
                        exerciseList = new ExerciseList(namefield.getText().toString(),
                                Integer.parseInt(timeon.getText().toString()),
                                Integer.parseInt(timeoff.getText().toString()),
                                Integer.parseInt(rest.getText().toString()),
                                Integer.parseInt(side.getText().toString()),
                                Integer.parseInt(reps.getText().toString()),
                                Integer.parseInt(sets.getText().toString()));
                        String[] addEx = {namefield.getText().toString(), timeon.getText().toString(),
                                timeoff.getText().toString(), rest.getText().toString(),
                                side.getText().toString(), reps.getText().toString(), sets.getText().toString()};
                        if (qualityCheck(addEx)) {
                            boolean success = dataBaseHelper.addOne(exerciseList);
                            if (success) Toast.makeText(ExercisesPage.this, "Exercise added successfully.", Toast.LENGTH_SHORT).show();
                            arrayAdapter.clear();
                            arrayAdapter.addAll(dataBaseHelper.getAllNames());
                        }
                    } catch (Exception e) {
                        Toast.makeText(ExercisesPage.this, "One or more of the fields are blank. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ExercisesPage.this, "One or more of the fields are blank. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
////////////////////////// Perform button
        buttonPerform = findViewById(R.id.button_perform);
        buttonPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exerciseListNames.getSelectedItem().toString().equals("Select Exercise")) {
                    String a = namefield.getText().toString();
                    String b = timeon.getText().toString();
                    String c = timeoff.getText().toString();
                    String d = rest.getText().toString();
                    String e = side.getText().toString();
                    String f = reps.getText().toString();
                    String g = sets.getText().toString();
                    String[] selectedWorkout = {a,b,c,d,e,f,g};
                    Intent intent = new Intent(ExercisesPage.this, WorkoutPage.class);
                    intent.putExtra("sendvar", selectedWorkout);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ExercisesPage.this, "Please select an exercise to perform.", Toast.LENGTH_SHORT).show();
                }
            }
        });
////////////////////////// Update button
        buttonUpdate = findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Not good coding practice, but we just need placeholder variables to pass through the function.
                try {
                    String a = namefield.getText().toString();
                    String b = timeon.getText().toString();
                    String c = timeoff.getText().toString();
                    String d = rest.getText().toString();
                    String e = side.getText().toString();
                    String f = reps.getText().toString();
                    String g = sets.getText().toString();
                    if (qualityCheck(new String[]{a, b, c, d, e, f, g})) {
                        boolean success = dataBaseHelper.updateEntry(new String[]{a, b, c, d, e, f, g});
                        if (success) {
                            Toast.makeText(ExercisesPage.this, "Entry has been modified.", Toast.LENGTH_SHORT).show();
                        } else if (a.equals("Select Exercise")) {
                            Toast.makeText(ExercisesPage.this, "Please select an exercise to modify.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ExercisesPage.this, "Could not find entry.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ExercisesPage.this, "Exercise cannot be updated with blank fields. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
/////////////////////////// Delete button
        buttonDelete = findViewById(R.id.button_deleteexercise);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = dataBaseHelper.deleteOne(exerciseListNames.getSelectedItem().toString());
                clearTextFields();
                arrayAdapter.clear();
                arrayAdapter.addAll(dataBaseHelper.getAllNames());
                exerciseListNames.setSelection(0);
                if (success) {
                    Toast.makeText(ExercisesPage.this, "Entry has been deleted.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ExercisesPage.this, "Please select an entry to delete.", Toast.LENGTH_SHORT).show();
                }
            }
        });
///////////////////////////
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {//Change editable fields to selected spinner item.
        String[] ename = dataBaseHelper.getItem(exerciseListNames.getSelectedItem().toString());
        if (!ename[0].equals("Select Exercise")) {
            namefield.setText(ename[0]);
            timeon.setText(ename[1]);
            timeoff.setText(ename[2]);
            rest.setText(ename[3]);
            side.setText(ename[4]);
            reps.setText(ename[5]);
            sets.setText(ename[6]);
        }
        else{
            namefield.setText("");
            timeon.setText("");
            timeoff.setText("");
            rest.setText("");
            side.setText("");
            reps.setText("");
            sets.setText("");
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}//Not used b/c we have default to keep the app from breaking.
    public void clearTextFields() {//Clears all editable fields
        namefield.setText("");
        timeon.setText("");
        timeoff.setText("");
        rest.setText("");
        side.setText("");
        reps.setText("");
        sets.setText("");
    }
    public boolean qualityCheck(String[] exerciseData){//We don't want all times to be 0, or any rep/side/set to be 0.
        //String a = exerciseData[0]; // name: not used in quality check
        int b = Integer.parseInt(exerciseData[1]); //time on
        int c = Integer.parseInt(exerciseData[2]); //time off
        int d = Integer.parseInt(exerciseData[3]); //rest
        int e = Integer.parseInt(exerciseData[4]); // sides
        int f = Integer.parseInt(exerciseData[5]); // reps
        int g = Integer.parseInt(exerciseData[6]); // sets
        if(b==0 && c==0 && d==0){
            Toast.makeText(ExercisesPage.this, "Quality check failed: Cannot have all 0 times.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(e==0 || f==0 || g==0){
            Toast.makeText(ExercisesPage.this, "Quality check failed: Cannot have 0 sides, reps, or sets.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
}