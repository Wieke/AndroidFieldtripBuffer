package com.dcc.fieldtripthreads;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ThreadService extends Service {

	private WakeLock wakeLock;
	private WifiLock wifiLock;

	private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			switch (intent.getIntExtra(C.MESSAGE_TYPE, -1)) {

			default:
			}
		}

	};

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	/**
	 * Called when the service is stopped. Stops the buffer.
	 */
	@Override
	public void onDestroy() {
		Log.i(C.TAG, "Stopping Thread Service.");

		if (wakeLock != null) {
			wakeLock.release();
		}
		if (wifiLock != null) {
			wifiLock.release();
		}
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mMessageReceiver);
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {
		Log.i(C.TAG, "Buffer Thread Running");
		// If no buffer is running.
		if (wakeLock == null && wifiLock == null) {

			final int port = intent.getIntExtra("port", 1972);
			// Get Wakelocks

			final PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					C.WAKELOCKTAG);
			wakeLock.acquire();

			final WifiManager wifiMan = (WifiManager) getSystemService(WIFI_SERVICE);
			wifiLock = wifiMan.createWifiLock(C.WAKELOCKTAGWIFI);
			wifiLock.acquire();

			// Create Foreground Notification

			// Create notification text
			final Resources res = getResources();
			final String notification_text = res
					.getString(R.string.notification_text);

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
			Log.i(C.TAG, "Fieldtrip Thread Service moved to foreground.");

			// Add message listener
			LocalBroadcastManager.getInstance(this).registerReceiver(
					mMessageReceiver, new IntentFilter(C.FILTER));
		}
		return START_NOT_STICKY;
	}
}
