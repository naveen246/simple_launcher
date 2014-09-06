package in.digitrack.simplelauncher;

import android.app.Fragment;

public class SimpleLauncherActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new SimpleLauncherFragment();
	}

}
