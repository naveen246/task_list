package in.digitrack.android.tasklist;

import android.support.v4.app.Fragment;

public class TaskListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new TaskListFragment();
	}

}
