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

import com.dcc.fieldtripbuffer.R;
import com.dcc.fieldtripbuffer.services.BufferService;

public class RunningBufferFragment extends Fragment {
	private final Context context;
	public static final String FILTER = "com.dcc.fieldtripbuffer.RunningBufferFragment.filter";

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

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
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
		LocalBroadcastManager.getInstance(context).registerReceiver(receiver,
				new IntentFilter(FILTER));
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_runningbuffer,
				container, false);

		rootView.findViewById(R.id.fragment_runningbuffer_stop)
				.setOnClickListener(stopBuffer);

		return rootView;
	}

}
