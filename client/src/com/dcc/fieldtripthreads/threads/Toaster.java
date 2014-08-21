package com.dcc.fieldtripthreads.threads;

import java.io.IOException;
import java.io.PrintWriter;

import nl.fcdonders.fieldtrip.BufferClient;
import nl.fcdonders.fieldtrip.BufferEvent;
import nl.fcdonders.fieldtrip.Header;
import nl.fcdonders.fieldtrip.SamplesEventsCount;

import com.dcc.fieldtripthreads.base.Argument;
import com.dcc.fieldtripthreads.base.ThreadBase;

public class Toaster extends ThreadBase {
	private BufferClient client;

	/**
	 * Is used by the android app to determine what kind of arguments the thread
	 * requires.
	 */
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

	/**
	 * Is used by the android app to determine the name of the Class.
	 */
	@Override
	public String getName() {
		return "Toaster";
	}

	/**
	 * Is called from within the public void run() method of a Thread object.
	 *
	 * Before the mainloop is called, the arguments and android variables are
	 * set through functions defined in ThreadBase.
	 *
	 */
	@Override
	public void mainloop() {

		/**
		 * While the validate arguments function may have already parsed some of
		 * the arguments. It necessarily needs to happen here again because
		 * mainloop() and validateArguments() will not be called on the same
		 * instance of this class.
		 */
		final String[] split = arguments[0].getString().split(":");
		String adress = split[0];
		int port = Integer.parseInt(split[1]);
		String eventType = arguments[1].getString();
		boolean longMessage = arguments[2].getSelected() == 1;
		Integer timeout = arguments[3].getInteger();
		String path = arguments[4].getString();
		run = true;

		try {
			/**
			 * connect() is a convenience function defined in ThreadBase. It
			 * connects to the buffer if able and waits for the header. Returns
			 * false if the buffer could not be reached at the specified
			 * adress/port.
			 */
			if (!connect(client, adress, port)) {
				android.updateStatus("Could not connect to buffer.");
				run = false;
				return;
			}

			/**
			 * The status message will be shown in the list of threads in the
			 * app.
			 */
			android.updateStatus("Waiting for events.");

			Header hdr = client.getHeader();

			/**
			 * The openWriteFile() and openReadFile() functions can be used to
			 * access files on the device's external storage (usually the
			 * sdcard).
			 */
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
							/**
							 * The small feedback popups that are sometimes
							 * shown at the botter/center of the screen on
							 * android devices are called toast. Calling the
							 * toast() or toastLong() methods will create such a
							 * popup.
							 *
							 */
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

	/**
	 * Called when the thread needs to stop. (When the stop threads button is
	 * pressed in the app.) Is overriden so the buffer connection can be closed
	 * neatly.
	 */
	@Override
	public void stop() {
		super.stop();
		try {
			client.disconnect();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Used by the android app to determine if the arguments given by the user
	 * are okay. If an argument is wrong, call the invalidate() method with some
	 * kind reason for the invalidation in as the argument, this message will be
	 * shown in red next to the input fields.
	 */
	@Override
	public void validateArguments(final Argument[] arguments) {
		final String adress = arguments[0].getString();

		try {
			final String[] split = adress.split(":");
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
