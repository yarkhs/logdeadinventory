package com.yarkhs.ldi.jdbc.dao.model;

import java.util.List;

public class Item {

	Integer id;
	String type;
	Integer typeId;
	Integer Amount;
	Short durability;
	Boolean itemInHand;
	Boolean hasEnchantment;
	List<Enchantment> enchantments;
	Event event;


	public Item() {
		super();
		itemInHand = false;
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


	public Boolean getItemInHand() {
		return itemInHand;
	}


	public void setItemInHand(Boolean itemInHand) {
		this.itemInHand = itemInHand;
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


	@Override
	public String toString() {
		return "Item [id=" + id + ", type=" + type + ", typeId=" + typeId + ", Amount=" + Amount + ", durability=" + durability + ", itemInHand=" + itemInHand + ", hasEnchantment=" + hasEnchantment
				+ ", enchantments=" + enchantments + ", event=" + event + "]";
	}

}
