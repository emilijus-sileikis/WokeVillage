package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.npc.services.FisherLootTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

public class LootTableRandomizationTests {

    @ParameterizedTest
    @MethodSource("numberRandomizeProvider")
    void fisherLootNeverNull(int num) {
        FisherLootTable loot = FisherLootTable.fromInt(num);
        Assertions.assertNotNull(loot);
    }

    static Stream<Integer> numberRandomizeProvider() {
        return Stream.of(
                new Random().nextInt(FisherLootTable.values().length)
        );
    }

    @Test
    void fisherSaddleLootWhenOverBounds() {
        FisherLootTable loot = FisherLootTable.fromInt(FisherLootTable.values().length);
        Assertions.assertSame(loot, FisherLootTable.SADDLE);
    }
}
