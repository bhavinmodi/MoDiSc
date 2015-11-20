package com.example.modisc;

import java.io.InputStream;
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
 *	Class is responsible for sending messages to the server.
 */
public class SendDataToServer extends AsyncTask<JSONObject, String, Integer>{

	private Context context;
	private OnTaskCompleted listener;
	
	public SendDataToServer(Context context, OnTaskCompleted listener){
        this.listener=listener;
        this.context = context;
    }
	
	@Override
	protected Integer doInBackground(JSONObject... params) {
		
		try{
			//Try our original connection method, which will work if a valid signing authority has issued the certificate
			//Else fall back on our self-signed certificate method
			URL url = new URL("modisc.atwebpages.com/post");
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
            	//Write json object to server backlog file
            	return -1;
            }
	            
            Log.println(Log.ASSERT, "JSON OBJECT", params[0].toString());
            
			OutputStream os = conn.getOutputStream();
			os.write(params[0].toString().getBytes());
			os.flush();
			os.close();
			
			//Reworking the way we get the response
			int status = conn.getResponseCode();

			InputStream in;
			
			if(status >= HttpURLConnection.HTTP_BAD_REQUEST)
			    in = conn.getErrorStream();
			else
			    in = conn.getInputStream();
			
			//Log.w("SendDataToServer", "Status is = " + String.valueOf(status));
			
            byte[] res = new byte[1024];
            String sResponse = null;

            in.read(res);
            sResponse = new String(res).trim();
            
            if(sResponse == null || !sResponse.contentEquals("Success.")){
            	//Server failed to receive event message, write to backlog file
            	return -1;
            }
            
           Log.println(Log.ASSERT, "SendDataToServer", "Response: "+sResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            //Message sending failed, write to backlog file if json object is not null
            if(params[0] != null){
            	return -1;
            }
        }
		
		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		listener.onTaskCompleted(result);	
	}

}
