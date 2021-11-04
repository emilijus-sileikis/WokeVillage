package lt.vu.mif.it.paskui.village.npc;

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
}
