package com.yarkhs.ldi.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

	public static Connection getConnection(Boolean isMySQL, String server, String database, String user, String password) {
		Connection conn = null;

		try {
			if (isMySQL) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				String url = "jdbc:mysql://" + server + "/" + database;
				conn = DriverManager.getConnection(url, user, password);
			} else {

				Class.forName("org.sqlite.JDBC").newInstance();
				conn = DriverManager.getConnection("jdbc:sqlite:plugins/LogDeadInventory/ldi.db");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}
