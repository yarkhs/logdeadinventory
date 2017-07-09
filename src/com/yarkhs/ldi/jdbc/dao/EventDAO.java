package com.yarkhs.ldi.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.yarkhs.ldi.LdiConfig;
import com.yarkhs.ldi.jdbc.Conexao;
import com.yarkhs.ldi.jdbc.dao.model.Event;
import com.yarkhs.ldi.jdbc.dao.model.Item;
import com.yarkhs.ldi.util.Util;

public class EventDAO implements BaseDAO {

	private final Connection connection;


	public EventDAO(LdiConfig ldiConfig) throws SQLException {
		this(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
	}


	public EventDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
	}


	@Override
	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_events ( ");
		sql.append("		id INT AUTO_INCREMENT, ");
		sql.append("		player_name VARCHAR(100), ");
		sql.append("		player_location VARCHAR(100), ");
		sql.append("		player_item_in_hand_id INT, ");

		sql.append("		killer_name VARCHAR(100), ");
		sql.append("		killer_location VARCHAR(100), ");
		sql.append("		killer_Item_in_hand_id INT, ");

		sql.append("		death_reason VARCHAR(100), ");
		sql.append("		death_date TIMESTAMP, ");
		sql.append("		xp_lost INT, ");
		sql.append("		PRIMARY KEY (id), ");
		sql.append("		FOREIGN KEY (player_item_in_hand_id) REFERENCES ldi_items(id), ");
		sql.append("		FOREIGN KEY (killer_Item_in_hand_id) REFERENCES ldi_items(id) ");
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

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_events ( ");
		sql.append("		id INTEGER PRIMARY KEY AUTOINCREMENT,");
		sql.append("		player_name text, ");
		sql.append("		player_location text, ");
		sql.append("		player_Item_in_hand_id INTEGER, ");

		sql.append("		killer_name text, ");
		sql.append("		killer_location text, ");
		sql.append("		killer_Item_in_hand_id INTEGER, ");

		sql.append("		death_reason text, ");
		sql.append("		death_date text, ");
		sql.append("		xp_lost INTEGER ");
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

		Event event = (Event) object;

		StringBuffer sql = new StringBuffer();
		sql.append("insert into ldi_events (death_reason, death_date, xp_lost, player_name, player_location,");

		if (!Util.empty(event.getPlayerItemInHand()) && !Util.empty(event.getPlayerItemInHand().getId())) {
			sql.append(", player_Item_in_hand_id");
		}

		if (!Util.empty(event.getKillerName())) {
			sql.append(",killer_name, killer_location, killer_Item_in_hand_id ");
		}

		sql.append(") values (?,?,?,?,?");

		if (!Util.empty(event.getPlayerItemInHand()) && !Util.empty(event.getPlayerItemInHand().getId())) {
			sql.append(",?");
		}

		if (!Util.empty(event.getKillerName())) {
			sql.append(",?,?,?");
		}

		sql.append(")");

		PreparedStatement stmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

		stmt.setString(1, event.getDeathReason());
		stmt.setString(2, event.getDeathDateString());
		stmt.setInt(3, event.getXpLost());

		stmt.setString(4, event.getPlayerName());
		stmt.setString(5, event.getPlayerLocation());

		Integer contador = 6;
		if (!Util.empty(event.getPlayerItemInHand()) && !Util.empty(event.getPlayerItemInHand().getId())) {
			stmt.setInt(contador++, event.getPlayerItemInHand().getId());
		}

		if (!Util.empty(event.getKillerName())) {
			stmt.setString(contador++, event.getKillerName());
			stmt.setString(contador++, event.getKillerLocation());
			stmt.setInt(contador, event.getKillerItemInHand().getId());
		}

		int affectedRows = stmt.executeUpdate();

		if (affectedRows == 0) {
			throw new SQLException("Creating user failed, no rows affected.");
		}

		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				event.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating user failed, no ID obtained.");
			}
		} finally {
			stmt.close();
		}

		return event.getId();
	}


	@Override
	public List<Event> listAll() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from ldi_events");

		ResultSet rs = stmt.executeQuery();
		List<Event> events = new ArrayList<Event>();

		while (rs.next()) {
			Event event = new Event();
			event.setPlayerItemInHand(new Item());
			event.setKillerItemInHand(new Item());

			event.setId(rs.getInt("id"));
			event.setPlayerName(rs.getString("player_name"));
			event.setPlayerLocation(rs.getString("player_location"));
			event.getPlayerItemInHand().setId(rs.getInt("player_Item_in_hand_id"));

			event.setKillerName(rs.getString("killer_name"));
			event.setKillerLocation(rs.getString("killer_location"));
			event.getKillerItemInHand().setId(rs.getInt("killer_Item_in_hand_id"));

			event.setDeathDate(rs.getString("death_date"));
			event.setDeathReason(rs.getString("death_reason"));
			event.setXpLost(rs.getInt("xp_lost"));

			events.add(event);
		}

		rs.close();
		stmt.close();
		return events;
	}


	@Override
	public void delete(Integer id) throws SQLException {

		String sql = "delete from ldi_events where id = " + id;
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
	public Event findById(Integer id) throws SQLException {

		String sql = "select * from ldi_events where id=" + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		Event event = new Event();

		while (rs.next()) {
			// TODO preencher itens
			event.setPlayerItemInHand(new Item());
			event.setKillerItemInHand(new Item());

			event.setId(rs.getInt("id"));
			event.setPlayerName(rs.getString("player_name"));
			event.setPlayerLocation(rs.getString("player_location"));
			event.getPlayerItemInHand().setId(rs.getInt("player_Item_in_hand_id"));

			event.setKillerName(rs.getString("killer_name"));
			event.setKillerLocation(rs.getString("killer_location"));
			event.getKillerItemInHand().setId(rs.getInt("killer_Item_in_hand_id"));

			event.setDeathDate(rs.getString("death_date"));
			event.setDeathReason(rs.getString("death_reason"));
			event.setXpLost(rs.getInt("xp_lost"));
		}

		rs.close();
		stmt.close();
		return event;
	}


	public List<Event> listByPlayer(String playerName) throws SQLException {

		String sql = "select * from ldi_events where player_name='" + playerName + "'";
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		List<Event> events = new ArrayList<Event>();

		while (rs.next()) {
			Event event = new Event();
			event.setPlayerItemInHand(new Item());
			event.setKillerItemInHand(new Item());

			event.setId(rs.getInt("id"));
			event.setPlayerName(rs.getString("player_name"));
			event.setPlayerLocation(rs.getString("player_location"));
			event.getPlayerItemInHand().setId(rs.getInt("player_Item_in_hand_id"));

			event.setKillerName(rs.getString("killer_name"));
			event.setKillerLocation(rs.getString("killer_location"));
			event.getKillerItemInHand().setId(rs.getInt("killer_Item_in_hand_id"));

			event.setDeathDate(rs.getString("death_date"));
			event.setDeathReason(rs.getString("death_reason"));
			event.setXpLost(rs.getInt("xp_lost"));

			events.add(event);
		}

		rs.close();
		stmt.close();
		return events;
	}


	public List<Event> listByPlayerAndDate(String playerName, Date deathDate) throws SQLException {

		java.sql.Date sqlDate = new java.sql.Date(deathDate.getTime());

		String sql = "select * from ldi_events where player_name='" + playerName + "' and deathDate=" + sqlDate;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		List<Event> events = new ArrayList<Event>();

		while (rs.next()) {
			Event event = new Event();
			event.setPlayerItemInHand(new Item());
			event.setKillerItemInHand(new Item());

			event.setId(rs.getInt("id"));
			event.setPlayerName(rs.getString("player_name"));
			event.setPlayerLocation(rs.getString("player_location"));
			event.getPlayerItemInHand().setId(rs.getInt("player_Item_in_hand_id"));

			event.setKillerName(rs.getString("killer_name"));
			event.setKillerLocation(rs.getString("killer_location"));
			event.getKillerItemInHand().setId(rs.getInt("killer_Item_in_hand_id"));

			event.setDeathDate(rs.getString("death_date"));
			event.setDeathReason(rs.getString("death_reason"));
			event.setXpLost(rs.getInt("xp_lost"));

			events.add(event);
		}

		rs.close();
		stmt.close();
		return events;

	}


	//	public Event findBy(Event event) throws SQLException {
	//
	//		String sql = "select id from ldi_events where player_name=? and death_reason=? and death_date=? and xp_lost=?";
	//		PreparedStatement stmt = connection.prepareStatement(sql);
	//		stmt.setString(1, event.getPlayerName());
	//		stmt.setString(2, event.getDeathReason());
	//		stmt.setString(3, event.getDeathDateString());
	//		stmt.setInt(4, event.getXpLost());
	//
	//		ResultSet rs = stmt.executeQuery();
	//
	//		while (rs.next()) {
	//			event.setId(rs.getInt("id"));
	//		}
	//
	//		rs.close();
	//		stmt.close();
	//		return event;
	//	}

	@Override
	public void update(Object object) throws SQLException {

		// Event event = (Event) object;
		// String sql = "update ldi_events set player_name=?, death_reason=?,
		// death_date=?, xp_lost=? where id=?";
		// PreparedStatement stmt = connection.prepareStatement(sql.toString());
		//
		// stmt.setString(1, event.getPlayerName());
		// stmt.setString(2, event.getDeathReason());
		// stmt.setDate(3, event.getDeathDateSql());
		// stmt.setInt(4, event.getXpLost());
		//
		// try {
		// stmt.execute();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// stmt.close();
		// }
	}

}
