package com.yarkhs.ldi.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yarkhs.ldi.jdbc.Conexao;
import com.yarkhs.ldi.jdbc.dao.model.Event;
import com.yarkhs.ldi.jdbc.dao.model.Item;
import com.yarkhs.ldi.util.Util;

public class EventDAO implements BaseDAO {

	private final Connection connection;


	public EventDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
	}


	@Override
	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_events ( ");
		sql.append("		id INT AUTO_INCREMENT, ");
		sql.append("		player_name VARCHAR(100), ");
		sql.append("		player_world VARCHAR(100), ");
		sql.append("		player_location_x INT, ");
		sql.append("		player_location_y INT, ");
		sql.append("		player_location_z INT, ");
		sql.append("		player_Item_in_hand_id INT, ");

		sql.append("		killer_name VARCHAR(100), ");
		sql.append("		killer_world VARCHAR(100), ");
		sql.append("		killer_location_x INT, ");
		sql.append("		killer_location_y INT, ");
		sql.append("		killer_location_z INT, ");
		sql.append("		killer_Item_in_hand_id INT, ");

		sql.append("		death_reason VARCHAR(100), ");
		sql.append("		death_date TIMESTAMP, ");
		sql.append("		xp_lost INT, ");
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


	@Override
	public void createTableSqlite() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS ldi_events ( ");
		sql.append("		id INTEGER PRIMARY KEY AUTOINCREMENT,");
		sql.append("		player_name text, ");
		sql.append("		player_world text, ");
		sql.append("		player_location_x INTEGER, ");
		sql.append("		player_location_y INTEGER, ");
		sql.append("		player_location_z INTEGER, ");
		sql.append("		player_Item_in_hand_id INTEGER, ");

		sql.append("		killer_name text, ");
		sql.append("		killer_world text, ");
		sql.append("		killer_location_x INTEGER, ");
		sql.append("		killer_location_y INTEGER, ");
		sql.append("		killer_location_z INTEGER, ");
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
	public void insert(Object object) throws SQLException {

		Event event = (Event) object;

		StringBuffer sql = new StringBuffer();
		sql.append("insert into ldi_events (death_reason, death_date, xp_lost ");
		sql.append(",player_name, player_world, player_location_x, player_location_y, player_location_z ");

		if (!Util.empty(event.getPlayerItemInHand()) && !Util.empty(event.getPlayerItemInHand().getId())) {
			sql.append(", player_Item_in_hand_id");
		}

		if (!Util.empty(event.getKillerName())) {
			sql.append(",killer_name, killer_world, killer_location_x, killer_location_y, killer_location_z, killer_Item_in_hand_id ");
		}

		sql.append(") values (?,?,?,?,?,?,?,?");

		if (!Util.empty(event.getPlayerItemInHand()) && !Util.empty(event.getPlayerItemInHand().getId())) {
			sql.append(",?");
		}

		if (!Util.empty(event.getKillerName())) {
			sql.append(",?,?,?,?,?,?");
		}

		sql.append(")");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, event.getDeathReason());
		stmt.setString(2, event.getDeathDateString());
		stmt.setInt(3, event.getXpLost());

		stmt.setString(4, event.getPlayerName());
		stmt.setString(5, event.getPlayerWorld());
		stmt.setInt(6, event.getPlayerLocationX());
		stmt.setInt(7, event.getPlayerLocationY());
		stmt.setInt(8, event.getPlayerLocationZ());

		Integer contador = 9;
		if (!Util.empty(event.getPlayerItemInHand()) && !Util.empty(event.getPlayerItemInHand().getId())) {
			stmt.setInt(contador++, event.getPlayerItemInHand().getId());
		}

		if (!Util.empty(event.getKillerName())) {
			stmt.setString(contador++, event.getKillerName());
			stmt.setString(contador++, event.getKillerWorld());
			stmt.setInt(contador++, event.getKillerLocationX());
			stmt.setInt(contador++, event.getKillerLocationY());
			stmt.setInt(contador++, event.getKillerLocationZ());
			stmt.setInt(contador, event.getKillerItemInHand().getId());
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
	public List<Event> listAll() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from ldi_events");

		ResultSet rs = stmt.executeQuery();
		List<Event> events = new ArrayList<Event>();

		while (rs.next()) {
			Event eventDatabase = new Event();

			eventDatabase.setId(rs.getInt("id"));
			eventDatabase.setPlayerName(rs.getString("player_name"));
			eventDatabase.setPlayerWorld(rs.getString("player_world"));
			eventDatabase.setPlayerLocationX(rs.getInt("player_location_x"));
			eventDatabase.setPlayerLocationY(rs.getInt("player_location_y"));
			eventDatabase.setPlayerLocationZ(rs.getInt("player_location_z"));

			eventDatabase.setPlayerItemInHand(new Item());
			eventDatabase.getPlayerItemInHand().setId(rs.getInt("player_Item_in_hand_id"));

			eventDatabase.setKillerName(rs.getString("killer_name"));
			eventDatabase.setKillerWorld(rs.getString("killer_world"));
			eventDatabase.setKillerLocationX(rs.getInt("killer_location_x"));
			eventDatabase.setKillerLocationY(rs.getInt("killer_location_y"));
			eventDatabase.setKillerLocationZ(rs.getInt("killer_location_z"));

			eventDatabase.setKillerItemInHand(new Item());
			eventDatabase.getKillerItemInHand().setId(rs.getInt("killer_Item_in_hand_id"));

			eventDatabase.setDeathDate(rs.getString("death_date"));
			eventDatabase.setDeathReason(rs.getString("death_reason"));
			eventDatabase.setXpLost(rs.getInt("xp_lost"));

			events.add(eventDatabase);

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
			event.setId(rs.getInt("id"));
			event.setPlayerName(rs.getString("player_name"));
			event.setPlayerWorld(rs.getString("player_world"));
			event.setPlayerLocationX(rs.getInt("player_location_x"));
			event.setPlayerLocationY(rs.getInt("player_location_y"));
			event.setPlayerLocationZ(rs.getInt("player_location_z"));

			event.setPlayerItemInHand(new Item());
			event.getPlayerItemInHand().setId(rs.getInt("player_Item_in_hand_id"));

			event.setKillerName(rs.getString("killer_name"));
			event.setKillerWorld(rs.getString("killer_world"));
			event.setKillerLocationX(rs.getInt("killer_location_x"));
			event.setKillerLocationY(rs.getInt("killer_location_y"));
			event.setKillerLocationZ(rs.getInt("killer_location_z"));

			event.setKillerItemInHand(new Item());
			event.getKillerItemInHand().setId(rs.getInt("killer_Item_in_hand_id"));

			event.setDeathDate(rs.getString("death_date"));
			event.setDeathReason(rs.getString("death_reason"));
			event.setXpLost(rs.getInt("xp_lost"));
		}

		rs.close();
		stmt.close();
		return event;
	}


	public List<Event> findByPlayer(String playerName) throws SQLException {

		String sql = "select * from ldi_events where player_name='" + playerName + "'";
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		List<Event> events = new ArrayList<Event>();

		while (rs.next()) {
			Event eventDatabase = new Event();

			eventDatabase.setId(rs.getInt("id"));
			eventDatabase.setPlayerName(rs.getString("player_name"));
			eventDatabase.setPlayerWorld(rs.getString("player_world"));
			eventDatabase.setPlayerLocationX(rs.getInt("player_location_x"));
			eventDatabase.setPlayerLocationY(rs.getInt("player_location_y"));
			eventDatabase.setPlayerLocationZ(rs.getInt("player_location_z"));

			eventDatabase.setPlayerItemInHand(new Item());
			eventDatabase.getPlayerItemInHand().setId(rs.getInt("player_Item_in_hand_id"));

			eventDatabase.setKillerName(rs.getString("killer_name"));
			eventDatabase.setKillerWorld(rs.getString("killer_world"));
			eventDatabase.setKillerLocationX(rs.getInt("killer_location_x"));
			eventDatabase.setKillerLocationY(rs.getInt("killer_location_y"));
			eventDatabase.setKillerLocationZ(rs.getInt("killer_location_z"));

			eventDatabase.setKillerItemInHand(new Item());
			eventDatabase.getKillerItemInHand().setId(rs.getInt("killer_Item_in_hand_id"));

			eventDatabase.setDeathDate(rs.getString("death_date"));
			eventDatabase.setDeathReason(rs.getString("death_reason"));
			eventDatabase.setXpLost(rs.getInt("xp_lost"));

			events.add(eventDatabase);
		}

		rs.close();
		stmt.close();
		return events;
	}


	public List<Event> findByPlayerAndDate(String playerName, Date deathDate) throws SQLException {

		java.sql.Date sqlDate = new java.sql.Date(deathDate.getTime());

		String sql = "select * from ldi_events where player_name='" + playerName + "' and deathDate=" + sqlDate;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		List<Event> events = new ArrayList<Event>();

		while (rs.next()) {
			Event eventDatabase = new Event();

			eventDatabase.setId(rs.getInt("id"));
			eventDatabase.setPlayerName(rs.getString("player_name"));
			eventDatabase.setPlayerWorld(rs.getString("player_world"));
			eventDatabase.setPlayerLocationX(rs.getInt("player_location_x"));
			eventDatabase.setPlayerLocationY(rs.getInt("player_location_y"));
			eventDatabase.setPlayerLocationZ(rs.getInt("player_location_z"));

			eventDatabase.setPlayerItemInHand(new Item());
			eventDatabase.getPlayerItemInHand().setId(rs.getInt("player_Item_in_hand_id"));

			eventDatabase.setKillerName(rs.getString("killer_name"));
			eventDatabase.setKillerWorld(rs.getString("killer_world"));
			eventDatabase.setKillerLocationX(rs.getInt("killer_location_x"));
			eventDatabase.setKillerLocationY(rs.getInt("killer_location_y"));
			eventDatabase.setKillerLocationZ(rs.getInt("killer_location_z"));

			eventDatabase.setKillerItemInHand(new Item());
			eventDatabase.getKillerItemInHand().setId(rs.getInt("killer_Item_in_hand_id"));

			eventDatabase.setDeathDate(rs.getString("death_date"));
			eventDatabase.setDeathReason(rs.getString("death_reason"));
			eventDatabase.setXpLost(rs.getInt("xp_lost"));

			events.add(eventDatabase);
		}

		rs.close();
		stmt.close();
		return events;
	}


	public Event findBy(Event event) throws SQLException {

		String sql = "select id from ldi_events where player_name=? and death_reason=? and death_date=? and xp_lost=?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, event.getPlayerName());
		stmt.setString(2, event.getDeathReason());
		stmt.setString(3, event.getDeathDateString());
		stmt.setInt(4, event.getXpLost());

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			event.setId(rs.getInt("id"));
		}

		rs.close();
		stmt.close();
		return event;
	}


	@Override
	public void update(Object object) throws SQLException {

		// Event event = (Event) object;
		// String sql = "update ldi_events set player_name=?, death_reason=?, death_date=?, xp_lost=? where id=?";
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
