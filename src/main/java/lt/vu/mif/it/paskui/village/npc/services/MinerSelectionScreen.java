package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.services.tables.MinerLootTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MinerSelectionScreen extends SelectionScreen {

    public MinerSelectionScreen(NPC npc) {
        super(npc, new int[]{10, 16, 10});
    }

    @Override
    protected void init(Role role, Personality personality) {
        super.init(role, personality);
        this.modifyPrices();

        List<Component> loreCobble = new ArrayList<>();
        loreCobble.add(Component.text("Task: 96 Cobblestone").color(NamedTextColor.YELLOW));
        loreCobble.add(Component.text("Price: " + prices[0] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Mining Stone").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.STONE_PICKAXE,
                loreCobble
        );

        List<Component> loreIron = new ArrayList<>();
        loreIron.add(Component.text("Task: 32 Iron Ore").color(NamedTextColor.YELLOW));
        loreIron.add(Component.text("Price: " + prices[1] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Mining Iron Ore").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.IRON_PICKAXE,
                loreIron
        );

        List<Component> loreCoal = new ArrayList<>();
        loreCoal.add(Component.text("Task: 64 Coal").color(NamedTextColor.YELLOW));
        loreCoal.add(Component.text("Price: " + prices[2] + " Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Mining Coal").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.WOODEN_PICKAXE,
                loreCoal
        );
    }

    @Override
    public void processService(Material item, Player player) {
        switch (item) {
            case STONE_PICKAXE:
                MinerLootTable treasureM = MinerLootTable.fromInt(randomInt(0, 1));
                processTrade(player, prices[0], treasureM.getGoods(), treasureM.getItem(), null);
                break;
            case IRON_PICKAXE:
                treasureM = MinerLootTable.fromInt(randomInt(1, 2));
                processTrade(player, prices[1], treasureM.getGoods(), treasureM.getItem(), null);
                break;
            case WOODEN_PICKAXE:
                treasureM = MinerLootTable.fromInt(randomInt(2, 3));
                processTrade(player, prices[2], treasureM.getGoods(), treasureM.getItem(), null);
                break;
        }

        super.processService(item, player);
    }
}