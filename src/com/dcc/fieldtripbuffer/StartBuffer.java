package com.dcc.fieldtripbuffer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartBuffer extends Fragment {
	private final Context context;

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

	private void startBuffer() {
		final Intent intent = new Intent(context, BufferService.class);
		intent.putExtra("port", 1972);
		intent.putExtra("nSamples", 1972);
		intent.putExtra("nEvents", 1972);
		context.startService(intent);
	}
}