package com.ssbbpeople.metallurgy.blocks.grinder.slots;

import com.ssbbpeople.metallurgy.blocks.grinder.ContainerGrinder;
import com.ssbbpeople.metallurgy.blocks.grinder.GrinderTileEntity;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import scala.sys.process.ProcessBuilderImpl.IStreamBuilder;

public class SlotGrinderFuel extends Slot
{
	public SlotGrinderFuel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) 
	{
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) 
	{
		return ContainerGrinder.isItemFuel(stack) || isBucket(stack);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) 
	{
		return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
	}
	
	public static boolean isBucket(ItemStack stack)
	{
		return stack.getItem() == Items.BUCKET;
	}
}