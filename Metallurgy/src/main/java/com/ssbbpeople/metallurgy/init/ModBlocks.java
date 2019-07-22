package com.ssbbpeople.metallurgy.init;

import java.util.ArrayList;
import java.util.List;

import com.ssbbpeople.metallurgy.blocks.BlockBase;
import com.ssbbpeople.metallurgy.blocks.ExtractedIronOre;
import com.ssbbpeople.metallurgy.blocks.grinder.Grinder;
import com.ssbbpeople.metallurgy.blocks.tileentities.TileEntityGrinder;
import com.ssbbpeople.metallurgy.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks 
	{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block EXTRACTED_IRON_ORE = new ExtractedIronOre("extracted_iron_ore", Material.IRON);
	public static final Block GRINDER = new Grinder();
	
	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				GRINDER
		);
		GameRegistry.registerTileEntity(TileEntityGrinder.class, new ResourceLocation(Reference.MOD_ID, Reference.te_grinder));
		}
}