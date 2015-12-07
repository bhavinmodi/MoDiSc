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
        this.listener = listener;
        this.context = context;
    }
	
	@Override
	protected Integer doInBackground(JSONObject... params) {
		
		try{
			URL url = new URL("http://modisc.dx.am/post.php");
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
            	return -2;
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
            
            Log.println(Log.ASSERT, "SendDataToServer", "Response: "+sResponse);
            
            if(sResponse == null || !sResponse.contentEquals("Success.")){
            	//Server failed to receive event message, write to backlog file
            	return -1;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
		
		return 1;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		listener.onTaskCompleted(result);	
	}

}
