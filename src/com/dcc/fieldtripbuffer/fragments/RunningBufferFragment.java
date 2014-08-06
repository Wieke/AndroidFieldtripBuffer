package com.dcc.fieldtripbuffer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dcc.fieldtripbuffer.BufferService;
import com.dcc.fieldtripbuffer.C;
import com.dcc.fieldtripbuffer.R;

public class RunningBufferFragment extends Fragment {
	private final Context context;

	private TextView connectionCount;

	OnClickListener stopBuffer = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			// Create Intent for stopping the buffer.
			final Intent intent = new Intent(context, BufferService.class);
			// Stop the buffer
			context.stopService(intent);

			// Replace this fragment with the StartBuffer Fragment.

			// Create a fragment transaction
			final FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			// Replace current fragment with a new RunningBuffer fragment
			transaction.replace(R.id.activity_main_container,
					new StartBufferFragment(context));

			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			// Commit the transaction
			transaction.commit();
		}
	};

	private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {

		}
	};

	/**
	 * Fragment for when the BufferService is running.
	 *
	 * @param context
	 */
	public RunningBufferFragment(final Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LocalBroadcastManager.getInstance(context).registerReceiver(
				mMessageReceiver, new IntentFilter(C.FILTER));
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_runningbuffer,
				container, false);

		rootView.findViewById(R.id.fragment_runningbuffer_stop)
				.setOnClickListener(stopBuffer);

		connectionCount = (TextView) rootView
				.findViewById(R.id.fragment_runningbuffer_connections);
		return rootView;
	}

	@Override
	public void onDestroy() {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(
				mMessageReceiver);
		super.onDestroy();
	}

	@Override
	public void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(context).registerReceiver(
				mMessageReceiver, new IntentFilter(C.FILTER));
	}

	@Override
	public void onStop() {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(
				mMessageReceiver);
		super.onStop();
	}

}
