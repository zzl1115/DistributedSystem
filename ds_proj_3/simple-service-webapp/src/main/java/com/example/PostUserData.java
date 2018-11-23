package com.example;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/{userId}/{day}/{timeInterval}/{stepCount}")
public class PostUserData{

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	public void postRecord(
					@PathParam("userId") int userId,
					@PathParam("day") int day,
					@PathParam("timeInterval") int timeInterval,
					@PathParam("stepCount") int stepCount) throws IOException, PropertyVetoException {
		insertData(userId,day, timeInterval, stepCount);
	}

		private void insertData(int userId, int day, int timeInterval, int stepCount) throws IOException, PropertyVetoException {
		Connection conn = null;
		try {
			ConnectionPool cp = new ConnectionPool();
			conn = cp.getConnection();
			Statement stmt = null;
			stmt = conn.createStatement();
			String insertUser = "INSERT IGNORE INTO users(user_id, day, time_interval, step_count)" 
		               + "VALUES (" + userId + "," + day + ","  + timeInterval 
		               + "," + stepCount + ")";
			stmt.executeUpdate(insertUser);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
