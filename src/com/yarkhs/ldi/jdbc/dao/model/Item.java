package com.yarkhs.ldi.jdbc.dao.model;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.yarkhs.ldi.LdiConfig;
import com.yarkhs.ldi.jdbc.dao.ItemDAO;

public class Item {

	Integer id;
	String type;
	Integer typeId;
	Integer Amount;
	Short durability;
	Boolean hasEnchantment;
	List<Enchantment> enchantments;
	Event event;


	public Item(ItemStack item) {
		this(item.getType().toString(), item.getTypeId(), item.getAmount(), item.getDurability(), true);
	}


	public Item(String type, Integer typeId, Integer amount, Short durability, Boolean itemInHand) {
		super();
		this.type = type;
		this.typeId = typeId;
		Amount = amount;
		this.durability = durability;
	}


	public Item() {
		super();
		hasEnchantment = false;
	}


	public Item(Integer id) {
		this();
		this.id = id;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getAmount() {
		return Amount;
	}


	public void setAmount(Integer amount) {
		Amount = amount;
	}


	public Short getDurability() {
		return durability;
	}


	public void setDurability(Short durability) {
		this.durability = durability;
	}


	public List<Enchantment> getEnchantments() {
		return enchantments;
	}


	public void setEnchantments(List<Enchantment> enchantments) {
		this.enchantments = enchantments;
	}


	public Event getEvent() {
		return event;
	}


	public void setEvent(Event event) {
		this.event = event;
	}


	public Boolean getHasEnchantment() {
		return hasEnchantment;
	}


	public void setHasEnchantment(Boolean hasEnchantment) {
		this.hasEnchantment = hasEnchantment;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getTypeId() {
		return typeId;
	}


	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}


	public void save(LdiConfig ldiConfig) throws SQLException {
		ItemDAO itemDAO = new ItemDAO(ldiConfig);

		this.id = itemDAO.insert(this);

		for (com.yarkhs.ldi.jdbc.dao.model.Enchantment enchantment : getEnchantments()) {
			enchantment.setItem(this);
			enchantment.save(ldiConfig);
		}

	}


	@Override
	public String toString() {
		return "Item [id=" + id + ", type=" + type + ", typeId=" + typeId + ", Amount=" + Amount + ", durability=" + durability + ", hasEnchantment="
				+ hasEnchantment + ", enchantments=" + enchantments + ", event=" + event + "]";
	}

}
