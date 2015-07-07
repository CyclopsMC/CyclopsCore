package org.cyclops.cyclopscore.recipe.xml;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.cyclops.cyclopscore.init.RecipeHandler;
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
	protected void handleIO(RecipeHandler recipeHandler, Element input, ItemStack output)
			throws XmlRecipeLoader.XmlRecipeException {
		List<Object> inputs = Lists.newLinkedList();
		NodeList inputNodes = input.getElementsByTagName("item");
		for(int i = 0; i < inputNodes.getLength(); i++) {
			inputs.add(getItem(recipeHandler, inputNodes.item(i)));
		}
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, inputs.toArray()));
	}

}
