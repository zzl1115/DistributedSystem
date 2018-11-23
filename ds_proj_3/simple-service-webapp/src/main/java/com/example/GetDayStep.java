package com.example;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/single/{userId}/{day}")
public class GetDayStep  {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getStepsByUserIdAndDay(@PathParam("userId") int userId, @PathParam("day") int day) throws IOException, PropertyVetoException {
		return getDayStep(userId, day);
	}
	
	private String getDayStep(int userId, int day) throws IOException, PropertyVetoException {
		int res = 0;
		Connection conn = null;
		try {
      ConnectionPool cp = new ConnectionPool();
      conn = cp.getConnection();
			Statement stmt = null;
			stmt = conn.createStatement();
			String getStep = "SELECT" +
		            "    SUM(step_count)" +
		            " FROM" +
		            "    users" +
		            " WHERE" +
		            "    user_id = '" + userId +
		            "' And day = '" + day +"';";
			ResultSet rs = stmt.executeQuery(getStep);
			while(rs.next()){
			  res += rs.getInt("SUM(step_count)");
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.valueOf(res);
	}
	

}
