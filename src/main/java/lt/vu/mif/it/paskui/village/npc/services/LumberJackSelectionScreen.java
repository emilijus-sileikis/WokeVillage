package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.services.tables.ItemRandomizer;
import lt.vu.mif.it.paskui.village.npc.services.tables.LumberjackLootTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LumberJackSelectionScreen extends SelectionScreen {

    public LumberJackSelectionScreen(NPC npc) {
        super(npc, new int[]{20, 10, 2});
    }

    @Override
    protected void init(Role role, Personality personality) {
        super.init(role, personality);
        this.modifyPrices();

        List<Component> loreLog = new ArrayList<>();
        loreLog.add(Component.text("Task: 128 Logs") .color(NamedTextColor.YELLOW));
        loreLog.add(Component.text("Price: " + prices[0] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Wood Chopping").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.STONE_AXE,
                loreLog
        );

        List<Component> loreApple = new ArrayList<>();
        loreApple.add(Component.text("Task: 64 Apples.").color(NamedTextColor.YELLOW));
        loreApple.add(Component.text("Price: " + prices[1] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Apple Gathering").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.APPLE,
                loreApple
        );

        List<Component> loreSaplings = new ArrayList<>();
        loreSaplings.add(Component.text("Task: 16 Saplings.").color(NamedTextColor.YELLOW));
        loreSaplings.add(Component.text("Price: " + prices[2] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Sapling Gathering").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD)
                        .decorate(TextDecoration.ITALIC),
                Material.OAK_SAPLING,
                loreSaplings
        );
    }

    @Override
    public void processService(Material item, Player player) {
        switch (item) {
            case STONE_AXE -> {
                LumberjackLootTable loot = ItemRandomizer.getRandomItem(
                        LumberjackLootTable.values(), 0, 5
                );
                processTrade(player, prices[0], loot.getGoods(), loot.getItem());
            }
            case APPLE -> {
                Material apple = Material.APPLE;
                processTrade(player, prices[1], 64, apple);
            }
            case OAK_SAPLING -> {
                LumberjackLootTable loot = ItemRandomizer.getRandomItem(
                        LumberjackLootTable.values(), 5, 10
                );
                processTrade(player, prices[2], loot.getGoods(), loot.getItem());
            }
        }

        super.processService(item, player);
    }
}