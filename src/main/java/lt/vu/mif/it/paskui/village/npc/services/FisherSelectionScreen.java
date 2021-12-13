package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.services.tables.FisherLootTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FisherSelectionScreen extends SelectionScreen {

    public FisherSelectionScreen(NPC npc) {
        super(npc);
    }



    @Override
    protected void init(Role role, Personality personality) {
        super.init(role, personality);


        pricesFisher = new int[]{10,5,10};

        if(getPersonality().equals(Personality.GREEDY))
        {
            for(int i=0; i<pricesFisher.length; i++)
            {
                pricesFisher[i] *= randomDouble(1, 2);
            }
        }
        if(getPersonality().equals(Personality.GENEROUS))
        {
            for(int i=0; i<pricesFisher.length; i++)
            {
                pricesFisher[i] *= randomDouble(0.5, 0.9);;
            }
        }



        List<Component> loreFish = new ArrayList<>();
        loreFish.add(Component.text("Task: 64 Fish").color(NamedTextColor.YELLOW));
        loreFish.add(Component.text("Price: " + pricesFisher[0] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Fishing").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.FISHING_ROD,
                loreFish
        );

        List<Component> loreMisc = new ArrayList<>();
        loreMisc.add(Component.text("Task: Fish for miscellaneous items").color(NamedTextColor.YELLOW));
        loreMisc.add(Component.text("Price: " + pricesFisher[1] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Fishing").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.ENCHANTED_BOOK,
                loreMisc
        );

        List<Component> loreTreasure = new ArrayList<>();
        loreTreasure.add(Component.text("Task: Search for treasure...").color(NamedTextColor.YELLOW));
        loreTreasure.add(Component.text("Price: " + pricesFisher[2] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Expedition").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.FILLED_MAP,
                loreTreasure
        );
    }

    @Override
    public void processService(Material item, Player player) {
        switch (item) {
            case FISHING_ROD -> {
                FisherLootTable loot = FisherLootTable.fromInt(randomInt(1, 4));
                processTrade(player, pricesFisher[0], loot.getGoods(), loot.getItem());
            }
            case ENCHANTED_BOOK -> {
                FisherLootTable loot = FisherLootTable.fromInt(randomInt(1, 11));
                processTrade(player, pricesFisher[1], loot.getGoods(), loot.getItem());
            }
            case FILLED_MAP -> {
                int temp = randomInt(1, 10);
                if (temp >= 3) {
                    Material treasureFail = Material.GOLD_NUGGET;
                    processTrade(player, pricesFisher[2], 7, treasureFail);
                } else {
                    FisherLootTable loot = FisherLootTable.fromInt(
                            randomInt(13, FisherLootTable.values().length)
                    );
                    processTrade(player, pricesFisher[2], loot.getGoods(), loot.getItem());
                }
            }
        }
        super.processService(item, player);
    }
}