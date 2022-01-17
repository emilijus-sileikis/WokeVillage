package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import org.bukkit.Location;

public class Move extends NPCLocState {
    private final int timeElapsed;
    private final CustomVillager villager;

    public Move(NPC npc, Location loc, int timeElapsed, CustomVillager villager) {
        super(npc, loc);
        this.timeElapsed = timeElapsed;
        this.villager = villager;
    }

    @Override
    public void run() {
        Location location = this.npc.getLoc();
        npc.moveFurther(location);

        if (npc.role != Role.MINER) {
            new Invisible(npc).runTaskLater(180);
            npc.itemReset();
        }

        new Pause(npc, this.loc, villager).runTaskLater(timeElapsed * 20L);
    }
}
