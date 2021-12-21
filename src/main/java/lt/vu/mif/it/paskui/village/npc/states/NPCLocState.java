package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Location;

/**
 * NPCState with additional Location field.
 */
public abstract class NPCLocState extends NPCState {
    protected final Location loc;

    public NPCLocState(final NPC npc, final Location loc) {
        super(npc);
        this.loc = loc;
    }
}
