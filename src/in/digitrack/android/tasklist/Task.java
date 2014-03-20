package in.digitrack.android.tasklist;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
	
	private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DONE = "done";
    private static final String JSON_DATE = "date";
    private static final String JSON_ASSIGNEE = "assignee";
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mDone;
	private String mAssignee;
	
	public Task() {
		//generate unique Id
		mId = UUID.randomUUID();
		mDate = new Date();
		mDone = false;
	}
	
	public Task(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		mTitle = json.getString(JSON_TITLE);
		mDate = new Date(json.getLong(JSON_DATE));
		mDone = json.getBoolean(JSON_DONE);
		mAssignee = json.getString(JSON_ASSIGNEE);
	}
	
	public UUID getId() {
		return mId;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public Date getDate() {
		return mDate;
	}
	
	public void setDate(Date date) {
		mDate = date;
	}
	
	public boolean isDone() {
		return mDone;
	}
	
	public void setDone(boolean done) {
		mDone = done;
	}
	
	public String getAssignee() {
		return mAssignee;
	}
	
	public void setAssignee(String assignee) {
		mAssignee = assignee;
	}
	
	@Override
	public String toString() {
		return mTitle;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_DATE, mDate.getTime());
		json.put(JSON_DONE, mDone);
		json.put(JSON_ASSIGNEE, mAssignee);
		return json;
	}
}





