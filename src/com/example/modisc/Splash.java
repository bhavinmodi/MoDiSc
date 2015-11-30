package com.example.modisc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Splash extends Activity {

	String openClass;
	
	/***
	 * Method: onCreate()
	 * Type: @Override
	 * @param Bundle
	 * Objective: Called When an Activity is Started
	 * Conditions: None
	 * Working: This is a Splash Screen that opens when the application is started.
	 * 			The splash displays an image stored in resources and after waiting for
	 * 			2 seconds we start the actual application by calling SettingsStart.
	 * 			In these 2 seconds or more (if needed) we can perform any startup tasks
	 * 			behind the scenes.
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Setting the Splash Screen background
		setContentView(R.layout.activity_splash);
		
		//Starting a thread that sleeps for 2 seconds and then calls the
		//SettingsStart Activity.
		Thread timer = new Thread() {
			@Override
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Class<?> ourClass;
					try {
						if(openClass != null){
							ourClass = Class.forName(openClass);
			                
							Intent ourIntent = new Intent(Splash.this, ourClass);
							
							if(openClass.contentEquals("com.example.modisc.StartWizard")){
								Bundle bundle = new Bundle();
				                bundle.putString("Caller", "Splash");
				                //Add the bundle into myIntent for referencing variables
				                ourIntent.putExtras(bundle);
				                
							}
							startActivity(ourIntent);
							
							finish();
						}
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			
			}
		};
		
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
        boolean first_time = spref.getBoolean(new Keys().KEY_FIRST_FLAG, true);
        
        if(first_time){
        	openClass = "com.example.modisc.StartWizard";
        	
        	String goal1 = "Create Distributed Scrum Application";
        	String goal2 = "Create SIS Server Application";
        	
        	//Initialize DataBase Entries
        	DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        	databaseHandler.addDeveloper(new DeveloperObject("jose@gmail.com", "Jose Michael",
        			1, goal1,"Working on Scrum Master","Time = 3 Days"));
        	databaseHandler.addDeveloper(new DeveloperObject("vivek@gmail.com", "Vivek Punjabi",
        			1, goal1, "Working on Sprint Planner", "Time = 4 Days"));
        	databaseHandler.addDeveloper(new DeveloperObject("jessie@gmail.com", "Jessie Davis",
        			1, goal1, "Working on Scrum Management", "Time = 3 Days"));
        	databaseHandler.addDeveloper(new DeveloperObject("steve@gmail.com", "Steve Faurie",
        			2, goal2, "", ""));
        	databaseHandler.addDeveloper(new DeveloperObject("Sam@gmail.com", "Sam Panati",
        			2, goal2, "", ""));
        	
        }else{
        	openClass = "com.example.modisc.DeveloperScreen";
        }

		timer.start();
	}
	
}