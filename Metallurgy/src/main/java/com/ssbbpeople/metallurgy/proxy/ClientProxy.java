package com.ssbbpeople.metallurgy.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {
	
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	public static boolean isInRectangle(final int x, final int y, final int xSize, final int ySize, final int mouseX, final int mouseY) {
		return (mouseX >= x) && (mouseX <= (x + xSize)) && (mouseY >= y) && (mouseY <= (y + ySize));
	}
	
}
