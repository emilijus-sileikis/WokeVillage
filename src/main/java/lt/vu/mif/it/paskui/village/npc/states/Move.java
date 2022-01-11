package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Role;
import org.bukkit.Location;

public class Move extends NPCLocState {
    private final int timeElapsed;

    public Move(NPC npc, Location loc, int timeElapsed) {
        super(npc, loc);
        this.timeElapsed = timeElapsed;
    }

    @Override
    public void run() {
        Location location = this.npc.getLoc();
        npc.moveFurther(location);

        if (npc.role != Role.MINER) {
            new Invisible(npc).runTaskLater(100);
            npc.itemReset();
        }

        new Pause(npc, this.loc).runTaskLater(timeElapsed * 20L);
    }
}
