package com.dcc.fieldtripthreads.base;

import java.io.IOException;

import nl.fcdonders.fieldtrip.BufferClient;
import nl.fcdonders.fieldtrip.Header;

public abstract class ThreadBase {
	protected AndroidHandle android;
	protected Argument[] arguments;

	public ThreadBase() {
	};

	public ThreadBase(final AndroidHandle android, final Argument[] arguments) {
		this.android = android;
		this.arguments = arguments;
	}

	protected boolean connect(final BufferClient client, final String adress,
			final int port) throws IOException, InterruptedException {
		if (!client.isConnected()) {
			client.connect(adress, port);
			android.updateStatus("Waiting for header.");
		} else {
			return false;
		}
		Header hdr = null;
		do {
			try {
				hdr = client.getHeader();
			} catch (IOException e) {
				if (!e.getMessage().contains("517")) {
					throw e;
				}
				Thread.sleep(1000);
			}
		} while (hdr == null);
		return true;
	}

	public abstract Argument[] getArguments();

	public abstract String getName();

	public abstract void mainloop();

	public abstract void pause();;

	public abstract void stop();

	public abstract void validateArguments(final Argument[] arguments);

}
