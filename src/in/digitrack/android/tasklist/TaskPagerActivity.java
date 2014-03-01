package in.digitrack.android.tasklist;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class TaskPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Task> mTasks;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mTasks = Tasks.get(this).getTasks();
		FragmentManager fm = getSupportFragmentManager();
		
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mTasks.size();
			}
			
			public Fragment getItem(int pos) {
				Task task = mTasks.get(pos);
				return TaskFragment.newInstance(task.getId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageScrollStateChanged(int arg0) { }

			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			public void onPageSelected(int pos) {
				Task task = mTasks.get(pos);
				if(task.getTitle() != null) {
					setTitle(task.getTitle());
				}
			}
			
		});
		
		UUID taskId = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
		for(int i = 0; i < mTasks.size(); i++) {
			if(mTasks.get(i).getId().equals(taskId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
