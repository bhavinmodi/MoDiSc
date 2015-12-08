package com.example.modisc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DeveloperFragment extends DialogFragment implements OnTaskCompleted{

	public Spinner typeValue;
	public TextView idValue;
	public EditText nameValue;
	public EditText descValue;
	public Spinner priorityValue;
	public Spinner statusValue;
	public Spinner ownerValue;
	
	Task values;
	private TaskList taskList;
	private Button buttonSave;
	private Button buttonCancel;
	
	private Activity activity;
	
	public DeveloperFragment(Task values, TaskList taskList) {
		this.values = values;
		this.taskList = taskList;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup parentViewGroup, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_developers, parentViewGroup,
				false);

		getDialog().setTitle("Edit Task/Bug");
		
		typeValue = (Spinner) rootView.findViewById(R.id.typeValue);
		idValue = (TextView) rootView.findViewById(R.id.idValue);
		nameValue = (EditText) rootView.findViewById(R.id.nameValue);
		descValue = (EditText) rootView.findViewById(R.id.descValue);
		priorityValue = (Spinner) rootView.findViewById(R.id.priorityValue);
		statusValue = (Spinner) rootView.findViewById(R.id.statusValue);
		ownerValue = (Spinner) rootView.findViewById(R.id.ownerValue);
		buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
		buttonCancel = (Button) rootView.findViewById(R.id.buttonCancel);
		
		idValue.setText(String.valueOf(values.getId()));
		nameValue.setText(values.getName());
		descValue.setText(values.getDescription());
		
		ArrayAdapter<CharSequence> tAdapter = ArrayAdapter.createFromResource(getContext(),
		        R.array.types, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		typeValue.setAdapter(tAdapter);
		typeValue.setSelection(tAdapter.getPosition(values.getType()), true);
		
		ArrayAdapter<CharSequence> pAdapter = ArrayAdapter.createFromResource(getContext(),
		        R.array.priorities, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		priorityValue.setAdapter(pAdapter);
		priorityValue.setSelection(values.getPriority(), true);
		
		ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(getContext(),
		        R.array.status, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		statusValue.setAdapter(sAdapter);
		statusValue.setSelection(sAdapter.getPosition(values.getStatus()), true);
		
		ArrayAdapter<CharSequence> oAdapter = ArrayAdapter.createFromResource(getContext(),
		        R.array.owners, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ownerValue.setAdapter(oAdapter);
		ownerValue.setSelection(oAdapter.getPosition(values.getOwner()), true);
		
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		buttonSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Task newValues = new Task((String)typeValue.getSelectedItem(), Integer.parseInt((String)idValue.getText()), nameValue.getText().toString(), descValue.getText().toString().trim(), priorityValue.getSelectedItemPosition(), (String)statusValue.getSelectedItem(), (String)ownerValue.getSelectedItem());
				taskList.saveData(newValues);
				/*try {
					new SendDataToServer(getActivity().getBaseContext(), fragment).execute(new Helper().createJSON(newValues));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}*/
				dismiss();
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onTaskCompleted(int result) {
		System.out.println(activity);
		if(result == 1)
			Toast.makeText(activity, "Task saved to server", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(activity, "Task wasn't saved to server. Please try again", Toast.LENGTH_SHORT).show();
	}
}
