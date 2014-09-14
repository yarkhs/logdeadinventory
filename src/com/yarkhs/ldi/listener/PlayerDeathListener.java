package com.yarkhs.ldi.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.yarkhs.ldi.LdiConfig;
import com.yarkhs.ldi.jdbc.dao.EnchantmentDAO;
import com.yarkhs.ldi.jdbc.dao.EventDAO;
import com.yarkhs.ldi.jdbc.dao.ItemDAO;
import com.yarkhs.ldi.jdbc.dao.model.Event;
import com.yarkhs.ldi.jdbc.dao.model.Item;
import com.yarkhs.ldi.util.Util;

public class PlayerDeathListener implements Listener {

	public ConsoleCommandSender console = Bukkit.getConsoleSender();

	public final String PrefixYellow = ChatColor.YELLOW.toString();
	public final String PrefixBlue = ChatColor.DARK_AQUA.toString();
	public final String PrefixRed = ChatColor.DARK_RED.toString();

	public final String PrefixYellowConsole = ChatColor.GOLD + "[LogDeadInventory] " + ChatColor.YELLOW;
	public final String PrefixBlueConsole = ChatColor.BLUE + "[LogDeadInventory] " + ChatColor.DARK_AQUA;
	public final String PrefixRedConsole = ChatColor.RED + "[LogDeadInventory] " + ChatColor.DARK_RED;

	private LdiConfig ldiConfig;


	public PlayerDeathListener(LdiConfig ldiConfig) {
		super();
		this.ldiConfig = ldiConfig;
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {

			Event event = fillEvent(e);

			List<Item> items = new ArrayList<Item>();
			for (ItemStack itemStack : e.getDrops()) {
				Item item = new Item();
				item.setType(itemStack.getType().toString());
				item.setAmount(itemStack.getAmount());
				item.setDurability(itemStack.getDurability());

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

				items.add(item);
			}
			event.setItens(items);

			// save
			try {
				EventDAO eventDAO = new EventDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
				ItemDAO itemDAO = new ItemDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
				EnchantmentDAO enchantmentDAO = new EnchantmentDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());

				if (!Util.empty(event.getPlayerItemInHand())) {
					itemDAO.insert(event.getPlayerItemInHand());
					event.setPlayerItemInHand(itemDAO.findBy(event.getPlayerItemInHand()));
				}

				if (!Util.empty(event.getKillerName()) && !Util.empty(event.getKillerItemInHand())) {
					itemDAO.insert(event.getKillerItemInHand());
					event.setKillerItemInHand(itemDAO.findBy(event.getKillerItemInHand()));
				}

				eventDAO.insert(event);
				event = eventDAO.findBy(event);

				for (Item item : event.getItens()) {
					item.setEvent(event);

					itemDAO.insert(item);
					item = itemDAO.findBy(item);

					for (com.yarkhs.ldi.jdbc.dao.model.Enchantment enchantment : item.getEnchantments()) {
						enchantment.setItem(item);
						enchantmentDAO.insert(enchantment);
					}
				}

			} catch (Exception exception) {
				console.sendMessage("------------------------------------------------------------------");
				console.sendMessage(PrefixRed + "Something went wrong in time to save the information of the player who died.");
				exception.printStackTrace();
				console.sendMessage("------------------------------------------------------------------");
			}

		}

	}


	private Event fillEvent(PlayerDeathEvent e) {

		Player player = e.getEntity();
		Player killer = player.getKiller();

		Event event = new Event();

		event.setPlayerName(player.getName());
		event.setPlayerWorld(player.getWorld().getName());
		event.setPlayerLocationX(player.getLocation().getX());
		event.setPlayerLocationY(player.getLocation().getY());
		event.setPlayerLocationZ(player.getLocation().getZ());

		if (!Util.empty(player.getItemInHand()) && !Util.empty(player.getItemInHand().getType())) {
			Item playerItem = new Item();
			playerItem.setType(player.getItemInHand().getType().toString());
			playerItem.setAmount(player.getItemInHand().getAmount());
			playerItem.setDurability(player.getItemInHand().getDurability());
			playerItem.setItemInHand(true);
			event.setPlayerItemInHand(playerItem);
		}

		if (!Util.empty(killer)) {
			event.setKillerName(killer.getName());
			event.setKillerWorld(killer.getWorld().getName());
			event.setKillerLocationX(killer.getLocation().getX());
			event.setKillerLocationY(killer.getLocation().getY());
			event.setKillerLocationZ(killer.getLocation().getZ());

			Item killerItem = new Item();
			killerItem.setType(killer.getItemInHand().getType().toString());
			killerItem.setAmount(killer.getItemInHand().getAmount());
			killerItem.setDurability(killer.getItemInHand().getDurability());
			killerItem.setItemInHand(true);
			event.setKillerItemInHand(killerItem);
		}

		event.setDeathDate(new Date());
		event.setDeathReason(e.getDeathMessage());
		event.setXpLost(e.getDroppedExp());

		return event;
	}
}