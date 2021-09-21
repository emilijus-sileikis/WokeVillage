package lt.vu.mif.it.paskui.paskui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (NPCManager.getNPCs() == null)
            return;
        if (NPCManager.getNPCs().isEmpty())
            return;
        NPCManager.addJoinPacket(event.getPlayer());
    }
}
