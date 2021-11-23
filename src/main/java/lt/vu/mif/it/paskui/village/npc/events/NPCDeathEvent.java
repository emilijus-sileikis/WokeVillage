package lt.vu.mif.it.paskui.village.npc.events;

import lt.vu.mif.it.paskui.village.npc.NPC;
import net.minecraft.world.entity.Entity.RemovalReason;

public class NPCDeathEvent extends NPCEvent{

    private final RemovalReason reason;

    public NPCDeathEvent(NPC npc, RemovalReason reason) {
        super(npc);
        this.reason = reason;
    }
    public RemovalReason getReason() {return reason;}
}
