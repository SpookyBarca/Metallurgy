package com.ssbbpeople.metallurgy.items;

import com.ssbbpeople.metallurgy.Main;
import com.ssbbpeople.metallurgy.init.ModItems;
import com.ssbbpeople.metallurgy.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.METALLURGY);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		
		Main.proxy.registerItemRenderer(this, 0, "inventory");
		
	}

}
