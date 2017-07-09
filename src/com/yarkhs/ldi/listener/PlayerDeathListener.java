package com.yarkhs.ldi.listener;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.yarkhs.ldi.LdiConfig;
import com.yarkhs.ldi.jdbc.dao.model.Event;

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
	public void onPlayerDeath(PlayerDeathEvent deathEvent) {
		if (deathEvent.getEntity() instanceof Player) {

			Event event = new Event(deathEvent);

			// save
			try {
				event.save(ldiConfig);
			} catch (SQLException e) {
				console.sendMessage("------------------------------------------------------------------");
				console.sendMessage(PrefixRed + "Something went wrong in time to save the information of the player who died.");
				e.printStackTrace();
				console.sendMessage("------------------------------------------------------------------");
			}

		}

	}

}