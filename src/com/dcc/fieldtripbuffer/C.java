package com.dcc.fieldtripbuffer;

/**
 * Class containing all the constants of this project.
 *
 * @author wieke
 *
 */
public final class C {
	public static final String FILTER = "com.dcc.fieldtripbuffer.RunningBufferFragment.filter";
	public static final String DATA_COUNT = "count";
	public static final String DATA_CLIENTID = "clientid";
	public static final String DATA_TIME = "time";
	public static final String DATA_ADRESS = "adress";
	public static final String DATA_DATATYPE = "dataType";
	public static final String DATA_FSAMPLE = "fSample";
	public static final String DATA_NCHANNELS = "nChannels";

	public static final String UPDATE_TYPE = "update_type";

	public static final int UPDATE_CLIENT_ACTIVITY = 0;
	public static final int UPDATE_CONNECTION_COUNT = 1;
	public static final int UPDATE_EVENT_COUNT = 2;
	public static final int UPDATE_HEADER = 3;
	public static final int UPDATE_SAMPLE_COUNT = 4;
	public static final int UPDATE_CLIENT_CLOSED = 5;
	public static final int UPDATE_DATA_FLUSHED = 6;
	public static final int UPDATE_EVENTS_FLUSHED = 7;
	public static final int UPDATE_HEADER_FLUSHED = 8;
	public static final int UPDATE_CLIENT_ERROR_PROTOCOL = 9;
	public static final int UPDATE_CLIENT_ERROR_CONNECTION = 10;
	public static final int UPDATE_CLIENT_ERROR_VERSION = 11;
	public static final int UPDATE_REQUEST = 12;

	public static final String TAG = "fieldtripbuffer";
}
