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
import buffer_bci.javaserver.Buffer;

public class BufferService extends Service {
	private Buffer buffer;
	public String adress;

	@Override
	public IBinder onBind(final Intent arg0) {
		// TODO
		return null;
	}

	@Override
	public void onDestroy() {
		buffer.stopBuffer();
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {

		// If no buffer is running.
		if (buffer == null) {

			// Start the buffer
			final int port = intent.getIntExtra("port", 1972);
			// Create a buffer and start it.
			buffer = new Buffer(port, intent.getIntExtra("nSamples", 100),
					intent.getIntExtra("nEvents", 100));
			buffer.start();

			// Create Foreground Notification

			// Get the currently used ip-adress
			final WifiManager wifiMan = (WifiManager) getSystemService(WIFI_SERVICE);
			final WifiInfo wifiInf = wifiMan.getConnectionInfo();
			final int ipAddress = wifiInf.getIpAddress();
			final String ip = String.format(Locale.ENGLISH, "%d.%d.%d.%d",
					ipAddress & 0xff, ipAddress >> 8 & 0xff,
					ipAddress >> 16 & 0xff, ipAddress >> 24 & 0xff);

			adress = ip + ":" + port;

			// Create notification text
			final Resources res = getResources();
			final String notification_text = String.format(
					res.getString(R.string.notification_text), adress);

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
		}
		return START_NOT_STICKY;
	}
}
