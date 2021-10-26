package lt.vu.mif.it.paskui.village.npc;

public interface NPCAttach {

    void initPathfinder();

    /** Returns attached NPC to Entity
     * @return NPC instance.
     */
    public NPC getNPC();
}
