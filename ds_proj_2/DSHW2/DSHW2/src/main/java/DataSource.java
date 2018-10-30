import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class DataSource {

  private static DataSource datasource;
  //private ComboPooledDataSource cpds;
  private HikariDataSource ds;
  // "dshw.cogzigijmnwr.us-east-1.rds.amazonaws.com";
  private final String hostName = "db.chg1p2kyt9ez.us-west-2.rds.amazonaws.com";
  private final int port= 3306;
  private final String schema = "db";

  public DataSource() throws IOException, SQLException, PropertyVetoException {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema + "?useSSL=false");
    //+ "?autoReconnect=true&useSSL=false"
    //jdbc:mysql://dshw.cogzigijmnwr.us-east-1.rds.amazonaws.com:3306/DSHW2?autoReconnect=true&useSSL=false
    config.setUsername("zzl1115");
    config.setPassword("12345678");
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    ds = new HikariDataSource(config);

//    cpds = new ComboPooledDataSource();
//    cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
//    cpds.setJdbcUrl("jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema);
//    cpds.setUser("ruijieshi");
//    cpds.setPassword("stoneruiruila");
//
//    // the settings below are optional -- c3p0 can work with defaults
//    cpds.setMinPoolSize(5);
//    cpds.setAcquireIncrement(5);
//    cpds.setMaxPoolSize(50);
//    cpds.setMaxStatements(300);
  }

  public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
    if (datasource == null) {
      datasource = new DataSource();
      return datasource;
    } else {
      return datasource;
    }
  }
  public Connection getConnection() throws SQLException {
    return this.ds.getConnection();
  }
}
