package com.yarkhs.ldi.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yarkhs.ldi.jdbc.Conexao;
import com.yarkhs.ldi.jdbc.dao.model.Event;
import com.yarkhs.ldi.jdbc.dao.model.Item;
import com.yarkhs.ldi.util.Util;

public class ItemDAO implements BaseDAO {

	private final Connection connection;


	public ItemDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
	}


	@Override
	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_items ( ");
		sql.append("		id INT AUTO_INCREMENT, ");
		sql.append("		type varchar(40), ");
		sql.append("		amount INT, ");
		sql.append("		durability SMALLINT, ");
		sql.append("		event_id INT, ");
		sql.append("		item_in_hand BOOLEAN, ");
		sql.append("		has_enchantment BOOLEAN, ");
		sql.append("		PRIMARY KEY (id), ");
		sql.append("		FOREIGN KEY (event_id) REFERENCES ldi_events(id) ");
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


	@Override
	public void createTableSqlite() throws SQLException {
		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_items ( ");
		sql.append("		id INTEGER PRIMARY KEY AUTOINCREMENT,");
		sql.append("		type text, ");
		sql.append("		amount INTEGER, ");
		sql.append("		durability INTEGER, ");
		sql.append("		event_id INTEGER, ");
		sql.append("		item_in_hand INTEGER, ");
		sql.append("		has_enchantment INTEGER, ");
		sql.append("		FOREIGN KEY (event_id) REFERENCES ldi_events(id) ");
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


	@Override
	public void insert(Object object) throws SQLException {

		Item item = (Item) object;
		StringBuffer sql = new StringBuffer();

		sql.append("insert into ldi_items (type, amount, durability, item_in_hand, has_enchantment");

		if (!Util.empty(item.getEvent())) {
			sql.append(", event_id");
		}

		sql.append(") values (?,?,?,?,?");

		if (!Util.empty(item.getEvent())) {
			sql.append(",?");
		}

		sql.append(")");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, item.getType());
		stmt.setInt(2, item.getAmount());
		stmt.setInt(3, item.getDurability());
		stmt.setBoolean(4, item.getItemInHand());
		stmt.setBoolean(5, item.getHasEnchantment());
		stmt.setString(6, item.getType());

		if (!Util.empty(item.getEvent())) {
			stmt.setInt(7, item.getEvent().getId());
		}

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	@Override
	public List<Item> listAll() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from ldi_items");

		ResultSet rs = stmt.executeQuery();
		List<Item> items = new ArrayList<Item>();

		while (rs.next()) {
			Item itemDatabase = new Item();
			itemDatabase.setId(rs.getInt("id"));
			itemDatabase.setType(rs.getString("type"));
			itemDatabase.setAmount(rs.getInt("amount"));
			itemDatabase.setDurability(rs.getShort("durability"));
			itemDatabase.setItemInHand(rs.getBoolean("item_in_hand"));
			itemDatabase.setHasEnchantment(rs.getBoolean("has_enchantment"));
			itemDatabase.setEvent(new Event(rs.getInt("event_id")));
			items.add(itemDatabase);
		}

		rs.close();
		stmt.close();
		return items;
	}


	@Override
	public void delete(Integer id) throws SQLException {

		String sql = "delete from ldi_items where id = " + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	@Override
	public Item findById(Integer id) throws SQLException {

		String sql = "select * from ldi_items where id=" + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		Item item = new Item();

		while (rs.next()) {
			item.setId(rs.getInt("id"));
			item.setType(rs.getString("type"));
			item.setAmount(rs.getInt("amount"));
			item.setDurability(rs.getShort("durability"));
			item.setItemInHand(rs.getBoolean("item_in_hand"));
			item.setHasEnchantment(rs.getBoolean("has_enchantment"));
			item.setEvent(new Event(rs.getInt("event_id")));

		}

		rs.close();
		stmt.close();
		return item;
	}


	public List<Item> findByEventId(Integer eventId) throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from ldi_items where event_id = " + eventId);

		ResultSet rs = stmt.executeQuery();
		List<Item> items = new ArrayList<Item>();

		while (rs.next()) {
			Item itemDatabase = new Item();
			itemDatabase.setId(rs.getInt("id"));
			itemDatabase.setType(rs.getString("type"));
			itemDatabase.setAmount(rs.getInt("amount"));
			itemDatabase.setDurability(rs.getShort("durability"));
			itemDatabase.setItemInHand(rs.getBoolean("item_in_hand"));
			itemDatabase.setHasEnchantment(rs.getBoolean("has_enchantment"));
			itemDatabase.setEvent(new Event(rs.getInt("event_id")));

			items.add(itemDatabase);
		}

		rs.close();
		stmt.close();
		return items;
	}


	public Item findBy(Item item) throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append("select id from ldi_items where type=? and amount=? and durability=? and item_in_hand=? and has_enchantment=? ");

		if (!Util.empty(item.getEvent())) {
			sql.append("and event_id=?");
		}

		PreparedStatement stmt = connection.prepareStatement(sql.toString());
		stmt.setString(1, item.getType());
		stmt.setInt(2, item.getAmount());
		stmt.setInt(3, item.getDurability());
		stmt.setBoolean(4, item.getItemInHand());
		stmt.setBoolean(5, item.getHasEnchantment());

		if (!Util.empty(item.getEvent())) {
			stmt.setInt(6, item.getEvent().getId());
		}

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			item.setId(rs.getInt("id"));
		}

		rs.close();
		stmt.close();
		return item;
	}


	@Override
	public void update(Object object) throws SQLException {

		Item item = (Item) object;
		String sql = "update ldi_items set type=?, amount=?, durability=?, item_in_hand=?, has_enchantment, event_id=? where id=?";
		PreparedStatement stmt = connection.prepareStatement(sql);

		stmt.setString(1, item.getType());
		stmt.setInt(2, item.getAmount());
		stmt.setInt(3, item.getDurability());
		stmt.setBoolean(4, item.getItemInHand());
		stmt.setBoolean(5, item.getHasEnchantment());
		stmt.setInt(6, item.getEvent().getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	public void alterTableAddTypeMySql() throws SQLException {
		String sql = "alter table lid_items add type varchar(40)";
		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	public void alterTableAddTypeSqlite() throws SQLException {
		String sql = "alter table lid_items add type text";
		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}

}
