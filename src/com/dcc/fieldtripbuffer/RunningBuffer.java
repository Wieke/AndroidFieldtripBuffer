package com.dcc.fieldtripbuffer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
		final View rootView = inflater.inflate(R.layout.fragment_startbuffer,
				container, false);

		return rootView;
	}
}
