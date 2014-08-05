package com.dcc.fieldtripbuffer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class StartBuffer extends Fragment {
	private final Context context;

	/**
	 * Fragment for starting the BufferService.
	 *
	 * @param context
	 */
	public StartBuffer(final Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_startbuffer,
				container, false);
		return rootView;
	}

	/**
	 * Called when you click the Start button.
	 */
	@SuppressWarnings("unused")
	private void startBuffer() {
		// Create Intent for starting the buffer.
		final Intent intent = new Intent(context, BufferService.class);

		// Grab port number and add to intent.
		final EditText editTextPort = (EditText) getView().findViewById(
				R.id.fragment_startbuffer_port);
		intent.putExtra("port",
				Integer.parseInt(editTextPort.getText().toString()));

		// Grab nSamples and add to intent.
		final EditText editTextnSamples = (EditText) getView().findViewById(
				R.id.fragment_startbuffer_nSamples);
		intent.putExtra("nSamples",
				Integer.parseInt(editTextnSamples.getText().toString()));

		// Grab nEvents and add to intent.
		final EditText editTextnEvents = (EditText) getView().findViewById(
				R.id.fragment_startbuffer_nEvents);
		intent.putExtra("nEvents",
				Integer.parseInt(editTextnEvents.getText().toString()));

		// Start the buffer.
		context.startService(intent);
	}
}