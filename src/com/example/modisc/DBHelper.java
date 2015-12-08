package com.example.modisc;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "SprintList.db";
	public static final String TASKLIST_TABLE_NAME = "TaskList";
	public static final String TASKLIST_COLUMN_ID = "id";
	public static final String TASKLIST_COLUMN_TYPE = "type";
	public static final String TASKLIST_COLUMN_NAME = "name";
	public static final String TASKLIST_COLUMN_DESC = "desc";
	public static final String TASKLIST_COLUMN_PRIORITY = "priority";
	public static final String TASKLIST_COLUMN_STATUS = "status";
	public static final String TASKLIST_COLUMN_OWNER = "owner";

	// private HashMap hp;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table TaskList "
				+ "(id integer primary key, type text, name text,desc text,priority integer, status text,owner text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS TaskList");
		onCreate(db);
	}

	public boolean insertTask(String type, String name, String desc,
			int priority, String status, String owner) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("type", type);
		//System.out.println("insert type:" + contentValues.getAsBoolean("type"));
		contentValues.put("name", name);
		contentValues.put("desc", desc);
		contentValues.put("priority", priority);
		contentValues.put("status", status);
		contentValues.put("owner", owner);
		/*db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (4,\"Bug\", \"Create database schema based on requirements\", \"Read the requirements document and come up with a schema for the database. Also, share it with the team and take feedback\", 1, \"Assigned\", \"Bhavin\"");

		db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (5,\"Task\", \"Arrange feedback meeting\", \"After the schema is created, arrange a meeting and take feedback from all the developers and managers and incorporate them in the updated schema\", 1, \"Assigned\", \"Bhavin\"");

		db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (6,\"Task\", \"Create updated schema\", \"Update the database schema based on the feedback received from the team members\", 1, \"Assigned\", \"Bhavin\"");

		db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (7,\"Task\", \"Add a new class that calculates the time between two gievn events\", \"Create a new class CalcTimeBetweenEvents that takes two event parameters and returns the time difference between these two events\", 1, \"Created\", \"Vivek\"");

		db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (8,\"Bug\", \"Update the patient details in summary\", \"The patient details are displayed in wromg columns om the summary page. Update the Summary class to display them in required order\", 1, \"Modified\", \"Sam\"");

		db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (9,\"Task\", \"Add abstract class to display all the parameters of the simulators\", \"Create an abstract class Display that contains all the method skeletons to display all the parameters and their respective timer values in that particular simulation\", 1, \"Re-assigned\", \"Jose\"");

		db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (10,\"Bug\", \"Update the event handlers for ECG\", \"Change the event handlers for class ECG to adjust to the manual setting changes\", 0, \"In Test\", \"Steve\"");

		db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (11,\"Task\", \"Modify graph view for ECG\", \"Update the graph view for ECG such that it shows the old graph beside the current along with the progress\", 0, \"Assigned\", \"Steve\"");

		db.execSQL("INSERT INTO `TaskList`(`id`, `type`, `name`, `desc`, `priority`, `status`, `owner`) VALUES (12,\"Task\", \"Add bubble chart to ECG display screen\", \"Create a new graph view called bubble chart to display the data in bubble format. Place it next to the original graph\", 0, \"Assigned\", \"Jesse\"");
*/
		db.insert("TaskList", null, contentValues);
		return true;
	}

	public Cursor getData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from TaskList where id=" + id + "",
				null);
		return res;
	}

	public int numberOfRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db,
				TASKLIST_TABLE_NAME);
		return numRows;
	}

	public boolean updateTask(Integer id, String type, String name,
			String description, int priority, String status, String owner) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("type", type);
		contentValues.put("name", name);
		contentValues.put("desc", description);
		contentValues.put("priority", priority);
		contentValues.put("status", status);
		contentValues.put("owner", owner);
		db.update("TaskList", contentValues, "id = ? ",
				new String[] { Integer.toString(id) });
		return true;
	}

	public Integer deleteTask(Integer id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("TaskList", "id = ? ",
				new String[] { Integer.toString(id) });
	}

	public ArrayList<Task> getAllTasks() {
		ArrayList<Task> array_list = new ArrayList<Task>();

		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from TaskList", null);
		res.moveToFirst();

		while (res.isAfterLast() == false) {
			String row = "";
			for (int i = 0; i < res.getColumnCount(); i++) {
				row += res.getString(i) + ",";
			}
			//System.out.println("column count:" + res.getColumnCount());
			String[] splitRow = row.split(",");
			array_list.add(new Task(splitRow[1], Integer
					.parseInt(splitRow[0]), splitRow[2], splitRow[3], Integer
					.parseInt(splitRow[4]), splitRow[5], splitRow[6]));
			res.moveToNext();
		}
		return array_list;
	}
	
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS TaskList");
		onCreate(db);
	}
}