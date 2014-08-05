package com.dcc.fieldtripbuffer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class RunningBuffer extends Fragment {
	private final Context context;

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
			transaction.replace(R.id.activity_main_container, new StartBuffer(
					context));
			// Don't add anything to the backstack (so hitting back won't return
			// the user to the startbuffer screen).
			transaction.addToBackStack(null);

			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			// Commit the transaction
			transaction.commit();
		}
	};

	/**
	 * Fragment for when the BufferService is running.
	 *
	 * @param context
	 */
	public RunningBuffer(final Context context) {
		this.context = context;
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
