package com.mitrais.rms.model;

public class Setting {
	private String propertyName;
	private String value;
	
	public Setting(String propertyName, String value) {
		this.setPropertyName(propertyName);
		this.setValue(value);
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
