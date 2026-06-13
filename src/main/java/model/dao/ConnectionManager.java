package model.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionManager {
    /*
    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@dblab.dongduk.ac.kr:1521/orclpdb";
    private static final String DB_USERNAME = "dbp";
    private static final String DB_PASSWORD = "dbp2023#";
    */
	private static DataSource ds = null;
    
	
    public ConnectionManager() {
		// 풀(ds)은 앱 전체에서 하나만 유지(static). 이미 만들어져 있으면 재생성하지 않고 공유한다.
		// (매번 new 로 풀을 새로 만들면 이전 풀이 누수되고, close 시 전체가 끊긴다.)
		if (ds != null) {
			return;
		}

		InputStream input = null;
    	Properties prop = new Properties();

		try {
			input = getClass().getResourceAsStream("/context.properties");
			prop.load(input);			// load the properties file
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		
		try {
    		// DataSource 생성 및 설정
			BasicDataSource bds = new BasicDataSource();
	        bds.setDriverClassName(prop.getProperty("db.driver"));
	        bds.setUrl(prop.getProperty("db.url"));
	        bds.setUsername(prop.getProperty("db.username"));
	        bds.setPassword(prop.getProperty("db.password"));     
			ds = bds;
			
			// 참고: WAS의 DataSource를 이용할 경우: 
			// Context init = new InitialContext();
			// ds = (DataSource)init.lookup("java:comp/env/jdbc/OracleDS");
		} catch (Exception ex) {
			ex.printStackTrace();
		} 	   
    }

    public Connection getConnection() {
    	Connection conn = null;
    	try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
    }
    
    public void close() {
		BasicDataSource bds = (BasicDataSource) ds;
		try {
			bds.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// 현재 활성화 상태인 Connection 의 개수와 비활성화 상태인 Connection 개수 출력
	public void printDataSourceStats() {
		try {
			BasicDataSource bds = (BasicDataSource) ds;
		} catch (Exception ex) {
			ex.printStackTrace();
		}   
	}
}
