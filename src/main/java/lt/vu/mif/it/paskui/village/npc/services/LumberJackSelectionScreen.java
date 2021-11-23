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

public class LumberJackSelectionScreen extends SelectionScreen {

    public LumberJackSelectionScreen(NPC npc) {
        super(npc);
    }

    @Override
    protected void init(Role role, Personality personality) {
        super.init(role, personality);

        List<Component> loreLog = new ArrayList<>();
        loreLog.add(Component.text("Task: 128 Spruce Logs.").color(NamedTextColor.YELLOW));
        loreLog.add(Component.text("Price: 20 Gold Ingots.").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Wood Chopping").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.STONE_AXE,
                loreLog
        );

        List<Component> loreApple = new ArrayList<>();
        loreApple.add(Component.text("Task: 64 Apples.").color(NamedTextColor.YELLOW));
        loreApple.add(Component.text("Price: 10 Gold Ingots.").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Apple Gathering").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC),
                Material.APPLE,
                loreApple
        );

        List<Component> loreSaplings = new ArrayList<>();
        loreSaplings.add(Component.text("Task: 16 Saplings.").color(NamedTextColor.YELLOW));
        loreSaplings.add(Component.text("Price: 2 Gold Ingots.").color(NamedTextColor.YELLOW));
        this.createAddItem(
                Component.text("Sapling Gathering").color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD)
                        .decorate(TextDecoration.ITALIC),
                Material.OAK_SAPLING,
                loreSaplings
        );
    }
}