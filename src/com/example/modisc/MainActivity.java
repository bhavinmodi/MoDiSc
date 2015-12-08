package com.example.modisc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Button loginButton;
	private EditText login;
	private EditText password;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent taskList;
				if(login.getText().toString().equals("") && password.getText().toString().equals("")) {
					taskList = new Intent(MainActivity.this, TaskList.class);
					MainActivity.this.startActivity(taskList);
				}
				else
					Toast.makeText(v.getContext(), "Invalid ID or password", Toast.LENGTH_SHORT).show();
			}
		});
    }
	
}
