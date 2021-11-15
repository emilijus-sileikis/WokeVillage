package lt.vu.mif.it.paskui.village.npc;

import java.util.Random;

public enum Role {
    LUMBERJACK,
    MINER,
    FISHER;

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
