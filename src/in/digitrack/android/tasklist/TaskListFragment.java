package in.digitrack.android.tasklist;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListFragment extends ListFragment {
	private ArrayList<Task> mTasks;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.tasks_title);
		mTasks = Tasks.get(getActivity()).getTasks();
		
		TaskAdapter adapter = new TaskAdapter(mTasks);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Task t = ((TaskAdapter)getListAdapter()).getItem(position);
		
		Intent i = new Intent(getActivity(), TaskPagerActivity.class);
		i.putExtra(TaskFragment.EXTRA_TASK_ID, t.getId());
		startActivity(i);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((TaskAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_task_list, menu);
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_item_new_task:
				Task t = new Task();
				Tasks.get(getActivity()).addTask(t);
				Intent i = new Intent(getActivity(), TaskPagerActivity.class);
				i.putExtra(TaskFragment.EXTRA_TASK_ID, t.getId());
				startActivityForResult(i, 0);
				return true;
			case R.id.menu_item_show_subtitle:
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				return true;
			default:
				return 	super.onOptionsItemSelected(item);
		}
	}
	
	private class TaskAdapter extends ArrayAdapter<Task> {
		public TaskAdapter(ArrayList<Task> tasks) {
			super(getActivity(), 0, tasks);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_task, null);	
			}
			Task t = getItem(position);
			
			TextView titleTextView = (TextView)convertView.findViewById(R.id.task_list_item_titleTextView);
			titleTextView.setText(t.getTitle());
			TextView dateTextView = (TextView)convertView.findViewById(R.id.task_list_item_dateTextView);
			dateTextView.setText(DateFormat.format("EEE, MMM dd yyyy,  hh:mm", t.getDate()).toString());
			CheckBox doneCheckBox = (CheckBox)convertView.findViewById(R.id.task_list_item_doneCheckBox);
			doneCheckBox.setChecked(t.isDone());
			return convertView;
		}
	}
	
}
