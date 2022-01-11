package lt.vu.mif.it.paskui.village.npc;

import java.util.Random;

public enum Personality {
    HARDWORKING(0, -240),
    LAZY(0, 240),
    RELIABLE(-5, 0),
    CLUMSY(15, 0),
    GENEROUS(0, 0),
    GREEDY(0, 0);

    private final int failChanceModRange;
    private final int workDurationModRange;

    Personality(final int failChanceModRange, final int workDurationModRange) {
        this.failChanceModRange = failChanceModRange;
        this.workDurationModRange = workDurationModRange;
    }

    public int getFailChanceMod() {
        return getRandomMod(this.failChanceModRange);
    }

    public int getWorkDurationMod() {
        return getRandomMod(this.workDurationModRange);
    }

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

    private static int getRandomMod(int modifier) {
        modifier += (modifier < 0)? -1 : 1;
        final int abs = Math.abs(modifier);
        final int mod = modifier / abs;

        return new Random().nextInt(abs) * mod;
    }
}
