package com.mitrais.rms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.omg.IOP.ExceptionDetailMessage;

import com.google.inject.Inject;
import com.mitrais.rms.connection.DatabaseConnection;
import com.mitrais.rms.dao.SettingDao;
import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.model.Setting;
import com.mitrais.rms.model.User;

public class UserDaoImpl extends BaseDaoImpl implements UserDao {

	@Inject
	public UserDaoImpl(DatabaseConnection databaseConnection) {
		super(databaseConnection);
	}

	@Override
	public Optional<User> find(Long id) {
		try {
			Connection connection = databaseConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USER WHERE id=?");
			preparedStatement.setLong(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				User user = new User(resultSet.getLong("ID"), resultSet.getString("USERNAME"),
						resultSet.getString("PASSWORD"));
				return Optional.of(user);
			}

			resultSet.close();
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return Optional.empty();
	}

	@Override
	public List<User> findAll() {
		List<User> result = new ArrayList<>();

		try {
			Connection connection = databaseConnection.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM USER WHERE DELETED = 0");
			while (resultSet.next()) {
				User user = new User(resultSet.getLong("ID"), resultSet.getString("USERNAME"),
						resultSet.getString("PASSWORD"));
				result.add(user);
			}

			resultSet.close();
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean save(User user) {
		boolean result = false;
		try {
			Connection connection = databaseConnection.getConnection();

			if (user.getId() == 0) {
				result = insertUser(user, connection);
			} else {
				result = updateUser(user, connection);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	private boolean insertUser(User user, Connection connection) throws SQLException {
		String maxUserProperty = "MAX_USER_ID";
		int i = 0;

		/*
		 * get latest id from setting table. SettingDao is intended to be open and let
		 * the outer DAO object to close the connection.
		 */
		@SuppressWarnings("resource")
		SettingDao settingDao = new SettingDaoImpl(databaseConnection);
		Optional<Integer> latestId = settingDao.getValueAsInt(maxUserProperty);

		int currentId = latestId.isPresent() ? (latestId.get() + 1) : 1;
		user.setId((long) currentId);

		connection.setAutoCommit(false);

		try (PreparedStatement userInsertStatement = connection
				.prepareStatement("INSERT INTO USER (ID, USERNAME, PASSWORD, DELETED) VALUES (?, ?, ?, 0)")) {

			userInsertStatement.setLong(1, user.getId());
			userInsertStatement.setString(2, user.getUserName());
			userInsertStatement.setString(3, user.getPassword());
			i = userInsertStatement.executeUpdate();

			// update value of MAX_USER property
			Setting maxUserSetting = new Setting(maxUserProperty, Integer.toString(currentId));
			settingDao.save(maxUserSetting);

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		}

		return (i == 1);
	}

	private boolean updateUser(User user, Connection connection) throws SQLException {
		int i = 0;
		User existingUser = null;
		Optional<User> optExistingUser = this.find(user.getId());

		if (!optExistingUser.isPresent()) {
			// TODO: should this throw message or something?
			return false;
		}

		existingUser = optExistingUser.get();
		existingUser.setUserName(user.getUserName());
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			existingUser.setPassword(user.getPassword());
		}

		PreparedStatement userUpdateStatement = connection
				.prepareStatement("UPDATE USER SET USERNAME = ?, PASSWORD = ? WHERE ID = ? AND DELETED = 0");
		userUpdateStatement.setString(1, existingUser.getUserName());
		userUpdateStatement.setString(2, existingUser.getPassword());
		userUpdateStatement.setLong(3, existingUser.getId());
		i = userUpdateStatement.executeUpdate();

		userUpdateStatement.close();

		return (i == 1);
	}

	@Override
	public boolean delete(User user) {
		try {
			Connection connection = databaseConnection.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE USER SET DELETED = 1 WHERE ID = ?");
			preparedStatement.setLong(1, user.getId());

			int i = preparedStatement.executeUpdate();
			if (i == 1) {
				return true;
			}

			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public Optional<User> findByUserName(String userName) {
		try {
			Connection connection = databaseConnection.getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE user_name=?");
			stmt.setString(1, userName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				User user = new User(rs.getLong("id"), rs.getString("user_name"), rs.getString("password"));
				return Optional.of(user);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return Optional.empty();
	}
}
