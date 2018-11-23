package cs6650;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class PostUserData implements RequestStreamHandler {
JSONParser parser = new JSONParser();

	
//	private Connection getConnection() throws SQLException {
//		String url = "jdbc:mysql://db.chg1p2kyt9ez.us-west-2.rds.amazonaws.com:3306/test";
//		String username = "zzl1115";
//		String password = "12345678";
//		Connection conn = DriverManager.getConnection(url, username, password);
//		return conn;
//	}
	
	private void insertData(int userId, int day, int timeInterval, int stepCount) throws IOException, PropertyVetoException {
		Connection conn = null;
		try {
			conn = ConnectionPool.getInstance().getConnection();
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
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

    	LambdaLogger logger = context.getLogger();

   	 	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
   	 	JSONObject responseJson = new JSONObject();
   	 	int userId = -1;
	 	int day = -1;
	 	int timeInterval = -1;
	 	int stepCount = -1;
	 	String responseCode = "200";
	 	 
	 	try {
	 		JSONObject event = (JSONObject)parser.parse(reader);
	 		if (event.get("pathParameters") != null) {
	 			JSONObject pps = (JSONObject)event.get("pathParameters");
	 			if ( pps.get("userid") != null) {
	                userId = Integer.parseInt((String)pps.get("userid"));
	            }
	            if ( pps.get("day") != null) {
	                day = Integer.parseInt((String)pps.get("day"));
	            }
	            if ( pps.get("timeinterval") != null) {
	                timeInterval = Integer.parseInt((String)pps.get("timeinterval"));
	            }
	            if ( pps.get("stepcount") != null) {
	                stepCount = Integer.parseInt((String)pps.get("stepcount"));
	            }
	 		}
	 		insertData(userId, day, timeInterval, stepCount);
			responseJson.put("statusCode", responseCode);
			responseJson.put("body", "successful!");  
			
	 	} catch(ParseException pex) {
            responseJson.put("statusCode", "401"); // self defined error code
        } catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	 	
	 	logger.log(responseJson.toJSONString());
	    OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
	    writer.write(responseJson.toJSONString());  
	    writer.close();
    }

}
