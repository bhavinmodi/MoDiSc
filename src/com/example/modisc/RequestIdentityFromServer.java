package com.example.modisc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
//import android.util.Log;
import android.util.Log;

/***
 * 
 * @author Bhavin
 *	Class is responsible for getting updates from the server.
 */
public class RequestIdentityFromServer extends AsyncTask<JSONObject, String, JSONObject>{

	private Context context;
	private OnReceiveIdentity listener;
	
	public RequestIdentityFromServer(Context context, OnReceiveIdentity listener){
        this.listener = listener;
        this.context = context;
    }
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		
		try{
			URL url = new URL("http://modisc.dx.am/requestIdentity.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept","*/*");
			conn.setConnectTimeout(10000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
             
			//Checking if an Internet connection is available
            //if not, store the message in a new file to be sent
            //when available
            if(!new Helper().getInstance().isConnectedToInternet(context)){
            	return null;
            }
	            
            Log.println(Log.ASSERT, "JSON OBJECT", params[0].toString());
            
			OutputStream os = conn.getOutputStream();
			os.write(params[0].toString().getBytes());
			os.flush();
			os.close();
			
			//Reworking the way we get the response
			int status = conn.getResponseCode();

			InputStream in;
			
			if(status >= HttpURLConnection.HTTP_BAD_REQUEST){
			    in = conn.getErrorStream();
			    return null;
			}else{
			    in = conn.getInputStream();
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		    while ((line = reader.readLine()) != null)
		    {
		        sb.append(line + "\n");
		    }
		    String sResponse = sb.toString();
		    
		    Log.println(Log.ASSERT, "RequestIdentityFromServer", "Response: "+sResponse);
		    
		    JSONObject json = new JSONObject(sResponse);
		    
            return json;
           
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;
	}

	@Override
	protected void onPostExecute(JSONObject json) {
		super.onPostExecute(json);
		if(json != null){
			listener.onReceiveIdentity(json);
		}	
	}

}