package com.dcc.fieldtripbuffer;

import java.io.IOException;
import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import buffer_bci.javaserver.Buffer;

public class BufferService extends Service {
	private Buffer buffer;

	@Override
	public IBinder onBind(final Intent arg0) {
		// TODO
		return null;
	}

	@Override
	public void onDestroy() {
		System.out.println("Destroying BufferService...");
		try {
			buffer.closeConnection();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {
		// If no buffer is running.
		if (buffer == null) {
			final int port = intent.getIntExtra("port", 1972);
			// Create a buffer and start it.
			buffer = new Buffer(port, intent.getIntExtra("nSamples", 100),
					intent.getIntExtra("nEvents", 100));
			buffer.start();

			// Create Foreground notification

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

			// Turn this service into a foreground service
			startForeground(1, mBuilder.build());
		}
		return START_REDELIVER_INTENT;
	}
}
