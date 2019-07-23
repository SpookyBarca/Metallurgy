package com.ssbbpeople.metallurgy.init;

import java.util.ArrayList;
import java.util.List;

import com.ssbbpeople.metallurgy.blocks.BlockBase;
import com.ssbbpeople.metallurgy.blocks.ExtractedIronOre;
import com.ssbbpeople.metallurgy.blocks.grinder.BlockGrinder;
import com.ssbbpeople.metallurgy.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks 
	{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block EXTRACTED_IRON_ORE = new ExtractedIronOre("extracted_iron_ore", Material.IRON);
	public static final Block GRINDER = new BlockGrinder();;
	
	//@GameRegistry.ObjectHolder("mm:grinder")
	//public static BlockGrinder grinderBlock;
	
	//@SideOnly(Side.CLIENT)
	//public static void initModels() {
		//GRINDER.intModels;
	//}
	
}