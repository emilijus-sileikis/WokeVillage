package lt.vu.mif.it.paskui.village.npc.services.tables;

import java.util.Random;

public class ItemRandomizer {
    public static <K> K getRandomItem(K[] values, int min, int max) {
        Random rng = new Random();
        int index = rng.nextInt(Math.abs(max - min));
        return values[index + min];
    }
}
