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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class GetCurrentStep implements RequestStreamHandler {
	JSONParser parser = new JSONParser();

//	private Connection getConnection() throws SQLException {
//		String url = "jdbc:mysql://db.chg1p2kyt9ez.us-west-2.rds.amazonaws.com:3306/test";
//		String username = "zzl1115";
//		String password = "12345678";
//		Connection conn = DriverManager.getConnection(url, username, password);
//		return conn;
//	}
	
	private String getCurrentStep(int userId) throws IOException, PropertyVetoException {
		int res = 0;
		Connection conn = null;
		try {
			conn = ConnectionPool.getInstance().getConnection();
			Statement stmt = null;
			stmt = conn.createStatement();
			String getStep = "SELECT" +
	                "    SUM(step_count)" +
	                " FROM" +
	                "    users" +
	                " WHERE" +
	                "    user_id = '" + userId + "';";
			ResultSet rs = stmt.executeQuery(getStep);
            while(rs.next()){
                res += rs.getInt("SUM(step_count)");
            }
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.valueOf(res);
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

    	LambdaLogger logger = context.getLogger();

   	 	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
   	 	JSONObject responseJson = new JSONObject();
   	 	int userId = -1;
	 	String responseCode = "200";
	 	 
	 	try {
	 		JSONObject event = (JSONObject)parser.parse(reader);
	 		if (event.get("pathParameters") != null) {
	 			JSONObject pps = (JSONObject)event.get("pathParameters");
	 			if ( pps.get("userid") != null) {
	                userId = Integer.parseInt((String)pps.get("userid"));
	            }
	 		}
	 		String response = getCurrentStep(userId);
			responseJson.put("statusCode", responseCode);
			responseJson.put("body", response);  
			
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
