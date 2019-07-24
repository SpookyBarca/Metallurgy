package com.ssbbpeople.metallurgy.blocks.grinder;

import com.ssbbpeople.metallurgy.util.ModUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class GrinderContainer extends Container{
	
	public GrinderContainer(final InventoryPlayer playerInv, final TileGrinder modFurnace) {

		this.addSlotToContainer(new SlotItemHandler(modFurnace.getInventory(), TileGrinder.INPUT_SLOT, 56, 17) {

		});

		this.addSlotToContainer(new SlotItemHandler(modFurnace.getInventory(), TileGrinder.FUEL_SLOT, 56, 53) {

		});

		this.addSlotToContainer(new SlotItemHandler(modFurnace.getInventory(), TileGrinder.OUTPUT_SLOT, 116, 35) {

		});

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
			}
		}
		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + (k * 18), 142));
		}

	}
	
	@Override
	public boolean canInteractWith(final EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int index) {
		return ModUtil.transferStackInSlot(player, index, this);
	}
}
