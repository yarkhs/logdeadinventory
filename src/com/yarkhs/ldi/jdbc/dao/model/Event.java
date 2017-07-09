package com.yarkhs.ldi.jdbc.dao.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.yarkhs.ldi.LdiConfig;
import com.yarkhs.ldi.jdbc.dao.EventDAO;
import com.yarkhs.ldi.util.Util;

public class Event {

	Integer id;
	String playerName;
	String playerLocation;
	Item playerItemInHand;

	String killerName;
	String killerLocation;
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


	public Event(PlayerDeathEvent e) {
		super();
		Player player = e.getEntity();

		setPlayerName(player.getName());
		setPlayerLocation(player.getWorld(), player.getLocation());
		setPlayerItemInHand(new Item(player.getItemInHand()));

		if (!Util.empty(player.getKiller())) {
			setKillerName(player.getKiller().getName());
			setKillerLocation(player.getKiller().getWorld(), player.getKiller().getLocation());
			setKillerItemInHand(new Item(player.getKiller().getItemInHand()));
		}

		setDeathDate(new Date());
		setDeathReason(e.getDeathMessage());
		setXpLost(e.getDroppedExp());

		this.items = new ArrayList<Item>();

		for (ItemStack itemStack : e.getDrops()) {
			Item item = new Item(itemStack.getType().toString(), itemStack.getTypeId(), itemStack.getAmount(), itemStack.getDurability(), false);

			List<com.yarkhs.ldi.jdbc.dao.model.Enchantment> enchantments = new ArrayList<com.yarkhs.ldi.jdbc.dao.model.Enchantment>();
			// Gets a map containing all enchantments and their levels on this item.
			for (Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
				Enchantment key = entry.getKey(); // enchantment
				Integer value = entry.getValue(); // level

				com.yarkhs.ldi.jdbc.dao.model.Enchantment enchantment = new com.yarkhs.ldi.jdbc.dao.model.Enchantment();
				enchantment.setType(key.getName());
				enchantment.setLevel(value);

				enchantments.add(enchantment);
			}

			item.setEnchantments(enchantments);
			if (item.getEnchantments().size() > 0) {
				item.setHasEnchantment(true);
			}

			this.items.add(item);
		}
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public List<Item> getItems() {
		return items;
	}


	public void setItems(List<Item> items) {
		this.items = items;
	}


	public String getPlayerName() {
		return playerName;
	}


	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	public String getPlayerLocation() {
		return playerLocation;
	}


	public void setPlayerLocation(String playerLocation) {
		this.playerLocation = playerLocation;
	}


	public void setPlayerLocation(World world, Location location) {
		this.playerLocation = world.getName() + " - " + location.getX() + "," + location.getY() + "," + location.getZ();
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


	public String getKillerLocation() {
		return killerLocation;
	}


	public void setKillerLocation(String killerLocation) {
		this.killerLocation = killerLocation;
	}


	public void setKillerLocation(World world, Location location) {
		this.killerLocation = world.getName() + " - " + location.getX() + "," + location.getY() + "," + location.getZ();
	}


	public Item getKillerItemInHand() {
		return killerItemInHand;
	}


	public void setKillerItemInHand(Item killerItemInHand) {
		this.killerItemInHand = killerItemInHand;
	}


	public void save(LdiConfig ldiConfig) throws SQLException {
		EventDAO eventDAO = new EventDAO(ldiConfig);

		if (!Util.empty(playerItemInHand)) {
			playerItemInHand.save(ldiConfig);
		}

		if (!Util.empty(killerName) && !Util.empty(killerItemInHand)) {
			killerItemInHand.save(ldiConfig);
		}

		this.id = eventDAO.insert(this);

		for (Item item : getItens()) {
			item.setEvent(this);
			item.save(ldiConfig);
		}

	}

}
