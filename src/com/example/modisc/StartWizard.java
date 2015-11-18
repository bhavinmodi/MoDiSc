package com.example.modisc;

import java.util.Locale;

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

public class StartWizard extends AppCompatActivity implements View.OnClickListener {
	
	EditText et_email, et_group, et_name;
	Button submit;
	DatabaseHandler databaseHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_startwizard);
		
		//Initilaize
		initialize();
		
	}
	
	private void initialize(){
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
        		
        submit = (Button) findViewById(R.id.SWBSubmit);
        submit.setOnClickListener(this);
        
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
			
			DeveloperObject developer = new DeveloperObject(email, name, group, "", "", "");
			databaseHandler.addDeveloper(developer);
			
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
	
}