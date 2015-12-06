package com.example.modisc;

import java.util.Locale;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.view.*;

public class StartWizard extends AppCompatActivity implements View.OnClickListener, OnTaskCompleted {
	
	EditText et_email, et_group, et_name;
	Button submit;
	DatabaseHandler databaseHandler;
	private AlertDialog authAlert;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		//Extract each value from the bundle for usage
		String caller = bundle.getString("Caller");
		
		setContentView(R.layout.activity_startwizard);
		
		//Initialize
		initialize(caller);
		
	}
	
	private void initialize(String caller){
		//Enabling Logo on the Action Bar
		Toolbar toolbar = (Toolbar) findViewById(R.id.TLToolBar);
		toolbar.setNavigationIcon(R.drawable.scrum_icon_small);
		toolbar.setTitle("Distributed Scrum");
		toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(toolbar);
        
        et_email = (EditText) findViewById(R.id.SWETEmail);
        et_name = (EditText) findViewById(R.id.SWETName);
        et_group = (EditText) findViewById(R.id.SWETGroup);
        		
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        et_email.setText(spref.getString(new Keys().KEY_EMAIL, ""));
        et_name.setText(spref.getString(new Keys().KEY_NAME, ""));
        
        if(!(spref.getInt(new Keys().KEY_GROUP, 0) == 0)){
        	et_group.setText(String.valueOf(spref.getInt(new Keys().KEY_GROUP, 0)));
        }
		
        submit = (Button) findViewById(R.id.SWBSubmit);
        submit.setOnClickListener(this);
        
        if(caller.contentEquals("Settings")){
        	// Email TextView hidden
        	findViewById(R.id.SWTVEmail).setVisibility(View.INVISIBLE);
        	
        	// Email Edittext hidden
        	et_email.setVisibility(View.INVISIBLE);
        }
        
        databaseHandler = new DatabaseHandler(getApplicationContext());
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.SWBSubmit:
			//Store data to Database
			String email = et_email.getText().toString();
			
			if(email.contentEquals("")){
				break;
			}
			
			String name = et_name.getText().toString().toUpperCase(Locale.ENGLISH);
			
			if(name.contentEquals("")){
				break;
			}
			
			String group_string = et_group.getText().toString();
			
			if(group_string.contentEquals("")){
				break;
			}
			
			int group = Integer.parseInt(group_string);
			
			DeveloperObject developer = new DeveloperObject(email, name, group, "", "", "",1);
			databaseHandler.addDeveloper(developer);
			
			// Send to server and update server database too
			try {
				new SendDataToServer(getApplicationContext(), this).execute(new Helper().createJSON(developer));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor editor = spref.edit();
			editor.putBoolean(new Keys().KEY_FIRST_FLAG, false);
			editor.putInt(new Keys().KEY_GROUP, group);
			editor.putString(new Keys().KEY_NAME, name);
			editor.putString(new Keys().KEY_EMAIL, email);
			editor.commit();
			
			Class<?> ourClass = null;
			try {
				ourClass = Class.forName("com.example.modisc.DeveloperScreen");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Intent ourIntent = new Intent(this, ourClass);
			startActivity(ourIntent);
			break;
		}
	}
	
	@Override
	public void onTaskCompleted(int result) {
		if(result == -1){
			AlertDialog.Builder authDialog = new AlertDialog.Builder(this)
	    			.setTitle("MoDiSc")
		            .setMessage("Send Failure")
	    			.setCancelable(false);
	    	authAlert = authDialog.create();
			if(!authAlert.isShowing()){
				authAlert.show();
			}
			
			//Dismiss the dialog after 2 seconds
    		new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(authAlert!=null && authAlert.isShowing()){
								authAlert.dismiss();
							}
						}
					});
				}
			}).start();
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//Make sure all dialog are dismissed
		if(authAlert != null && authAlert.isShowing()){
			authAlert.dismiss();
		}
	}
	
}