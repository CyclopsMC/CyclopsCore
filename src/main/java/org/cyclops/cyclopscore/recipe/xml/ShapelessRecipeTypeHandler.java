package org.cyclops.cyclopscore.recipe.xml;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.ShapelessOreRecipeNbtSensitive;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * Handler for shapeless recipes.
 * @author rubensworks
 *
 */
public class ShapelessRecipeTypeHandler extends GridRecipeTypeHandler {

	@Override
	protected List<Object> handleIO(RecipeHandler recipeHandler, Element input, ItemStack output, boolean nbtSensitive)
			throws XmlRecipeLoader.XmlRecipeException {
		List<Object> inputs = Lists.newLinkedList();
		NodeList inputNodes = input.getElementsByTagName("item");
		for(int i = 0; i < inputNodes.getLength(); i++) {
			inputs.add(getItem(recipeHandler, inputNodes.item(i)));
		}
		GameRegistry.register(new ShapelessOreRecipeNbtSensitive(CraftingHelpers.newRecipeIdentifier(output),
				output, nbtSensitive, inputs.toArray()));
		return inputs;
	}

}
