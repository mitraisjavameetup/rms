package com.mitrais.rms.connection;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.sqlite.SQLiteDataSource;

public class SqliteConnection implements DatabaseConnection {
	private DataSource dataSource;
	private Connection connection;

	public SqliteConnection() {
		this(false);
	}

	public SqliteConnection(boolean isForTest) {
		// TODO Auto-generated constructor stub
		SQLiteDataSource dataSource = new SQLiteDataSource();

		if (isForTest) {
			dataSource.setUrl("jdbc:sqlite:");
		} else {
			// sqlite path
			String mainAppPath = new File("").getAbsolutePath().toString().replaceFirst("rms-servlet-web", "");
			Path sbFilePath = Paths.get(mainAppPath, "\\src\\main\\sqlite", "rmsdb.db");
			dataSource.setUrl("jdbc:sqlite://" + sbFilePath.toAbsolutePath());
		}
		this.dataSource = dataSource;
	}

	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = this.dataSource.getConnection();
		}

		return connection;
	}

	@Override
	public void close() throws Exception {
		if (connection != null) {
			connection.close();
		}
	}
}
