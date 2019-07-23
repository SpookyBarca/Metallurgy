package com.ssbbpeople.metallurgy.util.handlers;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.ssbbpeople.metallurgy.blocks.grinder.TileGrinder;
import com.ssbbpeople.metallurgy.init.ModBlocks;
import com.ssbbpeople.metallurgy.init.ModItems;
import com.ssbbpeople.metallurgy.util.IHasModel;
import com.ssbbpeople.metallurgy.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class RegistryHandler {
	
	//Registers Items
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
		
	}
	
	//Registers Blocks
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		
		event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
        GameRegistry.registerTileEntity(TileGrinder.class, new ResourceLocation(Reference.MOD_ID + ":grinder"));
	
	}
	
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(Item item : ModItems.ITEMS) 
		{
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
		for(Block block : ModBlocks.BLOCKS) 
		{
			if(block instanceof IHasModel) 
			{
				((IHasModel)block).registerModels();
			}
		}
	}
	
	public static void initRegistries() {
		}
}
	
