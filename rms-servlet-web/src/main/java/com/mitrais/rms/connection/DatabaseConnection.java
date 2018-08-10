package com.mitrais.rms.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnection extends AutoCloseable {
	Connection getConnection() throws SQLException;
}
