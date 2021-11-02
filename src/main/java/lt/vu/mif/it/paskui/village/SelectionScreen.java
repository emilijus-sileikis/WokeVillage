package lt.vu.mif.it.paskui.village;

import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectionScreen implements InventoryHolder {

    private Inventory inv;

    public SelectionScreen(String role, String personality) {
        inv = Bukkit.createInventory(this, InventoryType.HOPPER, Component.text("Trading Menu").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA));
        init(role, personality);
    }

    private void init(String role, String personality) {
        ItemStack item;

        Logging.infoLog("create SelectionScreen{ role: %s ; personality: %s}", role, personality);
        //Left side
        //for (int i=0; i<2; i++) {
        item = createItem(Component.text("Help").color(NamedTextColor.GREEN), Material.BOOK, Collections.singletonList(Component.text("Click here for help")));
        inv.setItem(inv.firstEmpty(), item);
        //}

        //Lumberjack
        if (role == "LumberJack") {
            List<Component> loreLumberjack = new ArrayList<>();
            loreLumberjack.add(Component.text("Task: 128 Spruce Logs.").color(NamedTextColor.YELLOW));
            loreLumberjack.add(Component.text("Price: 20 Gold Ingots.").color(NamedTextColor.YELLOW));
            item = createItem(Component.text("Wood Gathering").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.STONE_AXE, loreLumberjack);
            inv.setItem(inv.firstEmpty(), item);
        }

        //Miner
        if (role == "Miner") {
            List<Component> loreMiner = new ArrayList<>();
            loreMiner.add(Component.text("Task: 96 Cobblestone").color(NamedTextColor.YELLOW));
            loreMiner.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
            item = createItem(Component.text("Mining").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.STONE_PICKAXE, loreMiner);
            inv.setItem(inv.firstEmpty(), item);
        }

        //Fisher
            if (role == "Fisher") {
                List<Component> loreFish = new ArrayList<>();
                loreFish.add(Component.text("Task: 64 Fish").color(NamedTextColor.YELLOW));
                loreFish.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
                item = createItem(Component.text("Fishing").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.FISHING_ROD, loreFish);
                inv.setItem(inv.firstEmpty(), item);
            }

        //Right side
        //for (int i=3; i<=4; i++) {
        item = createItem(Component.text("Close").color(NamedTextColor.RED), Material.BARRIER, Collections.singletonList(Component.text("Click to close the menu")));
        inv.setItem(inv.firstEmpty(), item);
        //}
    }

    private ItemStack createItem(Component name, Material mat, List<Component> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
