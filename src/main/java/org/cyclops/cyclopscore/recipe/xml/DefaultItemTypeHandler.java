package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.w3c.dom.Node;

/**
 * Default item type handler for text nodes of the form evilcraft:darkGem"
 * @author rubensworks
 */
public class DefaultItemTypeHandler implements IItemTypeHandler {

	protected Ingredient createItemIngredient(ItemStack itemStack, boolean nbtSensitive) {
		return nbtSensitive ? new IngredientNBT(itemStack) : Ingredient.fromStacks(itemStack);
	}

	protected Ingredient makeIngredient(RecipeHandler recipeHandler, String key, int amount, int meta, boolean nbtSensitive) throws XmlRecipeLoader.XmlRecipeException {
        Item item = Item.REGISTRY.getObject(new ResourceLocation(key));
        if(item == null) {
            throw new XmlRecipeLoader.XmlRecipeException(String.format("Item by name '%s' has not been found.", key));
        }
		return createItemIngredient(new ItemStack(item, amount, meta), nbtSensitive);
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
		
		int meta = 0;
		Node metaNode = itemNode.getAttributes().getNamedItem("meta");
		if(metaNode != null) {
            if("*".equals(metaNode.getTextContent())) {
                meta = OreDictionary.WILDCARD_VALUE;
            } else {
                meta = Integer.parseInt(metaNode.getTextContent());
            }
		}

		boolean nbtSensitive = itemNode.getAttributes().getNamedItem("nbt_sensitive") != null
				&& itemNode.getAttributes().getNamedItem("nbt_sensitive").getTextContent().equals("true");
		
		return makeIngredient(recipeHandler, element, amount, meta, nbtSensitive);
	}

	public static class IngredientNBT extends net.minecraftforge.common.crafting.IngredientNBT {
		protected IngredientNBT(ItemStack stack) {
			super(stack);
		}
	}

}
