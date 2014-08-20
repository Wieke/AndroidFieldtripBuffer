package com.dcc.fieldtripthreads.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dcc.fieldtripthreads.C;
import com.dcc.fieldtripthreads.R;
import com.dcc.fieldtripthreads.thread.ThreadInfo;

import dalvik.system.DexFile;

public class ThreadManagementFragment extends Fragment {

	private SparseArray<ThreadInfo> threads = new SparseArray<ThreadInfo>();

	private final ArrayList<ThreadInfo> threadsArray = new ArrayList<ThreadInfo>();
	private ListView threadlist;

	private ThreadListAdapter adapter;

	private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			if (intent.getIntExtra(C.MESSAGE_TYPE, -1) == C.UPDATE) {
				final ThreadInfo[] clientInfo = (ThreadInfo[]) intent
						.getParcelableArrayExtra(C.THREAD_LIST);

				if (clientInfo != null) {
					Log.i(C.TAG, "Received Client Info.");
					updateClients(clientInfo);
				}

			}

		}
	};

	OnClickListener launchThread = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			// Create Intent for stopping the buffer.
			// final Intent intent = new Intent(getActivity(),
			// ThreadService.class);
			// Stop the buffer
			// getActivity().stopService(intent);
			if (isExternalStorageReadable()) {

				String jardir = "Fieldtrip Threads";
				DexFile df;
				try {
					df = new DexFile(getActivity().getPackageCodePath());
					for (Enumeration<String> iter = df.entries(); iter
							.hasMoreElements();) {
						Log.w(C.TAG, iter.nextElement());
					}
				} catch (IOException e) {
					Log.w(C.TAG, "IOEXCEPTION CAUGsadglj");
					e.printStackTrace();
				}

			} else {
				Resources res = getActivity().getResources();
				Toast.makeText(getActivity(),
						res.getString(R.string.no_external_storage),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				mMessageReceiver, new IntentFilter(C.FILTER));
		if (savedInstanceState != null) {
			threads = savedInstanceState
					.getSparseParcelableArray(C.THREAD_LIST);
		}
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.thread_manager,
				container, false);

		threadlist = (ListView) rootView.findViewById(R.id.threadlist);

		// If there is a saved instance we will immediatly update the view
		// elements
		if (savedInstanceState != null) {
			threads = savedInstanceState
					.getSparseParcelableArray(C.THREAD_LIST);
			for (int i = 0; i < threads.size(); i++) {
				threadsArray.add(threads.valueAt(i));
			}
		} else {
			// Sending a update request to the service.
			final Intent intent = new Intent(C.FILTER);
			intent.putExtra(C.MESSAGE_TYPE, C.UPDATE_REQUEST);
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
					intent);
		}

		adapter = new ThreadListAdapter(getActivity(),
				R.layout.thread_list_item, threadsArray);

		threadlist.setAdapter(adapter);

		((Button) rootView.findViewById(R.id.launchbutton))
				.setOnClickListener(launchThread);

		return rootView;
	}

	@Override
	public void onDestroy() {
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				mMessageReceiver);
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		outState.putSparseParcelableArray(C.THREAD_LIST, threads);
	}

	@Override
	public void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				mMessageReceiver, new IntentFilter(C.FILTER));
	}

	@Override
	public void onStop() {
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				mMessageReceiver);
		super.onStop();
	}

	public void updateClients(final ThreadInfo[] clientinfo) {
		for (final ThreadInfo client : clientinfo) {
			if (threads.get(client.threadID) == null) {
				threads.put(client.threadID, client);
				threadsArray.add(client);
			} else {
				threads.get(client.threadID).update(client);
			}
		}
		Log.i(C.TAG,
				"Updating Thread list " + Integer.toString(clientinfo.length));
		adapter.notifyDataSetChanged();
	}
}
