package com.mitrais.rms.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mitrais.rms.connection.DatabaseConnection;
import com.mitrais.rms.connection.SqliteConnection;
import com.mitrais.rms.dao.SettingDao;
import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.model.Setting;
import com.mitrais.rms.model.User;

public class UserDaoImplTest {
	private DatabaseConnection databaseConnection = null;
	private UserDao userDao = null;
	private SettingDao settingDao = null;

	@Before
	public void beforeTest() {
		databaseConnection = new SqliteConnection(true);
		prepareMemoryDbForTesting();
		userDao = new UserDaoImpl(databaseConnection);
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
			
			String insertUserQuery = "INSERT INTO USER (ID, USERNAME, PASSWORD, DELETED) VALUES (1, 'admin_rms', 'admin', 0);";
			
			String createSettingQuery = "CREATE TABLE IF NOT EXISTS \"SETTING\" (" 
					+ "	PROPERTY_NAME VARCHAR(100) PRIMARY KEY NOT NULL, "
					+ "	VALUE VARCHAR(250) NULL, " 
					+ "	DELETED BIT DEFAULT O NOT NULL);";
			
			String insertSettingQuery = "INSERT INTO SETTING VALUES ('MAX_USER_ID', 1, 0);	";

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
			userDao.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindAllPositive() {
		List<User> users = userDao.findAll();
		assertEquals(1, users.size());
	}
	
	@Test
	public void testFindInEmptyData() {
		emptyUserTable();
		
		List<User> users = userDao.findAll();
		assertEquals(0, users.size());
	}
	
	private void emptyUserTable() {
		try {
			Connection connection = databaseConnection.getConnection();
	
			String truncateuSERQuery = "DELETE FROM USER";
			
			PreparedStatement insertUseStatement = connection.prepareStatement(truncateuSERQuery);
			insertUseStatement.executeUpdate();						
			insertUseStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindAllInDeletedTrue() {
		setDeletedUserTable();
		
		List<User> users = userDao.findAll();
		assertEquals(0, users.size());
	}
	
	private void setDeletedUserTable() {
		try {
			Connection connection = databaseConnection.getConnection();
	
			String truncateUserQuery = "UPDATE USER SET DELETED = 1";
			
			PreparedStatement insertUseStatement = connection.prepareStatement(truncateUserQuery);
			insertUseStatement.executeUpdate();						
			insertUseStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFindUserPositive() {
		Long id = (long) 1;
		Optional<User> user = userDao.find(id);
		
		assertTrue(user.isPresent());
		assertEquals(id, user.get().getId());
		assertEquals("admin_rms", user.get().getUserName());
		assertEquals("admin", user.get().getPassword());
	}
	
	@Test
	public void testFindUserNotDefinedId() {
		Long id = (long) -1;
		Optional<User> user = userDao.find(id);
		
		assertFalse(user.isPresent());
	}
	
	@Test
	public void testSaveWith0IdPositive() {
		Long id = (long) 2;
		User user = new User();
		user.setUserName("Maria_Mercedes");
		user.setPassword("fernandoHose");
		
		boolean saveUser = userDao.save(user);
		assertTrue(saveUser);
		
		List<User> allUsers = userDao.findAll();
		assertEquals(id.longValue(), (long) allUsers.size());
		
		Optional<User> savedUser = userDao.find(id);
		assertTrue(savedUser.isPresent());
		assertEquals(user.getUserName(), savedUser.get().getUserName());
		assertEquals(user.getPassword(), savedUser.get().getPassword());
		
		Optional<Setting> updatedSetting = settingDao.find("MAX_USER_ID");
		assertTrue(updatedSetting.isPresent());
		assertEquals(savedUser.get().getId().toString(), updatedSetting.get().getValue());
	}
	
	@Test
	public void testSaveWithExistingIdPositive() {
		Long id = (long) 1;
		User user = new User();
		user.setId(id);
		user.setUserName("Maria_Mercedes");
		user.setPassword("fernandoHose");
		
		boolean saveUser = userDao.save(user);
		assertTrue(saveUser);
		
		List<User> allUsers = userDao.findAll();
		assertEquals(1, allUsers.size());
		
		Optional<User> savedUser = userDao.find(id);
		assertTrue(savedUser.isPresent());
		assertEquals(user.getUserName(), savedUser.get().getUserName());
		assertEquals(user.getPassword(), savedUser.get().getPassword());
		
		Optional<Setting> updatedSetting = settingDao.find("MAX_USER_ID");
		assertTrue(updatedSetting.isPresent());
		assertEquals(savedUser.get().getId().toString(), updatedSetting.get().getValue());
	}
	
	@Test
	public void testSaveExistingIdWithoutPasswordPositive() {
		Long id = (long) 1;
		
		Optional<User> userBeforUpdated = userDao.find(id);
		assertTrue(userBeforUpdated.isPresent());
		
		User user = new User();
		user.setId(id);
		user.setUserName("Maria_Mercedes");
		
		boolean saveUser = userDao.save(user);
		assertTrue(saveUser);
		
		List<User> allUsers = userDao.findAll();
		assertEquals(1, allUsers.size());
		
		Optional<User> savedUser = userDao.find(id);
		assertTrue(savedUser.isPresent());
		assertEquals(user.getUserName(), savedUser.get().getUserName());
		assertEquals(userBeforUpdated.get().getPassword(), savedUser.get().getPassword());
		
		Optional<Setting> updatedSetting = settingDao.find("MAX_USER_ID");
		assertTrue(updatedSetting.isPresent());
		assertEquals(savedUser.get().getId().toString(), updatedSetting.get().getValue());
	}
	
	@Test
	public void testDeleteUserPositive() {
		Long id = (long) 1;
		
		User user = new User(id, "", "");
		boolean deleteUser = userDao.delete(user);
		assertTrue(deleteUser);
		
		List<User> allUsers = userDao.findAll();
		assertEquals(0, allUsers.size());
		
		Optional<User> deletedUser = userDao.find(id);
		assertFalse(deletedUser.isPresent());
	}
	
	@Test
	public void testDeleteUserNotDefinedId() {
		Long id = (long) -1;
		
		User user = new User(id, "", "");
		boolean deleteUser = userDao.delete(user);
		assertFalse(deleteUser);
		
		List<User> allUsers = userDao.findAll();
		assertEquals(1, allUsers.size());
	}
	
	@Test
	public void testBulkAddUserPositive() {
		Long id = (long) 11;
		List<User> users = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			User currentUser = new User();
			currentUser.setUserName("testBulkInsert" + i);
			currentUser.setPassword("user");
			users.add(currentUser);
		}

		boolean saveUser = userDao.BulkAddUsers(users);
		assertTrue(saveUser);
		
		List<User> allUsers = userDao.findAll();
		assertEquals(id.longValue(), (long) allUsers.size());
		
		Optional<User> savedUser = userDao.find(id);
		assertTrue(savedUser.isPresent());
		assertEquals("testBulkInsert9", savedUser.get().getUserName());
		assertEquals("user", savedUser.get().getPassword());
		
		Optional<Setting> updatedSetting = settingDao.find("MAX_USER_ID");
		assertTrue(updatedSetting.isPresent());
		assertEquals(savedUser.get().getId().toString(), updatedSetting.get().getValue());
	}
	
	@Test
	public void testBulkAddUserWithEmptyList() {
		Long id = (long) 1;
		List<User> users = new ArrayList<>();

		boolean saveUser = userDao.BulkAddUsers(users);
		assertFalse(saveUser);
		
		List<User> allUsers = userDao.findAll();
		assertEquals(id.longValue(), (long) allUsers.size());
		
		Optional<User> savedUser = userDao.find(id);
		assertTrue(savedUser.isPresent());
		assertEquals("admin_rms", savedUser.get().getUserName());
		assertEquals("admin", savedUser.get().getPassword());
		
		Optional<Setting> updatedSetting = settingDao.find("MAX_USER_ID");
		assertTrue(updatedSetting.isPresent());
		assertEquals(savedUser.get().getId().toString(), updatedSetting.get().getValue());
	}
	
}
