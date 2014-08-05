package com.dcc.fieldtripbuffer;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	public class RunningBuffer extends Fragment {

		/**
		 * Fragment for when the BufferService is running.
		 *
		 * @param context
		 */
		public RunningBuffer() {
		}

		@Override
		public View onCreateView(final LayoutInflater inflater,
				final ViewGroup container, final Bundle savedInstanceState) {
			final View rootView = inflater.inflate(
					R.layout.fragment_startbuffer, container, false);
			return rootView;
		}

	}

	public class StartBuffer extends Fragment {

		/**
		 * Fragment for starting the BufferService.
		 *
		 * @param context
		 */
		public StartBuffer() {
		}

		@Override
		public View onCreateView(final LayoutInflater inflater,
				final ViewGroup container, final Bundle savedInstanceState) {
			final View rootView = inflater.inflate(
					R.layout.fragment_startbuffer, container, false);
			return rootView;
		}

	}

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
				getSupportFragmentManager().beginTransaction()
				.add(R.id.activity_main_container, new RunningBuffer())
				.commit();
			} else {
				getSupportFragmentManager().beginTransaction()
				.add(R.id.activity_main_container, new StartBuffer())
				.commit();
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

	/**
	 * Called when you click the Start button.
	 */
	@SuppressWarnings("unused")
	private void startBuffer() {
		// Create Intent for starting the buffer.
		final Intent intent = new Intent(this, BufferService.class);

		// Grab port number and add to intent.
		final EditText editTextPort = (EditText) findViewById(R.id.fragment_startbuffer_port);
		intent.putExtra("port",
				Integer.parseInt(editTextPort.getText().toString()));

		// Grab nSamples and add to intent.
		final EditText editTextnSamples = (EditText) findViewById(R.id.fragment_startbuffer_nSamples);
		intent.putExtra("nSamples",
				Integer.parseInt(editTextnSamples.getText().toString()));

		// Grab nEvents and add to intent.
		final EditText editTextnEvents = (EditText) findViewById(R.id.fragment_startbuffer_nEvents);
		intent.putExtra("nEvents",
				Integer.parseInt(editTextnEvents.getText().toString()));

		// Start the buffer.
		startService(intent);

		// Replace this fragment with the RunningBuffer Fragment.

		// Create a fragment transaction
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		// Replace current fragment with a new RunningBuffer fragment
		transaction.replace(R.id.activity_main_container, new RunningBuffer());
		// Don't add anything to the backstack (so hitting back won't return the
		// user to the startbuffer screen).
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}

	/**
	 * Called when you click the Stop button.
	 */
	@SuppressWarnings("unused")
	private void stopBuffer() {
		// Create Intent for stopping the buffer.
		final Intent intent = new Intent(this, BufferService.class);
		// Stop the buffer
		stopService(intent);
	}
}
