package com.example.timothy.exercisetimer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
// This class deals with the SQLite database functions
public class DataBaseHelper extends SQLiteOpenHelper {//initialization
    public static final String EXERCISE_LIST = "EXERCISE_LIST";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_TIMEON = "TIMEON";
    public static final String COLUMN_TIMEOFF = "TIMEOFF";
    public static final String COLUMN_REST = "REST";
    public static final String COLUMN_SIDES = "SIDES";
    public static final String COLUMN_REPS = "REPS";
    public static final String COLUMN_SETS = "SETS";
    public DataBaseHelper(@Nullable Context context) {
        super(context, "exercises.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {//Creates new table
        String createTableStatement= "CREATE TABLE " + EXERCISE_LIST + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_TIMEON + " INT, " + COLUMN_TIMEOFF + " INT, " + COLUMN_REST + " INT, " + COLUMN_SIDES + " INT, " + COLUMN_REPS + " INT, " + COLUMN_SETS + " INT)";

        db.execSQL(createTableStatement);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//Upgrades new table
        db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_LIST);
        onCreate(db);
    }
    public boolean addOne(ExerciseList exerciseList) {//Adds an exercise to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, exerciseList.getName());
        cv.put(COLUMN_TIMEON, exerciseList.getTimeon());
        cv.put(COLUMN_TIMEOFF, exerciseList.getTimeoff());
        cv.put(COLUMN_REST, exerciseList.getRest());
        cv.put(COLUMN_SIDES, exerciseList.getSides());
        cv.put(COLUMN_REPS, exerciseList.getReps());
        cv.put(COLUMN_SETS, exerciseList.getSets());
        long insert = db.insert(EXERCISE_LIST,null,cv);
        return insert != -1;
    }
    public boolean deleteOne(String s) {//Deletes an exercise from the database
        SQLiteDatabase db = this.getWritableDatabase();
        if (!s.equals("Select Exercise")) {//Select Exercise is a default option. Wasn't sure how to add it via code so it was done manually.
            db.execSQL("DELETE FROM " + EXERCISE_LIST + " WHERE " + COLUMN_NAME + "= '" + s + "'");
            db.close();
            return true;
        }
        return false;
    }
    public String[] getItem(String s) {//Returns an exercise from the list
        String queryString = "SELECT * FROM " + EXERCISE_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            //loop through cursor
            do{
                if(cursor.getString(1).equals(s)){
                    String n = cursor.getString(1);
                    String t_on = cursor.getString(2);
                    String t_off = cursor.getString(3);
                    String rst = cursor.getString(4);
                    String sds = cursor.getString(5);
                    String rps = cursor.getString(6);
                    String sts = cursor.getString(7);
                    cursor.close();
                    db.close();
                    return new String[]{n,t_on,t_off,rst,sds,rps,sts};
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return new String[]{};
    }
    public List<String> getAllNames() {//For spinner initialization, get all the names.
        List<String> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + EXERCISE_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            //loop through cursor
            do{
                String exercise_name = cursor.getString(1);
                returnList.add(exercise_name);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }
    public boolean updateEntry(String[] update){//Updates an entry with the text fields
        String s = update[0];
        String t_on = update[1];
        String t_off = update[2];
        String rst = update[3];
        String sds = update[4];
        String rps = update[5];
        String sts = update[6];
        if (!s.equals("Select Exercise")) {//Default check
            String queryString = "SELECT * FROM " + EXERCISE_LIST;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(queryString, null);
            if (cursor.moveToFirst()) {
                //loop through cursor
                do {
                    String exercise_name = cursor.getString(1);
                    if (cursor.getString(1).equals(s)) {
                        ContentValues cv = new ContentValues();
                        cv.put("ID", cursor.getString(0));
                        cv.put(COLUMN_NAME, s);
                        cv.put(COLUMN_TIMEON, Integer.parseInt(t_on));
                        cv.put(COLUMN_TIMEOFF, Integer.parseInt(t_off));
                        cv.put(COLUMN_REST, Integer.parseInt(rst));
                        cv.put(COLUMN_SIDES, Integer.parseInt(sds));
                        cv.put(COLUMN_REPS, Integer.parseInt(rps));
                        cv.put(COLUMN_SETS, Integer.parseInt(sts));
                        db.update(EXERCISE_LIST, cv, COLUMN_NAME + " = ?", new String[]{exercise_name});
                        cursor.close();
                        db.close();
                        return true;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return false;
        }
        return false;
    }
}

