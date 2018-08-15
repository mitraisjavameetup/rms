package com.mitrais.rms.dao.impl;

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
import org.junit.Test;

import com.mitrais.rms.connection.DatabaseConnection;
import com.mitrais.rms.connection.SqliteConnection;
import com.mitrais.rms.dao.SettingDao;
import com.mitrais.rms.model.Setting;

public class SettingDaoImplTest {
	private DatabaseConnection databaseConnection = null;
	private SettingDao settingDao = null;

	@Before
	public void beforeTest() {
		databaseConnection = new SqliteConnection(true);
		prepareMemoryDbForTesting();
		settingDao = new SettingDaoImpl(databaseConnection);
	}

	private void prepareMemoryDbForTesting() {
		try {
			Connection connection = databaseConnection.getConnection();

			String createSettingQuery = "CREATE TABLE IF NOT EXISTS \"SETTING\" (" 
					+ "	PROPERTY_NAME VARCHAR(100) PRIMARY KEY NOT NULL, "
					+ "	VALUE VARCHAR(250) NULL, " 
					+ "	DELETED BIT DEFAULT O NOT NULL);";
			String insertSettingQuery = "INSERT INTO SETTING VALUES ('MAX_USER_ID', 1, 0);	";
			
			PreparedStatement createSettingStatement = connection.prepareStatement(createSettingQuery);
			createSettingStatement.executeUpdate();
			createSettingStatement.close();
			
			PreparedStatement insertSettingStatement = connection.prepareStatement(insertSettingQuery);
			insertSettingStatement.executeUpdate();			
			insertSettingStatement.close();			
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
	public void testFindAllPositive() {
		List<Setting> settings = settingDao.findAll();
		assertEquals(1, settings.size());
	}
	
	@Test
	public void testFindAllEmptyData() {
		emptySettingTable();
		List<Setting> settings = settingDao.findAll();
		assertEquals(0, settings.size());
	}
	
	private void emptySettingTable() {
		try {
			Connection connection = databaseConnection.getConnection();
	
			String truncateSettingQuery = "DELETE FROM SETTING";
			
			PreparedStatement insertSettingStatement = connection.prepareStatement(truncateSettingQuery);
			insertSettingStatement.executeUpdate();						
			insertSettingStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindPositive() {
		Optional<Setting> setting = settingDao.find("MAX_USER_ID");
		assertTrue(setting.isPresent());
		assertEquals("1", setting.get().getValue());
	}
	
	@Test
	public void testFindNotRegisteredProperty() {
		Optional<Setting> setting = settingDao.find("NOT_REGISTERED");
		assertFalse(setting.isPresent());
	}
	
	@Test
	public void testSaveRegisteredProperty() {
		String userIdProperty = "MAX_USER_ID";
		
		Setting setting = new Setting(userIdProperty, "2");
		Boolean saveSetting = settingDao.save(setting);
		
		assertTrue(saveSetting);
		
		Optional<Setting> settingFromDb = settingDao.find(userIdProperty);
		List<Setting> allSetting = settingDao.findAll();
		
		assertEquals(1, allSetting.size());
		assertTrue(settingFromDb.isPresent());
		assertEquals(setting.getValue(), settingFromDb.get().getValue());
	}
	
	@Test
	public void testSaveNonRegisteredProperty() {
		String userIdProperty = "NON_REGISTERED";
		
		Setting setting = new Setting(userIdProperty, "abc");
		boolean saveSetting = settingDao.save(setting);
		
		assertTrue(saveSetting);
		
		Optional<Setting> settingFromDb = settingDao.find(userIdProperty);
		List<Setting> allSetting = settingDao.findAll();
		
		assertEquals(2, allSetting.size());
		assertTrue(settingFromDb.isPresent());
		assertEquals(setting.getValue(), settingFromDb.get().getValue());
	}

	@Test
	public void testDeletePositive() {
		String userIdProperty = "MAX_USER_ID";
		Setting setting = new Setting(userIdProperty, "1");
		
		boolean deleteSetting = settingDao.delete(setting);		
		assertTrue(deleteSetting);
		
		List<Setting> allSetting = settingDao.findAll();
		assertEquals(0, allSetting.size());
	}
	
	@Test
	public void testDeleteNonRegisteredProperty() {
		String userIdProperty = "NON_REGISTERED";
		Setting setting = new Setting(userIdProperty, "1");
		
		boolean deleteSetting = settingDao.delete(setting);		
		assertFalse(deleteSetting);
		
		List<Setting> allSetting = settingDao.findAll();
		assertEquals(1, allSetting.size());
	}
	
	@Test
	public void testGetValueAsIntPositive() {
		String userIdProperty = "MAX_USER_ID";
		
		Optional<Integer> value = settingDao.getValueAsInt(userIdProperty);
		assertTrue(value.isPresent());
		assertEquals(1, 1);
	}
	
	@Test
	public void testGetValueAsIntValueIsNotNumber() {
		String userIdProperty = "NON_REGISTERED";
		
		testSaveNonRegisteredProperty();
		
		Optional<Integer> value = settingDao.getValueAsInt(userIdProperty);
		assertFalse(value.isPresent());
	}
}
