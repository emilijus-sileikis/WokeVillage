package lt.vu.mif.it.paskui.village.npc.entities;

import net.minecraft.world.entity.Entity;
import org.bukkit.Location;

import java.util.UUID;

public interface NPCEntity {
    // getters
    String getNameNPC();

    UUID getUUID();

    org.bukkit.entity.Entity getBukkitEntity();

    Entity getNMSEntity();

    // setters
    void setNameNPC(String name);

    void setInitialPos(Location loc);

    void setUUIDnpc(UUID npcUUID);

    // public
    void initPathfinder();

    void remove();
}
