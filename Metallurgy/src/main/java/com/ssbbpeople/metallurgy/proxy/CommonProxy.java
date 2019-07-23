package com.ssbbpeople.metallurgy.proxy;

import com.ssbbpeople.metallurgy.blocks.grinder.BlockGrinder;
import com.ssbbpeople.metallurgy.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public void registerItemRenderer(Item item, int metadata, String id) {
		
	}

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockGrinder());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(ModBlocks.GRINDER).setRegistryName(ModBlocks.GRINDER.getRegistryName()));
    }
    

	public Side getPhysicalSide() {
		return Side.SERVER;
	}

	public String localize(Block blockType) {
		// TODO Auto-generated method stub
		return null;
	}
}
