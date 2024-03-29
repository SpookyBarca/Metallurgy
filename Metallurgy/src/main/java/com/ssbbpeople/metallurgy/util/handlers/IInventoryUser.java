package com.ssbbpeople.metallurgy.util.handlers;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IInventoryUser extends ICapabilitySerializable<NBTTagCompound>{

	String INVENTORY_TAG = "inventory";

	@Nonnull
	ModItemStackHandler getInventory();

	default NBTTagCompound serializeNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setTag(INVENTORY_TAG, getInventory().serializeNBT());
		return compound;
	}

	default void deserializeNBT(final NBTTagCompound compound) {
		if (compound.hasKey(INVENTORY_TAG)) {
			this.getInventory().deserializeNBT(compound.getCompoundTag(INVENTORY_TAG));
		}
	}
	
}
