package com.dcc.fieldtripthreads.thread;

import java.io.Serializable;

public class Argument implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5926084016803995901L;

	public final static int TYPE_INTEGER = 0;
	public final static int TYPE_DOUBLE = 1;
	public final static int TYPE_BOOLEAN = 2;
	public final static int TYPE_STRING = 3;
	public final static int TYPE_RADIO = 4;
	public final static int TYPE_CHECK = 5;

	private String description;
	private int valueInteger;
	private double valueDouble;
	private boolean valueBoolean;
	private String valueString;
	private int[] valueCheck;
	private final int type;
	private boolean valid = false;
	private String invalidationMessage;
	private String[] options;

	public Argument(final String description, final boolean value) {
		setDescription(description);
		type = TYPE_BOOLEAN;
		setValue(value);
	}

	public Argument(final String description, final double value) {
		setDescription(description);
		type = TYPE_DOUBLE;
		setValue(value);
	}

	public Argument(final String description, final int value) {
		setDescription(description);
		type = TYPE_INTEGER;
		setValue(value);
	}

	public Argument(final String description, final int value,
			final String[] options) {
		setDescription(description);
		type = TYPE_RADIO;
		setOptions(options);
		setValue(value);
	}

	public Argument(final String description, final int[] value,
			final String[] options) {
		setDescription(description);
		type = TYPE_CHECK;
		setOptions(options);
		setValue(value);
	}

	public Argument(final String description, final String value) {
		setDescription(description);
		type = TYPE_STRING;
		setValue(value);
	}

	public Boolean getBoolean() {
		if (type == TYPE_BOOLEAN) {
			return valueBoolean;
		} else {
			return null;
		}
	}

	public int[] getChecked() {
		if (type == TYPE_CHECK) {
			return valueCheck;
		} else {
			return null;
		}
	}

	public String getDefault() {
		switch (type) {
		case TYPE_RADIO:
		case TYPE_INTEGER:
			return Integer.toString(valueInteger);
		case TYPE_DOUBLE:
			return Double.toString(valueDouble);
		case TYPE_BOOLEAN:
			return Boolean.toString(valueBoolean);
		case TYPE_STRING:
			return valueString;
		case TYPE_CHECK:
			if (valueCheck.length > 0) {
				StringBuilder string = new StringBuilder();
				for (int i : valueCheck) {
					string.append(i + ", ");
				}
				return string.substring(0, string.length() - 3);
			}
		}
		return "";
	}

	public String getDescription() {
		return description;
	}

	public Double getDouble() {
		if (type == TYPE_DOUBLE) {
			return valueDouble;
		} else {
			return null;
		}
	}

	public Integer getInteger() {
		if (type == TYPE_INTEGER) {
			return valueInteger;
		} else {
			return null;
		}
	}

	public String getInvalidationMessage() {
		return invalidationMessage;
	}

	public String[] getOptions() {
		return options;
	}

	public Integer getSelected() {
		if (type == TYPE_RADIO) {
			return valueInteger;
		} else {
			return null;
		}
	}

	public String getString() {
		if (type == TYPE_STRING) {
			return valueString;
		} else {
			return null;
		}
	}

	public int getType() {
		return type;
	}

	public void invalidate(final String message) {
		valid = false;
		setInvalidationMessage(message);
	}

	public boolean isInvalid() {
		return !valid;
	}

	public boolean isValid() {
		return valid;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setInvalidationMessage(final String message) {
		invalidationMessage = message;
	}

	public void setOptions(final String[] options) {
		this.options = options;
	}

	public void setValue(final boolean value) {
		valueBoolean = value;
	}

	public void setValue(final double value) {
		valueDouble = value;
	}

	public void setValue(final int value) {
		valueInteger = value;
	}

	public void setValue(final int[] value) {
		valueCheck = value;
	}

	public void setValue(final String value) {
		valueString = value;
	}

	public void validate() {
		valid = true;
	}
}