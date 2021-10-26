package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.command.Command;
import lt.vu.mif.it.paskui.village.commands.NPCCommands;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.UUID;

/**
 * Class for storing npc and it's data
 * TODO: make this primary class for npc
 */
public class NPC {

    private String name;
    private Location loc;
    private CustomVillager npcEntity;

    public NPC(String name, Location loc) {
        this.name = name;
        this.loc = loc;
        npcEntity = new CustomVillager(this, loc);
        npcEntity.setPos(loc.getX(), loc.getY(), loc.getZ());
        //npcEntity.setCanPickUpLoot(true);
        npcEntity.setCustomName(new TextComponent(this.getName()));
        npcEntity.causeFallDamage(3, 0.5F, DamageSource.FALL);
    }

    // Getters
    public String getName() {
        return name;
    }

    public Location getLoc() {
        return loc;
    }

    public org.bukkit.entity.Entity getEntity() {
        return npcEntity.getBukkitEntity();
    }

    public UUID getUUID() {
        return npcEntity.getUUID();
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setEntity(CustomVillager villager) {
        this.npcEntity = villager;
    }

    // Other
    /** Creates npc inside minecraft world.
     * @return status whether npc is spawned
     */
    public boolean spawn() {
        ServerLevel nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        return nmsWorld.addEntity(npcEntity, CreatureSpawnEvent.SpawnReason.COMMAND);
    }

    /**
     * Removes npc from minecraft world.
     */
    public void remove() {
        npcEntity.remove(Entity.RemovalReason.DISCARDED);
    }
}
