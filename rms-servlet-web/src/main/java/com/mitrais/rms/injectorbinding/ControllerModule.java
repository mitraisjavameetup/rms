package com.mitrais.rms.injectorbinding;

import com.google.inject.AbstractModule;
import com.mitrais.rms.connection.DatabaseConnection;
import com.mitrais.rms.connection.SqliteConnection;

public class ControllerModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(DatabaseConnection.class).to(SqliteConnection.class);
	}
}
