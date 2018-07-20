package com.mitrais.rms.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class provides MySQL datasource to be used to connect to database. It
 * implements singleton pattern See
 * <a href="http://www.oodesign.com/singleton-pattern.html">Singleton
 * Pattern</a>
 */
public class DataSourceFactory {

    private final DataSource dataSource;

    DataSourceFactory() {
        MysqlDataSource dataSource = new MysqlDataSource();
        try {
            Map<String, String> properties = this.getDatabaseSetting();
            System.out.println(properties.toString());
            dataSource.setDatabaseName(properties.get("dbName"));
            dataSource.setServerName(properties.get("serverName"));
            dataSource.setPort(Integer.parseInt(properties.get("port")));
            dataSource.setUser(properties.get("username"));
            dataSource.setPassword(properties.get("password"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        this.dataSource = dataSource;
    }

    private Map<String, String> getDatabaseSetting() throws IOException, ClassNotFoundException {
        String resourceName = "database.properties"; // could also be a constant
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        }

        String driver = props.getProperty("jdbc.driver");
        if (driver != null) {
            Class.forName(driver);
        }

        Map<String, String> mapProperties = new HashMap<>();
        mapProperties.put("dbName", props.getProperty("jdbc.dbName"));
        mapProperties.put("serverName", props.getProperty("jdbc.serverName"));
        mapProperties.put("port", props.getProperty("jdbc.port"));
        mapProperties.put("username", props.getProperty("jdbc.username"));
        mapProperties.put("password", props.getProperty("jdbc.password"));

        return mapProperties;
    }

    /**
     * Get a data source to database
     *
     * @return DataSource object
     */
    public static Connection getConnection() throws SQLException {
        return SingletonHelper.INSTANCE.dataSource.getConnection();
    }

    private static class SingletonHelper {

        private static final DataSourceFactory INSTANCE = new DataSourceFactory();
    }
}
