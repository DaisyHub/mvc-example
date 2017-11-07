package com.edu118.customer.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class DBConnect {
	public static Connection getConnect(){
		InputStream in = DBConnect.class.getClassLoader().getResourceAsStream("DBConnect.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
			Class.forName(prop.getProperty("driverName"));
			Connection conn = DriverManager.getConnection(prop.getProperty("url"),
					prop.getProperty("username"), prop.getProperty("password"));
			
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	
	public static void closeDB(PreparedStatement pstmt, Connection conn, ResultSet rs){
		try{
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
			if(rs != null) rs.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
