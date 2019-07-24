package com.ssbbpeople.metallurgy.blocks.grinder;

import com.ssbbpeople.metallurgy.event.ModEventFactory;
import com.ssbbpeople.metallurgy.util.handlers.IInventoryUser;
import com.ssbbpeople.metallurgy.util.handlers.IModTileEntity;
import com.ssbbpeople.metallurgy.util.handlers.ITileEntitySyncable;
import com.ssbbpeople.metallurgy.util.handlers.ModItemStackHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
//Replace FR with GR
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileGrinder extends TileEntity implements ITickable, IModTileEntity, ITileEntitySyncable, IInventoryUser{
	
	public static final int	INPUT_SLOT	= 0;
	public static final int	FUEL_SLOT	= 1;
	public static final int	OUTPUT_SLOT	= 2;

	/** The ItemStacks that hold the items currently being used in the furnace */
    private NonNullList<ItemStack> grinderItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
	
	public static final String	FUEL_TIME_REMAINING_TAG	= "fuelTimeRemaining";
	public static final String	SMELT_TIME_TAG			= "smeltTime";
	public static final String	MAX_FUEL_TIME_TAG		= "maxFuelTime";
	public static final String	MAX_SMELT_TIME_TAG		= "maxSmeltTime";
	
	private String furnaceCustomName;

	private final ModItemStackHandler inventory;

	private int	fuelTimeRemaining;
	private int	smeltTime;
	private int	maxFuelTime;
	private int	maxSmeltTime;
	
	public int getMaxFuelTime() {
		return this.maxFuelTime;
	}

	public int getMaxSmeltTime() {
		return this.maxSmeltTime;
	}

	public int getFuelTimeRemaining() {
		return this.fuelTimeRemaining;
	}

	public int getSmeltTime() {
		return this.smeltTime;
	}
	
	public TileGrinder() {
		this.inventory = new ModItemStackHandler(3) {
			
			public ItemStack insertitem(final int slot, final ItemStack stack, final boolean simulate) {
				if(!this.isItemValid(slot, stack)) {
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
			
			@Override
			protected void onContentsChanged(final int slot) {
				if (slot == INPUT_SLOT) {
					final ItemStack input = TileGrinder.this.getInventory().getStackInSlot(INPUT_SLOT);
					if(!input.isEmpty()) {
						TileGrinder.this.maxSmeltTime = TileGrinder.this.getSmeltTime(input);
					} else {
						TileGrinder.this.smeltTime = 0;
						TileGrinder.this.maxSmeltTime = 200;
					}
				}
			}
			
			@Override
			public boolean isItemValid(final int slot, final ItemStack stack) {
				if (slot == INPUT_SLOT) {
					//Replace FR with GR
					if (!GrinderRecipes.instance().getGrindingResult(stack).isEmpty()) {
						return true;
					} else {
						return false;
					}
				}
				if (slot == FUEL_SLOT) {
					if (TileGrinder.isItemFuel(stack)) {
						return true;
					} else {
						return false;
					}
				}
				if (slot == OUTPUT_SLOT) {
					return false;
				}
				return super.isItemValid(slot, stack);
				
			}
		};
	}
	
	protected static boolean isItemFuel(ItemStack stack) {
		Item item = stack.getItem();
		
		
        if (item == Items.REDSTONE)
        {
            return true;
        }
        if (item == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK))
        {
            return true;
        }
        return false;
	}

	@Override
	public void update () {
		if (this.fuelTimeRemaining >0) {
			this.fuelTimeRemaining--;
		}
		
		this.getWorld().checkLight(this.getPos());
		
		if(this.world.isRemote) {
			return;
		}
		this.burnFuel();
		this.smelt();
		this.trySmeltItem();
		//this.syncToClients();
		this.markDirty();
	}
	//Burns fuel from the Grinder
	private void burnFuel () {
		if (!this.shouldSmelt()) {
			return;
		}
		if (this.fuelTimeRemaining > 0) {
			return;
		}
		final ItemStack fuel = this.getInventory().getStackInSlot(FUEL_SLOT);
		
		if (fuel.isEmpty()) {
			return;
		}

		this.fuelTimeRemaining = TileGrinder.getItemBurnTime(fuel.copy());

		this.maxFuelTime = this.fuelTimeRemaining;

		fuel.shrink(1);

		if (!fuel.isEmpty()) {
			return;
		}

		final ItemStack containerItem = fuel.getItem().getContainerItem(fuel.copy());

		if (!containerItem.isEmpty()) {
			this.getInventory().setStackInSlot(FUEL_SLOT, containerItem.copy());
		}
	}
	
	//Burn time for items, note that other items can be inserted but wont do anything.
	public static int getItemBurnTime(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return 0;
        } else {
        	
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
            if (burnTime >= 0) return burnTime;
            Item item = stack.getItem();

            if (item == Items.REDSTONE)
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK))
            {
                return 3200;
            }
           }
        {
            return 0;
        }
    }

	//Tells the Grinder to "Smelt"
	private void smelt() {

		if (!this.shouldSmelt()) {
			return;
		}

		if (this.fuelTimeRemaining <= 0) {
			return;
		}

		this.smeltTime++;
	}
	private boolean shouldSmelt() {

		//Replace FR with GR
		final ItemStack input = this.getInventory().getStackInSlot(INPUT_SLOT);
		final ItemStack result = GrinderRecipes.instance().getGrindingResult(input);

		final ItemStack output = this.getInventory().getStackInSlot(OUTPUT_SLOT);

		if (result.isEmpty() || input.isEmpty()) {
			return false;
		}

		if ((output.getCount() + result.getCount()) > output.getMaxStackSize()) {
			return false;
		}

		if (output.isEmpty() || output.isItemEqual(result)) {
			return true;
		}

		return false;
	}
	
	private void trySmeltItem() {

		if ((this.fuelTimeRemaining <= 0) && (this.smeltTime > 0)) {
			this.smeltTime--;
		}

		if (!this.shouldSmelt()) {
			return;
		}

        if (this.canSmeltItem())
        {
            ItemStack itemstack = this.grinderItemStacks.get(0);
            ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
            ItemStack itemstack2 = this.grinderItemStacks.get(2);

            if (itemstack2.isEmpty())
            {
                this.grinderItemStacks.set(2, itemstack1.copy());
            }
            else if (itemstack2.getItem() == itemstack1.getItem())
            {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(1);
        }
		this.smeltTime = 0;

	  
		//Replace FR with GR
		final ItemStack input = this.getInventory().getStackInSlot(INPUT_SLOT);
		final ItemStack result = GrinderRecipes.instance().getGrindingResult(input);
		final ItemStack output = this.getInventory().getStackInSlot(OUTPUT_SLOT);

		if (output.isEmpty()) {
			this.getInventory().setStackInSlot(OUTPUT_SLOT, result.copy());
		} else if (output.isItemEqual(result)) {
			output.grow(result.getCount());
		}

		input.shrink(1);

		if (!input.isEmpty()) {
			this.maxSmeltTime = this.getSmeltTime(input);
		} else {
			this.maxSmeltTime = 0;
		}

	}

	   /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmeltItem()
    {
        if (((ItemStack)this.grinderItemStacks.get(0)).isEmpty())
        {
            return false;
        }
        else
        {
        	//Replace FR with GR
            ItemStack itemstack = GrinderRecipes.instance().getGrindingResult(this.grinderItemStacks.get(0));

            if (itemstack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack itemstack1 = this.grinderItemStacks.get(2);

                if (itemstack1.isEmpty())
                {
                    return true;
                }
                else if (!itemstack1.isItemEqual(itemstack))
                {
                    return false;
                }
                else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize())  // Forge fix: make furnace respect stack sizes in furnace recipes
                {
                    return true;
                }
                else
                {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
    }

	private int getInventoryStackLimit() {
		return 64;
	}

	public int getSmeltTime(final ItemStack input) {
		final int smeltTime = ModEventFactory.getItemSmeltTime(input);
		if (smeltTime >= 0) {
			return smeltTime;
		}
		return 200;
	}
	public ModItemStackHandler getInventory() {
		return this.inventory;
	}

	public BlockPos getPosition() {
		return this.pos;
	}
	
	//Checks if Grinder is on
	public boolean isOn() {
		if (this.fuelTimeRemaining > 0) {
			return true;
		} else if (!this.getInventory().getStackInSlot(FUEL_SLOT).isEmpty()) {
			if (!this.getInventory().getStackInSlot(INPUT_SLOT).isEmpty()) {
				if (this.getInventory().getStackInSlot(OUTPUT_SLOT).getCount() != this.getInventory().getStackInSlot(OUTPUT_SLOT).getMaxStackSize()) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}
	
	//Sees if hoppers input or take out items from Grinder
	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			final NonNullList<ItemStack> stacks = NonNullList.create();
			final int realSlot;
			switch (facing) {
			case UP:
				realSlot = INPUT_SLOT;
				stacks.add(this.getInventory().getStackInSlot(realSlot));
				break;
			case NORTH:
			case SOUTH:
			case EAST:
			case WEST:
				realSlot = FUEL_SLOT;
				stacks.add(this.getInventory().getStackInSlot(realSlot));
				break;
			default:
			case DOWN:
				realSlot = OUTPUT_SLOT;
				stacks.add(this.getInventory().getStackInSlot(realSlot));
			}
			return (T) new ItemStackHandler(stacks) {
				@Override
				public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
					return TileGrinder.this.getInventory().insertItem(realSlot, stack, simulate);
				}

				@Override
				public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
					return TileGrinder.this.getInventory().extractItem(realSlot, amount, simulate);
				}

				@Override
				public boolean isItemValid(final int slot, final ItemStack stack) {
					return TileGrinder.this.getInventory().isItemValid(realSlot, stack);
				}
			};
		}
		return super.getCapability(capability, facing);
	}
	
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }
	
	//NBT and TileEntity data
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), this.getUpdateTag());
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	@Override
	public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}
	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		if (compound.hasKey(FUEL_TIME_REMAINING_TAG)) {
			this.fuelTimeRemaining = compound.getInteger(FUEL_TIME_REMAINING_TAG);
		}
		if (compound.hasKey(SMELT_TIME_TAG)) {
			this.smeltTime = compound.getInteger(SMELT_TIME_TAG);
		}
		if (compound.hasKey(MAX_FUEL_TIME_TAG)) {
			this.maxFuelTime = compound.getInteger(MAX_FUEL_TIME_TAG);
		}
		if (compound.hasKey(MAX_SMELT_TIME_TAG)) {
			this.maxSmeltTime = compound.getInteger(MAX_SMELT_TIME_TAG);
		}
		//if (compound.hasKey(INVENTORY_TAG)) {
		//	this.getInventory().deserializeNBT(compound.getCompoundTag(INVENTORY_TAG));
		//}
	}
	
	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		compound.setInteger(FUEL_TIME_REMAINING_TAG, this.fuelTimeRemaining);
		compound.setInteger(SMELT_TIME_TAG, this.smeltTime);
		compound.setInteger(MAX_FUEL_TIME_TAG, this.maxFuelTime);
		compound.setInteger(MAX_SMELT_TIME_TAG, this.maxSmeltTime);
		//compound.setTag(INVENTORY_TAG, this.getInventory().serializeNBT());
		return compound;
	}
}


