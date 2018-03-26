package cn.nukkit.server.block.behavior;

import cn.nukkit.api.block.BlockType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class BlockBehaviors {
    private static final Map<BlockType, BlockBehavior> BLOCK_BEHAVIORS;

    static {
        BLOCK_BEHAVIORS = ImmutableMap.<BlockType, BlockBehavior>builder()
                .build();
    }

    public static BlockBehavior getBlockBehavior(BlockType type) {
        Preconditions.checkNotNull(type, "type");
        BlockBehavior behavior = BLOCK_BEHAVIORS.get(type);
        return behavior == null ? SimpleBlockBehavior.INSTANCE : behavior;
    }
}
