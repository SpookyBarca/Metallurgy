package com.ssbbpeople.metallurgy.blocks.grinder;

import com.ssbbpeople.metallurgy.Main;
import com.ssbbpeople.metallurgy.init.ModBlocks;
import com.ssbbpeople.metallurgy.init.ModItems;
import com.ssbbpeople.metallurgy.util.ModUtil;
import com.ssbbpeople.metallurgy.util.Reference;
import com.ssbbpeople.metallurgy.util.handlers.ModGuiHandler;
import com.ssbbpeople.metallurgy.util.handlers.ModItemStackHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrinder extends Block {

	public static final ResourceLocation GRINDER = new ResourceLocation(Reference.MOD_ID, "grinder");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockGrinder() {
		super(Material.IRON);
		//main:furnace
		setRegistryName("grinder");
		setUnlocalizedName("grinder");
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(Main.METALLURGY);
		
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	//Registers block to CLIENT side
	   @SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }
	   
	//When right-clicked
		@Override
		public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
			if (worldIn.isRemote) {
				return true;
			} else {
				final TileEntity tileentity = worldIn.getTileEntity(pos);

				if (tileentity instanceof TileGrinder) {
					playerIn.openGui(Main.instance, ModGuiHandler.MOD_GRINDER, worldIn, pos.getX(), pos.getY(), pos.getZ());
					playerIn.addStat(StatList.FURNACE_INTERACTION);
				}

				return true;
			}
		}
		
		@Override
		public boolean hasComparatorInputOverride(final IBlockState state) {
			return true;
		}

		@Override
		public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
			final TileEntity tile = worldIn.getTileEntity(pos);
			if ((tile == null) || !(tile instanceof TileGrinder)) {
				return 0;
			}

			final ModItemStackHandler inventory = ((TileGrinder) tile).getInventory();

			int max = 0;

			for (int i = 0; i < inventory.getSlots(); i++) {
				max += inventory.getSlotLimit(i);
			}

			int size = 0;
			for (int i = 0; i < inventory.getSlots(); i++) {
				size += inventory.getStackInSlot(i).getCount();
			}

			return Math.round(Math.round(ModUtil.map(0, max, 0, 15, size)));
		}
	
	//FACING textures
	 @Override
	    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
	        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
	    }

	    public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
	        return EnumFacing.getFacingFromVector(
	             (float) (entity.posX - clickedBlock.getX()),
	             (float) (entity.posY - clickedBlock.getY()),
	             (float) (entity.posZ - clickedBlock.getZ()));
	    }

	    @Override
	    public IBlockState getStateFromMeta(int meta) {
	        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
	    }

	    @Override
	    public int getMetaFromState(IBlockState state) {
	        return state.getValue(FACING).getIndex();
	    }

	    @Override
	    protected BlockStateContainer createBlockState() {
	        return new BlockStateContainer(this, FACING);
	    }

}
