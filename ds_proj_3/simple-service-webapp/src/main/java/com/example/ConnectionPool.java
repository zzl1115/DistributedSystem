package com.example;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool {
    private final String username = "likcu";
    private final String password = "19930716";
    private final String hostName = "35.230.20.75:3306";
    private final int port = 3306;
    private final String schema = "likcu";

    /**
     * Connect to database
     */
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        Properties connProperties = new Properties();
        connProperties.put("user", username);
        connProperties.put("password", password);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MySQL JDBC Driver NOT Registered!");
            throw new SQLException(e);
        }

        connection = DriverManager.getConnection(
                "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema + "?useSSL=false",
                connProperties);

        return connection;
    }

    /**
     * Close connection to database
     */
    public void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------ Connection close failure------");
            throw e;
        }
    }
}
