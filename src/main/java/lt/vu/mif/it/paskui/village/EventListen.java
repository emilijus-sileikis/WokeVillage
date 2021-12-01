package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.npc.NPCManager;
import lt.vu.mif.it.paskui.village.npc.events.NPCDeathEvent;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import net.minecraft.world.entity.Entity.RemovalReason;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class EventListen implements Listener {

    private final NPCManager npcManager;
    private final DataManager dataManager;

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
            screen.getNPC().stopTrade();
        }
    }
}
