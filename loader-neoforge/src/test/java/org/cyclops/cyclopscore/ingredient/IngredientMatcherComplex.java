package org.cyclops.cyclopscore.ingredient;

import net.minecraft.network.chat.MutableComponent;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;

public class IngredientMatcherComplex implements IIngredientMatcher<ComplexStack, Integer> {

    @Override
    public boolean isInstance(Object object) {
        return object instanceof ComplexStack;
    }

    @Override
    public Integer getAnyMatchCondition() {
        return ComplexStack.Match.ANY;
    }

    @Override
    public Integer getExactMatchCondition() {
        return ComplexStack.Match.EXACT;
    }

    @Override
    public Integer getExactMatchNoQuantityCondition() {
        return ComplexStack.Match.GROUP | ComplexStack.Match.META | ComplexStack.Match.TAG;
    }

    @Override
    public Integer withCondition(Integer matchCondition, Integer with) {
        return matchCondition | with;
    }

    @Override
    public Integer withoutCondition(Integer matchCondition, Integer without) {
        return matchCondition & ~without;
    }

    @Override
    public boolean hasCondition(Integer matchCondition, Integer searchCondition) {
        return (matchCondition & searchCondition) > 0;
    }

    @Override
    public boolean matches(ComplexStack a, ComplexStack b, Integer matchCondition) {
        return ComplexStack.Match.equal(a, b, matchCondition);
    }

    @Override
    public ComplexStack getEmptyInstance() {
        return null;
    }

    @Override
    public boolean isEmpty(ComplexStack instance) {
        return instance == null || instance.getAmount() == 0;
    }

    @Override
    public int hash(ComplexStack instance) {
        if (instance == null) {
            return 0;
        }
        return instance.getGroup().hashCode() + instance.getAmount() + instance.getMeta()
                + (instance.getTag() == null ? 0 : instance.getTag().hashCode());
    }

    @Override
    public ComplexStack copy(ComplexStack instance) {
        if (instance == null) {
            return null;
        }
        return new ComplexStack(instance.getGroup(), instance.getMeta(), instance.getAmount(), instance.getTag());
    }

    @Override
    public long getQuantity(ComplexStack instance) {
        if (instance == getEmptyInstance()) {
            return 0;
        }
        return instance.getAmount();
    }

    @Override
    public ComplexStack withQuantity(ComplexStack instance, long quantity) throws ArithmeticException {
        if (quantity == 0) {
            return getEmptyInstance();
        }
        if (instance == null) {
            return new ComplexStack(ComplexStack.Group.A, 0, Math.toIntExact(quantity), ComplexStack.Tag.A);
        }
        return new ComplexStack(instance.getGroup(), instance.getMeta(), Math.toIntExact(quantity), instance.getTag());
    }

    @Override
    public long getMaximumQuantity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int conditionCompare(Integer a, Integer b) {
        return Integer.compare(a, b);
    }

    @Override
    public String localize(ComplexStack instance) {
        return instance.toString();
    }

    @Override
    public MutableComponent getDisplayName(ComplexStack instance) {
        return null;
    }

    @Override
    public String toString(ComplexStack instance) {
        return null;
    }

    @Override
    public int compare(ComplexStack o1, ComplexStack o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (o2 == null) {
            return 1;
        } else if (o1.getGroup() == o2.getGroup()) {
            int m1 = o1.getMeta();
            int m2 = o2.getMeta();
            if (m1 == m2) {
                int c1 = o1.getAmount();
                int c2 = o2.getAmount();
                if (c1 == c2) {
                    ComplexStack.Tag t1 = o1.getTag();
                    ComplexStack.Tag t2 = o2.getTag();
                    if (t1 == null) {
                        if (t2 == null) {
                            return 0;
                        } else {
                            return -1;
                        }
                    } else if (t2 == null) {
                        return 1;
                    } else {
                        return t1.ordinal() - t2.ordinal();
                    }
                }
                return c1 - c2;
            }
            return m1 - m2;
        }
        return o1.getGroup().ordinal() - o2.getGroup().ordinal();
    }
}
