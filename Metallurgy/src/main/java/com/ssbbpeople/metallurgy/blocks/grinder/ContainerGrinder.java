package com.ssbbpeople.metallurgy.blocks.grinder;

import com.ssbbpeople.metallurgy.blocks.grinder.slots.SlotGrinderFuel;
import com.ssbbpeople.metallurgy.blocks.grinder.slots.SlotGrinderOutput;
import com.ssbbpeople.metallurgy.blocks.tileentities.TileEntityGrinder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
public class ContainerGrinder extends Container {
	
	private IInventory TileEntityGrinder;
	GrinderTileEntity tileentity;
    private IItemHandler InvHandler;
    private final IInventory tileFurnace;
	private int cookTime, totalCookTime, ovenBurnTime, currentItemBurnTime;
	
	public ContainerGrinder(InventoryPlayer playerInventory, IInventory furnaceInventory, GrinderTileEntity containerTileEntity) 
	{
		this.tileentity = containerTileEntity;
		this.tileFurnace = furnaceInventory;
		this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));
		this.addSlotToContainer(new SlotGrinderFuel(furnaceInventory, 1, 56, 53));
		this.addSlotToContainer(new SlotGrinderOutput(playerInventory.player, furnaceInventory, 2, 116, 35));
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for(int k = 0; k < 9; k++)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}
	
	public ContainerGrinder(IInventory playerInventory, IInventory furnaceInventory, GrinderTileEntity containerTileEntity) {
		this.tileentity = containerTileEntity;
		this.tileFurnace = furnaceInventory;
        this.InvHandler = ((ICapabilityProvider) tileentity).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}

	@Override
	public void addListener(IContainerListener listener) 
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.TileEntityGrinder);
	}
	
	@Override
	public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.cookTime != this.TileEntityGrinder.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.TileEntityGrinder.getField(2));
            }

            if (this.ovenBurnTime != this.TileEntityGrinder.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.TileEntityGrinder.getField(0));
            }

            if (this.currentItemBurnTime != this.TileEntityGrinder.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.TileEntityGrinder.getField(1));
            }

            if (this.totalCookTime != this.TileEntityGrinder.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.TileEntityGrinder.getField(3));
            }
        }

        this.cookTime = this.TileEntityGrinder.getField(2);
        this.ovenBurnTime = this.TileEntityGrinder.getField(0);
        this.currentItemBurnTime = this.TileEntityGrinder.getField(1);
        this.totalCookTime = this.TileEntityGrinder.getField(3);
	}

	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) 
	{
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (!GrinderRecipes.instance().getCookingResult(itemstack1).isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (ContainerGrinder.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
	
	 public static int getItemBurnTime(ItemStack stack)
	    {
	        if (stack.isEmpty())
	        {
	            return 0;
	        }
	        else
	        {
	            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
	            if (burnTime >= 0) return burnTime;
	            Item item = stack.getItem();

	            if (item == Items.REDSTONE)
	            {
	                return 300;
	            }
	            else if (item ==Item.getItemFromBlock(Blocks.REDSTONE_BLOCK)) 
	            {
	            	return 4000;
	            }
	        }
			return 300;
	    }
	
    public static boolean isItemFuel(ItemStack stack)
    {
    	return getItemBurnTime(stack) > 0;
    }
    
	public ContainerGrinder(InventoryPlayer playerInventory, IInventory furnaceInventory) 
	{
		this.TileEntityGrinder = furnaceInventory;
		this.tileFurnace = furnaceInventory;
		this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));
		this.addSlotToContainer(new SlotGrinderFuel(furnaceInventory, 1, 56, 53));
		this.addSlotToContainer(new SlotGrinderOutput(playerInventory.player, furnaceInventory, 2, 116, 35));
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for(int k = 0; k < 9; k++)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}
	
	//Tile Entities
	@Override
	public void updateProgressBar(int id, int data) 
	{
		this.TileEntityGrinder.setField(id, data);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return  this.TileEntityGrinder.isUsableByPlayer(playerIn);
	}
    
	}
