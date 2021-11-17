package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LumberJackSelectionScreen extends SelectionScreen {

    public LumberJackSelectionScreen(NPC npc) {
        super(npc);
    }

    @Override
    protected void init(Role role, Personality personality) {
        ItemStack item;

        item = createItem(Component.text("Help").color(NamedTextColor.GREEN), Material.BOOK, Collections.singletonList(Component.text("Click here for help")));
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreFish = new ArrayList<>();
        loreFish.add(Component.text("Task: 64 Fish").color(NamedTextColor.YELLOW));
        loreFish.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Fishing").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.FISHING_ROD, loreFish);
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreMisc = new ArrayList<>();
        loreMisc.add(Component.text("Task: Fish for miscellaneous items").color(NamedTextColor.YELLOW));
        loreMisc.add(Component.text("Price: 5 Gold Ingots").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Fishing").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.ENCHANTED_BOOK, loreMisc);
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreTreasure = new ArrayList<>();
        loreTreasure.add(Component.text("Task: Search for treasure...").color(NamedTextColor.YELLOW));
        loreTreasure.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Expedition").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.FILLED_MAP, loreTreasure);
        inv.setItem(inv.firstEmpty(), item);

        item = createItem(Component.text("Close").color(NamedTextColor.RED), Material.BARRIER, Collections.singletonList(Component.text("Click to close the menu")));
        inv.setItem(inv.firstEmpty(), item);

    }
}