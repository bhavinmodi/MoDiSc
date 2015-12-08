package com.example.modisc;

import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DeveloperTab extends Fragment implements View.OnClickListener, OnTaskCompleted {
	
	private View mView;
	protected static SectionsPagerAdapter mSectionsPagerAdapter;
	protected static DatabaseHandler databaseHandler;
	protected static SharedPreferences spref;
	private ViewPager viewPager;
	
	protected static List<DeveloperObject> developers;
	
	Button status_Button;
	ImageView personal_statusIcon;
	TextView personal_status;
	EditText personal_goals;
    EditText personal_todaysGoals;
    EditText personal_obstacle;
    
    private AlertDialog authAlert;
	
	public DeveloperTab(){
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
		if(mView == null){
			spref = PreferenceManager.getDefaultSharedPreferences(getContext());
			databaseHandler = new DatabaseHandler(getContext());
			
			mView = inflater.inflate(R.layout.fragment_developertab, container, false);
			View layout = mView.findViewById(R.id.FDLayout);
			layout.setBackgroundColor(Color.WHITE);
			
			//Initialize
			initializeMainLayout();
			
			mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
			viewPager = (ViewPager) mView.findViewById(R.id.FDViewPager);
			viewPager.setAdapter(mSectionsPagerAdapter);
			viewPager.setCurrentItem(0);
		}
		
		return mView;
	}
	
	protected void initializeMainLayout(){
		//Initialize list of entries
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getContext());
 		int group = spref.getInt(new Keys().KEY_GROUP, -1);
 		
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        developers = databaseHandler.getAllDevelopers(group);
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {  
		  
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }  
  
        @Override  
        public Fragment getItem(int position) {
        		return new PageFragment().init(position);
        }  
  
        @Override  
        public int getCount() {
    		int group = spref.getInt(new Keys().KEY_GROUP, -1);
    		
    		int countTabs = 1;
    		if(spref.getString(new Keys().KEY_USER, "null").contains("master")){
    			countTabs = databaseHandler.getDeveloperCount();
            }else{
            	if(spref.getString(new Keys().KEY_USER, "null").contains("developer")){
            		countTabs = databaseHandler.getDeveloperCount(group);
            	}
            }
    		
    		return countTabs;
        }  
  
    }  
   
    public class PageFragment extends Fragment {  
        
    	int fragVal;
    	
    	PageFragment init(int val) {
    		PageFragment frag = new PageFragment();
	    	 // Supply val input as an argument.
	    	 Bundle args = new Bundle();
	    	 args.putInt("val", val);
	    	 frag.setArguments(args);
	    	 return frag;
    	 }
    	 
    	 @Override
    	 public void onCreate(Bundle savedInstanceState) {
    		 super.onCreate(savedInstanceState);
    	 	 fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    	 }
    	 
    	 @Override
    	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		 View layoutView = inflater.inflate(R.layout.page_developer, container, false);
    		 
    		 //Initialize Developer Entry Fragments
    		 initializeDeveloperEntry(layoutView, fragVal);
    		 
    		 return layoutView;
    	 }

    }

	private void initializeDeveloperEntry(View v, int fragVal){
		//Initialize list of entries		
        TextView identifier = (TextView) v.findViewById(R.id.DTTVIdentifier);
        Button update = (Button) v.findViewById(R.id.DTBUpdate);
       
        Button status_change;
        ImageView statusIcon = null;
        TextView status = null;
        EditText goals = null, todaysGoals = null, obstacle = null;
        
        if(fragVal == 0){
        	// Activate status change option
        	status_Button = (Button) v.findViewById(R.id.DTDummy);
        	status_Button.setOnClickListener(this);
        	
        	personal_statusIcon = (ImageView) v.findViewById(R.id.DTIVStatus);
        	personal_status = (TextView) v.findViewById(R.id.DTTVStatus);
        	personal_goals = (EditText) v.findViewById(R.id.DTETGoals);
        	personal_todaysGoals = (EditText) v.findViewById(R.id.DTETTodaysGoals);
        	personal_obstacle =  (EditText) v.findViewById(R.id.DTETObstacles);
        	
        	SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getContext());
    		String email = spref.getString(new Keys().KEY_EMAIL, "Unknown");
    		
        	DeveloperObject developer = new DatabaseHandler(getContext()).getDeveloper(email);
        	
        	identifier.setText(developer.getName().toUpperCase(Locale.ENGLISH));
        	personal_goals.setText(developer.getGoal());
        	personal_todaysGoals.setText(developer.getTodaysGoal());
        	personal_obstacle.setText(developer.getObstacle());
        	
        	switch(developer.getStatus()){
        	case 0:
        		personal_status.setText("Offline");
        		personal_statusIcon.setBackgroundResource(R.drawable.red);
        		break;
        	case 1:
        		personal_status.setText("Online");
        		personal_statusIcon.setBackgroundResource(R.drawable.green);
        		break;
        	case 2:
        		personal_status.setText("Busy");
        		personal_statusIcon.setBackgroundResource(R.drawable.yellow);
        		break;
        	}
        	
        	update.setOnClickListener(this);
        }else{
        	status_change = (Button) v.findViewById(R.id.DTDummy);
        	status_change.setClickable(false);
        	
        	statusIcon = (ImageView) v.findViewById(R.id.DTIVStatus);
        	status = (TextView) v.findViewById(R.id.DTTVStatus);
        	goals = (EditText) v.findViewById(R.id.DTETGoals);
            todaysGoals = (EditText) v.findViewById(R.id.DTETTodaysGoals);
            obstacle = (EditText) v.findViewById(R.id.DTETObstacles);
            
            identifier.setText(developers.get(fragVal).getName().toUpperCase(Locale.ENGLISH));
        	update.setVisibility(View.INVISIBLE);
        	
        	goals.setFocusable(false);
        	goals.setClickable(true);
        	goals.setVerticalScrollBarEnabled(true);
        	todaysGoals.setFocusable(false);
        	todaysGoals.setClickable(true);
        	todaysGoals.setVerticalScrollBarEnabled(true);
        	obstacle.setFocusable(false);
        	obstacle.setClickable(true);
        	obstacle.setVerticalScrollBarEnabled(true);
        	
        	goals.setText(developers.get(fragVal).getGoal());
        	todaysGoals.setText(developers.get(fragVal).getTodaysGoal());
        	obstacle.setText(developers.get(fragVal).getObstacle());
        	
        	switch(developers.get(fragVal).getStatus()){
        	case 0:
        		status.setText("Offline");
        		statusIcon.setBackgroundResource(R.drawable.red);
        		break;
        	case 1:
        		status.setText("Online");
        		statusIcon.setBackgroundResource(R.drawable.green);
        		break;
        	case 2:
        		status.setText("Busy");
        		statusIcon.setBackgroundResource(R.drawable.yellow);
        		break;
        	}
        }
        
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.DTBUpdate:
			callUpdate();
			break;
		case R.id.DTDummy:
			//Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(getActivity(), status_Button);
            
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                	
                	SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getContext());
    				Editor editor = spref.edit();
    				
                    switch(item.getItemId()){
                    case R.id.online:
                    	editor.putInt(new Keys().KEY_STATUS, 1);
                    	personal_status.setText("Online");
                    	personal_statusIcon.setBackgroundResource(R.drawable.green);
                    	callUpdate();
                    	break;
                    case R.id.offline:
                    	editor.putInt(new Keys().KEY_STATUS, 0);
                    	personal_status.setText("Offline");
                    	personal_statusIcon.setBackgroundResource(R.drawable.red);
                    	callUpdate();
                    	break;
                    case R.id.busy:
                    	editor.putInt(new Keys().KEY_STATUS, 2);
                    	personal_status.setText("Busy");
                    	personal_statusIcon.setBackgroundResource(R.drawable.yellow);
                    	callUpdate();
                    	break;
                    }
                    
    				editor.commit();
                    return true;
                }
            });
            
            popup.show(); //showing popup menu
            
			break;
		}
	}

	private void callUpdate(){
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getContext());
		String email = spref.getString(new Keys().KEY_EMAIL, "Unknown");
		String name = spref.getString(new Keys().KEY_NAME, "Unknown");
		int groupid = spref.getInt(new Keys().KEY_GROUP, 0);
		String goals = personal_goals.getText().toString();
		String todaysGoal = personal_todaysGoals.getText().toString();
		String obstacle = personal_obstacle.getText().toString();
		String statusString = personal_status.getText().toString();
		
		int status = 0;
		switch(statusString){
		case "Online":
			status = 1;
			break;
		case "Offline":
			status = 0;
			break;
		case "Busy":
			status = 2;
			break;
		}
		
		new DatabaseHandler(getContext()).updateDeveloper(email, 
				goals, todaysGoal, obstacle, status);
		
		// Send to server and update server database too
		DeveloperObject developer = new DeveloperObject(email, name, groupid, goals, todaysGoal, obstacle, status);
		try {
			new SendDataToServer(getContext(), this).execute(new Helper().createJSON(developer));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void onTaskCompleted(int result) {
		switch(result){
		case 1:
			SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getContext());
			Editor editor = spref.edit();
			editor.putString(new Keys().KEY_GOAL, personal_goals.getText().toString());
			editor.putString(new Keys().KEY_TGOAL, personal_todaysGoals.getText().toString());
			editor.putString(new Keys().KEY_OBSTACLE, personal_obstacle.getText().toString());
			editor.commit();
			break;
		case -1:
			AlertDialog.Builder authDialog = new AlertDialog.Builder(getContext())
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
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(authAlert!=null && authAlert.isShowing()){
								authAlert.dismiss();
							}
						}
					});
				}
			}).start();
					break;
		case -2:
			AlertDialog.Builder authDialog2 = new AlertDialog.Builder(getContext())
			.setTitle("MoDiSc")
            .setMessage("Send Failure - No Internet Connection")
			.setCancelable(false);
			authAlert = authDialog2.create();
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
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if(authAlert!=null && authAlert.isShowing()){
								authAlert.dismiss();
							}
						}
					});
				}
			}).start();
			break;
		}
		
	}

}