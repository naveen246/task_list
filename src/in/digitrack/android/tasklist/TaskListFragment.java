package in.digitrack.android.tasklist;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListFragment extends ListFragment {
	private ArrayList<Task> mTasks;
	private boolean mSubtitleVisible = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.tasks_title);
		mTasks = Tasks.get(getActivity()).getTasks();
		
		TaskAdapter adapter = new TaskAdapter(mTasks);
		setListAdapter(adapter);
		setRetainInstance(true);
		mSubtitleVisible = false;
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if(mSubtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// Use floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);
		} else {
			// Use contextual action bar on Honeycomb and higher
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch(item.getItemId()) {
						case R.id.menu_item_delete_task:
							TaskAdapter adapter = (TaskAdapter)getListAdapter();
							Tasks tasks = Tasks.get(getActivity());
							for(int i = adapter.getCount(); i >= 0; i--) {
								if(getListView().isItemChecked(i)) {
									tasks.deleteTask(adapter.getItem(i));
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
						default:
							return false;
					}
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.task_list_item_context, menu);
					return true;
				}

				@Override
				public void onDestroyActionMode(ActionMode arg0) { }

				@Override
				public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) { 
					return false;
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode arg0,
						int arg1, long arg2, boolean arg3) { }
			});
		}
		return v;
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
		MenuItem showSubTitle = menu.findItem(R.id.menu_item_show_subtitle);
		if(mSubtitleVisible && showSubTitle != null) {
			showSubTitle.setTitle(R.string.hide_subtitle);	
		}
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
				if(getActivity().getActionBar().getSubtitle() == null) {
					getActivity().getActionBar().setSubtitle(R.string.subtitle);
					mSubtitleVisible = true;
					item.setTitle(R.string.hide_subtitle);
				} else {
					getActivity().getActionBar().setSubtitle(null);
					mSubtitleVisible = false;
					item.setTitle(R.string.show_subtitle);
				}
				return true;
			default:
				return 	super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.task_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		TaskAdapter adapter = (TaskAdapter)getListAdapter();
		Task task = adapter.getItem(position);
		switch(item.getItemId()) {
			case R.id.menu_item_delete_task:
				Tasks.get(getActivity()).deleteTask(task);
				adapter.notifyDataSetChanged();	
				return true;
			default:
				return super.onContextItemSelected(item);
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
