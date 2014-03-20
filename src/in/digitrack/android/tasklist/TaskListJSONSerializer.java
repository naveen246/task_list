package in.digitrack.android.tasklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class TaskListJSONSerializer {
	private Context mContext;
	private String mFilename;
	
	public TaskListJSONSerializer(Context c, String f) {
		mContext = c;
		mFilename = f;
	}
	
	public ArrayList<Task> loadTasks() throws IOException, JSONException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		BufferedReader reader = null;
		try {
			//Open and read the file in a StringBuilder
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while( (line = reader.readLine()) != null ) {
				jsonString.append(line);
			}
			//Parse the json using JSONTokener
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			//BUild array of tasks from JSONObjects
			for(int i = 0; i < array.length(); i++) {
				tasks.add(new Task(array.getJSONObject(i)));
			}
		} catch(Exception e) { 
		} finally {
			if(reader != null) {
				reader.close();
			}
		}
		return tasks;
	}
	
	public void saveTasks(ArrayList<Task> tasks) throws JSONException, IOException {
		//Build an array in JSON
		JSONArray array = new JSONArray();
		for(Task t: tasks) {
			array.put(t.toJSON());
		}
		
		//Write the file to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} catch(Exception e) {
			Log.d("TaskJsonSaveErr", "Error");
		}finally {
			if(writer != null)
				writer.close();
		}
	}
}
