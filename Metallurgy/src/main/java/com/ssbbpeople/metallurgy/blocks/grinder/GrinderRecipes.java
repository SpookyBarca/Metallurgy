package com.ssbbpeople.metallurgy.blocks.grinder;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.ssbbpeople.metallurgy.init.ModBlocks;
import com.ssbbpeople.metallurgy.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class GrinderRecipes {
    private static final GrinderRecipes SMELTING_BASE = new GrinderRecipes();
    /** The list of smelting results. */
    private final Map<ItemStack, ItemStack> grindingList = Maps.<ItemStack, ItemStack>newHashMap();
    /** A list which contains how many experience points each recipe output will give. */
    private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();
    
    /**
     * Returns an instance of FurnaceRecipes.
     */
    public static GrinderRecipes instance()
    {
        return SMELTING_BASE;
    }
    
    private GrinderRecipes () {
    	//Extracted Iron Ore = 2 Pulverized Iron
    	this.addSmeltingRecipeForBlock(ModBlocks.EXTRACTED_IRON_ORE, new ItemStack(ModItems.PULVERIZED_IRON, 2), 0.7F);
    }
    
    /**
     * Adds a smelting recipe, where the input item is an instance of Block.
     */
    public void addSmeltingRecipeForBlock(Block input, ItemStack stack, float experience)
    {
        this.addGrinding(Item.getItemFromBlock(input), stack, experience);
    }

    /**
     * Adds a smelting recipe using an Item as the input item.
     */
    public void addGrinding(Item input, ItemStack stack, float experience)
    {
        this.addGrindingRecipe(new ItemStack(input, 1, 32767), stack, experience);
    }
    public void addGrindingRecipe(ItemStack input, ItemStack stack, float experience)
    {
        if (getGrindingResult(input) != ItemStack.EMPTY) { net.minecraftforge.fml.common.FMLLog.log.info("Ignored smelting recipe with conflicting input: {} = {}", input, stack); return; }
        this.grindingList.put(input, stack);
        this.experienceList.put(stack, Float.valueOf(experience));
    }
    /**
     * Returns the smelting result of an item.
     */
    public ItemStack getGrindingResult(ItemStack stack)
    {
        for (Entry<ItemStack, ItemStack> entry : this.grindingList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }
    /**
     * Compares two itemstacks to ensure that they are the same. This checks both the item and the metadata of the item.
     */
    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }
    public Map<ItemStack, ItemStack> getSmeltingList()
    {
        return this.grindingList;
    }
}
