package com.example.timothy.exercisetimer;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;
public class WorkoutPage extends AppCompatActivity {
    int pos = 0; //position of the arrays
    long time_left = 0; //time left overall, in milliseconds
    long total_time = 0; //holder for total time
    List<Integer> breakpoints = new ArrayList<>(); // Time left in centi-seconds
    List<Integer> metadata = new ArrayList<>(); //time on (0), time off (1), or rest (2)
    List<Integer[]> counter = new ArrayList<>(); //{side, rep, set}
    TextView side_label, rep_label, set_label, exercise_name, time_on_label, time_off_label, rest_label, timer_label, side_label_txt, rep_label_txt, set_label_txt;
    Button startbutton, pausebutton, exitbutton, resumebutton, skipbutton;
    MediaPlayer mp;
    String name;
    int time_on,time_off,rest,sides,reps,sets;
    CountDownTimer cdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_page);
        String[] selWorkout = {};
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            selWorkout = extras.getStringArray("sendvar"); //Bring in selected exercise from previous screen
        }
        assert selWorkout != null;
        name = selWorkout[0];
        time_on = Integer.parseInt(selWorkout[1]);
        time_off = Integer.parseInt(selWorkout[2]);
        rest = Integer.parseInt(selWorkout[3]);
        sides = Integer.parseInt(selWorkout[4]);
        reps = Integer.parseInt(selWorkout[5]);
        sets = Integer.parseInt(selWorkout[6]);
        time_left = sets * ((time_on+time_off)*((long) sides *reps)+rest) * 1000; //Track ms
        total_time = time_left; //this doesn't change, just used to bug-fix initial ticks
        side_label = findViewById(R.id.textView_sideNumber);
        rep_label = findViewById(R.id.textView_repNumber);
        set_label = findViewById(R.id.textView_setNumber);
        exercise_name = findViewById(R.id.textView_exerciseName);
        time_on_label = findViewById(R.id.textView_timeon);
        time_off_label = findViewById(R.id.textView_timeOff);
        rest_label = findViewById(R.id.textView_timerRest);
        startbutton = findViewById(R.id.button_start);
        pausebutton = findViewById(R.id.button_pause);
        exitbutton = findViewById(R.id.button_exitWorkout);
        resumebutton = findViewById(R.id.button_resume);
        skipbutton = findViewById(R.id.button_skip);
        timer_label = findViewById(R.id.textView_timer);
        side_label_txt = findViewById(R.id.textView_side);
        rep_label_txt = findViewById(R.id.textView_rep);
        set_label_txt = findViewById(R.id.textView_set);
        setCountdown(time_on, time_off, rest, sides, reps, sets); //Initialize the countdown breakpoints.
        exercise_name.setText(name);
///////////// Start button ////////////////////////////////////////////////////////
        startbutton.setOnClickListener(view -> {
            startbutton.setVisibility(View.INVISIBLE);
            exitbutton.setVisibility(View.INVISIBLE);
            pausebutton.setVisibility(View.VISIBLE);
            resumebutton.setVisibility(View.INVISIBLE);
            skipbutton.setVisibility(View.VISIBLE);
            startWorkout();
        });
///////////////////////////////////////////////////////////////////////////////////
//////////// Pause button ////////////////////////////////////////////////////////
        pausebutton.setOnClickListener(view -> {
            startbutton.setVisibility(View.INVISIBLE);
            exitbutton.setVisibility(View.VISIBLE);
            pausebutton.setVisibility(View.INVISIBLE);
            resumebutton.setVisibility(View.VISIBLE);
            skipbutton.setVisibility(View.INVISIBLE);
            pauseWorkout();
        });
////////////////////////////////////////////////////////////////////////////////////
/////////// Resume button //////////////////////////////////////////////////////////
        resumebutton.setOnClickListener(view -> {
            startbutton.setVisibility(View.INVISIBLE);
            exitbutton.setVisibility(View.INVISIBLE);
            pausebutton.setVisibility(View.VISIBLE);
            resumebutton.setVisibility(View.INVISIBLE);
            skipbutton.setVisibility(View.VISIBLE);
            resumeWorkout();
        });
////////////////////////////////////////////////////////////////////////////////////
////////// Exit button /////////////////////////////////////////////////////////////
        exitbutton.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutPage.this, ExercisesPage.class);
            startActivity(intent);
        });
////////////////////////////////////////////////////////////////////////////////////
////////// Skip button /////////////////////////////////////////////////////////////
        skipbutton.setOnClickListener(view -> {
            cdt.cancel();
            time_left = breakpoints.get(pos+1);
            changeTimer();
            startWorkout();

        });
////////////////////////////////////////////////////////////////////////////////////
    }

    public void startWorkout(){//Start the workout.
        side_label_txt.setVisibility(View.VISIBLE);
        rep_label_txt.setVisibility(View.VISIBLE);
        set_label_txt.setVisibility(View.VISIBLE);
        setLabels(counter.get(pos)[0], sides, counter.get(pos)[1], reps, counter.get(pos)[2], sets);
        changeTimer();
        cdt = new CountDownTimer(time_left, 1) {
            @Override
            public void onTick(long l) {
                time_left = l;
                if (l < breakpoints.get(pos+1)) {
                    pos++;
                    setLabels(counter.get(pos)[0], sides, counter.get(pos)[1], reps, counter.get(pos)[2], sets);
                    changeTimer();
                    if(metadata.get(pos)==2) play("long");
                    else play("short");
                }
                int displaySecs = (((int)l - breakpoints.get(pos+1))/1000)+1;
                timer_label.setText(String.valueOf(displaySecs));
            }
            @Override
            public void onFinish() {
                play("done");
                stopIt();
            }
        }.start();
    }
    public void setLabels(int ct_side, int side_max, int ct_rep, int rep_max, int ct_set, int set_max) {//One line to increment the side, rep, set labels.
        side_label.setText(String.format("%s / %s", ct_side, side_max));
        rep_label.setText(String.format("%s / %s", ct_rep, rep_max));
        set_label.setText(String.format("%s / %s", ct_set, set_max));
    }
    public void play(String s){
        switch (s) {
            case "short":
                mp = MediaPlayer.create(this, R.raw.beep_short);
                break;
            case "long":
                mp = MediaPlayer.create(this, R.raw.beep_long);
                break;
            case "done":
                mp = MediaPlayer.create(this, R.raw.beep_done);
                break;
            default:
                break;
        }
        mp.start();
    }
    public void pauseWorkout(){//Pause workout, aka cancel the timer. time_left is saved, so you can pick it back up if you need to.
        cdt.cancel();
    }
    public void resumeWorkout(){//Because time and index-cursor, are saved, just call startWorkout() again to continue.
        startWorkout();
    }
    public void setCountdown(int time_on, int time_off, int rest, int sides, int reps, int sets){//Code to initialize the arrays and countdown breakpoints
        long timer = time_left;
        int i,j,k=0;
        for (i=1; i<=sets; i++){
            for (j=1; j<=reps; j++){
                for (k=1; k<=sides; k++){
                    if (time_on>0) {
                        breakpoints.add((int) timer);
                        metadata.add(0);
                        counter.add(new Integer[]{k, j, i});
                        timer -= time_on * 1000L;
                    }
                    if (time_off > 0) {
                        breakpoints.add((int) timer);
                        metadata.add(1);
                        counter.add(new Integer[]{k, j, i});
                        timer -= time_off * 1000L;
                    }
                }
            }
            if (rest > 0){
                breakpoints.add((int) timer);
                metadata.add(2);
                counter.add(new Integer[]{k-1, j-1, i});
                timer -= rest * 1000L;
            }
        }
        breakpoints.add(0);
        metadata.add(-1);
        counter.add(new Integer[]{0,0,0}); //Never reached, but necessary to avoid overflow.
    }
    public void changeTimer(){//Handles which timer (on/off/rest) shows and sets timer color accordingly.
        int mode = metadata.get(pos);
        switch (mode){
            case 0:
                time_on_label.setVisibility(View.VISIBLE);
                time_off_label.setVisibility(View.INVISIBLE);
                rest_label.setVisibility(View.INVISIBLE);
                timer_label.setTextColor(Color.parseColor("#ff669900"));
                break;
            case 1:
                time_on_label.setVisibility(View.INVISIBLE);
                time_off_label.setVisibility(View.VISIBLE);
                rest_label.setVisibility(View.INVISIBLE);
                timer_label.setTextColor(Color.parseColor("#ffcc0000"));
                break;
            case 2:
                time_on_label.setVisibility(View.INVISIBLE);
                time_off_label.setVisibility(View.INVISIBLE);
                rest_label.setVisibility(View.VISIBLE);
                timer_label.setTextColor(Color.parseColor("#ff0099cc"));
                break;
        }
    }
    public void stopIt(){//Safe stop for the timer (only called at 0) and move to the DonePage.
        cdt.cancel();
        Intent intent = new Intent(WorkoutPage.this, DonePage.class);
        startActivity(intent);
    }




}