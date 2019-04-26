package org.lp00579.www;

public class comboItem {
	private String key;
	private String value;

	public comboItem(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return key;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}