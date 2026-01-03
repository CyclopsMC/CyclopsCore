package org.cyclops.cyclopscore.modcompat.almostunified;

import com.almostreliable.unified.api.AlmostUnified;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class AlmostUnifiedAdapter {

    public static boolean enabled = false;

    @Nullable
    public static Item getTagTargetItem(TagKey<Item> tag) {
        if (enabled) {
            return Adapter.getTagTargetItem(tag);
        }

        return null;
    }

    private static final class Adapter {

        @Nullable
        private static Item getTagTargetItem(TagKey<Item> tag) {
            return AlmostUnified.INSTANCE.getTagTargetItem(tag);
        }
    }
}
