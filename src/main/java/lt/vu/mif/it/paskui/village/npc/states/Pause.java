package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import org.bukkit.Location;

public class Pause extends NPCLocState {
   CustomVillager villager;

    public Pause(NPC npc, Location loc, CustomVillager villager) {
        super(npc, loc);
        this.villager = villager;
    }

    @Override
    public void run() {
        npc.setVisible();
        npc.moveBack(loc);
        npc.setGoodsHand();
        new Teleport(npc, loc, villager).runTaskLater(60);
    }
}