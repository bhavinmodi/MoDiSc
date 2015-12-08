package com.example.modisc;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TaskList extends Fragment implements SaveData, OnReceiveUpdates {
	
	private View mView;
	ListView list;
	CustomListAdapter adapter;
	public TaskList CustomListView = null;
	public ArrayList<Task> CustomListViewValuesArr = new ArrayList<Task>();
	DBHelper myDB;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
		if(mView == null){
			mView = inflater.inflate(R.layout.activity_task_list, container, false);
			
			CustomListView = this;

			/******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
			myDB = new DBHelper(getContext());
			addRandomData();
			loadData();

			Resources res = getResources();
			list = (ListView) mView.findViewById(R.id.customList); // List defined in XML
																// ( See Below )

			/**************** Create Custom Adapter *********/
			adapter = new CustomListAdapter(CustomListView,
					CustomListViewValuesArr, res);
			list.setAdapter(adapter);
		}
		
		return mView;
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
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void addRandomData() {
		// myDB.deleteAll();
		// System.out.print("rows:" + myDB.numberOfRows());
		String[] bugStatus = { "Created", "Assigned", "In Progress",
				"Modified", "In Test", "Re-assigned", "Resolved" };
		String[] bugDevelopers = { "Vivek", "Bhavin", "Jose", "Jesse", "Sam",
				"Steve" };
		String[] types = { "Task", "Bug" };
		Random random = new Random();
		int tempID = myDB.numberOfRows();
		for (int i = 1; i <= 20; i++) {
			myDB.insertTask(types[random.nextInt(types.length)], "Bug "
					+ (tempID + i), "Description " + (tempID + i),
					Boolean.compare(true, random.nextBoolean()),
					bugDevelopers[random.nextInt(bugDevelopers.length)],
					bugStatus[random.nextInt(bugStatus.length)]);
		}
		/*
		 * myDB.insertTask(true, "First", "First description", 1, "Vivek",
		 * "In Progress"); myDB.insertTask(false, "Second", "First description",
		 * 0, "Vivek", "In Progress");
		 */
		// CustomListViewValuesArr.add(new Task(true, 1, "First",
		// "First description", 1, "Vivek", "In Progress"));
		// CustomListViewValuesArr.add(new Task(false, 2, "Second",
		// "First description", 0, "Vivek", "In Progress"));
		CustomListViewValuesArr = myDB.getAllTasks();
		// System.out.println("type:" +
		// CustomListViewValuesArr.get(0).getType());
	}
	
	public void loadData() {
		addRandomData();
		CustomListViewValuesArr = myDB.getAllTasks();
	}

	/***************** This function used by adapter ****************/
	public void onItemClick(int mPosition) {
		//Log.d("onclick", "inside");
		Task tempValues = (Task) CustomListViewValuesArr.get(mPosition);

		// Open fragment for assigning a developer
		// FragmentManager fragmentManager = getFragmentManager();
		// Or: FragmentManager fragmentManager = getSupportFragmentManager()
		// FragmentTransaction fragmentTransaction =
		// fragmentManager.beginTransaction();
		DeveloperFragment fragment = new DeveloperFragment(tempValues, this);
		// fragmentTransaction.replace(R.id.fragment_container, fragment);
		// fragmentTransaction.commit();
		fragment.show(getFragmentManager(), "Edit task details");
	}
	
	public boolean saveData(Task newValues) {
		myDB.updateTask(newValues.getId(), newValues.getType(), newValues.getName(), newValues.getDescription(), newValues.getPriority(), newValues.getStatus(), newValues.getOwner());
		adapter.updateData(myDB.getAllTasks());
		adapter.notifyDataSetChanged();
		return true;
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_list, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_delete) {
			myDB.deleteAll();
		} else if (id == R.id.action_load) {
			addRandomData();
		}
		adapter.updateData(myDB.getAllTasks());
		adapter.notifyDataSetChanged();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onReceiveUpdates(JSONObject json) {
		
	}
}
