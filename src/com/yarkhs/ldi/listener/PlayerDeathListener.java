package com.yarkhs.ldi.listener;

import org.bukkit.event.Listener;

public class PlayerDeathListener implements Listener {

//	public PlayerDeathListener(LogDeadInventory plugin) {
//		plugin.getServer().getPluginManager().registerEvents(this, plugin);
//	}
//
//
//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void onPlayerDeath(PlayerDeathEvent e) {
//		getLogger().info(PREFIX + "entrou no onPlayerDeath");
//		if (e.getEntity() instanceof Player) {
//
//			//Preenchimento
//			Player player = e.getEntity();
//
//			Event event = new Event();
//			event.setPlayerName(player.getName());
//			event.setDeathDate(new Date());
//			event.setDeathReason(e.getDeathMessage());
//			event.setXp(e.getDroppedExp());
//
//			List<Item> items = new ArrayList<Item>();
//			for (ItemStack itemStack : e.getDrops()) {
//				Item item = new Item();
//				item.setMinecraftItemId(itemStack.getTypeId());
//				item.setAmount(itemStack.getAmount());
//				item.setDurability(itemStack.getDurability());
//
//				List<ldi.jdbc.dao.model.Enchantment> enchantments = new ArrayList<ldi.jdbc.dao.model.Enchantment>();
//				//Gets a map containing all enchantments and their levels on this item.
//				for (Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
//					Enchantment key = entry.getKey(); //enchantment
//					Integer value = entry.getValue(); //level
//
//					ldi.jdbc.dao.model.Enchantment enchantment = new ldi.jdbc.dao.model.Enchantment();
//					enchantment.setBukkitEnchantmentId(key.getId());
//					enchantment.setName(key.getName());
//					enchantment.setLevel(value);
//
//					enchantments.add(enchantment);
//				}
//
//				item.setEnchantments(enchantments);
//				items.add(item);
//			}
//			event.setItens(items);
//
//			//save
//
//			try {
//				EventDAO eventDAO = new EventDAO(sqlServer, sqlDatabase, sqlUser, sqlPassword);
//				ItemDAO itemDAO = new ItemDAO(sqlServer, sqlDatabase, sqlUser, sqlPassword);
//				EnchantmentDAO enchantmentDAO = new EnchantmentDAO(sqlServer, sqlDatabase, sqlUser, sqlPassword);
//
//				eventDAO.insert(event);
//
//				for (Item item : event.getItens()) {
//					itemDAO.insert(event);
//
//					for (ldi.jdbc.dao.model.Enchantment enchantment : item.getEnchantments()) {
//						enchantmentDAO.insert(enchantment);
//					}
//				}
//
//				getLogger().info(PREFIX + "salvou inventario do jogador com sucesso");
//			} catch (Exception exception) {
//				getLogger().info("------------------------------------------------------------------");
//				getLogger().info(PREFIX + "Something went wrong in time to save the information of the player who died.");
//				exception.printStackTrace();
//				getLogger().info("------------------------------------------------------------------");
//			}
//
//		}
//
//	}
}