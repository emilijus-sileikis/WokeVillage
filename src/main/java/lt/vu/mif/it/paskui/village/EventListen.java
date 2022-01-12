package lt.vu.mif.it.paskui.village;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import lt.vu.mif.it.paskui.village.commands.NPCNames;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.events.NPCDeathEvent;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftVillagerZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

public class EventListen implements Listener {

    private final NPCManager npcManager;
    private final DataManager dataManager;
    boolean newChunk;
    int limit = 0;

    public EventListen(NPCManager npcManager, DataManager dataManager) {
        this.npcManager = npcManager;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        boolean req = event.getClickedInventory() == null;
        req = req || !(event.getClickedInventory().getHolder() instanceof SelectionScreen);
        req = req || event.getCurrentItem() == null;

        if (req) return;

        req = event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY);
        req = req || event.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD);
        req = req || event.getAction().equals(InventoryAction.HOTBAR_SWAP);

        if (req) {
            event.setCancelled(true);
            return;
        }

        SelectionScreen screen = (SelectionScreen) event.getClickedInventory().getHolder();
        Player p = (Player) event.getWhoClicked();
        screen.processService(event.getCurrentItem().getType(), p);
    }

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        if (event.getReason().equals(RemovalReason.KILLED)) {
            npcManager.deleteNPC(event.getNpc());
            dataManager.getConfig().set("data." + event.getNpc().id, null);
            dataManager.saveConfig();
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof SelectionScreen) {
            SelectionScreen screen = (SelectionScreen) event.getInventory().getHolder();
            screen.npc.stopTrade();
            screen.npc.setKillable();
        }
    }

/*    @EventHandler
    public void turnEvent (EntityTransformEvent event) {
        if (event.getTransformedEntity().getType().equals(EntityType.ZOMBIE_VILLAGER)) {
            Bukkit.broadcast(Component.text("A Villager was turned!"));
        }
    }
    
 */

    @EventHandler
    public void spawnEvent (CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Villager && event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CURED) {
            event.getEntity().remove();
            String name = NPCNames.getRandomName().getName() + " The " + Role.getRandomRole().toStringWithCapInitial();
            npcManager.createNPC(name, event.getLocation(), Role.getRandomRole(), Personality.getRandomPersonality());
        }
    }
}
