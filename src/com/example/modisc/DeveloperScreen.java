package com.example.modisc;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost.OnTabChangeListener;

public class DeveloperScreen extends AppCompatActivity implements OnReceiveUpdates {
	
	//Tab parameters
	protected FragmentTabHost mTabHost;
	protected android.support.v4.app.FragmentManager fragmentManager;
	
	// Broadcast Receiver registered flag
	private final String updateBroadcast = "com.modisc.developerScreen.update"; 
	private Boolean broadcastRegistered = false;
	
	String name;
	int group;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_developerscreen);
		
		// Initialize Broadcast Receiver
		broadcastReceiverSetup();
		
		//Initialize UI Components
		initializeComponents();
       
		// Setup alarm to ask for updates after regular intervals
		updateAlarm();
		
		//Initialize Tabbed View
		tabView();
	}
	
	@Override
	protected void onResume() {
		// Ask server for updates
		serverUpdate();
		super.onResume();
	}

	private void broadcastReceiverSetup(){
		if(!broadcastRegistered){
			IntentFilter filter1 = new IntentFilter(updateBroadcast);
		    this.registerReceiver(broadcastReceiver, filter1);
			broadcastRegistered = true;
		}
	}
	
	// Broadcast Receiver
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		  
		@Override
		  public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
			    
			    switch(action){
			    	case updateBroadcast:
			    		serverUpdate();
			    		break;
			    }
		}
	};
			
	private void initializeComponents(){
		//Enabling Logo on the Action Bar
		Toolbar toolbar = (Toolbar) findViewById(R.id.TLToolBar);
		toolbar.setNavigationIcon(R.drawable.scrum_icon_small);
		toolbar.setTitle("Distributed Scrum");
		toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(toolbar);
        
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		group = spref.getInt(new Keys().KEY_GROUP, -1);
		name = spref.getString(new Keys().KEY_NAME, "Unknown");
    }
	
	private void serverUpdate(){
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String email = spref.getString(new Keys().KEY_EMAIL, "Unknown");
		int groupid = spref.getInt(new Keys().KEY_GROUP, -1);
		
		if(groupid != -1){
			if(spref.getString(new Keys().KEY_USER, "null").contains("master")){
				try {
					new AskUpdateFromServerMaster(getApplicationContext(), this).execute(new Helper().createJSON(email, groupid));
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }else{
            	if(spref.getString(new Keys().KEY_USER, "null").contains("developer")){
            		try {
        				new AskUpdateFromServer(getApplicationContext(), this).execute(new Helper().createJSON(email, groupid));
        			} catch (JSONException e) {
        				e.printStackTrace();
        			}
            	}
            }
		}
	}
	
	//Alarm that will update database every 30 mins
	private void updateAlarm(){
		
		Log.w("updateAlarm","Alarm Set");
		
		/* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent("UpdateAlarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1800000; //Number of milliseconds in 30 mins

        /* Set the alarm to repeat every 30 mins */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        		interval, pendingIntent);
	}
			
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		super.onOptionsItemSelected(item);
		 
        switch(item.getItemId()){
           case R.id.configure_menu:
        	   try {
					Class<?> ourClass = Class.forName("com.example.modisc.StartWizard");
					Intent ourIntent = new Intent(DeveloperScreen.this, ourClass);
					
					Bundle bundle = new Bundle();
	                bundle.putString("Caller", "Settings");
	                //Add the bundle into myIntent for referencing variables
	                ourIntent.putExtras(bundle);
	                
    				startActivity(ourIntent);
    				
    				finish();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
        }
        
        return true;
 
	}
	
	private void tabView(){
		mTabHost  = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		
		mTabHost.addTab(mTabHost.newTabSpec("tab1")
				.setIndicator("Group = " + String.valueOf(group) + ", Name = " + name, null), DeveloperTab.class, null);
		
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				fragmentManager = getSupportFragmentManager();
				DeveloperTab homeScreen = (DeveloperTab) fragmentManager.findFragmentByTag("tab1");
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

				if(tabId.equalsIgnoreCase("tab1")){
					if(homeScreen != null){
						fragmentTransaction.show(homeScreen);
					}
				}
				
				fragmentTransaction.commitAllowingStateLoss();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	        // do nothing
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onReceiveUpdates(JSONObject json) {
		// Check number of rows (entries)
		try {
			DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
			
			int numEntries = json.getInt("rows");
			
			int counter = 1;
			while(counter <= numEntries){
				String email = json.getString("email_".concat(String.valueOf(counter)));
				int groupid = json.getInt("groupid_".concat(String.valueOf(counter)));
				String name = json.getString("name_".concat(String.valueOf(counter)));
				String goals = json.getString("goals_".concat(String.valueOf(counter)));
				String todaysgoals = json.getString("todaysgoals_".concat(String.valueOf(counter)));
				String obstacles = json.getString("obstacles_".concat(String.valueOf(counter)));
				int status = json.getInt("status_".concat(String.valueOf(counter)));
				
				// Check if it already exists in the database
				if(handler.getDeveloper(email) != null){
					handler.updateDeveloper(email, goals, todaysgoals, obstacles, status);
				}else{
					// Not in database, create a new entry
					handler.addDeveloper(new DeveloperObject(email, name, groupid, goals, todaysgoals, obstacles, status));
				}
				
				counter++;
			}
			
			if(numEntries != 0){
				updateList();
				
				if(DeveloperTab.mSectionsPagerAdapter != null){
					DeveloperTab.mSectionsPagerAdapter.notifyDataSetChanged();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void updateList(){
		//Initialize list of entries
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
 		int group = spref.getInt(new Keys().KEY_GROUP, -1);
 		
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        DeveloperTab.developers.clear();
        
        if(spref.getString(new Keys().KEY_USER, "null").contains("master")){
        	DeveloperTab.developers = databaseHandler.getAllDevelopers();
        }else{
        	if(spref.getString(new Keys().KEY_USER, "null").contains("developer")){
        		DeveloperTab.developers = databaseHandler.getAllDevelopers(group);
        		
        		Log.w("updateList", "Size  = " + String.valueOf(DeveloperTab.developers.size()));
        	}
        }
        
	}
	
	@Override
	public void onDestroy() {
		//Close all open menus, to prevent window leaks	
		closeOptionsMenu();
		
		// Unregister Broadcast Receiver
		unregisterReceiver(broadcastReceiver);
		broadcastRegistered = false;
		
		super.onDestroy();
	}

}