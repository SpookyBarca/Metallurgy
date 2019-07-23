package com.ssbbpeople.metallurgy.util.handlers;

import javax.annotation.Nullable;

import com.ssbbpeople.metallurgy.blocks.grinder.GrinderContainer;
import com.ssbbpeople.metallurgy.blocks.grinder.GuiModGrinder;
import com.ssbbpeople.metallurgy.blocks.grinder.TileGrinder;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModGuiHandler implements IGuiHandler {

	public static final int MOD_GRINDER = 2;
	
	//Gets server GUI element, @return a {@link net.minecraft.inventory.Container Container} for the server
	@Nullable
	public Container getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		switch (ID) {
		case MOD_GRINDER:
			return new GrinderContainer(player.inventory, (TileGrinder) world.getTileEntity(new BlockPos(x, y, z)));
		default:
			return null;
		}
	}
	@Nullable
	@SideOnly(Side.CLIENT)
	public Gui getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		switch (ID) {
		case MOD_GRINDER:
			return new GuiModGrinder(player.inventory, (TileGrinder) world.getTileEntity(new BlockPos(x, y, z)));
		
		default:
			return null;
		}
	}
}
