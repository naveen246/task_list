package in.digitrack.android.tasklist;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class Tasks {
    private static final String FILENAME = "tasks.json";
    private ArrayList<Task> mTasks;
	private static Tasks sTasks;
	private Context mAppContext;
	
	private TaskListJSONSerializer mSerializer;
	
	private Tasks(Context appContext) {
		mAppContext = appContext;
		mSerializer = new TaskListJSONSerializer(mAppContext, FILENAME);
		try {
			mTasks = mSerializer.loadTasks();
			Log.d("TaskLoadFromJson", "success" );
		} catch(Exception e) {
			Log.d("TaskLoadFromJson", "fail" );
			mTasks = new ArrayList<Task>();
		}
	}
	
	public static Tasks get(Context c) {
		if(sTasks == null) {
			sTasks = new Tasks(c.getApplicationContext());
		}
		return sTasks;
	}
	
	public void addTask(Task t) {
		mTasks.add(t);
	}
	
	public void deleteTask(Task t) {
		mTasks.remove(t);
	}
	
	public boolean saveTasks() {
		try {
			mSerializer.saveTasks(mTasks);
			Log.d("TaskSaveToJson", "success" );
			return true;
		} catch(Exception e) {
			Log.d("TaskSaveToJson", "fail" );
			return false;
		}
	}
	
	public ArrayList<Task> getTasks() {
		return mTasks;
	}
	
	public Task getTask(UUID id) {
		for(Task t: mTasks) {
			if(t.getId().equals(id)) {
				return t;
			}
		}
		return null;
	}
}
