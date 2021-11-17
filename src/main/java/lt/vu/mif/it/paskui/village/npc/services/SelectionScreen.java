package lt.vu.mif.it.paskui.village.npc.services;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Personality;
import lt.vu.mif.it.paskui.village.npc.Role;
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

public abstract class SelectionScreen implements InventoryHolder {

    protected Inventory inv;
    private NPC npc;

    public SelectionScreen(NPC npc) {
        this.npc = npc;
        inv = Bukkit.createInventory(this,
                InventoryType.HOPPER,
                Component.text(getRole().toString() + " " + getPersonality().toString()).decorate(TextDecoration.BOLD).color(NamedTextColor.RED)
        );
        init(npc.getRole(), npc.getPersonality());
    }

    public Role getRole() {
        return npc.getRole();
    }

    public Personality getPersonality() { return npc.getPersonality(); }

    public NPC getNPC() {
        return npc;
    }

    protected abstract void init(Role role, Personality personality);

    protected ItemStack createItem(Component name, Material mat, List<Component> lore) {
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

/*{
    //    ItemStack item;

    //    Logging.infoLog("create SelectionScreen{ role: %s ; personality: %s}", role, personality);
    //    //Left side
    //    //for (int i=0; i<2; i++) {
    //    //item = createItem(Component.empty(), Material.GRAY_STAINED_GLASS_PANE, Collections.emptyList());
    //    //inv.setItem(inv.firstEmpty(), item);

    //    item = createItem(Component.text("Help").color(NamedTextColor.GREEN), Material.BOOK, Collections.singletonList(Component.text("Click here for help")));
    //    inv.setItem(inv.firstEmpty(), item);
    //    //}

    //    //Lumberjack
    //    if (role == Role.LUMBERJACK) {
    //        List<Component> loreLog = new ArrayList<>();
    //        loreLog.add(Component.text("Task: 128 Spruce Logs.").color(NamedTextColor.YELLOW));
    //        loreLog.add(Component.text("Price: 20 Gold Ingots.").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Wood Chopping").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.STONE_AXE, loreLog);
    //        inv.setItem(inv.firstEmpty(), item);

    //        List<Component> loreApple = new ArrayList<>();
    //        loreApple.add(Component.text("Task: 64 Apples.").color(NamedTextColor.YELLOW));
    //        loreApple.add(Component.text("Price: 10 Gold Ingots.").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Apple Gathering").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.APPLE, loreApple);
    //        inv.setItem(inv.firstEmpty(), item);

    //        List<Component> loreSaplings = new ArrayList<>();
    //        loreSaplings.add(Component.text("Task: 16 Saplings.").color(NamedTextColor.YELLOW));
    //        loreSaplings.add(Component.text("Price: 2 Gold Ingots.").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Sapling Gathering").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.OAK_SAPLING, loreSaplings);
    //        inv.setItem(inv.firstEmpty(), item);
    //    }

    //    //Miner
    //    if (role == Role.MINER) {
    //        List<Component> loreCobble = new ArrayList<>();
    //        loreCobble.add(Component.text("Task: 96 Cobblestone").color(NamedTextColor.YELLOW));
    //        loreCobble.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Mining Stone").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.STONE_PICKAXE, loreCobble);
    //        inv.setItem(inv.firstEmpty(), item);

    //        List<Component> loreIron = new ArrayList<>();
    //        loreIron.add(Component.text("Task: 32 Iron Ore").color(NamedTextColor.YELLOW));
    //        loreIron.add(Component.text("Price: 16 Gold Ingots").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Mining Iron Ore").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.IRON_PICKAXE, loreIron);
    //        inv.setItem(inv.firstEmpty(), item);

    //        List<Component> loreCoal = new ArrayList<>();
    //        loreCoal.add(Component.text("Task: 64 Coal").color(NamedTextColor.YELLOW));
    //        loreCoal.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Mining Coal").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.WOODEN_PICKAXE, loreCoal);
    //        inv.setItem(inv.firstEmpty(), item);
    //    }

    //    //Fisher
    //    if (role == Role.FISHER) {
    //        List<Component> loreFish = new ArrayList<>();
    //        loreFish.add(Component.text("Task: 64 Fish").color(NamedTextColor.YELLOW));
    //        loreFish.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Fishing").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.FISHING_ROD, loreFish);
    //        inv.setItem(inv.firstEmpty(), item);

    //        List<Component> loreMisc = new ArrayList<>();
    //        loreMisc.add(Component.text("Task: Fish for miscellaneous items").color(NamedTextColor.YELLOW));
    //        loreMisc.add(Component.text("Price: 5 Gold Ingots").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Fishing").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.ENCHANTED_BOOK, loreMisc);
    //        inv.setItem(inv.firstEmpty(), item);

    //        List<Component> loreTreasure = new ArrayList<>();
    //        loreTreasure.add(Component.text("Task: Search for treasure...").color(NamedTextColor.YELLOW));
    //        loreTreasure.add(Component.text("Price: 10 Gold Ingots").color(NamedTextColor.YELLOW));
    //        item = createItem(Component.text("Expedition").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).decorate(TextDecoration.ITALIC), Material.FILLED_MAP, loreTreasure);
    //        inv.setItem(inv.firstEmpty(), item);
    //    }

    //    //Right side
    //    //for (int i=3; i<=4; i++) {
    //    item = createItem(Component.text("Close").color(NamedTextColor.RED), Material.BARRIER, Collections.singletonList(Component.text("Click to close the menu")));
    //    inv.setItem(inv.firstEmpty(), item);

    //    //item = createItem(Component.empty(), Material.GRAY_STAINED_GLASS_PANE, Collections.emptyList());
    //    //inv.setItem(inv.firstEmpty(), item);
    //    //}
    //}*/