package in.digitrack.android.tasklist;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class Tasks {
	private ArrayList<Task> mTasks;
	private static Tasks sTasks;
	private Context mAppContext;
	
	private Tasks(Context appContext) {
		mAppContext = appContext;
		mTasks = new ArrayList<Task>();
		for(int i = 0; i < 50; i++) {
			Task t = new Task();
			t.setTitle("Task #" + i);
			t.setDone(i%2 == 0);
			mTasks.add(t);
		}
	}
	
	public static Tasks get(Context c) {
		if(sTasks == null) {
			sTasks = new Tasks(c.getApplicationContext());
		}
		return sTasks;
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
