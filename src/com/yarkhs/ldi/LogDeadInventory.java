package com.yarkhs.ldi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import com.yarkhs.ldi.jdbc.dao.EnchantmentDAO;
import com.yarkhs.ldi.jdbc.dao.EventDAO;
import com.yarkhs.ldi.jdbc.dao.ItemDAO;
import com.yarkhs.ldi.jdbc.dao.model.Event;
import com.yarkhs.ldi.jdbc.dao.model.Item;
import com.yarkhs.ldi.listener.PlayerDeathListener;
import com.yarkhs.ldi.util.DateUtil;
import com.yarkhs.ldi.util.Util;

public final class LogDeadInventory extends JavaPlugin implements Listener {

	public ConsoleCommandSender console = Bukkit.getConsoleSender();

	public final String PrefixYellow = ChatColor.YELLOW.toString();
	public final String PrefixBlue = ChatColor.DARK_AQUA.toString();
	public final String PrefixRed = ChatColor.DARK_RED.toString();

	public final String PrefixYellowConsole = ChatColor.GOLD + "[LogDeadInventory] " + ChatColor.YELLOW;
	public final String PrefixBlueConsole = ChatColor.BLUE + "[LogDeadInventory] " + ChatColor.DARK_AQUA;
	public final String PrefixRedConsole = ChatColor.RED + "[LogDeadInventory] " + ChatColor.DARK_RED;

	public File configFile;
	public FileConfiguration config;
	public LdiConfig ldiConfig;


	@Override
	public void onEnable() {

		loadConfig();

		console.sendMessage("LogDeadInventory by Yarkhs enabled");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new PlayerDeathListener(ldiConfig), this);

		try {
			EventDAO eventDAO = new EventDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
			ItemDAO itemDAO = new ItemDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
			EnchantmentDAO enchantmentDAO = new EnchantmentDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());

			if (ldiConfig.getIsMySQL()) {
				eventDAO.createTableMySql();
				itemDAO.createTableMySql();
				enchantmentDAO.createTableMySql();
			} else {
				eventDAO.createTableSqlite();
				itemDAO.createTableSqlite();
				enchantmentDAO.createTableSqlite();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
			// Failed to submit the stats :-(
		}

	}


	@Override
	public void onDisable() {
		console.sendMessage("LogDeadInventory disabled");
		// try {
		// config.save(configFile);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}


	private void loadConfig() {

		configFile = new File(getDataFolder(), "config.yml");
		try {
			if (!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				copy(getResource("config.yml"), configFile);
				console.sendMessage(PrefixBlueConsole + "Config.yml created!");
			}

			config = new YamlConfiguration();
			config.load(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		console.sendMessage(PrefixBlueConsole + "config.yml loaded successfully");

		ldiConfig = new LdiConfig();

		try {
			ldiConfig.setIsMySQL(getConfig().getBoolean("isMySQL"));
		} catch (Exception e) {
			printError(e, "isMySQL");
		}

		try {
			ldiConfig.setServer(getConfig().getString("Server"));
		} catch (Exception e) {
			printError(e, "Server");
		}

		try {
			ldiConfig.setDatabase(getConfig().getString("Database"));
		} catch (Exception e) {
			printError(e, "Database");
		}

		try {
			ldiConfig.setUser(getConfig().getString("User"));
		} catch (Exception e) {
			printError(e, "User");
		}

		try {
			ldiConfig.setPassword(getConfig().getString("Password"));
		} catch (Exception e) {
			printError(e, "Password");
		}
	}


	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void printError(Exception e, String nameParameter) {
		console.sendMessage(PrefixBlueConsole + "==================================================");
		console.sendMessage(PrefixYellowConsole + " LogDeadInventory - error in config file. The default value will be used.");
		console.sendMessage(PrefixRedConsole + "parameter: " + nameParameter);
		console.sendMessage(PrefixBlueConsole + "---------------------ERROR------------------------");
		console.sendMessage("         ");
		e.printStackTrace();
		console.sendMessage("         ");
		console.sendMessage(PrefixBlueConsole + "==================================================");
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		if (player.hasPermission("logDeadInventory.use") || player.isOp()) {
			if (cmd.getName().equalsIgnoreCase("logDeadInventory")) {

				if (args.length == 0) {

					player.sendMessage(ChatColor.GREEN + "****************************************************************");
					player.sendMessage(ChatColor.YELLOW + "LogDeadInventory will save the inventory of players every time they die");
					player.sendMessage(ChatColor.YELLOW + "/ldi list " + ChatColor.RED + "[playerName] [dd/mm/yyyy] (optional)" + ChatColor.AQUA + "- show summary list of deaths per player");
					player.sendMessage(ChatColor.YELLOW + "/ldi info " + ChatColor.RED + "[id]" + ChatColor.AQUA + "- show all info about one death event");
					player.sendMessage(ChatColor.YELLOW + "/ldi recover " + ChatColor.RED + "[id]" + ChatColor.AQUA + "- recover items using id");
					player.sendMessage(ChatColor.GREEN + "****************************************************************");

					return true;
				} else if (args[0].equalsIgnoreCase("list")) {
					return list(player, args);

				} else if (args[0].equalsIgnoreCase("info")) {
					return info(player, args);

				} else if (args[0].equalsIgnoreCase("recover")) {
					return recover(player, args);

				}

			}
		}
		return false;
	}


	private Boolean list(Player player, String[] args) {
		if (args.length == 1) {
			player.sendMessage(ChatColor.YELLOW + "use /ldi list " + ChatColor.RED + "[playerName]" + ChatColor.YELLOW + " or ");
			player.sendMessage(ChatColor.YELLOW + "use /ldi list " + ChatColor.RED + "[playerName] [deathDate] (dd/mm/yyyy)");
			return true;
		} else if (args.length > 1) {
			String playerName = args[1];
			Date deathDate = null;

			if (args.length > 2) {

				if (ldiConfig.getIsMySQL()) {
					try {
						deathDate = DateUtil.stringToDate(args[2]);
					} catch (ParseException e) {
						player.sendMessage("Date format wrong. dd/mm/yyyy");
						e.printStackTrace();
					}
				} else {
					player.sendMessage("Date params only works on MySql");
				}
			}

			try {
				EventDAO eventDAO = new EventDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
				ItemDAO itemDAO = new ItemDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());

				List<Event> events = null;

				if (Util.empty(deathDate)) {
					events = eventDAO.findByPlayer(playerName);
				} else {
					events = eventDAO.findByPlayerAndDate(playerName, deathDate);
				}

				String str = ChatColor.WHITE + "-" + ChatColor.GREEN;
				if (events.size() > 0) {
					player.sendMessage(ChatColor.GREEN + "Id" + str + "Death Reason" + str + "Death Date" + str + "Killer" + str + "Drop Count");
				}

				String str2 = ChatColor.DARK_RED + "-" + ChatColor.WHITE;
				for (Event event : events) {
					String killerName = "none";

					if (!Util.empty(event.getKillerName())) {
						killerName = event.getKillerName();
					}

					player.sendMessage(ChatColor.WHITE + event.getId().toString() + str2 + event.getDeathReason() + str2 + event.getDeathDateString() + str2 + killerName + str2
							+ itemDAO.findByEventId(event.getId()).size());
				}
			} catch (SQLException e) {
				player.sendMessage(PrefixRed + "Problems with sql");
				e.printStackTrace();
			}
		}

		return true;
	}


	private Boolean info(Player player, String[] args) {
		// TODO - por mais info
		if (args.length == 1) {
			player.sendMessage(ChatColor.YELLOW + "use /ldi info " + ChatColor.RED + "[id]");
			return true;
		}

		if (args.length > 1) {

			Integer eventId = 0;

			try {
				eventId = new Integer(args[1]);
			} catch (Exception e) {
				player.sendMessage(PrefixRed + "you need to use an integer");
				return true;
			}

			try {
				EventDAO eventDAO = new EventDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
				ItemDAO itemDAO = new ItemDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
				EnchantmentDAO enchantmentDAO = new EnchantmentDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());

				Event event = eventDAO.findById(eventId);

				if (!Util.empty(event.getPlayerItemInHand())) {
					Integer itemId = event.getPlayerItemInHand().getId();
					event.setPlayerItemInHand(itemDAO.findById(itemId));
					event.getPlayerItemInHand().setEnchantments(enchantmentDAO.findByItemId(itemId));
				}

				if (!Util.empty(event.getKillerItemInHand())) {
					Integer itemId = event.getKillerItemInHand().getId();
					event.setKillerItemInHand(itemDAO.findById(itemId));
					event.getKillerItemInHand().setEnchantments(enchantmentDAO.findByItemId(itemId));
				}

				player.sendMessage(ChatColor.YELLOW + "Id : " + ChatColor.AQUA + event.getId().toString());
				player.sendMessage(ChatColor.YELLOW + "Death Reason : " + ChatColor.AQUA + event.getDeathReason());
				player.sendMessage(ChatColor.YELLOW + "Death Date : " + ChatColor.AQUA + event.getDeathDateString());
				player.sendMessage(ChatColor.YELLOW + "Drop Count : " + ChatColor.AQUA + itemDAO.findByEventId(event.getId()).size());

				player.sendMessage(ChatColor.YELLOW + "Player : " + ChatColor.AQUA + event.getPlayerName());
				player.sendMessage(ChatColor.YELLOW + "Player xp : " + ChatColor.AQUA + event.getXpLost());
				player.sendMessage(ChatColor.YELLOW + "Player world: " + ChatColor.AQUA + event.getPlayerWorld());
				player.sendMessage(ChatColor.YELLOW + "Player location : " + ChatColor.AQUA + event.getPlayerLocationX() + ", " + event.getKillerLocationY() + ", " + event.getPlayerLocationZ());
				player.sendMessage(ChatColor.YELLOW + "Player item in hand: " + ChatColor.AQUA + event.getPlayerItemInHand().getType());

				if (!Util.empty(event.getKillerName())) {
					player.sendMessage(ChatColor.YELLOW + "Killer : " + ChatColor.AQUA + event.getKillerName());
					player.sendMessage(ChatColor.YELLOW + "Killer world: " + ChatColor.AQUA + event.getKillerWorld());
					player.sendMessage(ChatColor.YELLOW + "Killer location : " + ChatColor.AQUA + event.getKillerLocationX() + ", " + event.getKillerLocationY() + ", " + event.getKillerLocationZ());
					player.sendMessage(ChatColor.YELLOW + "Killer item in hand: " + ChatColor.AQUA + event.getKillerItemInHand().getType());
				}

			} catch (SQLException e) {
				player.sendMessage(PrefixRed + "Problems with sql");
				e.printStackTrace();
			}
		}

		return true;
	}


	private Boolean recover(Player player, String[] args) {
		if (args.length == 1) {
			player.sendMessage(ChatColor.YELLOW + "use /ldi recover " + ChatColor.RED + "[id]");
			return true;
		}

		if (args.length > 1) {

			Integer eventId = 0;

			try {
				eventId = new Integer(args[1]);
			} catch (Exception e) {
				player.sendMessage(PrefixRed + "you need to use an integer");
				return true;
			}

			try {
				EventDAO eventDAO = new EventDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
				ItemDAO itemDAO = new ItemDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());
				EnchantmentDAO enchantmentDAO = new EnchantmentDAO(ldiConfig.getIsMySQL(), ldiConfig.getServer(), ldiConfig.getDatabase(), ldiConfig.getUser(), ldiConfig.getPassword());

				Event event = eventDAO.findById(eventId);

				Player playerExact = getServer().getPlayerExact(event.getPlayerName());
				playerExact.setExp(playerExact.getExp() + event.getXpLost());
				PlayerInventory inventory = playerExact.getInventory();

				for (Item item : itemDAO.findByEventId(eventId)) {
					ItemStack itemstack = new ItemStack(Material.getMaterial(item.getTypeId()), item.getAmount(), item.getDurability());

					if (item.getHasEnchantment()) {
						item.setEnchantments(enchantmentDAO.findByItemId(item.getId()));

						for (com.yarkhs.ldi.jdbc.dao.model.Enchantment enchantment : item.getEnchantments()) {
							itemstack.addEnchantment(Enchantment.getByName(enchantment.getType()), enchantment.getLevel());
						}

					}
					inventory.addItem(itemstack);
				}

				player.sendMessage("Items retrieved successfully");
				playerExact.sendMessage("Items retrieved successfully");
			} catch (SQLException e) {
				player.sendMessage(PrefixRed + "Problems with sql");
				e.printStackTrace();
			}
		}

		return true;
	}

}
