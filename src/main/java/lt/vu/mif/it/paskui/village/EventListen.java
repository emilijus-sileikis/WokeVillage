package lt.vu.mif.it.paskui.village;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.npc.events.NPCDeathEvent;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import net.minecraft.world.entity.Entity.RemovalReason;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.world.ChunkLoadEvent;

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
            dataManager.getConfig().set("data." + event.getNpc().getId(), null);
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

    @EventHandler
    public boolean ChunkCheck (ChunkLoadEvent event) {
        if (event.isNewChunk()) {
            return newChunk = true;
            //Chunk chunk = event.getChunk();
            //VillageCheck(chunk);
        }
        else return newChunk = false;
    }

    @EventHandler
    public void ChunkLoad (PlayerChunkLoadEvent event) {
        Player p = event.getPlayer();
        //if (newChunk) {
            VillageCheck(p);
        //}
    }

    public void VillageCheck (Player p) {
        World world = p.getWorld();
        Location village = world.locateNearestStructure(p.getLocation(), StructureType.VILLAGE, 1, false);
        Location player = p.getLocation();
        double range = village.distance(player);
        if (village.getChunk().isLoaded() && range <= 100) {
            //TODO: DO SOME RANDOM SPAWNING.
            // MAKE THE SPAWN LIMIT 1.
        }
    }
}
