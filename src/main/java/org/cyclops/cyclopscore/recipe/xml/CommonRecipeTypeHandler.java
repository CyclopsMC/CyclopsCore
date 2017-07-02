package org.cyclops.cyclopscore.recipe.xml;

import com.google.common.collect.Maps;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.w3c.dom.Node;

import java.util.Map;

/**
 * A default implementation of {@link IRecipeTypeHandler} with some helper methods and
 * {@link IItemTypeHandler}'s.
 * @author rubensworks
 *
 */
public abstract class CommonRecipeTypeHandler<I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> implements IRecipeTypeHandler<I, O, P>, IItemTypeHandler {

	private static final Map<String, IItemTypeHandler> ITEM_TYPE_HANDLERS = Maps.newHashMap();
	protected static final String DEFAULT_ITEM_TYPE = "default";
	static {
		ITEM_TYPE_HANDLERS.put(DEFAULT_ITEM_TYPE, new DefaultItemTypeHandler());
		ITEM_TYPE_HANDLERS.put("oredict", new OreDictItemTypeHandler());
		ITEM_TYPE_HANDLERS.put("predefined", new PredefinedItemTypeHandler());
	}
	
	/**
	 * Register a new item type handler.
	 * @param type The type name.
	 * @param handler The handler instance.
	 */
	public static void registerItemTypeHandler(String type, IItemTypeHandler handler) {
		ITEM_TYPE_HANDLERS.put(type, handler);
	}
	
	@Override
	public Ingredient getIngredient(RecipeHandler recipeHandler, Node itemNode) throws XmlRecipeLoader.XmlRecipeException {
		String type = DEFAULT_ITEM_TYPE;
		Node typeAttribute = itemNode.getAttributes().getNamedItem("type");
		if(typeAttribute != null) {
			type = typeAttribute.getTextContent();
		}
		return getItem(recipeHandler, itemNode, type);
	}
	
	protected Ingredient getItem(RecipeHandler recipeHandler, Node itemNode, String type) throws XmlRecipeLoader.XmlRecipeException {
		IItemTypeHandler handler = ITEM_TYPE_HANDLERS.get(type);
		if(handler == null) {
			throw new XmlRecipeLoader.XmlRecipeException(String.format(
					"Could not find an item type handler of type '%s'", type));
		}
		return handler.getIngredient(recipeHandler, itemNode);
	}

	protected FluidStack getFluid(RecipeHandler recipeHandler, Node fluidNode) throws XmlRecipeLoader.XmlRecipeException {
		int amount = 1000;
		Node amountNode = fluidNode.getAttributes().getNamedItem("amount");
		if(amountNode != null) {
			amount = Integer.parseInt(amountNode.getTextContent());
		}
		String fluidName = fluidNode.getTextContent();
		Fluid fluid = FluidRegistry.getFluid(fluidName);
		if(fluid == null) {
			throw new XmlRecipeLoader.XmlRecipeException(String.format("Fluid by name '%s' has not been found.", fluidName));
		}
		return new FluidStack(fluid, amount);
	}
	
}
