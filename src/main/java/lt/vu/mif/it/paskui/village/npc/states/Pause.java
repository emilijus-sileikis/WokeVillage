package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Location;

public class Pause extends NPCLocState {

    public Pause(NPC npc, Location loc) {
        super(npc, loc);
    }

    @Override
    public void run() {
        npc.setVisible();
        npc.moveBack(loc);
        npc.setGoodsHand();
        new Teleport(npc, loc).runTaskLater(120); //80
    }
}