package com.ssbbpeople.metallurgy.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
	
	public static void init() {
			GameRegistry.addSmelting(ModBlocks.EXTRACTED_IRON_ORE, new ItemStack(Items.IRON_INGOT, 2), 1.4f);
			GameRegistry.addSmelting(ModItems.PULVERIZED_IRON, new ItemStack(Items.IRON_INGOT, 2), 1.4f);
	}
	
}
