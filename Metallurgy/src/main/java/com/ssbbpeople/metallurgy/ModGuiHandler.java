package com.ssbbpeople.metallurgy;

import com.ssbbpeople.metallurgy.blocks.grinder.ContainerGrinder;
import com.ssbbpeople.metallurgy.blocks.grinder.GrinderTileEntity;
import com.ssbbpeople.metallurgy.blocks.grinder.GuiGrinder;
import com.ssbbpeople.metallurgy.blocks.tileentities.TileEntityGrinder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
	public static final int GRINDER = 0;
	
	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case GRINDER:
				return new ContainerGrinder(player.inventory, (TileEntityGrinder)world.getTileEntity(new BlockPos(x, y, z)));
			default:
				return null;
		}
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case GRINDER:
				return new GuiGrinder(getServerGuiElement(0, player, world, x, y, z), player.inventory);
			default:
				return null;
		}
}
}
