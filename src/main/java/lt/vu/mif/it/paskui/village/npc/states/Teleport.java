package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Location;

public class Teleport extends NPCLocState {
    public Teleport(NPC npc, Location loc) {
        super(npc, loc);
    }

    @Override
    public void run() {
        if (npc.getEntity().isInWaterOrBubbleColumn()
                || npc.getEntity().getVelocity().getX() == 0.0D
                || npc.getEntity().getVelocity().getZ() == 0.0D) {
            npc.getEntity().teleport(loc);
        }
    }
}