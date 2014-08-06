package com.dcc.fieldtripbuffer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import buffer_bci.javaserver.FieldtripBufferMonitor;

class BufferMonitor extends Thread implements FieldtripBufferMonitor {

	private boolean run;
	private final Context context;

	private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			if (intent.getIntExtra(C.UPDATE_TYPE, -1) == C.UPDATE_REQUEST) {
				sendRequestedUpdate();
			}
		}

	};

	public BufferMonitor(final Context context) {
		this.context = context;
		LocalBroadcastManager.getInstance(context).registerReceiver(
				mMessageReceiver, new IntentFilter(C.FILTER));
	}

	@Override
	public void run() {
		while (run) {

		}
	}

	private void sendRequestedUpdate() {
		// TODO Auto-generated method stub

	}

	public void stopMonitoring() {
		run = false;
		LocalBroadcastManager.getInstance(context).unregisterReceiver(
				mMessageReceiver);
	}

	@Override
	public void updateClientActivity(final int clientID, final long time) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CLIENT_CLOSED);
		intent.putExtra(C.DATA_CLIENTID, clientID);
		intent.putExtra(C.DATA_TIME, time);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "Client " + clientID + " active at " + time);
	}

	@Override
	public void updateClientError(final int clientID, final int errorType,
			final long time) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.DATA_CLIENTID, clientID);
		intent.putExtra(C.DATA_TIME, time);

		if (errorType == FieldtripBufferMonitor.ERROR_PROTOCOL) {
			intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CLIENT_ERROR_PROTOCOL);
			Log.i(C.TAG, "Client " + clientID
					+ " violates network protocol at " + time + " .");
		} else if (errorType == FieldtripBufferMonitor.ERROR_VERSION) {
			intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CLIENT_ERROR_VERSION);
			Log.i(C.TAG, "Client " + clientID + " has wrong version " + time
					+ " .");
		} else {
			intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CLIENT_ERROR_CONNECTION);
			Log.i(C.TAG, "Client " + clientID
					+ " disconnected unexpectidly at " + time + " .");
		}

		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	@Override
	public void updateConnectionClosed(final int clientID) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CLIENT_CLOSED);
		intent.putExtra(C.DATA_CLIENTID, clientID);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "Client " + clientID + " closed connection.");
	}

	@Override
	public void updateConnectionOpened(final int clientID, final String adress) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CONNECTION_COUNT);
		intent.putExtra(C.DATA_ADRESS, adress);
		intent.putExtra(C.DATA_CLIENTID, clientID);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "Client " + clientID + " opened connection at " + adress);
	}

	@Override
	public void updateDataFlushed() {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_DATA_FLUSHED);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "Events Flushed");
	}

	@Override
	public void updateEventCount(final int count) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_EVENT_COUNT);
		intent.putExtra(C.DATA_COUNT, count);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "Event Count currently " + count);
	}

	@Override
	public void updateEventsFlushed() {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_EVENTS_FLUSHED);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "Events Flushed");
	}

	@Override
	public void updateHeader(final int dataType, final float fSample,
			final int nChannels) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_HEADER);
		intent.putExtra(C.DATA_DATATYPE, dataType);
		intent.putExtra(C.DATA_FSAMPLE, fSample);
		intent.putExtra(C.DATA_NCHANNELS, nChannels);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "New Header: dataType " + dataType + " fSample " + fSample
				+ " nCHannels " + nChannels);
	}

	@Override
	public void updateHeaderFlushed() {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_HEADER_FLUSHED);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "Header Flushed");
	}

	@Override
	public void updateSampleCount(final int count) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_SAMPLE_COUNT);
		intent.putExtra(C.DATA_COUNT, count);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		Log.i(C.TAG, "Sample Count currently " + count);
	}
}