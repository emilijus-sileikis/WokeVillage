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

public class MinerSelectionScreen extends SelectionScreen {

    public MinerSelectionScreen(NPC npc) {
        super(npc);
    }

    @Override
    protected void init(Role role, Personality personality) {
        ItemStack item;

        item = createItem(Component.text("Help").color(NamedTextColor.GREEN), Material.BOOK, Collections.singletonList(Component.text("Click here for help")));
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreCobble = new ArrayList<>();
        loreCobble.add(Component.text("Task: 96 Cobblestone").color(NamedTextColor.YELLOW));
        loreCobble.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Mining Stone").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.STONE_PICKAXE, loreCobble);
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreIron = new ArrayList<>();
        loreIron.add(Component.text("Task: 32 Iron Ore").color(NamedTextColor.YELLOW));
        loreIron.add(Component.text("Price: 16 Gold Ingots").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Mining Iron Ore").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.IRON_PICKAXE, loreIron);
        inv.setItem(inv.firstEmpty(), item);

        List<Component> loreCoal = new ArrayList<>();
        loreCoal.add(Component.text("Task: 64 Coal").color(NamedTextColor.YELLOW));
        loreCoal.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
        item = createItem(Component.text("Mining Coal").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.WOODEN_PICKAXE, loreCoal);
        inv.setItem(inv.firstEmpty(), item);


        item = createItem(Component.text("Close").color(NamedTextColor.RED), Material.BARRIER, Collections.singletonList(Component.text("Click to close the menu")));
        inv.setItem(inv.firstEmpty(), item);

    }
}