package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;

public class Invisible extends NPCState {
    public Invisible(NPC npc) {
        super(npc);
    }

    @Override
    public void run() {
        this.npc.setInvisible();
    }
}
