AndroidFieldtripBuffer
======================

An Android implementation of the Fieldtrip buffer, based on the [JavaFieldTripBuffer](https://github.com/Wieke/JavaFieldtripBuffer).

Development
=============

Set the development environment up as follows:

1. Set up eclipse with the following:
	- EGit
	- Android Development Tools
	- Android SDK 4.4W (API Level 20)
	- Android Support libraries
2. Import the project in this repository using EGit.
3. Import the [JavaFieldtripBuffer](https://github.com/Wieke/JavaFieldtripBuffer) project using EGit.
4. Import the appcompat_v7 project from <SDKPATH>/extras/android/support/v7/appcompat.
5. If the android project has build issues check the following:
	- FieldtripBuffer > Properties > Android should have appcompat_v7 as a project library (the path may be incorrect, if so remove and add it).
	- FieldtripBuffer > Properties > Java Build Path > Projects should contain JavaFieldtripBuffer.
	- FieldtripBuffer > Properties > Java Build Path > Order and Export should have ticked the box in front of JavaFieldtripBuffer.

Plan
==================

- [ ] Create a service activity
	- [ ] Ensure it keeps running even if the launcher app has been closed/destroyed
	- [ ] Apply performance optimizations to JavaFieldtripBuffer
- [ ] Create a launcher app.
	- [ ] Start Buffer activity with portNumber and buffer size fields.
	- [ ] Show/check if buffer is already running.
- [ ] Optional stuff: add some basic monitoring things.
	- [ ] Uptime
	- [ ] Number of open connections
		- [ ] List of open connections
			- [ ] Last time of activity of each connection
	- [ ] Number of samples
	- [ ] Number of events
	- [ ] Basic Header information (dataType, number of channels, sampling frequency
		- [ ] Channel names
	- [ ] List of last X events
	- [ ] List of last X samples