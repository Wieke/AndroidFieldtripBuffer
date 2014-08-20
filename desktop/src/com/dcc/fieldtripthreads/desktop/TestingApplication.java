package com.dcc.fieldtripthreads.desktop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.dcc.fieldtripthreads.thread.AndroidHandle;
import com.dcc.fieldtripthreads.thread.Argument;
import com.dcc.fieldtripthreads.thread.ThreadBase;

@SuppressWarnings({ "rawtypes" })
public class TestingApplication implements AndroidHandle {

	private static boolean getArgument(final Argument a) throws IOException {

		System.out.println(a.getDescription() + "(default: " + a.getDefault()
				+ "):");

		if (a.getType() == Argument.TYPE_RADIO
				|| a.getType() == Argument.TYPE_CHECK) {
			int n = 1;
			for (String o : a.getOptions()) {
				System.out.println(Integer.toString(n) + ". " + o);
				n = n + 1;
			}
		}

		String input = new BufferedReader(new InputStreamReader(System.in))
		.readLine();

		if (input.length() == 0) {
			a.validate();
			return true;
		}

		try {
			switch (a.getType()) {
			case Argument.TYPE_BOOLEAN:
				if (input.equalsIgnoreCase("yes")
						|| input.equalsIgnoreCase("y")) {
					a.setValue(true);
				} else if (input.equalsIgnoreCase("no")
						|| input.equalsIgnoreCase("n")) {
					a.setValue(false);
				} else {
					return false;
				}
				break;
			case Argument.TYPE_CHECK:
				break;
			case Argument.TYPE_DOUBLE:
				a.setValue(Double.parseDouble(input));
				break;
			case Argument.TYPE_RADIO:
			case Argument.TYPE_INTEGER:
				a.setValue(Integer.parseInt(input));
				break;
			case Argument.TYPE_STRING:
				a.setValue(input);
				break;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		a.validate();
		return true;

	}

	private static Class[] loadClasses(final String pathToJar) {
		ArrayList<Class> classes = new ArrayList<Class>();
		File libs = new File(pathToJar);
		try {

			for (String e : libs.list()) {
				URL[] urls = { new URL("jar:file:" + pathToJar + "/" + e + "!/") };
				URLClassLoader classLoader = URLClassLoader.newInstance(urls);
				Enumeration<JarEntry> entries = new JarFile("libs/" + e)
				.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					if (!entry.isDirectory()
							&& !entry.getName().endsWith(".class")) {
						continue;
					}
					String className = entry.getName().substring(0,
							entry.getName().length() - 6);
					className = className.replace('/', '.');
					Class c = classLoader.loadClass(className);
					Class superclass = c.getSuperclass();
					while (superclass != null) {
						if (superclass.getName() == "com.dcc.fieldtripthreads.thread.ThreadBase") {
							classes.add(c);
							break;
						}
						superclass = superclass.getSuperclass();
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("unchecked")
	public static void main(final String[] args) throws IOException,
	ClassNotFoundException {
		// DO DYNAMIC STUFF
		Class[] classes = loadClasses("libs");

		System.out.println("Choose one:");
		int n = 1;
		for (Class c : classes) {
			System.out.println(Integer.toString(n) + ". " + c.getName());
			n = n + 1;
		}

		n = -1;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		while (n < 0 || n >= classes.length) {
			String input = reader.readLine();

			try {
				n = Integer.parseInt(input) - 1;
			} catch (NumberFormatException e) {
			}

			if (n < 0 || n >= classes.length) {
				System.out.println("Chose one of the above (integer please).");
			}

		}

		try {
			Constructor constructorNoArgs = classes[n].getDeclaredConstructor();
			Constructor constructorWithArgs = classes[n]
					.getDeclaredConstructor(AndroidHandle.class,
							Argument[].class);

			ThreadBase thread = (ThreadBase) constructorNoArgs.newInstance();

			Argument[] arguments = thread.getArguments();
			boolean done;
			do {
				done = true;
				for (Argument a : arguments) {
					if (a.isInvalid()) {
						done = done && getArgument(a);
					}
				}
				if (done) {
					done = thread.validateArguments(arguments);
				}
			} while (!done);

			thread = (ThreadBase) constructorWithArgs.newInstance(
					new TestingApplication(), arguments);
			thread.mainloop();

		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public FileOutputStream openFile(final String path) throws IOException {
		return new FileOutputStream(path);
	}

	@Override
	public void toast(final String message) {
		System.out.println("SHORT POPUP " + message);
	}

	@Override
	public void toastLong(final String message) {
		System.out.println("LONG POPUP " + message);

	}

	@Override
	public void updateStatus(final String status) {
		System.out.println("STATUS UPDATE " + status);
	}

}
