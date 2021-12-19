package lt.vu.mif.it.paskui.village.npc;

import java.util.Random;

public enum Personality {
    HARDWORKING,
    LAZY,
    RELIABLE,
    CLUMSY,
    GENEROUS,
    GREEDY;

    public static Personality fromString(String val) {
        for (Personality personality : Personality.values()) {
            if ( val.toLowerCase().equals(personality.toString()) )
                return personality;
        }

        return Personality.HARDWORKING;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static Personality getRandomPersonality() {
        int id = new Random().nextInt(Personality.values().length);
        return Personality.values()[id];
    }
}
