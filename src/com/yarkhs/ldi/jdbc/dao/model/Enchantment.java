package com.yarkhs.ldi.jdbc.dao.model;

import java.sql.SQLException;

import com.yarkhs.ldi.LdiConfig;
import com.yarkhs.ldi.jdbc.dao.EnchantmentDAO;

public class Enchantment {

	Integer id;
	String type;
	Integer level;
	Item item;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getLevel() {
		return level;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}


	public void save(LdiConfig ldiConfig) throws SQLException {
		EnchantmentDAO enchantmentDAO = new EnchantmentDAO(ldiConfig);
		this.id = enchantmentDAO.insert(this);
	}


	@Override
	public String toString() {
		return "Enchantment [id=" + id + ", type=" + type + ", level=" + level + ", item=" + item + "]";
	}

}
