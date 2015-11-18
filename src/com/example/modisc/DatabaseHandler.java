package com.example.modisc;

import java.util.ArrayList;
import java.util.List;
 




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "Scrum";
 
    // Markers table name
    private static final String TABLE_DEVELOPERS = "developers";
    
    // Markers Table Columns names
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_GROUP = "groupid";
    private static final String KEY_GOALS = "goal";
    private static final String KEY_TODAYS_GOAL = "todaysgoal";
    private static final String KEY_OBSTACLE = "obstacle";
    
    private final String CREATE_DEVELOPERS_TABLE = "CREATE TABLE " + TABLE_DEVELOPERS + "("
    		+ KEY_EMAIL + " TEXT PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_GROUP + " INTEGER,"
    		+ KEY_GOALS + " TEXT, " + KEY_TODAYS_GOAL + " TEXT,"
    		+ KEY_OBSTACLE + " TEXT)";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Does not exist, create one
        db.execSQL(CREATE_DEVELOPERS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVELOPERS);
        
        // Create tables again
        onCreate(db);
    }
    
    // Adding new developer
    void addDeveloper(DeveloperObject developer) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, developer.getEmail());
        values.put(KEY_NAME, developer.getName());
        values.put(KEY_GROUP, developer.getGroup());
        values.put(KEY_GOALS, developer.getGoal());
        values.put(KEY_TODAYS_GOAL, developer.getTodaysGoal());
        values.put(KEY_OBSTACLE, developer.getObstacle());
        
        // Inserting Row
        db.insert(TABLE_DEVELOPERS, null, values);
        db.close(); // Closing database connection
    }
    
    // Adding new developer
    void updateDeveloper(String email, String goals, String todaysGoal, String obstacle) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_GOALS, goals);
        values.put(KEY_TODAYS_GOAL, todaysGoal);
        values.put(KEY_OBSTACLE, obstacle);
        
        // Inserting Row
        db.update(TABLE_DEVELOPERS, values, KEY_EMAIL + "='" + email + "'", null);
        db.close(); // Closing database connection
    }
     
    // Getting developer
    public DeveloperObject getDeveloper(String email) {
    	DeveloperObject developer = null;
    	
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DEVELOPERS + " WHERE " + KEY_EMAIL + "='" + email +"'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try{
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	developer = new DeveloperObject(cursor.getString(0),cursor.getString(1),
	            			cursor.getInt(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
	                
	            } while (cursor.moveToNext());
	        }
        }finally{
    		//Close connection
    		cursor.close();
            db.close();
        }
        
        // return developer
        return developer;
    }
    
    // Getting All developers
    public List<DeveloperObject> getAllDevelopers(int group) {
        List<DeveloperObject> developerList = new ArrayList<DeveloperObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DEVELOPERS + " WHERE " + KEY_GROUP + "=" + String.valueOf(group);
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try{
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	DeveloperObject developer = new DeveloperObject(cursor.getString(0),cursor.getString(1),
	            			cursor.getInt(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
	                
	                // Adding developer to list
	            	developerList.add(developer);
	            } while (cursor.moveToNext());
	        }
        }finally{
    		//Close connection
    		cursor.close();
            db.close();
        }
        
        // return developer list
        return developerList;
    }
    
    // Getting developer Count
    public int getDeveloperCount(int group) {
        String countQuery = "SELECT * FROM " + TABLE_DEVELOPERS + " WHERE " + KEY_GROUP + "=" + String.valueOf(group);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        
        try{
        	count = cursor.getCount();
	    }finally{
	    	//Close connection
    		cursor.close();
    		db.close();
	    }
        
        // return count
        return count;
    }
    
    //Delete all entries from Table
    public void resetDevelopersTable(){
    	// Delete all entries from table
    	SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DEVELOPERS);
        db.close();
    }
    
}
