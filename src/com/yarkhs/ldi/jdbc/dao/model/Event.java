package com.yarkhs.ldi.jdbc.dao.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Event {

	Integer id;
	String playerName;
	String playerWorld;
	Integer playerLocationX;
	Integer playerLocationY;
	Integer playerLocationZ;
	Item playerItemInHand;

	String killerName;
	String killerWorld;
	Integer killerLocationX;
	Integer killerLocationY;
	Integer killerLocationZ;
	Item killerItemInHand;

	String deathReason;
	Date deathDate;
	Integer xpLost;
	List<Item> items;


	public Event() {

	}


	public Event(Integer id) {
		super();
		this.id = id;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getPlayerName() {
		return playerName;
	}


	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	public String getPlayerWorld() {
		return playerWorld;
	}


	public void setPlayerWorld(String playerWorld) {
		this.playerWorld = playerWorld;
	}


	public Integer getPlayerLocationX() {
		return playerLocationX;
	}


	public void setPlayerLocationX(Integer playerLocationX) {
		this.playerLocationX = playerLocationX;
	}


	public Integer getPlayerLocationY() {
		return playerLocationY;
	}


	public void setPlayerLocationY(Integer playerLocationY) {
		this.playerLocationY = playerLocationY;
	}


	public Integer getPlayerLocationZ() {
		return playerLocationZ;
	}


	public void setPlayerLocationZ(Integer playerLocationZ) {
		this.playerLocationZ = playerLocationZ;
	}


	public Item getPlayerItemInHand() {
		return playerItemInHand;
	}


	public void setPlayerItemInHand(Item playerItemInHand) {
		this.playerItemInHand = playerItemInHand;
	}


	public String getKillerName() {
		return killerName;
	}


	public void setKillerName(String killerName) {
		this.killerName = killerName;
	}


	public String getKillerWorld() {
		return killerWorld;
	}


	public void setKillerWorld(String killerWorld) {
		this.killerWorld = killerWorld;
	}


	public Integer getKillerLocationX() {
		return killerLocationX;
	}


	public void setKillerLocationX(Integer killerLocationX) {
		this.killerLocationX = killerLocationX;
	}


	public Integer getKillerLocationY() {
		return killerLocationY;
	}


	public void setKillerLocationY(Integer killerLocationY) {
		this.killerLocationY = killerLocationY;
	}


	public Integer getKillerLocationZ() {
		return killerLocationZ;
	}


	public void setKillerLocationZ(Integer killerLocationZ) {
		this.killerLocationZ = killerLocationZ;
	}


	public Item getKillerItemInHand() {
		return killerItemInHand;
	}


	public void setKillerItemInHand(Item killerItemInHand) {
		this.killerItemInHand = killerItemInHand;
	}


	public String getDeathReason() {
		return deathReason;
	}


	public void setDeathReason(String deathReason) {
		this.deathReason = deathReason;
	}


	public Date getDeathDate() {
		return deathDate;
	}


	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}


	public Integer getXpLost() {
		return xpLost;
	}


	public void setXpLost(Integer xpLost) {
		this.xpLost = xpLost;
	}


	public List<Item> getItens() {
		return items;
	}


	public void setItens(List<Item> items) {
		this.items = items;
	}


	public void setDeathDate(String deathDateStr) {
		try {
			this.deathDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(deathDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	public String getDeathDateString() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");

		return sdf.format(this.deathDate);
	}


	// public java.sql.Date getDeathDateSql() {
	// return new java.sql.Date(deathDate.getTime());
	// }

	public void setPlayerLocationX(Double x) {
		this.playerLocationX = x.intValue();
	}


	public void setPlayerLocationY(Double y) {
		this.playerLocationY = y.intValue();
	}


	public void setPlayerLocationZ(Double z) {
		this.playerLocationZ = z.intValue();
	}


	public void setKillerLocationX(Double x) {
		this.killerLocationX = x.intValue();
	}


	public void setKillerLocationY(Double y) {
		this.killerLocationY = y.intValue();
	}


	public void setKillerLocationZ(Double z) {
		this.killerLocationZ = z.intValue();
	}


	@Override
	public String toString() {
		return "Event [id=" + id + ", playerName=" + playerName + ", playerWorld=" + playerWorld + ", playerLocationX=" + playerLocationX + ", playerLocationY=" + playerLocationY
				+ ", playerLocationZ=" + playerLocationZ + ", playerItemInHand=" + playerItemInHand + ", killerName=" + killerName + ", killerWorld=" + killerWorld + ", killerLocationX="
				+ killerLocationX + ", killerLocationY=" + killerLocationY + ", killerLocationZ=" + killerLocationZ + ", killerItemInHand=" + killerItemInHand + ", deathReason=" + deathReason
				+ ", deathDate=" + deathDate + ", xpLost=" + xpLost + ", items=" + items + "]";
	}

}
