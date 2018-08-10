package com.mitrais.rms.dao.impl;

import com.mitrais.rms.connection.DatabaseConnection;

public class BaseDaoImpl implements AutoCloseable {
	protected DatabaseConnection databaseConnection;
	
	public BaseDaoImpl(DatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}
	
	@Override
	public void close() throws Exception {
		if (databaseConnection != null) {
			databaseConnection.close();
		}
	}
}
