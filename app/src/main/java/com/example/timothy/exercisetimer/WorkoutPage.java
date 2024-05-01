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
    long timeleft = 0; //time left overall, in milliseconds
    long totaltime = 0; //holder for total time
    List<Integer> breakpoints = new ArrayList<>(); // Time left in centi-seconds
    List<Integer> metadata = new ArrayList<>(); //time on (0), time off (1), or rest (2)
    List<Integer[]> counter = new ArrayList<>(); //{side, rep, set}
    TextView sidelabel, replabel, setlabel, exercisename, timeonlabel, timeofflabel, restlabel, timerlabel, sidelabeltxt, replabeltxt, setlabeltxt;
    Button startbutton, pausebutton, exitbutton, resumebutton, skipbutton;
    MediaPlayer mp;
    String name;
    int timeon,timeoff,rest,sides,reps,sets;
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
        timeon = Integer.parseInt(selWorkout[1]);
        timeoff = Integer.parseInt(selWorkout[2]);
        rest = Integer.parseInt(selWorkout[3]);
        sides = Integer.parseInt(selWorkout[4]);
        reps = Integer.parseInt(selWorkout[5]);
        sets = Integer.parseInt(selWorkout[6]);
        timeleft = sets * ((timeon+timeoff)*(sides*reps)+rest) * 1000; //Track ms
        totaltime = timeleft; //this doesn't change, just used to bug-fix initial ticks
        sidelabel = findViewById(R.id.textView_sidenumber);
        replabel = findViewById(R.id.textView_repnumber);
        setlabel = findViewById(R.id.textView_setnumber);
        exercisename = findViewById(R.id.textView_exercisename);
        timeonlabel = findViewById(R.id.textView_timeon);
        timeofflabel = findViewById(R.id.textView_timeoff);
        restlabel = findViewById(R.id.textView_timerrest);
        startbutton = findViewById(R.id.button_start);
        pausebutton = findViewById(R.id.button_pause);
        exitbutton = findViewById(R.id.button_exitworkout);
        resumebutton = findViewById(R.id.button_resume);
        skipbutton = findViewById(R.id.button_skip);
        timerlabel = findViewById(R.id.textView_timer);
        sidelabeltxt = findViewById(R.id.textView_side);
        replabeltxt = findViewById(R.id.textView_rep);
        setlabeltxt = findViewById(R.id.textView_set);
        setCountdown(timeon, timeoff, rest, sides, reps, sets); //Initialize the countdown breakpoints.
        exercisename.setText(name);
///////////// Start button ////////////////////////////////////////////////////////
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startbutton.setVisibility(View.INVISIBLE);
                exitbutton.setVisibility(View.INVISIBLE);
                pausebutton.setVisibility(View.VISIBLE);
                resumebutton.setVisibility(View.INVISIBLE);
                skipbutton.setVisibility(View.VISIBLE);
                startWorkout();
            }
        });
///////////////////////////////////////////////////////////////////////////////////
//////////// Pause button ////////////////////////////////////////////////////////
        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startbutton.setVisibility(View.INVISIBLE);
                exitbutton.setVisibility(View.VISIBLE);
                pausebutton.setVisibility(View.INVISIBLE);
                resumebutton.setVisibility(View.VISIBLE);
                skipbutton.setVisibility(View.INVISIBLE);
                pauseWorkout();
            }
        });
////////////////////////////////////////////////////////////////////////////////////
/////////// Resume button //////////////////////////////////////////////////////////
        resumebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startbutton.setVisibility(View.INVISIBLE);
                exitbutton.setVisibility(View.INVISIBLE);
                pausebutton.setVisibility(View.VISIBLE);
                resumebutton.setVisibility(View.INVISIBLE);
                skipbutton.setVisibility(View.VISIBLE);
                resumeWorkout();
            }
        });
////////////////////////////////////////////////////////////////////////////////////
////////// Exit button /////////////////////////////////////////////////////////////
        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutPage.this, ExercisesPage.class);
                startActivity(intent);
            }
        });
////////////////////////////////////////////////////////////////////////////////////
////////// Skip button /////////////////////////////////////////////////////////////
        skipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdt.cancel();
                timeleft = breakpoints.get(pos+1);
                changeTimer();
                startWorkout();

            }
        });
////////////////////////////////////////////////////////////////////////////////////
    }

    public void startWorkout(){//Start the workout.
        sidelabeltxt.setVisibility(View.VISIBLE);
        replabeltxt.setVisibility(View.VISIBLE);
        setlabeltxt.setVisibility(View.VISIBLE);
        setLabels(counter.get(pos)[0], sides, counter.get(pos)[1], reps, counter.get(pos)[2], sets);
        changeTimer();
        cdt = new CountDownTimer(timeleft, 1) {
            @Override
            public void onTick(long l) {
                timeleft = l;
                if (l < breakpoints.get(pos+1)) {
                    pos++;
                    setLabels(counter.get(pos)[0], sides, counter.get(pos)[1], reps, counter.get(pos)[2], sets);
                    changeTimer();
                    if(metadata.get(pos)==2) play("long");
                    else play("short");
                }
                int displaySecs = (((int)l - breakpoints.get(pos+1))/1000)+1;
                timerlabel.setText(String.valueOf(displaySecs));
            }
            @Override
            public void onFinish() {
                play("done");
                stopIt();
            }
        }.start();
    }
    public void setLabels(int ctside, int sidemax, int ctrep, int repmax, int ctset, int setmax) {//One line to increment the side, rep, set labels.
        sidelabel.setText(String.format("%s / %s", ctside, sidemax));
        replabel.setText(String.format("%s / %s", ctrep, repmax));
        setlabel.setText(String.format("%s / %s", ctset, setmax));
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
    public void pauseWorkout(){//Pause workout, aka cancel the timer. timeleft is saved, so you can pick it back up if you need to.
        cdt.cancel();
    }
    public void resumeWorkout(){//Because time and index-cursor, are saved, just call startWorkout() again to continue.
        startWorkout();
    }
    public void setCountdown(int timeon, int timeoff, int rest, int sides, int reps, int sets){//Code to initialize the arrays and countdown breakpoints
        long timer = timeleft;
        int i,j,k=0;
        for (i=1; i<=sets; i++){
            for (j=1; j<=reps; j++){
                for (k=1; k<=sides; k++){
                    if (timeon>0) {
                        breakpoints.add((int) timer);
                        metadata.add(0);
                        counter.add(new Integer[]{k, j, i});
                        timer -= timeon * 1000;
                    }
                    if (timeoff > 0) {
                        breakpoints.add((int) timer);
                        metadata.add(1);
                        counter.add(new Integer[]{k, j, i});
                        timer -= timeoff * 1000;
                    }
                }
            }
            if (rest > 0){
                breakpoints.add((int) timer);
                metadata.add(2);
                counter.add(new Integer[]{k-1, j-1, i});
                timer -= rest * 1000;
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
                timeonlabel.setVisibility(View.VISIBLE);
                timeofflabel.setVisibility(View.INVISIBLE);
                restlabel.setVisibility(View.INVISIBLE);
                timerlabel.setTextColor(Color.parseColor("#ff669900"));
                break;
            case 1:
                timeonlabel.setVisibility(View.INVISIBLE);
                timeofflabel.setVisibility(View.VISIBLE);
                restlabel.setVisibility(View.INVISIBLE);
                timerlabel.setTextColor(Color.parseColor("#ffcc0000"));
                break;
            case 2:
                timeonlabel.setVisibility(View.INVISIBLE);
                timeofflabel.setVisibility(View.INVISIBLE);
                restlabel.setVisibility(View.VISIBLE);
                timerlabel.setTextColor(Color.parseColor("#ff0099cc"));
                break;
        }
    }
    public void stopIt(){//Safe stop for the timer (only called at 0) and move to the DonePage.
        cdt.cancel();
        Intent intent = new Intent(WorkoutPage.this, DonePage.class);
        startActivity(intent);
    }




}