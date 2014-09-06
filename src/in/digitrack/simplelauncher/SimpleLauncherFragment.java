package in.digitrack.simplelauncher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListFragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SimpleLauncherFragment extends ListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent startupIntent = new Intent(Intent.ACTION_MAIN);
		startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
		
		Log.i("SimpleLauncherFragment", "Result count = " + activities.size());
		
		Collections.sort(activities, new Comparator<ResolveInfo>() {
			@Override
			public int compare(ResolveInfo a, ResolveInfo b) {
				PackageManager pm = getActivity().getPackageManager();
				return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(), b.loadLabel(pm).toString());
			}
		});
		
		ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(getActivity(), android.R.layout.simple_list_item_1, activities) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				PackageManager pm = getActivity().getPackageManager();
				View v = super.getView(position, convertView, parent);
				TextView tv = (TextView)v;
				ResolveInfo ri = getItem(position);
				tv.setText(position+1 + ". " + ri.loadLabel(pm));
				tv.setCompoundDrawables(null, null, ri.loadIcon(pm), null);
				return v;
			}
		};
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ResolveInfo resolveInfo = (ResolveInfo)l.getAdapter().getItem(position);
		ActivityInfo activityInfo = resolveInfo.activityInfo;
		if(activityInfo == null) return;
		
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
}
