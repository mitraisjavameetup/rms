package com.mitrais.rms.dbInitializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mitrais.rms.connection.DatabaseConnection;
import com.mitrais.rms.connection.SqliteConnection;
import com.mitrais.rms.dao.SettingDao;
import com.mitrais.rms.dao.impl.SettingDaoImpl;
import com.mitrais.rms.model.Setting;

/**
 * This initializer should only run once.
 * Make sure there's no rmsdb.db file inside rms\src\main\sqlite
 * because this test will create rmsdb.db along with the initial data.
 * Please uncomment annotation @Ignore after first run.
 * @author Astri_A332
 *
 */
@Ignore
public class SqliteInitializer {
	private DatabaseConnection databaseConnection = null;
	private SettingDao settingDao = null;

	@Before
	public void beforeTest() {
		databaseConnection = new SqliteConnection(false);
		prepareMemoryDbForTesting();
		settingDao = new SettingDaoImpl(databaseConnection);
	}

	private void prepareMemoryDbForTesting() {
		try {
			Connection connection = databaseConnection.getConnection();

			String createUserQuery = "CREATE TABLE IF NOT EXISTS USER (" 
					+ "	ID BIGINT PRIMARY KEY NOT NULL, "
					+ "	USERNAME VARCHAR(100) NOT NULL, " 
					+ "	PASSWORD VARCHAR(100) NOT NULL, "
					+ "	LAST_LOGIN DATETIME NULL, "
					+ "	DELETED BIT NOT NULL DEFAULT 0);";			
			
			String insertUserQuery = "INSERT INTO USER (ID, USERNAME, PASSWORD, DELETED) VALUES (1, 'admin_rms', 'admin', 0), "
					+ "(2, 'user', 'user', 0);";
			
			String createSettingQuery = "CREATE TABLE IF NOT EXISTS \"SETTING\" (" 
					+ "	PROPERTY_NAME VARCHAR(100) PRIMARY KEY NOT NULL, "
					+ "	VALUE VARCHAR(250) NULL, " 
					+ "	DELETED BIT DEFAULT O NOT NULL);";
			
			String insertSettingQuery = "INSERT INTO SETTING VALUES ('MAX_USER_ID', 2, 0);	";

			PreparedStatement createUserStatement = connection.prepareStatement(createUserQuery);
			createUserStatement.executeUpdate();
			
			PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery);
			insertUserStatement.executeUpdate();
			
			PreparedStatement createSettingStatement = connection.prepareStatement(createSettingQuery);
			createSettingStatement.executeUpdate();
			
			PreparedStatement insertSettingStatement = connection.prepareStatement(insertSettingQuery);
			insertSettingStatement.executeUpdate();			
			
			createUserStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void afterTest() {		
		try {
			settingDao.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@Test
	public void initialize() {
		List<Setting> settings = settingDao.findAll();
		assertEquals(1, settings.size());
	}
}
