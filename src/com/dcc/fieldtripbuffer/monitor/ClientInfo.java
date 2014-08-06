package com.dcc.fieldtripbuffer.monitor;

import android.os.Parcel;
import android.os.Parcelable;

import com.dcc.fieldtripbuffer.C;

public class ClientInfo implements Parcelable {
	public final String adress;
	public final int clientID;
	public int samplesGotten = 0;
	public int samplesPut = 0;
	public int eventsGotten = 0;
	public int eventsPut = 0;
	public int lastActivity = 0;
	public int waitEvents = -1;
	public int waitSamples = -1;
	public int error = -1;
	public long timeLastActivity = 0;
	public long waitTimeout = -1;
	public boolean connected = true;
	public boolean changed = true;

	public static final Parcelable.Creator<ClientInfo> CREATOR = new Parcelable.Creator<ClientInfo>() {
		@Override
		public ClientInfo createFromParcel(final Parcel in) {
			return new ClientInfo(in);
		}

		@Override
		public ClientInfo[] newArray(final int size) {
			return new ClientInfo[size];
		}
	};

	private ClientInfo(final Parcel in) {
		final int[] integers = new int[9];
		final long[] longs = new long[2];

		adress = in.readString();
		in.readIntArray(integers);
		in.readLongArray(longs);
		connected = in.readInt() == 1;

		clientID = integers[0];
		samplesGotten = integers[1];
		samplesPut = integers[2];
		eventsGotten = integers[3];
		eventsPut = integers[4];
		lastActivity = integers[5];
		waitEvents = integers[6];
		waitSamples = integers[7];
		error = integers[8];

		timeLastActivity = longs[0];
		waitTimeout = longs[1];
	}

	public ClientInfo(final String adress, final int clientID, final long time) {
		this.adress = adress;
		this.clientID = clientID;
		timeLastActivity = time;
	}

	@Override
	public int describeContents() {
		return C.CLIENT_INFO_PARCEL;
	}

	@Override
	public void writeToParcel(final Parcel out, final int flags) {
		// Gathering data
		final int[] integers = new int[] { clientID, samplesGotten, samplesPut,
				eventsGotten, eventsPut, lastActivity, waitEvents, waitSamples,
				error };
		final long[] longs = new long[] { timeLastActivity, waitTimeout };

		// Write it to parcel
		out.writeString(adress);
		out.writeIntArray(integers);
		out.writeLongArray(longs);
		out.writeInt(connected ? 1 : 0);
	}
}