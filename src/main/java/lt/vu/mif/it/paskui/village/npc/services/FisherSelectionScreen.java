package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class FisherSelectionScreen extends SelectionScreen {

    public FisherSelectionScreen(NPC npc) {
        super(npc);
    }

    @Override
    protected void init(Role role, Personality personality) {
        super.init(role, personality);

        List<Component> loreFish = new ArrayList<>();
        loreFish.add(Component.text("Task: 64 Fish").color(NamedTextColor.YELLOW));
        loreFish.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Fishing").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.FISHING_ROD,
                loreFish
        );

        List<Component> loreMisc = new ArrayList<>();
        loreMisc.add(Component.text("Task: Fish for miscellaneous items").color(NamedTextColor.YELLOW));
        loreMisc.add(Component.text("Price: 5 Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Fishing").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.ENCHANTED_BOOK,
                loreMisc
        );

        List<Component> loreTreasure = new ArrayList<>();
        loreTreasure.add(Component.text("Task: Search for treasure...").color(NamedTextColor.YELLOW));
        loreTreasure.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Expedition").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.FILLED_MAP,
                loreTreasure
        );
    }
}