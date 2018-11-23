package cs6650;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//import java.beans.PropertyVetoException;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class ConnectionPool {
//	private static ConnectionPool datasource;
//	  private HikariDataSource ds;
//	  private final String hostName = "db.chg1p2kyt9ez.us-west-2.rds.amazonaws.com";
//	  private final int port= 3306;
//	  private final String schema = "test";
//
//	  public ConnectionPool() throws IOException, SQLException, PropertyVetoException {
//	    HikariConfig config = new HikariConfig();
//	    config.setJdbcUrl("jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema + "?useSSL=false");
//	    config.setUsername("zzl1115");
//	    config.setPassword("12345678");
//	    config.addDataSourceProperty("cachePrepStmts", "true");
//	    config.addDataSourceProperty("prepStmtCacheSize", "250");
//	    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//	    config.addDataSourceProperty("maximumPoolSize", "305");
//	    ds = new HikariDataSource(config);
//	  }
//
//	  public static ConnectionPool getInstance() throws IOException, SQLException, PropertyVetoException {
//	    if (datasource == null) {
//	      datasource = new ConnectionPool();
//	      return datasource;
//	    } else {
//	      return datasource;
//	    }
//	  }
//	  public Connection getConnection() throws SQLException {
//	    return this.ds.getConnection();
//	  }
//}

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;


public class ConnectionPool {

    private static BasicDataSource basicDataSource;

    private ConnectionPool(){}

    public static BasicDataSource getInstance() {
        if (basicDataSource != null) {
            return basicDataSource;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:mysql://db.chg1p2kyt9ez.us-west-2.rds.amazonaws.com:3306/test?useSSL=false");
        basicDataSource.setUsername("zzl1115");
        basicDataSource.setPassword("12345678");
        basicDataSource.setMinIdle(1);
        basicDataSource.setMaxIdle(300);
        basicDataSource.setMaxTotal(300);
        basicDataSource.setMaxOpenPreparedStatements(100000000);
        return basicDataSource;
    }

    public static void close() {
        try {
            basicDataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
