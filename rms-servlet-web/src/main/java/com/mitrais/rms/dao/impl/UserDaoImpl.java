package com.mitrais.rms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.mitrais.rms.connection.DatabaseConnection;
import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.model.User;

public class UserDaoImpl extends BaseDaoImpl implements UserDao {

	@Inject
	public UserDaoImpl(DatabaseConnection databaseConnection) {
		super(databaseConnection);
	}

	@Override
	public Optional<User> find(Long id) {
		
		try (Connection connection = databaseConnection.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USER WHERE id=?");
			preparedStatement.setLong(1, id);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				User user = new User(resultSet.getLong("ID"), resultSet.getString("USERNAME"), resultSet.getString("PASSWORD"));
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
		
		try (Connection connection = databaseConnection.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM USER");
			while (resultSet.next()) {
				User user = new User(resultSet.getLong("ID"), resultSet.getString("USERNAME"), resultSet.getString("PASSWORD"));
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
		try (Connection connection = databaseConnection.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO USERNAME VALUES (?, ?, ?)");
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			int i = stmt.executeUpdate();
			if (i == 1) {
				return true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(User user) {
		try (Connection connection = databaseConnection.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM user WHERE id=?");
			stmt.setLong(1, user.getId());
			int i = stmt.executeUpdate();
			if (i == 1) {
				return true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public Optional<User> findByUserName(String userName) {
		try (Connection connection = databaseConnection.getConnection()) {
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
