package com.example.modisc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost.OnTabChangeListener;

public class DeveloperScreen extends AppCompatActivity{
	
	//Tab parameters
	protected FragmentTabHost mTabHost;
	protected android.support.v4.app.FragmentManager fragmentManager;
	
	String name;
	int group;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_developerscreen);
		
		//Initialize UI Components
		initializeComponents();
       
		//Initialize Tabbed View
		tabView();
				
	}
	
	private void initializeComponents(){
		//Enabling Logo on the Action Bar
		Toolbar toolbar = (Toolbar) findViewById(R.id.TLToolBar);
		toolbar.setNavigationIcon(R.drawable.scrum_icon_small);
		toolbar.setTitle("Distributed Scrum");
		toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(toolbar);
        
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		group = spref.getInt(new Keys().KEY_GROUP, 0);
		name = spref.getString(new Keys().KEY_NAME, "Unknown");
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
    				startActivity(ourIntent);
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
	public void onDestroy() {
		//Close all open menus, to prevent window leaks	
		closeOptionsMenu();
		
		super.onDestroy();
	}
	
}