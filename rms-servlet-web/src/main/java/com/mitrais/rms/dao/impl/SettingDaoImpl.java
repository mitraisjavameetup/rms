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
import com.mitrais.rms.dao.*;
import com.mitrais.rms.model.Setting;

public class SettingDaoImpl extends BaseDaoImpl implements SettingDao {

	@Inject
	public SettingDaoImpl(DatabaseConnection databaseConnection) {
		super(databaseConnection);
	}

	@Override
	public Optional<Setting> find(String id) {

		try {
			Connection connection = databaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement(
					"SELECT PROPERTY_NAME, VALUE FROM SETTING WHERE DELETED = 0 AND PROPERTY_NAME = ?");
			statement.setString(1, id);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Setting setting = new Setting(resultSet.getString("PROPERTY_NAME"), resultSet.getString("VALUE"));

				return Optional.of(setting);
			}

			resultSet.close();
			statement.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return Optional.empty();
	}

	@Override
	public List<Setting> findAll() {
		List<Setting> result = new ArrayList<>();

		try {
			Connection connection = databaseConnection.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT PROPERTY_NAME, VALUE FROM SETTING " + "WHERE DELETED = 0");

			while (resultSet.next()) {
				Setting setting = new Setting(resultSet.getString("PROPERTY_NAME"), resultSet.getString("VALUE"));
				result.add(setting);
			}

			resultSet.close();
			statement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public boolean save(Setting setting) {
		Optional<Setting> existingSetting = this.find(setting.getPropertyName());

		try {
			Connection connection = databaseConnection.getConnection();
			PreparedStatement preparedStatement;

			if (existingSetting.isPresent()) {
				preparedStatement = connection.prepareStatement("UPDATE SETTING SET VALUE = ? WHERE PROPERTY_NAME = ?");
			} else {
				preparedStatement = connection
						.prepareStatement("INSERT INTO SETTING (VALUE, PROPERTY_NAME, DELETED) VALUES (?, ?, 0)");
			}
			preparedStatement.setString(1, setting.getValue());
			preparedStatement.setString(2, setting.getPropertyName());

			int i = preparedStatement.executeUpdate();
			preparedStatement.close();

			return (i == 1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean delete(Setting setting) {

		try {
			Connection connection = databaseConnection.getConnection();
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE SETTING SET DELETED = 1 WHERE PROPERTY_NAME = ?");
			preparedStatement.setString(1, setting.getPropertyName());
			int i = preparedStatement.executeUpdate();

			return (i == 1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public Optional<Integer> getValueAsInt(String propertyName) {
		Optional<Setting> setting = this.find(propertyName);
		Optional<Integer> result = Optional.empty();

		if (setting.isPresent()) {
			try {
				String value = setting.get().getValue();
				result = Optional.of(Integer.parseInt(value));
			} catch (NumberFormatException e) {
				// TODO: should put this output to logger instead
				System.out.println("Value of propery " + propertyName + " cannot be parsed to Integer");
			}
		}

		return result;
	}
}
