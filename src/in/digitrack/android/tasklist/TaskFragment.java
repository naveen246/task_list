package in.digitrack.android.tasklist;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
	public static final String EXTRA_TASK_ID = "in.digitrack.android.tasklist.task_id";
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID taskId = (UUID)getArguments().getSerializable(EXTRA_TASK_ID);
		mTask = Tasks.get(getActivity()).getTask(taskId);
		setHasOptionsMenu(true);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_task, parent, false);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { 
				if(NavUtils.getParentActivityName(getActivity()) != null) {
					getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
				}
		}
		
		mTitleField = (EditText)v.findViewById(R.id.task_title);
		mTitleField.setText(mTask.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				mTask.setTitle(c.toString());
			}
			public void beforeTextChanged(CharSequence c, int start, int before, int count) { }
			public void afterTextChanged(Editable c) { }
		});
		
		mDateButton = (Button)v.findViewById(R.id.task_date);
		updateDate();
		mDateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mTask.getDate());
				dialog.setTargetFragment(TaskFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mDoneCheckBox = (CheckBox)v.findViewById(R.id.task_done);
		mDoneCheckBox.setChecked(mTask.isDone());
		mDoneCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isDone) {
				mTask.setDone(isDone);
			}
		});
		return v;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				if(NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void updateDate() {
		mDateButton.setText(DateFormat.format("EEE, MMM dd yyyy,  hh:mm", mTask.getDate()).toString());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) {
			return;
		}
		if(requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mTask.setDate(date);
			updateDate();
		}
	}
	
	public static TaskFragment newInstance(UUID taskId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TASK_ID, taskId);
		
		TaskFragment fragment = new TaskFragment();
		fragment.setArguments(args);
		return fragment;
	}
}
