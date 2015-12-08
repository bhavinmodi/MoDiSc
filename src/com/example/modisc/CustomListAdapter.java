package com.example.modisc;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter implements OnClickListener {

	private Fragment fragment;
	private ArrayList<Task> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	Task tempValues = null;
	int i = 0;

	public CustomListAdapter(Fragment a, ArrayList<Task> d, Resources resLocal) {

		/********** Take passed values **********/
		fragment = a;
		data = d;
		res = resLocal;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) fragment.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {

		public TextView type;
		public TextView id;
		public TextView name;
		public TextView description;
		public ImageView priority;
		public TextView status;
		public TextView owner;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		LinearLayout customLayout = null;

		if (convertView == null) {

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.activity_task, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			customLayout = (LinearLayout) vi.findViewById(R.id.layout);
			holder.type = (TextView) vi.findViewById(R.id.typeValue);
			holder.id = (TextView) vi.findViewById(R.id.idValue);
			holder.name = (TextView) vi.findViewById(R.id.nameValue);
			holder.description = (TextView) vi.findViewById(R.id.descValue);
			//holder.priority = (ImageView) vi.findViewById(R.id.priority);
			holder.status = (TextView) vi.findViewById(R.id.statusValue);
			holder.owner = (TextView) vi.findViewById(R.id.ownerValue);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		if (data.size() <= 0) {
			holder.id.setText("*******No Tasks******");

		} else {
			/***** Get each Model object from Arraylist ********/
			tempValues = null;
			tempValues = data.get(position);

			/************ Set Model values in Holder elements ***********/

			//System.out.println("ID id is:"+holder.id.getText());
			holder.id.setText(String.valueOf(tempValues.getId()));
			
			holder.type.setText(tempValues.getType());
			
			holder.name.setText(tempValues.getName());
			holder.description.setText(tempValues.getDescription());
			holder.status.setText(tempValues.getStatus());
			holder.owner.setText(tempValues.getOwner());
			
			if(tempValues.getPriority() == 1 && customLayout != null)
				customLayout.setBackgroundResource(R.drawable.custom_border_red);
			else if(customLayout != null)
				customLayout.setBackgroundResource(R.drawable.custom_border_blue);

			/******** Set Item Click Listner for LayoutInflater for each row *******/

			vi.setOnClickListener(new OnItemClickListener(position));
		}
		return vi;
	}

	@Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

	public void updateData(ArrayList<Task> newList) {
		data = newList;
	}
     
    /********* Called when Item click in ListView ************/
    private class OnItemClickListener implements OnClickListener{           
        private int mPosition;
         
        OnItemClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View arg0) {
        
          TaskList sct = (TaskList)fragment;

         /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}
