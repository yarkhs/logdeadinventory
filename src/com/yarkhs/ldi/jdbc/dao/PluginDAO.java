package com.yarkhs.ldi.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.yarkhs.ldi.jdbc.Conexao;
import com.yarkhs.ldi.jdbc.dao.model.Enchantment;
import com.yarkhs.ldi.jdbc.dao.model.Item;
import com.yarkhs.ldi.jdbc.dao.model.Plugin;

public class PluginDAO {

	private final Connection connection;


	public PluginDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
	}


	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_plugins ( ");
		sql.append("		id INT AUTO_INCREMENT, ");
		sql.append("		version varchar(40), ");
		sql.append("		date TIMESTAMP, ");
		sql.append("		PRIMARY KEY (id) ");
		sql.append(" ) ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}

	}


	public void createTableSqlite() throws SQLException {
		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_plugins ( ");
		sql.append("		id INTEGER PRIMARY KEY AUTOINCREMENT,");
		sql.append("		version text, ");
		sql.append("		date text ");
		sql.append(" ) ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	public void insertPlugin(Plugin plugin) throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append("insert into ldi_plugins (version, date) values (?,?)");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, plugin.getVersion());
		stmt.setString(2, plugin.getDateString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	public Plugin findById(Integer id) throws SQLException {

		String sql = "select * from ldi_plugins where id=" + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		Plugin plugin = new Plugin();

		while (rs.next()) {
			plugin.setId(rs.getInt("id"));
			plugin.setVersion(rs.getString("version"));
			plugin.setDate(rs.getString("date"));

		}

		rs.close();
		stmt.close();
		return plugin;
	}


	public void version1_0(Boolean isMySQL, ItemDAO itemDAO, EnchantmentDAO enchantmentDAO) throws SQLException {
		// items
		alterTableItemsAddType(isMySQL);

		for (Item item : listAllItems()) {
			updateItem(item);
		}

		// enchantments
		alterTableEnchantmentsAddType(isMySQL);

		for (Enchantment enchantment : listAllEnchantments()) {
			updateEnchantment(enchantment);
		}

	}


	private void alterTableItemsAddType(Boolean isMySQL) throws SQLException {
		String sql = "";

		if (isMySQL) {
			sql = "alter table ldi_items add type varchar(40)";
		} else {
			sql = "alter table ldi_items add type text";
		}
		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	private List<Item> listAllItems() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select id, minecraft_item_id from ldi_items");

		ResultSet rs = stmt.executeQuery();
		List<Item> items = new ArrayList<Item>();

		while (rs.next()) {
			Item itemDatabase = new Item();
			itemDatabase.setId(rs.getInt("id"));
			itemDatabase.setType(Material.getMaterial(rs.getInt("minecraft_item_id")).toString());
			items.add(itemDatabase);
		}

		rs.close();
		stmt.close();
		return items;
	}


	private void updateItem(Item item) throws SQLException {

		String sql = "update ldi_items set type=? where id=?";
		PreparedStatement stmt = connection.prepareStatement(sql);

		stmt.setString(1, item.getType());
		stmt.setInt(2, item.getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	private void alterTableEnchantmentsAddType(Boolean isMySQL) throws SQLException {
		String sql = "";

		if (isMySQL) {
			sql = "alter table ldi_enchantments add type varchar(40)";
		} else {
			sql = "alter table ldi_enchantments add type text";
		}
		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	public List<Enchantment> listAllEnchantments() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select id, type from ldi_enchantments");

		ResultSet rs = stmt.executeQuery();
		List<Enchantment> enchantments = new ArrayList<Enchantment>();

		while (rs.next()) {
			Enchantment enchantmentDatabase = new Enchantment();
			enchantmentDatabase.setId(rs.getInt("id"));
			enchantmentDatabase.setType(Material.getMaterial(rs.getInt("bukkit_enchantment_id")).toString());

			enchantments.add(enchantmentDatabase);
		}

		rs.close();
		stmt.close();
		return enchantments;
	}


	public void updateEnchantment(Enchantment enchantment) throws SQLException {

		String sql = "update ldi_enchantments set type=?, where id=?";
		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, enchantment.getType());
		stmt.setInt(2, enchantment.getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}

}
