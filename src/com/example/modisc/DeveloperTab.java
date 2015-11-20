package com.example.modisc;

import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeveloperTab extends Fragment implements View.OnClickListener, OnTaskCompleted {
	
	private View mView;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager viewPager;
	
	private List<DeveloperObject> developers;
	
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
	
	private void initializeMainLayout(){
		//Initialize list of entries
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getContext());
 		int group = spref.getInt(new Keys().KEY_GROUP, 0);
 		
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        developers = databaseHandler.getAllDevelopers(group);
        
        for(int index = 0; index < developers.size(); index++){
        	if(developers.get(index).getName().contentEquals(spref.getString(new Keys().KEY_NAME, ""))){
        		developers.remove(index);
        	}
        }

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
        	SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getContext());
    		int group = spref.getInt(new Keys().KEY_GROUP, 0);
    		
    		DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
    		int countTabs = databaseHandler.getDeveloperCount(group);
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
       
        EditText goals = null, todaysGoals = null, obstacle = null;
        
        if(fragVal == 0){
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
        	update.setOnClickListener(this);
        }else{
        	goals = (EditText) v.findViewById(R.id.DTETGoals);
            todaysGoals = (EditText) v.findViewById(R.id.DTETTodaysGoals);
            obstacle = (EditText) v.findViewById(R.id.DTETObstacles);
            
            identifier.setText(developers.get(fragVal - 1).getName().toUpperCase(Locale.ENGLISH));
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
        	
        	goals.setText(developers.get(fragVal - 1).getGoal());
        	todaysGoals.setText(developers.get(fragVal - 1).getTodaysGoal());
        	obstacle.setText(developers.get(fragVal - 1).getObstacle());
        }
        
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.DTBUpdate:
			SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getContext());
			String email = spref.getString(new Keys().KEY_EMAIL, "Unknown");
			String name = spref.getString(new Keys().KEY_NAME, "Unknown");
			int groupid = spref.getInt(new Keys().KEY_GROUP, 0);
			String goals = personal_goals.getText().toString();
			String todaysGoal = personal_todaysGoals.getText().toString();
			String obstacle = personal_obstacle.getText().toString();
			
			new DatabaseHandler(getContext()).updateDeveloper(email, 
					goals, todaysGoal, obstacle);
			
			// Send to server and update server database too
			DeveloperObject developer = new DeveloperObject(email, name, groupid, goals, todaysGoal, obstacle);
			try {
				new SendDataToServer(getContext(), this).execute(new Helper().createJSON(developer));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onTaskCompleted(int result) {
		if(result == -1){
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
		}
	}

}