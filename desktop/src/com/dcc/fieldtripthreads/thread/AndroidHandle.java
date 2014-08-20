package com.dcc.fieldtripthreads.thread;

import java.io.FileOutputStream;
import java.io.IOException;

public interface AndroidHandle {

	public FileOutputStream openFile(String path) throws IOException;

	public void toast(String message);

	public void toastLong(String message);

	public void updateStatus(String status);

}
