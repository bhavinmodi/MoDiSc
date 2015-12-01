package com.example.modisc;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

public class Helper {

	private static Helper helper = new Helper();
	
	public Helper getInstance(){
		return helper;
	}
	
	protected JSONObject createJSON(String email, String name, int groupid, String goals,
			String todaysgoals, String obstacle) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("email", email);
		json.put("name", name);
		json.put("groupid", groupid);
		json.put("goals", goals);
		json.put("todaysgoals", todaysgoals);
		json.put("obstacle", obstacle);
		return json;
	}
	
	protected JSONObject createJSON(DeveloperObject developer) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("email", developer.getEmail());
		json.put("name", developer.getName());
		json.put("groupid", developer.getGroup());
		json.put("goals", developer.getGroup());
		json.put("todaysgoals", developer.getTodaysGoal());
		json.put("obstacle", developer.getObstacle());
		return json;
	}
	
	protected JSONObject createJSON(String email, int groupid) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("email", email);
		json.put("groupid", groupid);
		return json;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	protected boolean isConnectedToInternet(Context context){
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) 
        {
      	  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
      		  Network[] allNetworks = connectivity.getAllNetworks();
	        	  NetworkInfo[] info = new NetworkInfo[allNetworks.length];
	        	  	        	  
	        	  for(int i = 0; i<allNetworks.length; i++){
	        		  info[i] = connectivity.getNetworkInfo(allNetworks[i]);
	        	  }
	               
	              if (info != null) 
	                  for (int i = 0; i < info.length; i++) 
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
      	  }else{
      		  NetworkInfo[] info = connectivity.getAllNetworkInfo();
      		  if (info != null) 
	                  for (int i = 0; i < info.length; i++) 
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
      	  }

        }
        return false;
	}
	
}
