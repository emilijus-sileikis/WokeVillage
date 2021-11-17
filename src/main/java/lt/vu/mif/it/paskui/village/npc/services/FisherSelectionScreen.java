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

public class FisherSelectionScreen extends SelectionScreen {

    public FisherSelectionScreen(NPC npc) {
        super(npc);
    }

    @Override
    protected void init(Role role, Personality personality) {
        ItemStack item;

        item = createItem(Component.text("Help").color(NamedTextColor.GREEN), Material.BOOK, Collections.singletonList(Component.text("Click here for help")));
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreLog = new ArrayList<>();
        loreLog.add(Component.text("Task: 128 Spruce Logs.").color(NamedTextColor.YELLOW));
        loreLog.add(Component.text("Price: 20 Gold Ingots.").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Wood Chopping").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.STONE_AXE, loreLog);
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreApple = new ArrayList<>();
        loreApple.add(Component.text("Task: 64 Apples.").color(NamedTextColor.YELLOW));
        loreApple.add(Component.text("Price: 10 Gold Ingots.").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Apple Gathering").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.APPLE, loreApple);
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreSaplings = new ArrayList<>();
        loreSaplings.add(Component.text("Task: 16 Saplings.").color(NamedTextColor.YELLOW));
        loreSaplings.add(Component.text("Price: 2 Gold Ingots.").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Sapling Gathering").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.OAK_SAPLING, loreSaplings);
        inv.setItem(inv.firstEmpty(), item);

        item = createItem(Component.text("Close").color(NamedTextColor.RED), Material.BARRIER, Collections.singletonList(Component.text("Click to close the menu")));
        inv.setItem(inv.firstEmpty(), item);

    }
}