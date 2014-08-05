package com.dcc.fieldtripbuffer;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	private boolean isBufferServiceRunning() {
		final ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (final RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (BufferService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			if (isBufferServiceRunning()) {
				getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.activity_main_container,
						new RunningBuffer(this)).commit();
			} else {
				getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.activity_main_container,
						new StartBuffer(this)).commit();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
