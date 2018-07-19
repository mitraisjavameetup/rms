package com.mitrais.rms.dao;

import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class provides MySQL datasource to be used to connect to database.
 * It implements singleton pattern See <a href="http://www.oodesign.com/singleton-pattern.html">Singleton Pattern</a>
 */
public class DataSourceFactory
{
    private final DataSource dataSource;

    DataSourceFactory()
    {
        MysqlDataSource dataSource = new MysqlDataSource();
        // TODO: make these database setting configurable by moving to properties file
        dataSource.setDatabaseName("rmsdb");
        dataSource.setServerName("localhost");
        //        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("");
        this.dataSource = dataSource;

    }

    /**
     * Get a data source to database
     *
     * @return DataSource object
     */
    public static Connection getConnection() throws SQLException
    {
        return SingletonHelper.INSTANCE.dataSource.getConnection();
    }

    private static class SingletonHelper
    {
        private static final DataSourceFactory INSTANCE = new DataSourceFactory();
    }
}
