package com.yarkhs.ldi.jdbc.dao.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Plugin {

	Integer id;
	String version;
	Date date;


	public Plugin() {
		super();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public void setDate(String dateStr) {
		try {
			this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	public String getDateString() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

		return sdf.format(this.date);
	}


	@Override
	public String toString() {
		return "Plugin [id=" + id + ", version=" + version + ", date=" + date + "]";
	}

}
//5840 3167