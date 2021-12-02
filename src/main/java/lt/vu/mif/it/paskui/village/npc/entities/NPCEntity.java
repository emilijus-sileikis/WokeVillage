package lt.vu.mif.it.paskui.village.npc.entities;

import lt.vu.mif.it.paskui.village.npc.NPC;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Abstraction layer for minecraft entities
 * representing NPC in world.
 */
public interface NPCEntity {
    // getters
    /**
     * Gets name given to minecraft entity.
     * @return minecraft entity name in {@link String}.
     */
    String getEntityName();

    /**
     * Getter for entity uuid.
     * @return entity {@link UUID}.
     */
    UUID getEntityUUID();

    /**
     * Getter for bukkit entity.
     * @return abstracted minecraft entity in {@link org.bukkit.entity.Entity}.
     */
    org.bukkit.entity.Entity getBukkitEntity();

    /**
     * Getter for minecraft entity.
     * @return raw minecraft entity.
     */
    Entity getNMSEntity();

    /**
     * Returns attached NPC to Entity.
     * @return NPC instance.
     */
    NPC getNPC();

    /**
     * Gets current NPC position.
     */
    Vec3 getNPCPos();

    // setters
    /**
     * Setter for minecraft entity name.
     * @param name name that entity will be given.
     */
    void setEntityName(final @NotNull String name);

    /**
     * Setter for updating minecraft entity locations.
     * @param loc location minecraft entity will be placed at.
     */
    void setEntityPos(final @NotNull Location loc);

    /**
     * Setter for unique id on minecraft entity.
     * Should only be called only when instantiating
     * already created npc.
     * @param npcUUID unique id of minecraft entity to be set.
     */
    void setEntityUUID(final UUID npcUUID);

    // public
    /**
     * Ends trading with player.
     */
    void stopEntityTrading();

    /**
     * Used to remove NPC entity from world.
     */
    void removeEntity();

    /**
     * Makes the NPC go to the nearest Spruce Log block.
     * Then waits some time to simulate chopping.
     * @param timeElapsed time for collecting resources.
     * @param material    material to find and collect.
     */
    void moveTo(int timeElapsed, Material material);

    /**
     * Makes the NPC to move back to the starting point.
     * @param loc - NPC trade location.
     */
    void moveBack(final Location loc);

    /**
     * Counts the distance between start and end points.
     * @param material - Required material.
     */
    double distanceTo(Material material);

    void removeBrain();

    void refreshBrain();

    void moveFurther(Location location);
}
