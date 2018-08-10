package com.mitrais.rms.dao;

import java.util.Optional;

import com.mitrais.rms.model.Setting;

public interface SettingDao extends Dao<Setting, String> {	
	
	/**
	 * Get value of a setting by its property name.
	 * @param propertyName
	 * @return Optional<Integer> 
	 */
	public Optional<Integer> getValueAsInt(String propertyName);	
}