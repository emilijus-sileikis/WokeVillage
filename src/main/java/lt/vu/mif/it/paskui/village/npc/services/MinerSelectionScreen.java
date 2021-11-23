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

public class MinerSelectionScreen extends SelectionScreen {

    public MinerSelectionScreen(NPC npc) {
        super(npc);
    }

    @Override
    protected void init(Role role, Personality personality) {
        super.init(role, personality);

        List<Component> loreCobble = new ArrayList<>();
        loreCobble.add(Component.text("Task: 96 Cobblestone").color(NamedTextColor.YELLOW));
        loreCobble.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Mining Stone").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.STONE_PICKAXE,
                loreCobble
        );

        List<Component> loreIron = new ArrayList<>();
        loreIron.add(Component.text("Task: 32 Iron Ore").color(NamedTextColor.YELLOW));
        loreIron.add(Component.text("Price: 16 Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Mining Iron Ore").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.IRON_PICKAXE,
                loreIron
        );

        List<Component> loreCoal = new ArrayList<>();
        loreCoal.add(Component.text("Task: 64 Coal").color(NamedTextColor.YELLOW));
        loreCoal.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Mining Coal").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.WOODEN_PICKAXE,
                loreCoal
        );
    }
}