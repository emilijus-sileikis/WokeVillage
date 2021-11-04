package lt.vu.mif.it.paskui.village.npc;

public enum Personality {
    HARDWORKING,
    LAZY,
    RELIABLE,
    CLUMSY,
    GENEROUS,
    GREEDY;

    public static Personality fromString(String val) {
        for (Personality personality : Personality.values()) {
            if ( val.equals(personality.toString()) )
                return personality;
        }

        return Personality.HARDWORKING;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
