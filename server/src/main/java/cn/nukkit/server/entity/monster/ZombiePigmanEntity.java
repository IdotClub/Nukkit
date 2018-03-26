package cn.nukkit.server.entity.monster;

import cn.nukkit.api.entity.monster.ZombiePigman;
import cn.nukkit.server.NukkitServer;
import cn.nukkit.server.entity.EntityType;
import cn.nukkit.server.entity.LivingEntity;
import cn.nukkit.server.level.NukkitLevel;
import com.flowpowered.math.vector.Vector3f;

public class ZombiePigmanEntity extends LivingEntity implements ZombiePigman {

    public ZombiePigmanEntity(Vector3f position, NukkitLevel level, NukkitServer server) {
        super(EntityType.ZOMBIE_PIGMAN, position, level, server, 20);
    }
}
