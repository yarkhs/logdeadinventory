package com.yarkhs.ldi;

public class LdiConfig {

	private Boolean isMySQL = false;
	private String server = "Localhost";
	private String database = "ldi";
	private String user = "root";
	private String password = "root";


	public LdiConfig() {
		super();
	}


	public Boolean getIsMySQL() {
		return isMySQL;
	}


	public void setIsMySQL(Boolean isMySQL) {
		this.isMySQL = isMySQL;
	}


	public String getServer() {
		return server;
	}


	public void setServer(String server) {
		this.server = server;
	}


	public String getDatabase() {
		return database;
	}


	public void setDatabase(String database) {
		this.database = database;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("-----------------LDI_Config-----------------\n");
		str.append("isMySQL: " + isMySQL + "\n");
		str.append("server: " + server + "\n");
		str.append("database: " + database + "\n");
		str.append("user: " + user + "\n");
		str.append("password: " + password + "\n");
		str.append("--------------------------------------------");

		return str.toString();
	}

}