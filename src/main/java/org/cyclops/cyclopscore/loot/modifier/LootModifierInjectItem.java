package org.cyclops.cyclopscore.loot.modifier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class LootModifierInjectItem extends LootModifier {
    public static final Supplier<MapCodec<LootModifierInjectItem>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
            inst.group(
                    ItemStack.CODEC.fieldOf("item").forGetter(LootModifierInjectItem::getItemStack),
                    Codec.list(Codec.STRING).fieldOf("loot_tables").forGetter(LootModifierInjectItem::getLootTables),
                    Codec.INT.optionalFieldOf("maxStackSize", 1).forGetter(LootModifierInjectItem::getMaxStackSize)
            )
    ).apply(inst, LootModifierInjectItem::new)));

    private final ItemStack itemStack;
    private final List<String> lootTables;
    private final int maxStackSize;

    public LootModifierInjectItem(LootItemCondition[] conditionsIn, ItemStack itemStack, List<String> lootTables, int maxStackSize) {
        super(conditionsIn);
        this.itemStack = itemStack;
        this.lootTables = lootTables;
        this.maxStackSize = maxStackSize;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getLootTables() {
        return lootTables;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (getLootTables().contains(context.getQueriedLootTableId().toString())) {
            ItemStack stack = getItemStack().copy();
            stack.setCount(context.getRandom().nextInt(1, getMaxStackSize() + 1));
            generatedLoot.add(stack);
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
