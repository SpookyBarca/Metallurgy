package com.ssbbpeople.metallurgy;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.ssbbpeople.metallurgy.blocks.grinder.TileGrinder;
import com.ssbbpeople.metallurgy.init.ModBlocks;
import com.ssbbpeople.metallurgy.init.ModItems;
import com.ssbbpeople.metallurgy.init.ModRecipes;
import com.ssbbpeople.metallurgy.proxy.CommonProxy;
import com.ssbbpeople.metallurgy.util.Reference;
import com.ssbbpeople.metallurgy.util.handlers.ModGuiHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.Version)
public class Main {
	
	public static final CreativeTabs METALLURGY = (new CreativeTabs("metallurgy")
	{
		public ItemStack getTabIconItem()
		{
			return new ItemStack(ModItems.CHISEL);
		}
	});
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
	}
	
	@EventHandler
	public static void Init(FMLInitializationEvent event)
	{
		ModRecipes.init();
		GameRegistry.registerTileEntity(TileGrinder.class, new ResourceLocation(Reference.MOD_ID + ":grinder"));
			
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	
}
