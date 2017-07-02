package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Handler for shaped recipes.
 * @author rubensworks
 *
 */
public class ShapedRecipeTypeHandler extends GridRecipeTypeHandler {

	@Override
	protected NonNullList<Ingredient> handleIO(RecipeHandler recipeHandler, Element input, ItemStack output)
			throws XmlRecipeLoader.XmlRecipeException {
		Element inputGrid = (Element) input.getElementsByTagName("grid").item(0);
		NodeList gridRows = inputGrid.getElementsByTagName("gridrow");
		NonNullList<Ingredient> inputs = NonNullList.create();
		CraftingHelper.ShapedPrimer shape = new CraftingHelper.ShapedPrimer();
		shape.height = 0;
		shape.width = 0;
		shape.input = NonNullList.create();
		
        // Add the three recipe box lines.
		for(int row = 0; row < gridRows.getLength(); row++) {
			shape.height++;
			Element gridRow = (Element) gridRows.item(row);
			NodeList gridColumns = gridRow.getElementsByTagName("item");
			for(int col = 0; col < gridColumns.getLength(); col++) {
				if (row == 0) shape.width++;
				Node gridColumn = gridColumns.item(col);
				shape.input.add(getIngredient(recipeHandler, gridColumn));
			}
		}
        
        // Register with the recipe lines we just constructed.
		ResourceLocation id = CraftingHelpers.newRecipeIdentifier(output);
		CraftingHelpers.registerRecipe(id, new ShapedOreRecipe(id, output, shape));
		return inputs;
	}

}
