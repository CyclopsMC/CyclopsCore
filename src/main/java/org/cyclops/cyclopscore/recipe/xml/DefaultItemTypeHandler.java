package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.w3c.dom.Node;

/**
 * Default item type handler for text nodes of the form evilcraft:darkGem"
 * @author rubensworks
 */
public class DefaultItemTypeHandler implements IItemTypeHandler {

	protected Ingredient createItemIngredient(ItemStack itemStack, boolean nbtSensitive) {
		return nbtSensitive ? new NBTIngredient(itemStack) : Ingredient.fromStacks(itemStack);
	}

	protected Ingredient makeIngredient(RecipeHandler recipeHandler, String key, int amount, boolean nbtSensitive) throws XmlRecipeLoader.XmlRecipeException {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
        if(item == null) {
            throw new XmlRecipeLoader.XmlRecipeException(String.format("Item by name '%s' has not been found.", key));
        }
		return createItemIngredient(new ItemStack(item, amount), nbtSensitive);
    }
	
	@Override
	public Ingredient getIngredient(RecipeHandler recipeHandler, Node itemNode) throws XmlRecipeLoader.XmlRecipeException {
		String element = itemNode.getTextContent();
		if(element == null || element.isEmpty()) return Ingredient.EMPTY;
		
		int amount = 1;
		Node amountNode = itemNode.getAttributes().getNamedItem("amount");
		if(amountNode != null) {
            amount = Integer.parseInt(amountNode.getTextContent());
		}

		boolean nbtSensitive = itemNode.getAttributes().getNamedItem("nbt_sensitive") != null
				&& itemNode.getAttributes().getNamedItem("nbt_sensitive").getTextContent().equals("true");
		
		return makeIngredient(recipeHandler, element, amount, nbtSensitive);
	}

	public static class NBTIngredient extends net.minecraftforge.common.crafting.NBTIngredient {
		public NBTIngredient(ItemStack stack) {
			super(stack);
		}
	}

}
