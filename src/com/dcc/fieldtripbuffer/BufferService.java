package com.dcc.fieldtripbuffer;

import java.util.Locale;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import buffer_bci.javaserver.Buffer;
import buffer_bci.javaserver.FieldtripBufferMonitor;

public class BufferService extends Service implements FieldtripBufferMonitor {

	private Buffer buffer;

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	/**
	 * Called when the service is stopped. Stops the buffer.
	 */
	@Override
	public void onDestroy() {
		buffer.stopBuffer();
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {
		Log.i(C.LOGTAG, "Buffer Service Running");
		// If no buffer is running.
		if (buffer == null) {

			// Start the buffer
			final int port = intent.getIntExtra("port", 1972);
			// Create a buffer and start it.
			buffer = new Buffer(port, intent.getIntExtra("nSamples", 100),
					intent.getIntExtra("nEvents", 100));
			buffer.addMonitor(this);
			buffer.start();
			Log.i(C.LOGTAG, "Buffer thread started.");

			// Create Foreground Notification

			// Get the currently used ip-adress
			final WifiManager wifiMan = (WifiManager) getSystemService(WIFI_SERVICE);
			final WifiInfo wifiInf = wifiMan.getConnectionInfo();
			final int ipAddress = wifiInf.getIpAddress();
			final String ip = String.format(Locale.ENGLISH, "%d.%d.%d.%d",
					ipAddress & 0xff, ipAddress >> 8 & 0xff,
					ipAddress >> 16 & 0xff, ipAddress >> 24 & 0xff);

			// Create notification text
			final Resources res = getResources();
			final String notification_text = String.format(
					res.getString(R.string.notification_text), ip + ":" + port);

			final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(res.getString(R.string.notification_title))
			.setContentText(notification_text);

			// Creates an intent for when the notification is clicked
			final Intent resultIntent = new Intent(this, MainActivity.class);

			// Creates a backstack so hitting back would return the user to the
			// home screen.
			final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(MainActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			final PendingIntent resultPendingIntent = stackBuilder
					.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);

			// Turn this service into a foreground service
			startForeground(1, mBuilder.build());
			Log.i(C.LOGTAG, "Fieldtrip Buffer Service moved to foreground.");
		}
		return START_NOT_STICKY;
	}

	@Override
	public void updateClientActivity(final int clientID, final long time) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CLIENT_CLOSED);
		intent.putExtra(C.DATA_CLIENTID, clientID);
		intent.putExtra(C.DATA_TIME, time);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "Client " + clientID + " active at " + time);
	}

	@Override
	public void updateConnectionClosed(final int clientID) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CLIENT_CLOSED);
		intent.putExtra(C.DATA_CLIENTID, clientID);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "Client " + clientID + " closed connection.");
	}

	@Override
	public void updateConnectionOpened(final int clientID, final String adress,
			final int count) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_CONNECTION_COUNT);
		intent.putExtra(C.DATA_COUNT, count);
		intent.putExtra(C.DATA_ADRESS, adress);
		intent.putExtra(C.DATA_CLIENTID, clientID);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "Client " + clientID + " opened connection at "
				+ adress + "\nCurrently there are " + count
				+ " open connections");
	}

	@Override
	public void updateDataFlushed() {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_EVENTS_FLUSHED);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "Events Flushed");
	}

	@Override
	public void updateEventCount(final int count) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_EVENT_COUNT);
		intent.putExtra(C.DATA_COUNT, count);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "Event Count currently " + count);
	}

	@Override
	public void updateHeader(final int dataType, final int fSample,
			final int nChannels) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_HEADER);
		intent.putExtra(C.DATA_DATATYPE, dataType);
		intent.putExtra(C.DATA_FSAMPLE, fSample);
		intent.putExtra(C.DATA_NCHANNELS, nChannels);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "New Header: dataType " + dataType + " fSample "
				+ fSample + " nCHannels " + nChannels);
	}

	@Override
	public void updateHeaderFlushed() {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_HEADER_FLUSHED);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "Header Flushed");
	}

	@Override
	public void updateSampleCount(final int count) {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_SAMPLE_COUNT);
		intent.putExtra(C.DATA_COUNT, count);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "Sample Count currently " + count);
	}

	@Override
	public void updateSamplesFlushed() {
		final Intent intent = new Intent(C.FILTER);
		intent.putExtra(C.UPDATE_TYPE, C.UPDATE_DATA_FLUSHED);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.i(C.LOGTAG, "Data Flushed");
	}
}
