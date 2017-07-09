package com.yarkhs.ldi.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.yarkhs.ldi.LdiConfig;
import com.yarkhs.ldi.jdbc.Conexao;
import com.yarkhs.ldi.jdbc.dao.model.Enchantment;
import com.yarkhs.ldi.jdbc.dao.model.Item;

public class EnchantmentDAO implements BaseDAO {

	private final Connection connection;


	public EnchantmentDAO(LdiConfig ldiConfig) throws SQLException {
		this(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
	}


	public EnchantmentDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
	}


	@Override
	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_enchantments ( ");
		sql.append("		id INT AUTO_INCREMENT, ");
		sql.append("		type VARCHAR(50), ");
		sql.append("		level INT, ");
		sql.append("		item_id INT, ");
		sql.append("		PRIMARY KEY (id), ");
		sql.append("		FOREIGN KEY (item_id) REFERENCES ldi_items(id) ");
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

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_enchantments ( ");
		sql.append("		id INTEGER PRIMARY KEY AUTOINCREMENT,");
		sql.append("		type text, ");
		sql.append("		level INTEGER, ");
		sql.append("		item_id INTEGER, ");
		sql.append("		FOREIGN KEY (item_id) REFERENCES ldi_items(id) ");
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
	public Integer insert(Object object) throws SQLException {

		Enchantment enchantment = (Enchantment) object;
		String sql = "insert into ldi_enchantments (type, level, item_id) values (?,?,?)";
		PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		stmt.setString(1, enchantment.getType());
		stmt.setInt(2, enchantment.getLevel());
		stmt.setInt(3, enchantment.getItem().getId());

		int affectedRows = stmt.executeUpdate();

		if (affectedRows == 0) {
			throw new SQLException("Creating user failed, no rows affected.");
		}

		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				enchantment.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating user failed, no ID obtained.");
			}
		} finally {
			stmt.close();
		}

		return enchantment.getId();
	}


	@Override
	public List<Enchantment> listAll() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from ldi_enchantments");

		ResultSet rs = stmt.executeQuery();
		List<Enchantment> enchantments = new ArrayList<Enchantment>();

		while (rs.next()) {
			Enchantment enchantmentDatabase = new Enchantment();
			enchantmentDatabase.setId(rs.getInt("id"));
			enchantmentDatabase.setType(rs.getString("type"));
			enchantmentDatabase.setLevel(rs.getInt("level"));
			enchantmentDatabase.setItem(new Item(rs.getInt("item_id")));

			enchantments.add(enchantmentDatabase);
		}

		rs.close();
		stmt.close();
		return enchantments;
	}


	@Override
	public void delete(Integer id) throws SQLException {

		String sql = "delete from ldi_enchantments where id = " + id;
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
	public Object findById(Integer id) throws SQLException {

		String sql = "select * from ldi_enchantments where id=" + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		Enchantment enchantment = new Enchantment();

		while (rs.next()) {
			enchantment.setId(rs.getInt("id"));
			enchantment.setType(rs.getString("type"));
			enchantment.setLevel(rs.getInt("level"));
			enchantment.setItem(new Item(rs.getInt("item_id")));
		}

		rs.close();
		stmt.close();
		return enchantment;
	}


	public List<Enchantment> listByItemId(Integer itemId) throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from ldi_enchantments where item_id=" + itemId);

		ResultSet rs = stmt.executeQuery();
		List<Enchantment> enchantments = new ArrayList<Enchantment>();

		while (rs.next()) {
			Enchantment enchantmentDatabase = new Enchantment();
			enchantmentDatabase.setId(rs.getInt("id"));
			enchantmentDatabase.setType(rs.getString("type"));
			enchantmentDatabase.setLevel(rs.getInt("level"));
			enchantmentDatabase.setItem(new Item(rs.getInt("item_id")));

			enchantments.add(enchantmentDatabase);
		}

		rs.close();
		stmt.close();
		return enchantments;

	}


	@Override
	public void update(Object object) throws SQLException {

		Enchantment enchantment = (Enchantment) object;
		String sql = "update ldi_enchantments set type=?, level=?, item_id=? where id=?";
		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, enchantment.getType());
		stmt.setInt(2, enchantment.getLevel());
		stmt.setInt(3, enchantment.getItem().getId());
		stmt.setInt(3, enchantment.getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}

}
