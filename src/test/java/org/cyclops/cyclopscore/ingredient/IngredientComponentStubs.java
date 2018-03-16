package org.cyclops.cyclopscore.ingredient;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

public class IngredientComponentStubs {

    public static IngredientComponent<Integer, Boolean> SIMPLE =
            new IngredientComponent<>("cyclopscore:simple", new IngredientMatcherSimple(),
                    new IngredientSerializerStub<>(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:simple/amount"),
                            Integer.class, false, amount -> amount, true)
            )).setUnlocalizedName("recipecomponent.cyclopscore.simple");

    public static IngredientComponent<ComplexStack, Integer> COMPLEX =
            new IngredientComponent<>("cyclopscore:complex", new IngredientMatcherComplex(),
                    new IngredientSerializerStub<>(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/group"),
                            ComplexStack.Group.class, true, ComplexStack::getGroup, ComplexStack.Match.GROUP),
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/meta"),
                            Integer.class, false, ComplexStack::getMeta, ComplexStack.Match.META),
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/amount"),
                            Integer.class, false, ComplexStack::getAmount, ComplexStack.Match.AMOUNT),
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/tag"),
                            ComplexStack.Tag.class, true, ComplexStack::getTag, ComplexStack.Match.TAG)
            )).setUnlocalizedName("recipecomponent.cyclopscore.complex");

}
