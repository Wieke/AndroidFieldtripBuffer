package com.dcc.fieldtripthreads;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.dcc.fieldtripthreads.fragments.ThreadManagementFragment;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.activity_main_container,
							new ThreadManagementFragment()).commit();
		}
	}

}
