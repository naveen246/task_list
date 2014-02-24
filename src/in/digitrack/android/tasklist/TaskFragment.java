package in.digitrack.android.tasklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class TaskFragment extends Fragment {
	private Task mTask;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mDoneCheckBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTask = new Task();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_task, parent, false);
		
		mTitleField = (EditText)v.findViewById(R.id.task_title);
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				mTask.setTitle(c.toString());
			}
			public void beforeTextChanged(CharSequence c, int start, int before, int count) { }
			public void afterTextChanged(Editable c) { }
		});
		
		mDateButton = (Button)v.findViewById(R.id.task_date);
		mDateButton.setText(DateFormat.format("MMM dd, yyyy;  hh:mm", mTask.getDate()).toString());
		mDateButton.setEnabled(false);
		
		mDoneCheckBox = (CheckBox)v.findViewById(R.id.task_done);
		mDoneCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isDone) {
				mTask.setDone(isDone);
			}
		});
		
		return v;
	}
}
