package in.digitrack.android.tasklist;

import java.util.Date;
import java.util.UUID;

public class Task {
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mDone;
	
	public Task() {
		//generate unique Id
		mId = UUID.randomUUID();
		mDate = new Date();
		mDone = false;
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
}
