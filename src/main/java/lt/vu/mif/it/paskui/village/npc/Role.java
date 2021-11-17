package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.npc.services.*;
import lt.vu.mif.it.paskui.village.npc.services.FisherSelectionScreen;
import lt.vu.mif.it.paskui.village.npc.services.FisherSelectionScreen;


import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;

import java.util.Random;

public enum Role {
    LUMBERJACK(LumberJackSelectionScreen.class),
    MINER(MinerSelectionScreen.class),
    FISHER(FisherSelectionScreen.class);

    private final Class<? extends SelectionScreen> clazz;

    private Role(Class<? extends SelectionScreen> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends SelectionScreen> getClazz() {
        return clazz;
    }

    public static Role fromString(String val) {
        for (Role role : Role.values()) {
            if ( val.equals(role.toString()) )
                return role;
        }

        return Role.LUMBERJACK;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static Role getRandomRole() {
        int id = new Random().nextInt(Role.values().length);
        return Role.values()[id];
    }
}
