package org.cyclops.cyclopscore.ingredient;

import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

public class IngredientMatcherSimple implements IIngredientMatcher<Integer, Boolean> {
    @Override
    public boolean isInstance(Object object) {
        return object instanceof Integer;
    }

    @Override
    public Boolean getAnyMatchCondition() {
        return false;
    }

    @Override
    public Boolean getExactMatchCondition() {
        return true;
    }

    @Override
    public Boolean withCondition(Boolean matchCondition, Boolean with) {
        return matchCondition || with;
    }

    @Override
    public Boolean withoutCondition(Boolean matchCondition, Boolean without) {
        return matchCondition == without ? false : matchCondition;
    }

    @Override
    public boolean hasCondition(Boolean matchCondition, Boolean searchCondition) {
        return matchCondition == searchCondition;
    }

    @Override
    public boolean matches(Integer a, Integer b, Boolean matchCondition) {
        return !matchCondition || a.intValue() == b.intValue();
    }

    @Override
    public Integer getEmptyInstance() {
        return 0;
    }

    @Override
    public boolean isEmpty(Integer instance) {
        return instance == 0;
    }

    @Override
    public int hash(Integer instance) {
        return instance;
    }

    @Override
    public Integer copy(Integer instance) {
        return instance;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        return o1 - o2;
    }
}
