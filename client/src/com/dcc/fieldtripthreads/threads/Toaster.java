package com.dcc.fieldtripthreads.threads;

import java.io.IOException;
import java.io.PrintWriter;

import nl.fcdonders.fieldtrip.BufferClient;
import nl.fcdonders.fieldtrip.BufferEvent;
import nl.fcdonders.fieldtrip.Header;
import nl.fcdonders.fieldtrip.SamplesEventsCount;

import com.dcc.fieldtripthreads.base.AndroidHandle;
import com.dcc.fieldtripthreads.base.Argument;
import com.dcc.fieldtripthreads.base.ThreadBase;

public class Toaster extends ThreadBase {
	private String adress;
	private int port;
	private String eventType;
	private boolean longMessage;
	private boolean run = true;
	private final BufferClient client = new BufferClient();
	private Integer timeout;
	private String path;

	public Toaster() {
	}

	public Toaster(final AndroidHandle android, final Argument[] arguments) {
		super(android, arguments);
		final String[] split = arguments[0].getString().split(":");
		adress = split[0];
		port = Integer.parseInt(split[1]);
		eventType = arguments[1].getString();
		longMessage = arguments[2].getSelected() == 1;
		timeout = arguments[3].getInteger();
		path = arguments[4].getString();
	}

	@Override
	public Argument[] getArguments() {
		final Argument[] arguments = new Argument[5];

		arguments[0] = new Argument("Buffer Adress", "localhost:1972");

		arguments[1] = new Argument(
				"Triggering event type (value of the event will be shown in popup)",
				"Toaster.toast");

		final String[] options = { "long", "short" };
		arguments[2] = new Argument("Toast duration", 0, options);
		arguments[3] = new Argument("Timeout", 500, false);

		arguments[4] = new Argument("File Path", "toastlist");
		return arguments;
	}

	@Override
	public String getName() {
		return "Toaster";
	}

	@Override
	public void mainloop() {
		run = true;
		try {
			if (!connect(client, adress, port)) {
				android.updateStatus("Could not connect to buffer.");
				run = false;
				return;
			}

			android.updateStatus("Waiting for events.");

			Header hdr = client.getHeader();

			PrintWriter floor = new PrintWriter(android.openWriteFile(path));
			int nEventsOld = hdr.nEvents;

			while (run) {

				SamplesEventsCount count = client.waitForEvents(nEventsOld,
						timeout);

				if (count.nEvents != nEventsOld) {
					BufferEvent[] events = client.getEvents(nEventsOld,
							count.nEvents - 1);
					for (BufferEvent e : events) {
						if (e.getType().toString().contentEquals(eventType)) {
							if (longMessage) {
								android.toastLong(e.getValue().toString());
							} else {
								android.toast(e.getValue().toString());
							}
							android.updateStatus("Last toast: "
									+ e.getValue().toString());
							floor.write(e.getValue().toString() + "\n");
							floor.flush();
						}
					}

					nEventsOld = count.nEvents;
				}

			}
		} catch (final IOException e) {
			android.updateStatus("IOException caught, stopping.");
		} catch (final InterruptedException e) {
			android.updateStatus("InterruptException caught, stopping.");
		}
	}

	@Override
	public void pause() {
		run = false;
	}

	@Override
	public void stop() {
		run = false;
		try {
			client.disconnect();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void validateArguments(final Argument[] arguments) {
		final String adress = arguments[0].getString();

		try {
			final String[] split = adress.split(":");
			arguments[0].validate();
			try {
				Integer.parseInt(split[1]);
			} catch (final NumberFormatException e) {
				arguments[0].invalidate("Wrong adress format.");
			}

		} catch (final ArrayIndexOutOfBoundsException e) {
			arguments[0].invalidate("Integer expected after colon.");
		}

	}
}
