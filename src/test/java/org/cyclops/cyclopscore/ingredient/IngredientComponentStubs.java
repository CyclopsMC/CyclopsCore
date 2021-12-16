package org.cyclops.cyclopscore.ingredient;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

public class IngredientComponentStubs {

    public static IngredientComponent<Integer, Boolean> SIMPLE =
            new IngredientComponent<>("cyclopscore:simple", new IngredientMatcherSimple(),
                    new IngredientSerializerInt(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:simple/amount"),
                            Integer.class, false, amount -> amount, true, true)
            )).setTranslationKey("recipecomponent.cyclopscore.simple");

    public static IngredientComponent<ComplexStack, Integer> COMPLEX =
            new IngredientComponent<>("cyclopscore:complex", new IngredientMatcherComplex(),
                    new IngredientSerializerStub<>(), Lists.newArrayList(
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/group"),
                            ComplexStack.Group.class, true, ComplexStack::getGroup, ComplexStack.Match.GROUP, false),
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/meta"),
                            Integer.class, false, ComplexStack::getMeta, ComplexStack.Match.META, false),
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/amount"),
                            Integer.class, false, ComplexStack::getAmount, ComplexStack.Match.AMOUNT, true),
                    new IngredientComponentCategoryType<>(new ResourceLocation("cyclopscore:complex/tag"),
                            ComplexStack.Tag.class, true, ComplexStack::getTag, ComplexStack.Match.TAG, false)
            )).setTranslationKey("recipecomponent.cyclopscore.complex");

}
