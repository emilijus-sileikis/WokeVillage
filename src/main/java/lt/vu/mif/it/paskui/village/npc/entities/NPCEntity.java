package lt.vu.mif.it.paskui.village.npc.entities;

import lt.vu.mif.it.paskui.village.npc.NPC;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;

import java.util.UUID;

public interface NPCEntity {
    // getters
    String getEntityName();

    UUID getEntityUUID();

    org.bukkit.entity.Entity getBukkitEntity();

    Entity getNMSEntity();

    /** Returns attached NPC to Entity
     * @return NPC instance.
     */
    NPC getNPC();

    // setters
    void setEntityName(String name);

    void setEntityPos(Location loc);

    void setEntityUUID(UUID npcUUID);

    // public
    void stopEntityTrading();

    void removeEntity();

    void moveTo(int timeElapsed);
}
